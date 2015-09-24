package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRWebPage;

public class QRSignupPasswordPage extends QRWebPage{
	private static String signupPasswordHTML = "<h1 styleClass=\"margin:0 ;padding: 0;text-align: center;display:table-cell;vertical-align:middle; background:grey;height: 300px;width: 450px;\">Please choose a qr to use as a password to finish signup. Remember this qr, you will be asked for it every time you need to login.</h1>";
	public QRSignupPasswordPage(QRSquare qrSquare) {
		super(qrSquare.getText(), signupPasswordHTML);
		setOne(qrSquare.getOne());
		setTwo(qrSquare.getTwo());
		setThree(qrSquare.getThree());
		setFour(qrSquare.getFour());
	}


}
