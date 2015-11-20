package com.example.qrboard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
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
	private CustomSelector selector = null;

	
	private float  dx = -1,dy = -1,tx = -1, ty = -1,ddy = -1,ddx = -1;
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
			ImageButton addTextButton) {
		this.qrsquare = qrsquare;
		document = Jsoup.parse(qrsquare.getHtml());

		this.addImageButton = addImageButton;
		this.addDivButton = addDivButton;
		this.addLinkButton = addLinkButton;
		this.addTextButton = addTextButton;

		this.addImageButton.setOnClickListener(this);
		this.addDivButton.setOnClickListener(this);
		this.addLinkButton.setOnClickListener(this);
		this.addTextButton.setOnClickListener(this);

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
			if (qrsquare.getWebview() != null
					&& qrsquare.getWebview().isChangeListenerListEmpty()) {
				qrsquare.getWebview().setJsParameters(
						new LWebViewJsParameters(false, false, false, true,
								false, false, true));
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

	@Override
	public void onBrowserClickEvent(BrowserClickEvent event) {
		Log.d("QRWebPageEditor",
				"Touched browser element :" + event.getTagname()
						+ " with attributes :" + event.getAttributes()
						+ " and parents : " + event.getParents()
						+ " the pressure time on this element is : "
						+ event.getPressureTime() + " event : "
						+ event.getMotionEventAction());
		if (event.getMotionEventAction() == MotionEvent.ACTION_DOWN) {
			tx = -1;
			ty = -1;
			dx = 0;
			dy = 0;
		
		}else if(event.getMotionEventAction() == MotionEvent.ACTION_UP){
			if(dy<15 && dx <15){
				if (selector == null && !event.getTagname().equals("") && event.getElementBounds() != null) {
					selector = new CustomSelector(event);
				} else {
					selector = null;
				}
			}
			tx = -1;
			ty = -1;
			dx = 0;
			dy = 0;
		}else if(event.getMotionEventAction() == MotionEvent.ACTION_MOVE){
			if (tx != -1) {
				ddx = tx - event.getTouchX();
				ddy = ty - event.getTouchY();
				dx += Math.abs(ddx);
				dy += Math.abs(ddy);
			}
			tx = event.getTouchX();
			ty = event.getTouchY();
		}
	}

	@Override
	public void onClick(View v) {
		// Clicked on button

		if (v.getId() == addImageButton.getId()) {
			Log.d("WEB PAGE EDITOR", "clicked on addImageButton");
		} else if (v.getId() == addDivButton.getId()) {
			Log.d("WEB PAGE EDITOR", "clicked on addDivButton");
		} else if (v.getId() == addLinkButton.getId()) {
			Log.d("WEB PAGE EDITOR", "clicked on addLinkButton");
		} else if (v.getId() == addTextButton.getId()) {
			Log.d("WEB PAGE EDITOR", "clicked on addTextButton");
		} else {

		}

	}

}
