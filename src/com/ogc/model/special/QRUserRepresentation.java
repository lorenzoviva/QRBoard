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
		String html = "First name : " + user.getFirstName() +" <br> Last name : " + user.getLastName();
		return html;
	}
	public QRUser getUser() {
		return user;
	}
	public void setUser(QRUser user) {
		this.user = user;
	}
	
}
