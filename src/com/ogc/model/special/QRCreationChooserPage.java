package com.ogc.model.special;

import android.util.Log;

import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquare;

public class QRCreationChooserPage extends QRRepresentation{

	public QRCreationChooserPage(QRSquare realQR, String choises) {
		super(realQR.getText(), getStaticHtml(choises));
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}
	
	public static String getStaticHtml(Object nullobj) {
			String html = "<!DOCTYPE html>"
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
					+ "<div style='text-align: center; font-size: 32px; color:#FFFFFF'>Choose content to create on this QR code.</div>"
					+ "</body>"
					+ "</html>";
			return html;
	}

}
