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
				+ "<link rel='stylesheet' href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css' rel='stylesheet' type='text/css'>"
				+ "</head>"
				+ "<body style='margin:0; background-color:#5DB5E0'>"
				+ "<div style='text-align: center; color: white; font-family: fantasy; font-size: 60px; text-shadow: -2px -2px 0 #000, 2px -2px 0 #000, -2px 2px 0 #000, 2px 2px 0 #000;'>THIS<br>QR IS<br>FREE</div>"
				+ "</body>"
				+ "</html>";
	}
}
