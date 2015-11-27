package com.example.qrboard;

import com.ogc.model.ACL;
import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.special.QRCode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class QRExplorerRowHeader {
	private Bitmap qrc = null;
	private Bitmap qrum = null;
	private Bitmap qru = null;
	private Bitmap qrs = null;
	private Bitmap qrsu = null;
	private int height = 50;
	private int width = 50;
	Paint externalmargin = new Paint();
	Paint internalmargin = new Paint();
	Paint backgroundPaint = new Paint();
	private int request;

	public QRExplorerRowHeader(int request, View view) {
		this.request = request;
		if (request == 3) {
			qrc = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.qricon);
			qrsu = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.qricon);
			qrs = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.qricon);

		} else {
			qru = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.usericon);
			qrum = BitmapFactory.decodeResource(view.getContext().getResources(),  R.drawable.qricon);
			qrsu = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.usericon);
		}
	}
	public int getHeight(Rect bounds){
		return (bounds.right-bounds.left)/12;
	}
	public int getWidth(Rect bounds){
		return (bounds.right-bounds.left)/5;
	}
	public void draw(Canvas canvas, QRExplorer qrExplorer, Rect bounds) {
		width = getWidth(bounds);
		height = getHeight(bounds);
		externalmargin.setColor(Color.WHITE);
		externalmargin.setStrokeWidth(6);
		internalmargin.setColor(Color.WHITE);
		internalmargin.setStrokeWidth(2);
		backgroundPaint.setColor(Color.rgb(0,100,150));
		canvas.drawRect(new Rect((bounds.right - bounds.left)- 3 * width,bounds.top,(bounds.right - bounds.left),bounds.top+height), backgroundPaint);
		canvas.drawLine((bounds.right - bounds.left) - 3 * width, + bounds.top +  height, (bounds.right - bounds.left) - 3 * width,  + bounds.top, externalmargin);
		canvas.drawLine((bounds.right - bounds.left), -2 + bounds.top +  height, (bounds.right - bounds.left) - 3 * width, -2  + bounds.top +  height, externalmargin);
		canvas.drawLine((bounds.right - bounds.left),  + bounds.top, (bounds.right - bounds.left) - 3 * width,  + bounds.top , externalmargin);
		canvas.drawLine((bounds.right - bounds.left),  + bounds.top +  height, (bounds.right - bounds.left),   + bounds.top , externalmargin);
		canvas.drawLine((bounds.right - bounds.left) - 2 * width,   bounds.top +  height, (bounds.right - bounds.left) - 2 * width,   bounds.top , internalmargin);
		canvas.drawLine((bounds.right - bounds.left) - width,   bounds.top +  height, (bounds.right - bounds.left) - width,   bounds.top , internalmargin);
		if(qrc!=null){
			canvas.drawBitmap(qrc, (bounds.right - bounds.left) - 5 * (width/2) -(qrc.getWidth()/2), bounds.top+(height/2) - (qrc.getHeight()/2), internalmargin);
		}
		if(qrsu!=null){
			canvas.drawBitmap(qrsu, (bounds.right - bounds.left) - 3 * (width/2) -(qrsu.getWidth()/2), bounds.top+(height/2) - (qrsu.getHeight()/2), internalmargin);
		}
		if(qrs!=null){
			canvas.drawBitmap(qrs, (bounds.right - bounds.left) - (width/2) -(qrs.getWidth()/2), bounds.top+(height/2) - (qrs.getHeight()/2), internalmargin);
		}
		
		if(qrum!=null){
			canvas.drawBitmap(qrum, (bounds.right - bounds.left) - 5 * (width/2) -(qrum.getWidth()/2), bounds.top+(height/2) - (qrum.getHeight()/2), internalmargin);
		}
		if(qru!=null){
			canvas.drawBitmap(qru, (bounds.right - bounds.left) - (width/2) -(qru.getWidth()/2), bounds.top+(height/2) - (qru.getHeight()/2), internalmargin);
		}
		
	}
}
