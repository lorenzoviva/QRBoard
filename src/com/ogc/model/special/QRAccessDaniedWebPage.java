package com.ogc.model.special;

import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRRepresentation;

public class QRAccessDaniedWebPage extends QRRepresentation{

	public QRAccessDaniedWebPage(String text) {
		super(text, getStaticHtml(null));
		// TODO Auto-generated constructor stub
	}
	public static String getStaticHtml(Object param){
		return "<!DOCTYPE html>"
				+ "<html>"
				+ "<head>"
				+ "<style type='text/css'>"
				+ "@font-face {"
				+ "font-family: myfont2;"
				+ "src: url('Impacted2.0.ttf'); }"
				+ "body {font-family: myfont2;}"
				+ "</style>"
				+ "</head>"
				+ "<body style='margin:0; background-color:#FFFFFF'>"
				+ "<div style='text-align: center; font-size: 60px;'>YOU CAN'T<br>SEE<br>THIS QR!</div>"
				+ "</body>"
				+ "</html>";
	}


}
