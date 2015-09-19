package com.ogc.action;

import android.content.Context;
import android.graphics.Color;

import com.example.qrboard.ARGUI;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;
import com.ogc.model.special.QRFreePage;
import com.ogc.model.special.QRSignupPage;


public class Create extends Action {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(ARGUI argui) {
		if(!(argui.getQRSquare() instanceof QRSignupPage)){
			QRFreePage freeQRWebPage = new QRFreePage(argui.getQRSquare());
			argui.setQRSquare(freeQRWebPage, true);
		}
	}
	@Override
	public int getColor(ARGUI argui) {
		return Color.MAGENTA;
	}
}
