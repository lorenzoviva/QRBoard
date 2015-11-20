package com.ogc.model.special;

import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquare;
import com.ogc.model.QRWebPage;

public class QRLoginPasswordPage  extends QRRepresentation{
	
		
	public QRLoginPasswordPage(QRSquare qrSquare) {
		super(qrSquare.getText(), getStaticHtml(null));
		setOne(qrSquare.getOne());
		setTwo(qrSquare.getTwo());
		setThree(qrSquare.getThree());
		setFour(qrSquare.getFour());
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
				+ "<body style='margin:0; background-color:#5DB5E0'>"
				+ "<div style='text-align: center; font-size: 32px; color:#FFFFFF'>Please frame the QR you choosed as password during signup.</div>"
				+ "</body>"
				+ "</html>";
	}


}
