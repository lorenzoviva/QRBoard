package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;
import com.ogc.model.QRWebPage;

public class QRLoginPage extends QRUserMenager{
	private static String loginHtml = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,300,700' rel='stylesheet' type='text/css'>"
			+ "</head>"
			+ "<body style='margin:0; background-color:#5DB5E0'>"
			+ "<div style='text-align: center; font-family: 'Open Sans', sans-serif;font-size: 32px;font-weight: 300;'>Hello!<br>This is a User's personal QR.<br><br>Do you want to login?</div>"
			+ "</body>"
			+ "</html>";
	public QRLoginPage(QRSquare realQR) {
		super(realQR.getText(), loginHtml, "");
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}

}
