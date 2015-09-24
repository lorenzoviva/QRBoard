package com.ogc.action;

import android.content.Context;
import android.graphics.Color;

import com.example.qrboard.ARGUI;
import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;

public class Logout extends Action{

	private ARGUI argui;

	@Override
	public void execute() {
		super.execute();
		argui.finishAction("Successfully logged out");
		
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		argui.setUser(null);
		argui.setQRSquare(null, true);
		this.argui = argui;
		execute();
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		execute();;
		
	}
	public void prepare(ARGUI argui) {
		QRUserMenager qrUserMenager = (QRUserMenager) argui.getQRSquare();
		qrUserMenager.setPassword("");
		argui.setQRSquare((QRInternalWebPage)qrUserMenager, true);
	}
	@Override
	public int getColor(ARGUI argui) {
		return Color.rgb(250, 110, 110);
	}
}
