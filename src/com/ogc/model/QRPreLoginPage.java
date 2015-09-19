package com.ogc.model;

public class QRPreLoginPage extends QRUserMenager{
	private static String loginHtml = "hello ! wanna login? =)";
	public QRPreLoginPage(String text, String password) {
		super(text, loginHtml, password);
	}

}
