package com.ogc.browsers;

public class BrowserClickEvent{

	private String tagname;
	private String attributes;
	private String parents;
	private long pressureTime;
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
	public BrowserClickEvent(String tagname, String attributes, String parents,long pressureTime) {
		this.tagname = tagname;
		this.attributes = attributes;
		this.parents = parents;
		this.pressureTime = pressureTime;
	}
	public long getPressureTime() {
		return pressureTime;
	}
	public void setPressureTime(long pressureTime) {
		this.pressureTime = pressureTime;
	}

}
