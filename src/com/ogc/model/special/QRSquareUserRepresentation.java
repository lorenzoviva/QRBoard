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
		if (squareuser == null) {
			html = "ADD YOUR FRIEND";
		} else {
			html = "Role name : " + squareuser.getRole().getName() + " <br> Date : " + squareuser.getDate();
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
