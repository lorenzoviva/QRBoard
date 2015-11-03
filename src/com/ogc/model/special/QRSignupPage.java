package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;
// this qr is free, you can also signup from this page!

public class QRSignupPage extends QRUserMenager{
	private static String signupHTML = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<link rel='stylesheet' href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css' rel='stylesheet' type='text/css'>"
			+ "</head>"
			+ "<body style='margin:0; background-color:#5DB5E0;'>"
			+ "<div style='text-align: center; font-family: sans-serif;font-size: 24px; color:#FFFFFF'>This qr is free!<br>You can signup from this page</div>"
			+ "</body>"
			+ "</html>";
	public QRSignupPage(QRSquare realQR) {
		super(realQR.getText(), signupHTML, "");
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}

}