package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRWebPage;
// Please choose a qr to use as a password to finish signup. Remember this qr, you will be asked for it every time you need to login.

public class QRSignupPasswordPage extends QRWebPage{
	private static String signupPasswordHTML = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<link rel='stylesheet' href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css' rel='stylesheet' type='text/css'>"
			+ "</head>"
			+ "<body style='margin:0; background-color:#5DB5E0'>"
			+ "<div style='text-align: center; font-family: sans-serif;font-size: 24px; color:#FFFFFF'>Please, frame another QR to use as a password</div>"
			+ "</body>"
			+ "</html>";
	public QRSignupPasswordPage(QRSquare qrSquare) {
		super(qrSquare.getText(), signupPasswordHTML);
		setOne(qrSquare.getOne());
		setTwo(qrSquare.getTwo());
		setThree(qrSquare.getThree());
		setFour(qrSquare.getFour());
	}


}
