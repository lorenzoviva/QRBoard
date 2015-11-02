package com.ogc.model;

public class QRUserRepresentation extends QRInternalWebPage{

	private QRUser user;
	public QRUserRepresentation(QRUser user) {
		super("QRUserRepresentation", getRepresentationHtml(user));
		this.user = user;
	}
	public static String getRepresentationHtml(QRUser user){
		String html = "First name : " + user.getFirstName() +" <br> Last name : " + user.getLastName() ;
		
		return html;
	}
	
}
