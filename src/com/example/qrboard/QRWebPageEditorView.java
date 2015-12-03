package com.example.qrboard;

import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ogc.browsers.BrowserListener;
import com.ogc.browsers.LWebViewJsParameters;
import com.ogc.browsers.attrubutes.QRAttribute;
import com.ogc.browsers.elements.QRElement;
import com.ogc.browsers.utils.AttributeListAdapter;
import com.ogc.browsers.utils.BrowserClickEvent;
import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.QRWebPage;

public class QRWebPageEditorView extends ARLayerView implements
		BrowserListener, OnClickListener {
	QRWebPage qrsquare;
	private Document document;
	private ImageButton addImageButton;
	private ImageButton addDivButton;
	private ImageButton addLinkButton;
	private ImageButton addTextButton;
	private ImageButton removeButton;
	private CustomSelector selector = null;
	private String selectedId = "";
	private String action = "";
	private static int offset = 10;
	private float dx = -1, dy = -1, tx = -1, ty = -1, ddy = -1, ddx = -1;
	private ListView listViewMessages;
	private TextView selectTextView;
	private AttributeListAdapter adapter;

	public QRWebPageEditorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public QRWebPageEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public QRWebPageEditorView(Context context) {
		super(context);
	}

	public void setup(QRWebPage qrsquare, ImageButton addImageButton,
			ImageButton addDivButton, ImageButton addLinkButton,
			ImageButton addTextButton, ImageButton removeButton, ListView listViewMessages, TextView selectTextView) {
		this.qrsquare = qrsquare;
		document = Jsoup.parse(qrsquare.getHtml());

		this.addImageButton = addImageButton;
		this.addDivButton = addDivButton;
		this.addLinkButton = addLinkButton;
		this.addTextButton = addTextButton;
		this.removeButton = removeButton;

		this.addImageButton.setOnClickListener(this);
		this.addDivButton.setOnClickListener(this);
		this.addLinkButton.setOnClickListener(this);
		this.addTextButton.setOnClickListener(this);
		this.removeButton.setOnClickListener(this);
		this.listViewMessages = listViewMessages;
		this.selectTextView = selectTextView;
	}

	@Override
	public void draw(Canvas canvas) {
		if (qrsquare != null) {
			int w = getBottom() - getTop();
			qrsquare.setOne(new PointF((getRight() - getLeft() - w) / 2, w));
			qrsquare.setTwo(new PointF((getRight() - getLeft() - w) / 2, 0));
			qrsquare.setThree(new PointF((getRight() - getLeft() + w) / 2, 0));
			qrsquare.setFour(new PointF((getRight() - getLeft() + w) / 2, w));
			qrsquare.draw(canvas, this);
			if (!qrsquare.getSelectedId().equals("")) {
				listen();
				qrsquare.draw(canvas, this);
			}
			if (selector != null) {
				selector.draw(canvas, qrsquare);
			}

		}

	}

	public void listen() {
		if (qrsquare.getWebview() != null
				&& qrsquare.getWebview().isChangeListenerListEmpty()) {
			qrsquare.getWebview().setJsParameters(
					new LWebViewJsParameters(false, false, false, true, false,
							false, true));
			qrsquare.getWebview().addChangeListener(this);

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (getQRTouched(event)) {
			listen();
			qrsquare.onTouch(event);
		}
		return true;
	}

	private boolean getQRTouched(MotionEvent event) {
		if (qrsquare != null) {
			Quadrilateral polygon = new Quadrilateral();
			polygon.addVertex(new Point(qrsquare.getTwo().x,
					qrsquare.getTwo().y));
			polygon.addVertex(new Point(qrsquare.getThree().x, qrsquare
					.getThree().y));
			polygon.addVertex(new Point(qrsquare.getFour().x, qrsquare
					.getFour().y));
			polygon.addVertex(new Point(qrsquare.getOne().x,
					qrsquare.getOne().y));
			return Utility.PointInPolygon(
					new Point(event.getX(), event.getY()), polygon);
		} else {
			return false;
		}

	}

	@Override
	public QRWebPage getQRSquare() {
		return qrsquare;
	}

	public void setQRSquare(QRWebPage qrsquare) {
		this.qrsquare = qrsquare;
	}

	public String getFirstFreeID(String prefix) {
		int number = 1;
		while (isIDTooken(prefix + number)) {
			number++;
		}
		return prefix + number;
	}

	public boolean isIDTooken(String elementID) {
		return document.getElementById(elementID) != null;
	}
	public void startResize(String directionTouched){
		action = "resize " + directionTouched;
		qrsquare.getWebview().setDisableMove(true);
	}
	public void stopResize(LWebViewJsParameters jsParameters){
		if(qrsquare.getWebview() !=null && !action.equals("")){
			 

			qrsquare.setHtml(document.html());
			qrsquare.setWebview(null);
			requestLayout();
			qrsquare.select(selectedId);
			
//			qrsquare.getWebview().setDisableMove(false);
			jsParameters.setSelectedId("");//selectedId
			qrsquare.setJsParameters(jsParameters);
			setSelector(null);
		}
		action = "";
	}

	@Override
	public void onBrowserClickEvent(BrowserClickEvent event) {
		Log.d("QRWebPageEditor", event.toString());
		if(qrsquare!=null && qrsquare.getWebview()!=null && qrsquare.getWebview().getJsParameters()!=null){
			LWebViewJsParameters currentParameters = qrsquare.getWebview().getJsParameters();
			document = Jsoup.parse(event.getHtml());
			if (event.getMotionEventAction() == MotionEvent.ACTION_DOWN) {
				tx = -1;
				ty = -1;
				dx = 0;
				dy = 0;
				if (selector != null && selector.sameElementTouched(event)) {
					String directionTouched = selector.getDirectionTouched(event,qrsquare);
					Log.d("QRWEP", "Clicked on selected square : " + directionTouched);
					if (!directionTouched.equals("")) {
						startResize(directionTouched);					
						tx = event.getTouchX();
						ty = event.getTouchY();
					}
				}
			} else if (event.getMotionEventAction() == MotionEvent.ACTION_UP) {
				stopResize(currentParameters);
				if (dy < 15 && dx < 15) {
					if (selector == null && !event.getTagname().equals("")
							&& event.getElementBounds() != null
							&& !event.getAttributes().equals("")
							&& event.getAttributes().matches(".*id<.+>.*") && !event.getTagname().toLowerCase().equals("body")) {
						
						if (!event.getTagname().toLowerCase(Locale.ROOT)
								.contains("body")) {
							setSelector(new CustomSelector(event));
						} else {
							setSelector(null);
						}
						selectedId = event.getAttributes().split("id<")[1]
								.split(">")[0];
						LWebViewJsParameters newParameters = currentParameters;
						newParameters.setSelectedId(selectedId);
						qrsquare.setJsParameters(newParameters);
	
					} else {
						if (!event.getTagname().equals("")
								&& event.getElementBounds() != null
								&& !event.getAttributes().equals("")
								&& event.getAttributes().matches(".*id<.+>.*") && !event.getTagname().toLowerCase().equals("body")) {
	
							
							if (!event.getTagname().toLowerCase(Locale.ROOT)
									.contains("body")) {
								setSelector(new CustomSelector(event));
							} else {
								setSelector(null);
							}
							selectedId = event.getAttributes().split("id<")[1]
									.split(">")[0];
							LWebViewJsParameters newParameters = currentParameters;
							newParameters.setSelectedId(selectedId);
							qrsquare.setJsParameters(newParameters);
						} else {
							setSelector(null);
						}
					}
				}
				tx = -1;
				ty = -1;
				dx = 0;
				dy = 0;
			} else if (event.getMotionEventAction() == MotionEvent.ACTION_MOVE) {
				if (tx != -1) {
					ddx = tx - event.getTouchX();
					ddy = ty - event.getTouchY();
					dx += Math.abs(ddx);
					dy += Math.abs(ddy);
					if (selector != null) {
						BrowserClickEvent selectedEvent = selector.getEvent();
						if (!event.getTagname().equals("")
								&& event.getElementBounds() != null
								&& !event.getAttributes().equals("")
								&& event.getAttributes().matches(".*id<.+>.*") && !event.getTagname().toLowerCase().equals("body")) {
	
							if (!action.equals("")) {
								if(action.startsWith("resize")){
		
									Log.d("QRWEP", "mooved : " + action.replace("resize ", "") + " ddx:" + ddx +" , ddy:" + ddy);
									Rect selectedBounds = event.getElementBounds();//selectedEvent.getElementBounds();
									String js = "javascript:(function(){var obj = document.getElementById('"+selector.getSelectedId()+"'); ";
									selectedBounds.offset((int)(qrsquare.getHorizontalScroll()/selectedEvent.getF()),(int) (qrsquare.getVerticalScroll()/selectedEvent.getF()));
									Log.d("QRWEP",selectedBounds.toShortString());
									ddx = ddx/selectedEvent.getF();
									ddy = ddy/selectedEvent.getF();
								
									if(ddy != 0  && (((int)(selectedBounds.height()-ddy))>offset || ddy<0) &&  action.contains("bottom")){
										js += "obj.style.position = 'absolute';" ;
										selectedId = selectedEvent.getAttributes().split("id<")[1].split(">")[0];
										if(document.getElementById(selectedId).hasText() && !document.getElementById(selectedId).hasAttr("src") ){
											if(document.getElementById(selectedId).hasAttr("style") && document.getElementById(selectedId).attr("style").contains("font-size")){
												js += "var style = window.getComputedStyle(obj, null).getPropertyValue('font-size');" ;
												js += "var fontSize = parseFloat(style); " ;
												js += "obj.style.fontSize = String(fontSize - "+ddy+")+'px';" ;
											}else{
												js += "obj.style.fontSize = String(10 - "+ddy+")+'px';" ;
												
											}
									
										}else{
											if(document.getElementById(selectedId).hasText()){
												if(document.getElementById(selectedId).hasAttr("style") && document.getElementById(selectedId).attr("style").contains("font-size")){
													js += "var style = window.getComputedStyle(obj, null).getPropertyValue('font-size');" ;
													js += "var fontSize = parseFloat(style); " ;
													js += "obj.style.fontSize = String(fontSize - "+ddy+")+'px';" ;
												}else{
													js += "obj.style.fontSize = String(10 - "+ddy+")+'px';" ;
												}
											}
											js += "obj.style.height = '"+ (int)(selectedBounds.height()-ddy) + "px';" ;
		
										}
		//								selectedBounds.offset(-(int)(qrsquare.getHorizontalScroll()/selectedEvent.getF()),-(int) (qrsquare.getVerticalScroll()/selectedEvent.getF()));
		//								if(((int)(selectedBounds.top-ddy))>selectedEvent.getWindowHeight()-offset){
		//									if(qrsquare.getVerticalScroll()+ddy < qrsquare.getMaxVerticalScroll() && qrsquare.getVerticalScroll()+ddy>=0){
		//										qrsquare.setVerticalScroll((int) (qrsquare.getVerticalScroll()+ddy));
		//									}
		//								}
		//								selectedBounds.offset((int)(qrsquare.getHorizontalScroll()/selectedEvent.getF()),(int) (qrsquare.getVerticalScroll()/selectedEvent.getF()));
		
									}
									if(ddy != 0  &&action.contains("top") && ((int)((selectedBounds.top-(int)(qrsquare.getVerticalScroll()/selectedEvent.getF()))-ddy)>offset || ddy<0)){ // && ((int)(selectedBounds.top-ddy))>0  && ((int)(selectedBounds.height()-ddy))<selectedEvent.getWindowHeight()
										js += "obj.style.position = 'absolute';" ;
										js += "obj.style.top = '"+ (int)(selectedBounds.top-ddy) + "px';" ;//+ qrsquare.getVerticalScroll()
		
									}
									if(ddx != 0  && action.contains("right") && (((int)(selectedBounds.width()-ddx))>offset || ddx<0)){//&& ((int)(selectedBounds.width()-ddx))>offset
										js += "obj.style.position = 'absolute';" ;
										js += "obj.style.width = '"+ (int)(selectedBounds.width()-ddx) + "px';" ;
		
		//								selectedBounds.offset(-(int)(qrsquare.getHorizontalScroll()/selectedEvent.getF()),-(int) (qrsquare.getVerticalScroll()/selectedEvent.getF()));
		//								if(((int)(selectedBounds.right-ddx))< offset){
		//									if(qrsquare.getHorizontalScroll()+ddx < qrsquare.getMaxHorizontalScroll() && qrsquare.getHorizontalScroll()+ddx>=0){
		//										qrsquare.setHorizontalScroll((int) (qrsquare.getHorizontalScroll()+ddx));
		//									}
		//								}
		//								selectedBounds.offset((int)(qrsquare.getHorizontalScroll()/selectedEvent.getF()),(int) (qrsquare.getVerticalScroll()/selectedEvent.getF()));
		
									}
									if(ddx != 0  &&  action.contains("left") && ((int)((selectedBounds.left-(int)(qrsquare.getHorizontalScroll()/selectedEvent.getF()))-ddx)>offset || ddx<0)){//((int)(selectedBounds.left-ddx))>0 && ((int)(selectedBounds.width()+ddx))>10 &&
										js += "obj.style.position = 'absolute';" ;
										js += "obj.style.left = '"+ (int)((selectedBounds.left)-ddx) + "px';" ;//+ qrsquare.getHorizontalScroll()
		
									}
									
									js+=	"})()";
									Log.d("QRWEP",js);
									qrsquare.getWebview().loadUrl(js);
									
									selectedBounds.offset(-(int)(qrsquare.getHorizontalScroll()/selectedEvent.getF()),-(int) (qrsquare.getVerticalScroll()/selectedEvent.getF()));
		
									selectedEvent.setElementBounds(selectedBounds);
								}
								
							} else {
								
								BrowserClickEvent selectorEvent = selectedEvent;
								Rect elementBounds = event.getElementBounds();						
								selectorEvent.setElementBounds(elementBounds);
								selectorEvent.setScrollX(event.getScrollX());
								selectorEvent.setScrollY(event.getScrollY());
								selector.setEvent(selectorEvent);
							}
						}
					}
	
				}else{
					if(action.startsWith("resize")){
						stopResize(currentParameters);
					}
				}
				tx = event.getTouchX();
				ty = event.getTouchY();
			}
		}
	}

	
	public void addElement(String tagname) {
		String id = getFirstFreeID(tagname);
		Element appendElement;

		if (selector == null) {
			Element body = document.body();

			appendElement = body.appendElement(tagname);
			appendElement
					.attr("id", id)
					.attr("style",
							"height: 100px;width: 100px;position: absolute; left: 0px; top:0px");
			String attributes = "";
			for (Attribute a : appendElement.attributes()) {
				attributes += a.getKey() + "<" + a.getValue() + ">";
			}

		} else {
			Element selected = document.getElementById(selectedId);
			appendElement = selected.appendElement(tagname);
			appendElement
					.attr("id", id)
					.attr("style",
							"height: 100px;width: 100px;position: absolute; left: 0px; top:0px");
			String attributes = "";
			for (Attribute a : appendElement.attributes()) {
				attributes += a.getKey() + "<" + a.getValue() + ">";
			}
			BrowserClickEvent ce = new BrowserClickEvent(document.html(),tagname, attributes,
					"", 1, MotionEvent.ACTION_UP, new Rect(0, 0, 100, 100),
					selector.getEvent().getWindowWidth(), selector.getEvent()
							.getWindowWidth(), 0, 0, selector.getEvent()
							.getScrollX(), selector.getEvent().getScrollY(),
					selector.getEvent().getF());
			setSelector(new CustomSelector(ce));
		}
		Log.d("QRWPE", "added element : " + appendElement.toString()
				+ " new HTML: " + document.html());

		selectedId = id;
		// Log.d("QRWPE", "new html : " + document.html());
		qrsquare.setHtml(document.html());
		qrsquare.setWebview(null);

		requestLayout();
		qrsquare.select(selectedId);
	}

	@Override
	public void onClick(View v) {
		// Clicked on button

		if (v.getId() == addImageButton.getId()) {
			Log.d("WEB PAGE EDITOR", "clicked on addImageButton");
			addElement("img");
		} else if (v.getId() == addDivButton.getId()) {
			addElement("div");
			Log.d("WEB PAGE EDITOR", "clicked on addDivButton");
		} else if (v.getId() == addLinkButton.getId()) {
			addElement("button");
			Log.d("WEB PAGE EDITOR", "clicked on addLinkButton");
		} else if (v.getId() == addTextButton.getId()) {
			addElement("div");
			Log.d("WEB PAGE EDITOR", "clicked on addTextButton");
		} else if (v.getId() == removeButton.getId()) {
			document.getElementById(selectedId).remove();
			Log.d("QRWPE", "removed element : " + selectedId + " new HTML: "
					+ document.html());
			qrsquare.setHtml(document.html());
			qrsquare.setWebview(null);
			setSelector(null);
			selectedId = "";

		}

	}

	public CustomSelector getSelector() {
		return selector;
	}

	public void setSelector(CustomSelector selector) {
		
		if(selector!=null){ 
			BrowserClickEvent event = selector.getEvent();
			
			if( event != null && !event.getTagname().equals("")&& event.getElementBounds() != null
					&& !event.getAttributes().equals("")&& event.getAttributes().matches(".*id<.+>.*") && !event.getTagname().toLowerCase().equals("body")) {
				removeButton.setVisibility(VISIBLE);
				selectTextView.setVisibility(INVISIBLE);
				listViewMessages.setVisibility(VISIBLE);
				String tagname = selector.getEvent().getTagname();
				final String id =  event.getAttributes().split("id<")[1].split(">")[0];
				Element el = document.getElementById(id);
				try {
					QRElement qrel = QRElement.getQRElement(tagname);
					List<QRAttribute> elatt= qrel.getAttributes(el);
					adapter = new AttributeListAdapter(getContext(),elatt);
					listViewMessages.setAdapter(adapter);
					final QRWebPageEditorView fa = this;
					listViewMessages.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long viewid) {
							adapter.onTouch(position,fa,id);
							
						}
					});
				} catch (InstantiationException | IllegalAccessException
						| ClassNotFoundException e) {
					Log.d("QRWebPageEditorView", "ERROR, you selected a tag that cant be managed");
				}
				
				
			}else{
				removeButton.setVisibility(INVISIBLE);
				selectTextView.setVisibility(VISIBLE);
				listViewMessages.setVisibility(INVISIBLE);
				selector = null;
			}
		}else{
			removeButton.setVisibility(INVISIBLE);
			selectTextView.setVisibility(VISIBLE);
			listViewMessages.setVisibility(INVISIBLE);
		}
		this.selector = selector;
	}
	public void edit(String html,String id){
		selectedId = id;
		Element el = document.getElementById(id);
		el.html(html);
		action = "edit";
		stopResize(qrsquare.getWebview().getJsParameters());
	}

}
