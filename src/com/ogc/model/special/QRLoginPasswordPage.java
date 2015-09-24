package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRWebPage;

public class QRLoginPasswordPage  extends QRWebPage{
	private static String loginPasswordHTML = "<h1 styleClass=\"margin:0 ;padding: 0;text-align: center;display:table-cell;vertical-align:middle; background:grey;height: 300px;width: 450px;\">Please frame the qr you choosed as password during signup. </h1>";
	public QRLoginPasswordPage(QRSquare qrSquare) {
		super(qrSquare.getText(), loginPasswordHTML);
		setOne(qrSquare.getOne());
		setTwo(qrSquare.getTwo());
		setThree(qrSquare.getThree());
		setFour(qrSquare.getFour());
	}


}
