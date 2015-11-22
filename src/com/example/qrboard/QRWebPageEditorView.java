package com.example.qrboard;

import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;

import android.app.Notification.Action;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import org.jsoup.nodes.Element;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.ogc.browsers.BrowserClickEvent;
import com.ogc.browsers.BrowserListener;
import com.ogc.browsers.LWebViewJsParameters;
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

	private float dx = -1, dy = -1, tx = -1, ty = -1, ddy = -1, ddx = -1;

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
			ImageButton addTextButton, ImageButton removeButton) {
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
			if (selector != null) {
				selector.draw(canvas, qrsquare);
			}

		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (getQRTouched(event)) {
			if (qrsquare.getWebview() != null && qrsquare.getWebview().isChangeListenerListEmpty()) {
				qrsquare.getWebview().setJsParameters(new LWebViewJsParameters(false, false, false, true,false, false, true));
				qrsquare.getWebview().addChangeListener(this);

			}
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

	public String getFirstFreeID(String prefix){
		int number = 1;
		while(isIDTooken(prefix + number)){
			number++;
		}
		return prefix + number;
	}
	public boolean isIDTooken(String elementID){
		return document.getElementById(elementID) != null;
	}
	@Override
	public void onBrowserClickEvent(BrowserClickEvent event) {
		Log.d("QRWebPageEditor", event.toString());
		if (event.getMotionEventAction() == MotionEvent.ACTION_DOWN) {
			tx = -1;
			ty = -1;
			dx = 0;
			dy = 0;

		} else if (event.getMotionEventAction() == MotionEvent.ACTION_UP) {
			if (dy < 15 && dx < 15) {
				if (selector == null && !event.getTagname().equals("")
						&& event.getElementBounds() != null
						&& !event.getAttributes().equals("")
						&& event.getAttributes().matches(".*id<.+>.*")) {
					selector = new CustomSelector(event);
					if (!event.getTagname().toLowerCase(Locale.ROOT).contains("body")) {
						removeButton.setVisibility(VISIBLE);
					}else{
						removeButton.setVisibility(INVISIBLE);
					}
					 selectedId = event.getAttributes().split("id<")[1]
							.split(">")[0];
					LWebViewJsParameters jsParameters = qrsquare.getWebview()
							.getJsParameters();
					jsParameters.setSelectedId(selectedId);
					qrsquare.setJsParameters(jsParameters);

				} else {
					if (!event.getTagname().equals("")
							&& event.getElementBounds() != null
							&& !event.getAttributes().equals("")
							&& event.getAttributes().matches(".*id<.+>.*")) {
						selector = new CustomSelector(event);
						if (!event.getTagname().toLowerCase(Locale.ROOT).contains("body")) {
							removeButton.setVisibility(VISIBLE);
						}else{
							removeButton.setVisibility(INVISIBLE);
						}
						selectedId = event.getAttributes().split("id<")[1]
								.split(">")[0];
						LWebViewJsParameters jsParameters = qrsquare
								.getWebview().getJsParameters();
						jsParameters.setSelectedId(selectedId);
						qrsquare.setJsParameters(jsParameters);
					} else {
						removeButton.setVisibility(INVISIBLE);
						selector = null;
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
					BrowserClickEvent selectorEvent = selector.getEvent();
					Rect elementBounds = selectorEvent.getElementBounds();
					// if(event.getAttributes().equals(selectorEvent.getAttributes())){
					elementBounds = event.getElementBounds();
					// }else{
					// if (selectorEvent.getScrollX() != event.getScrollX() ||
					// selectorEvent.getScrollY() != event.getScrollY()) {
					// int dx =
					// (int)(-((float)(event.getScrollY()-selectorEvent.getScrollY())/(float)selectorEvent.getF()));
					// int dy =
					// (int)(-((float)(event.getScrollX()-selectorEvent.getScrollX())/(float)selectorEvent.getF()));
					// elementBounds.offset(dx,dy);
					//
					// elementBounds.set(elementBounds.left+dx,
					// elementBounds.top+dy, elementBounds.right+dx,
					// elementBounds.bottom + dy);
					// }
					// }
					selectorEvent.setElementBounds(elementBounds);
					selectorEvent.setScrollX(event.getScrollX());
					selectorEvent.setScrollY(event.getScrollY());
					selector.setEvent(selectorEvent);
				}

			}
			tx = event.getTouchX();
			ty = event.getTouchY();
		}
	}

	public void addElement(String tagname){
		String id = getFirstFreeID(tagname);
		Element appendElement;
		if(selector==null){
			Element body = document.body();
			
			appendElement = body.appendElement(tagname);
			appendElement.attr("id", id);
			String attributes = "";
			for(Attribute a : appendElement.attributes()){
				attributes += a.getKey() + "<" + a.getValue() + ">";
			}
			
			MotionEvent event = MotionEvent.obtain(1, 100, MotionEvent.ACTION_UP, 10, 10, 0);
			qrsquare.onTouch(event);
//			int w = qrsquare.getWebview().computeHorizontal();
//			Log.d("CALCULATED WIDTH", "W:"+w);
//			ce = new BrowserClickEvent(tagname, attributes, "", 1, MotionEvent.ACTION_UP, new Rect(0,0,100,100), w, w, 0, 0, 0, 0, 0);
		}else{
			Element selected = document.getElementById(selectedId);
			appendElement = selected.appendElement(tagname);
			appendElement.attr("id",id).attr("style","height: 100px;width: 100px;position: absolute; left: 0px; top:0px");
			String attributes = "";
			for(Attribute a : appendElement.attributes()){
				attributes += a.getKey() + "<" + a.getValue() + ">";
			}
			BrowserClickEvent ce = new BrowserClickEvent(tagname, attributes, "", 1, MotionEvent.ACTION_UP, new Rect(0,0,100,100), selector.getEvent().getWindowWidth(),  selector.getEvent().getWindowWidth()	, 0, 0, selector.getEvent().getScrollX(), selector.getEvent().getScrollY(), selector.getEvent().getF());
			selector = new CustomSelector(ce);
		}
		
		removeButton.setVisibility(VISIBLE);
		selectedId = id;		
//		Log.d("QRWPE", "new html : " + document.html());
		qrsquare.setHtml(document.html());
		qrsquare.setWebview(null);
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
			addElement("a");
			Log.d("WEB PAGE EDITOR", "clicked on addLinkButton");
		} else if (v.getId() == addTextButton.getId()) {
			addElement("div");
			Log.d("WEB PAGE EDITOR", "clicked on addTextButton");
		} else if(v.getId() == removeButton.getId()){
			removeButton.setVisibility(INVISIBLE);
			document.getElementById(selectedId).remove();
			Log.d("QRWPE", "removed element : "  + selectedId + " new HTML: " + document.html());
			qrsquare.setHtml(document.html());
			qrsquare.setWebview(null);
			selector = null;
			selectedId = "";
			
		}

	}

}
