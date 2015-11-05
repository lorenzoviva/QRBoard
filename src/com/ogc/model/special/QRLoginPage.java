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
				+ "<link rel='stylesheet' href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css' rel='stylesheet' type='text/css'>"
				+ "</head>"
				+ "<body style='margin:0; background-color:#5DB5E0;'>"
				+ "<div style='text-align: center; font-family: sans-serif;font-size: 32px; color:#FFFFFF'>Hello!<br>This is a User's personal QR.<br><br>Do you want to login?</div>"
				+ "</body>"
				+ "</html>";
	}
}
