package com.ogc.browsers;

import android.graphics.Rect;

public class BrowserClickEvent{

	private String tagname;
	private String attributes;
	private String parents;
	private long pressureTime;
	private int motionEventAction;
	private Rect elementBounds;
	int windowWidth;
	int windowHeight;
	float touchX;
	float touchY;
	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getParents() {
		return parents;
	}
	public void setParents(String parents) {
		this.parents = parents;
	}
	public BrowserClickEvent(String tagname, String attributes, String parents,long pressureTime, int eventAction,Rect elementBounds, int w, int h, float touchX, float touchY) {
		this.tagname = tagname;
		this.attributes = attributes;
		this.parents = parents;
		this.pressureTime = pressureTime;
		this.motionEventAction = eventAction;
		this.elementBounds = elementBounds;
		this.windowWidth = w;
		this.windowHeight = h;
		this.touchX = touchX;
		this.touchY = touchY;
		
	}
	public long getPressureTime() {
		return pressureTime;
	}
	public void setPressureTime(long pressureTime) {
		this.pressureTime = pressureTime;
	}
	public int getMotionEventAction() {
		return motionEventAction;
	}
	public void setMotionEventAction(int motionEventAction) {
		this.motionEventAction = motionEventAction;
	}

	public Rect getElementBounds() {
		return elementBounds;
	}
	public void setElementBounds(Rect elementBounds) {
		this.elementBounds = elementBounds;
	}
	public int getWindowWidth() {
		return windowWidth;
	}
	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}
	public int getWindowHeight() {
		return windowHeight;
	}
	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}
	public float getTouchX() {
		return touchX;
	}
	public void setTouchX(float touchX) {
		this.touchX = touchX;
	}
	public float getTouchY() {
		return touchY;
	}
	public void setTouchY(float touchY) {
		this.touchY = touchY;
	}
}
