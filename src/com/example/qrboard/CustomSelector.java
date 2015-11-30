package com.example.qrboard;

import com.ogc.browsers.BrowserClickEvent;
import com.ogc.model.QRWebPage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class CustomSelector {
	BrowserClickEvent event;
	private static int offset = 6;
	public CustomSelector(BrowserClickEvent event) {
		this.event = event;
	}

	public void draw(Canvas canvas, QRWebPage square) {
		Rect r = new Rect(event.getElementBounds());
		int left = (int) ((((double) square.getThree().x - (double) square.getTwo().x) * ((double) r.left ))/ (double) (event.getWindowWidth()));
		int top = (int) ((((double) square.getFour().y - (double) square.getThree().y) * ((double) r.top ))/ (double) (event.getWindowHeight()));
		int right = (int) ((((double) square.getThree().x - (double) square.getTwo().x) * ((double) (r.right ))) / (double) (event.getWindowWidth()));
		int bottom = (int) ((((double) square.getFour().y - (double) square.getThree().y) * ((double) (r.bottom))) / (double) (event.getWindowHeight()));
		r.set(left, top, right, bottom);
		r.offset((int) square.getTwo().x, (int) square.getTwo().y);
		drawCorners(canvas,r);
		Paint borderPaint = new Paint();
		borderPaint.setARGB(255, 93, 181, 224);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(1);
		for (int i = 0; i < 2*offset; i++) {
			borderPaint.setAlpha(255 - (i * 20));
			r.set(r.left - 1, r.top - 1, r.right + 1, r.bottom + 1);
			canvas.drawRect(r, borderPaint);

		}
		
	}
	public String getDirectionTouched(BrowserClickEvent event,QRWebPage square){
		Rect r = new Rect(event.getElementBounds());
		int left = (int) ((((double)square.getWebview().getMeasuredWidth()) * ((double) r.left ))/ (double) (event.getWindowWidth()));
		int top = (int) ((((double) square.getWebview().getMeasuredHeight()) * ((double) r.top ))/ (double) (event.getWindowHeight()));
		int right = (int) ((((double) square.getWebview().getMeasuredWidth()) * ((double) (r.right ))) / (double) (event.getWindowWidth()));
		int bottom = (int) ((((double) square.getWebview().getMeasuredHeight()) * ((double) (r.bottom))) / (double) (event.getWindowHeight()));
		r.set(left, top, right, bottom);
//		r.offset((int) square.getTwo().x, (int) square.getTwo().y);
		
		float x = event.getTouchX();
		float y = event.getTouchY();
		Log.d("QRWPE", "x :" + x + " y :" + y +" , rect: " + r.toString() + " square two: " + square.getTwo().toString() + " square w: " +(square.getThree().x - square.getTwo().x)) ;

		if(isInCorner(x,y, r.left+offset,r.top+offset)){
			return "top left";
		}
		if(isInCorner(x,y, r.left+offset,r.bottom-offset)){
			return "bottom left";
		}
		if(isInCorner(x,y, r.right-offset,r.top+offset)){
			return "top right";
		}
		if(	isInCorner(x,y, r.right-offset,r.bottom-offset)){
			return "bottom right";
		}
		if(isInCorner(x,y, r.left+offset,(r.top+r.bottom)/2)){
			return "left";
		}
		
		if(isInCorner(x,y, (r.left+r.right)/2,r.top+offset)){
			return "top";
		}
		if(isInCorner(x,y, (r.left+r.right)/2,r.bottom-offset)){
			return "bottom";
		}
		
		if(isInCorner(x,y, r.right-offset,(r.top+r.bottom)/2)){
			return "right";
		}
		
		return "";
	}
	public boolean isInCorner(float x, float y,int cx,int cy){
		int toffset = offset+1;
		return (x>cx-4*toffset && x<cx+4*toffset && y>cy-4*toffset && y<cy+4*toffset);
	}
	public void drawCorners(Canvas canvas,Rect r){
		drawCorner(canvas, r.left+offset,r.top+offset);
		drawCorner(canvas, r.left+offset,(r.top+r.bottom)/2);
		drawCorner(canvas, r.left+offset,r.bottom-offset);
	
		drawCorner(canvas, (r.left+r.right)/2,r.top+offset);
		drawCorner(canvas, (r.left+r.right)/2,r.bottom-offset);
		
		drawCorner(canvas, r.right-offset,r.top+offset);
		drawCorner(canvas, r.right-offset,(r.top+r.bottom)/2);
		drawCorner(canvas, r.right-offset,r.bottom-offset);
	}
	public void drawCorner(Canvas canvas, int x,int y){
		int left = x-offset*2;
		int top = y-offset*2;
		int right = x+offset*2;
		int bottom = y+offset*2;
		Rect r = new Rect(left, top, right, bottom);

		Paint borderPaint = new Paint();
		borderPaint.setARGB(255, 93, 181, 224);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(1);
		for (int i = 0; i < 2*offset; i++) {
			borderPaint.setAlpha(255 - (i * 20));
			r.set(r.left +1 , r.top +1 , r.right -1, r.bottom - 1);
			canvas.drawRect(r, borderPaint);

		}
	}
	
	public BrowserClickEvent getEvent() {
		return event;
	}

	public void setEvent(BrowserClickEvent event) {
		this.event = event;
	}

	public boolean sameElementTouched(BrowserClickEvent otherevent) {
		
		return event!=null && event.getAttributes()!=null && event.getAttributes().matches(".*id<.+>.*") && otherevent!=null && otherevent.getAttributes()!=null && otherevent.getAttributes().matches(".*id<.+>.*") && otherevent.getAttributes().split("id<")[1].split(">")[0].equals(event.getAttributes().split("id<")[1].split(">")[0]);
	}

	public String getSelectedId() {
		if(event!=null && event.getAttributes()!=null && event.getAttributes().matches(".*id<.+>.*")){
			return event.getAttributes().split("id<")[1].split(">")[0];
		}else{
			return "";
		}
	}

}
