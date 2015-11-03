package com.ogc.model.special;

import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;

public class QRFreePage extends QRInternalWebPage{
	private static String freeQRHTML = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<link rel='stylesheet' href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css' rel='stylesheet' type='text/css'>"
			+ "</head>"
			+ "<body style='margin:0; background-color:#5DB5E0'>"
			+ "<div style='text-align: center; color: white; font-family: fantasy; font-size: 60px; text-shadow: -2px -2px 0 #000, 2px -2px 0 #000, -2px 2px 0 #000, 2px 2px 0 #000;'>THIS<br>QR IS<br>FREE</div>"
			+ "</body>"
			+ "</html>";
	public QRFreePage(QRSquare realQR) {
		super(realQR.getText(), freeQRHTML);
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}

}
