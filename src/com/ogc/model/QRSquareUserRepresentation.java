package com.ogc.model;

public class QRSquareUserRepresentation extends QRInternalWebPage{

	private QRSquareUser squareuser;
	public QRSquareUserRepresentation(QRSquareUser squareuser){
		super("QRSquareUserRepresentation",getRepresentationHtml(squareuser));
		this.squareuser = squareuser;
	}
	public static String getRepresentationHtml(QRSquareUser squareuser){
		String html = "Role name : " + squareuser.getRole().getName() +" <br> Date : " + squareuser.getDate() ;
		
		return html;
	}
}
