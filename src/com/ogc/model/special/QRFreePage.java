package com.ogc.model.special;

import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;

public class QRFreePage extends QRUserMenager{
	private static String freeQRHTML = "<h1 styleClass=\"margin:0 ;padding: 0;text-align: center;display:table-cell;vertical-align:middle; background:grey;height: 300px;width: 450px;\">This qr is free!</h1>";
	public QRFreePage(QRSquare realQR) {
		super(realQR.getText(), freeQRHTML, "");
	}

}