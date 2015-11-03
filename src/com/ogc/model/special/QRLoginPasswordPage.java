package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRWebPage;

public class QRLoginPasswordPage  extends QRWebPage{
	private static String loginPasswordHTML = "<!DOCTYPE html>"
			+ "<html>"
			+ "<head>"
			+ "<link rel='stylesheet' href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css' rel='stylesheet' type='text/css'>"
			+ "</head>"
			+ "<body style='margin:0; background-color:#5DB5E0'>"
			+ "<div style='text-align: center; font-family: sans-serif;font-size: 32px;font-weight: 300;'>Please frame the QR you choosed as password during signup.</div>"
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
