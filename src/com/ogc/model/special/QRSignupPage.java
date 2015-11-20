package com.ogc.model.special;

import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;
// this qr is free, you can also signup from this page!

public class QRSignupPage extends QRRepresentation{
	
	
	public QRSignupPage(QRSquare realQR) {
		super(realQR.getText(), getStaticHtml(null));
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}
	public static String getStaticHtml(Object nullonb){
		return "<!DOCTYPE html>"
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
				+ "<div style='text-align: center;font-size: 36px; color:#FFFFFF'>This qr is free!<br>You can signup from this page</div>"
				+ "</body>"
				+ "</html>";
	}

}