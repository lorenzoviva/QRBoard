package com.ogc.model;

import java.util.Date;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.example.qrboard.ARLayerView;
import com.google.zxing.ResultPoint;

public class QRSquare {

	private String text;
	private Date creationDate;
	private Long visit;
	private ACL acl;
	private int maxHorizontalScroll = 0;
	private int maxVerticalScroll = 0;
	private int horizontalScroll = 0;
	private int verticalScroll = 0;
	
	protected PointF one;
	protected PointF two;
	protected PointF three;
	protected PointF four;
	int qrsize;
	
	public QRSquare() {
		
	}
	public void setShape(ResultPoint[] resultPoints, int qrsize){
		this.qrsize = qrsize;
		this.one = new PointF(resultPoints[0].getX(),resultPoints[0].getY());
		this.two = new PointF(resultPoints[1].getX(),resultPoints[1].getY());
		this.three = new PointF(resultPoints[2].getX(),resultPoints[2].getY());
		this.four = new PointF(resultPoints[3].getX(),resultPoints[3].getY());
		correctShape();
		float scale = (float)((float)(qrsize)/(float)(qrsize-6));
//		Log.d("scale ","scael :" + scale);
		expandSquare(scale);
//		Log.d("QRSQUARE",this.toString());
	}
	public QRSquare(ResultPoint[] resultPoints, int qrsize) {
		setShape(resultPoints,qrsize);
	}
	public QRSquare(String text) {
		this.text = text;
	}
	public void correctShape(){
		four.x = four.x + (float)((float)3.5/(float)(qrsize-8)) * (four.x - two.x);
		four.y = four.y + (float)((float)3.5/(float)(qrsize-8)) * (four.y - two.y);
	}
	public void draw(Canvas canvas, ARLayerView arview) {

		Paint wallpaint = new Paint();
		wallpaint.setColor(Color.WHITE);
//		wallpaint.setAlpha(128);
		wallpaint.setStyle(Paint.Style.FILL);
		Path wallpath = new Path();
		wallpath.reset(); // only needed when reusing this path for a new build
		wallpath.moveTo(one.x, one.y); // used for first point
		wallpath.lineTo(two.x, two.y);
		wallpath.lineTo(three.x, three.y);
		wallpath.lineTo(four.x,four.y);
		wallpath.lineTo(one.x, one.y); // there is a setLastPoint action but i found it not to work as expected
		
		canvas.drawPath(wallpath, wallpaint);
	}
	
	private void expandSquare(float f){
		float onex = one.x-three.x;
		float oney = one.y-three.y;
		float twox = two.x-four.x;
		float twoy = two.y-four.y;
		one.x = one.x + onex*(f-1);
		one.y = one.y + oney*(f-1);
		two.x = two.x + twox*(f-1);
		two.y = two.y + twoy*(f-1);
		three.x = three.x - onex*(f-1);
		three.y = three.y - oney*(f-1);
		four.x = four.x - twox*(f-1);
		four.y = four.y - twoy*(f-1);
	}
	
	public void onTouch(MotionEvent event){
		
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Long getVisit() {
		return visit;
	}
	public void setVisit(Long visit) {
		this.visit = visit;
	}
	public ACL getAcl() {
		return acl;
	}
	public void setAcl(ACL acl) {
		this.acl = acl;
	}
	@Override
	public String toString() {
		return "QRSquare [text=" + text + ", creationDate=" + creationDate + ", visit=" + visit + ", acl=" + acl.toString() ;
	}
	public PointF getOne() {
		return one;
	}
	public void setOne(PointF one) {
		this.one = one;
	}
	public PointF getTwo() {
		return two;
	}
	public void setTwo(PointF two) {
		this.two = two;
	}
	public PointF getThree() {
		return three;
	}
	public void setThree(PointF three) {
		this.three = three;
	}
	public PointF getFour() {
		return four;
	}
	public void setFour(PointF four) {
		this.four = four;
	}
	public int getHorizontalScroll() {
		return horizontalScroll;
	}
	public void setHorizontalScroll(int horizontalScroll) {
		this.horizontalScroll = horizontalScroll;
	}
	public int getVerticalScroll() {
		return verticalScroll;
	}
	public void setVerticalScroll(int verticalScroll) {
		this.verticalScroll = verticalScroll;
	}
	public int getMaxHorizontalScroll() {
		return maxHorizontalScroll;
	}
	public void setMaxHorizontalScroll(int maxHorizontalScroll) {
		this.maxHorizontalScroll = maxHorizontalScroll;
	}
	public int getMaxVerticalScroll() {
		return maxVerticalScroll;
	}
	public void setMaxVerticalScroll(int maxVerticalScroll) {
		this.maxVerticalScroll = maxVerticalScroll;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QRSquare other = (QRSquare) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
}
