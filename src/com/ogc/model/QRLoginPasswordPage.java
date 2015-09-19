package com.ogc.model;

public class QRLoginPasswordPage extends QRUserMenager{
	private static String loginHtml = "please frame the qr you choosed as a password to finish authentication.";

	public QRLoginPasswordPage(String text, String password) {
		super(text, loginHtml, password);
		// TODO Auto-generated constructor stub
	}


}
