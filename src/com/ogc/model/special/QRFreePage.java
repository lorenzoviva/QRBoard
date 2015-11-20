package com.ogc.model.special;

import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquare;

public class QRFreePage extends QRRepresentation{

	
	public QRFreePage(QRSquare realQR) {
		super(realQR.getText(), getStaticHtml(null));
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}
	public static String getStaticHtml(Object nullobj){
		return  "<!DOCTYPE html>"
				+ "<html>"
				+ "<head>"
				+ "<style type='text/css'>"
				+ "@font-face {"
				+ "font-family: myfont2;"
				+ "src: url('Impacted2.0.ttf'); }"
				+ "body {font-family: myfont2;}"
				+ "</style>"
				+ "</head>"
				+ "<body style='margin:0; background-color:#5DB5E0'>"
				+ "<div style='text-align: center; font-size: 60px;'>THIS<br>QR IS<br>FREE!</div>"
				+ "</body>"
				+ "</html>";
	}
}
