package com.ogc.model;

public abstract class QRRepresentation extends QRInternalWebPage{
	
	public QRRepresentation(String text, String html) {
		super(text, html);
	}

	public static String getStaticHtml(Object param){
		return "";
	}
}
