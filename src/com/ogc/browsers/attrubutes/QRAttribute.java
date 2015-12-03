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
	public boolean hasStyle(){
		return element.hasAttr("style");
	}
	public boolean hasStyleAttribute(String styleAttribute){
		return hasStyle() && element.attr("style").matches(".*(;|\\s|^)"+styleAttribute+"\\s*:[^;]+;.*");
	}
	public boolean hasStyleAttribute(String style,String styleAttribute){
		return style.matches(".*(;|\\s|^)"+styleAttribute+"\\s*:[^;]+;.*");
	}
	public String getStyleAttribute(String styleAttribute){
		return element.attr("style").split(".*(;|\\s|^)"+styleAttribute+"\\s*:")[1].split(";")[0];
	}
	public Object getInfo() {
		return info;
	}
	public String editStyleAttribute(String styleAttribute,String newValue){
		String style = "";
		if(hasStyle()){
			style = getElement().attr("style");
		}
		if(hasStyleAttribute(styleAttribute)){
			style = style.replaceFirst("(;|\\s|^)"+styleAttribute+"\\s*:[^;]+;",";"+styleAttribute+":"+newValue+";");
			
		}else{
			style += ";"+styleAttribute+":"+newValue+";";
		}
		if(style.trim().startsWith(";")){
			style = style.replaceFirst(";", "");
		}
		if(style.trim().contains(";;")){
			style = style.replaceAll(";;", ";");
		}
		return style;
	}
	public String editStyleAttribute(String style,String styleAttribute,String newValue){

		if(hasStyleAttribute(style,styleAttribute)){
			style = style.replaceFirst("(;|\\s|^)"+styleAttribute+"\\s*:[^;]+;",";"+styleAttribute+":"+newValue+";");
			
		}else{
			style += ";"+styleAttribute+":"+newValue+";";
		}
		if(style.trim().startsWith(";")){
			style = style.replaceFirst(";", "");
		}
		if(style.trim().contains(";;")){
			style = style.replaceAll(";;", ";");
		}
		return style;
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
