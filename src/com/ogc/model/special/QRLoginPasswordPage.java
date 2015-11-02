package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRWebPage;

public class QRLoginPasswordPage  extends QRWebPage{
	private static String loginPasswordHTML = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,300,700' rel='stylesheet' type='text/css'>"
			+ "</head>"
			+ "<body style='margin:0; background-color:#5DB5E0'>"
			+ "<div style='text-align: center; font-family: 'Open Sans', sans-serif;font-size: 32px;font-weight: 300;'>Please frame the qr you choosed as password during signup.</div>"
			+ "</body>"
			+ "</html>";
	public QRLoginPasswordPage(QRSquare qrSquare) {
		super(qrSquare.getText(), loginPasswordHTML);
		setOne(qrSquare.getOne());
		setTwo(qrSquare.getTwo());
		setThree(qrSquare.getThree());
		setFour(qrSquare.getFour());
	}


}
