package com.ogc.browsers;

import android.content.Context;
import android.view.View;

public abstract class QRAttribute {
	private String name;


	private String property;
	private Object attribute;
	private Object info;
	
	
	public abstract View getView(Context context);
	public Object getAttribute() {
		return attribute;
	}

	public void setAttribute(Object attribute) {
		this.attribute = attribute;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}

	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
