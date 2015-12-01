package com.ogc.browsers.attrubutes;

import org.jsoup.nodes.Element;

import com.example.qrboard.QRWebPageEditorView;

import android.content.Context;
import android.view.View;

public abstract class QRAttribute {
	private String name;


	private String property;
	private Object attribute;
	private Object info;
	private Element element;
	
	public QRAttribute(Element element) {
		this.element = element;
	}
	public abstract void onTouch(QRWebPageEditorView fa, String id);
	public abstract void onEdit(String html);
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
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	 


}
