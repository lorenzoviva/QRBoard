package com.ogc.model.special;

import com.ogc.model.QRRepresentation;
import com.ogc.model.QRUser;

public class QRUserRepresentation extends QRRepresentation{

	private QRUser user;
	public QRUserRepresentation(QRUser user) {
		super("QRUserRepresentation", getStaticnHtml(user));
		this.user = user;
	}
	public static String getStaticnHtml(QRUser user){
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
				+ "<body style='margin:0; background-color:#FFFFFF'>"
				+ "<div style='text-align: center; font-size: 32px; color:#000000'><b>First name:</b><br>"
				+ user.getFirstName()
				+"<br><b>Last name:</b><br>"
				+ user.getLastName()
				+"</div>"
				+ "</body>"
				+ "</html>";
		return html;
	}
	public QRUser getUser() {
		return user;
	}
	public void setUser(QRUser user) {
		this.user = user;
	}
	
}
