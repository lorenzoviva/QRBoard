package com.ogc.model.special;

import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquareUser;

public class QRSquareUserRepresentation extends QRRepresentation {

	private QRSquareUser squareuser;
	private String squareText;
	public QRSquareUserRepresentation(QRSquareUser squareuser,String squareText) {
		super("QRSquareUserRepresentation", getStaticnHtml(squareuser));
		this.squareuser = squareuser;
		this.squareText = squareText;
	}

	public static String getStaticnHtml(QRSquareUser squareuser) {
		String html = "";
		if (squareuser.getIsnew()) {
			html = "ADD YOUR FRIEND";
		} else {
			html = "<!DOCTYPE html>"
					+ "<html>"
					+ "<head>"
					+ "<style type='text/css'>"
					+ "@font-face {"
					+ "font-family: myfont;"
					+ "src: url('OpenSans-Light.ttf'); }"
					+ "body {font-family: myfont;}"
					+ "</style>"
					+ "</head>"
					+ "<body style='margin:0; background-color:#FFFFFF'>"
					+ "<div style='text-align: center; font-size: 32px; color:#000000'><b>Role:</b><br>"
					+ squareuser.getRole().getName()
					+"<br><b>Creation date:</b><br>"
					+ squareuser.getDate()
					+"</div>"
					+ "</body>"
					+ "</html>";
		}
		return html;
	}

	public QRSquareUser getSquareuser() {
		return squareuser;
	}

	public void setSquareuser(QRSquareUser squareuser) {
		this.squareuser = squareuser;
	}

	public String getSquareText() {
		return squareText;
	}
}
