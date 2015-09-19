package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;
import com.ogc.model.QRWebPage;

public class QRLoginPage extends QRUserMenager{
	private static String loginHtml = "<h1 styleClass='margin:0 ;padding: 0;text-align: center;display:table-cell;vertical-align:middle; background:grey;height: 300px;width: 450px;'>Hello, this is a User's personal qr. Do you want to login?</h1>";
	public QRLoginPage(QRSquare realQR) {
		super(realQR.getText(), loginHtml, "");
	}

}
