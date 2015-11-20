package com.ogc.model.special;

import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquare;

public class QRLoginPage extends QRRepresentation{

	public QRLoginPage(QRSquare realQR) {
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
				+ "font-family: myfont;"
				+ "src: url('OpenSans-Light.ttf'); }"
				+ "body {font-family: myfont;}"
				+ "</style>"
				+ "</head>"
				+ "<body style='margin:0; background-color:#5DB5E0;'>"
				+ "<div style='text-align: center;font-size: 32px; color:#FFFFFF'>Hello!<br>This is a User's personal QR.<br>Do you want to login?</div>"
				+ "</body>"
				+ "</html>";
	}
}
