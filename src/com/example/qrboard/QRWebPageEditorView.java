package com.example.qrboard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.QRWebPage;

public class QRWebPageEditorView extends ARLayerView implements PropertyChangeListener{
	QRWebPage qrsquare;
	private Document document;
	public QRWebPageEditorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public QRWebPageEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public QRWebPageEditorView(Context context) {
		super(context);
	}
	
	public void setup(QRWebPage qrsquare){
		this.qrsquare = qrsquare;
		document = Jsoup.parse(qrsquare.getHtml());
	}

	@Override
	public void draw(Canvas canvas) {
		if(qrsquare != null){
			int w = getBottom()-getTop();
			qrsquare.setOne(new PointF((getRight()-getLeft()-w)/2, w));
			qrsquare.setTwo(new PointF((getRight()-getLeft()-w)/2, 0));
			qrsquare.setThree(new PointF((getRight()-getLeft()+w)/2, 0));
			qrsquare.setFour(new PointF((getRight()-getLeft()+w)/2, w));
			qrsquare.draw(canvas,this);
			
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if(getQRTouched(event)){
			if(qrsquare.getWebview()!=null && qrsquare.getWebview().isChangeListenerListEmpty()){
				qrsquare.getWebview().addChangeListener(this);
			}
			qrsquare.onTouch(event);
		}
		return true;
	}

	private boolean getQRTouched(MotionEvent event) {
			if (qrsquare != null) {
				Quadrilateral polygon = new Quadrilateral();
				polygon.addVertex(new Point(qrsquare.getTwo().x, qrsquare.getTwo().y));
				polygon.addVertex(new Point(qrsquare.getThree().x, qrsquare.getThree().y));
				polygon.addVertex(new Point(qrsquare.getFour().x, qrsquare.getFour().y));
				polygon.addVertex(new Point(qrsquare.getOne().x, qrsquare.getOne().y));
				return Utility.PointInPolygon(new Point(event.getX(), event.getY()), polygon);
			} else {
				return false;
			}
		
	}
	public void propertyChange(PropertyChangeEvent event) {
	    Log.d("Changed property: " , event.getPropertyName() + " [old -> "
	      + event.getOldValue() + "] | [new -> " + event.getNewValue() +"]");
	}
	@Override
	public QRWebPage getQRSquare() {
		return qrsquare;
	}
	public void setQRSquare(QRWebPage qrsquare) {
		this.qrsquare = qrsquare;
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		Log.d("EWP", "clicked :" + event.getAction());
//
//		if(getQRTouched(event)){
//			Log.d("EWP", "clicked on square!");
//			if(qrsquare.getWebview()!=null && qrsquare.getWebview().isChangeListenerListEmpty()){
//				qrsquare.getWebview().addChangeListener(this);
//			}
//			qrsquare.onTouch(event);
//		}else{
//			Log.d("EWP", "clicked in surface out of square!");
//		}
//		return false;
//	}
}
