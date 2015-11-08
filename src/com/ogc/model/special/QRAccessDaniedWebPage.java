package com.ogc.model.special;

import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRRepresentation;

public class QRAccessDaniedWebPage extends QRRepresentation{

	public QRAccessDaniedWebPage(String text) {
		super(text, getStaticHtml(null));
		// TODO Auto-generated constructor stub
	}
	public static String getStaticHtml(Object param){
		return "You cant see this QR!";
	}


}
