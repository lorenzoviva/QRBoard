package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;

public class QRSignupPage extends QRUserMenager{
	private static String signupHTML = "<h1 styleClass=\"margin:0 ;padding: 0;text-align: center;display:table-cell;vertical-align:middle; background:grey;height: 300px;width: 450px;\">this qr is free, you can also signup from this page!</h1>";
	public QRSignupPage(QRSquare realQR) {
		super(realQR.getText(), signupHTML, "");
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}

}