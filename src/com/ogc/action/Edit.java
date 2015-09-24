package com.ogc.action;

import android.content.Context;
import android.graphics.Color;

import com.example.qrboard.ARGUI;
import com.ogc.action.Create.QRSquareAction;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class Edit extends Action{
	private QRSquare qrSquare;
	private QRUser qrUser;
	private ARGUI argui;
	@Override
	public void execute() {
		super.execute();
		
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		qrSquare = argui.getQRSquare();
		qrUser = argui.getUser();
		this.argui = argui;
		execute();
		
	}
	@Override
	public int getColor(ARGUI argui) {
		// TODO Auto-generated method stub
		return Color.MAGENTA;
	}

	@Override
	public void prepare(ARGUI argui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
	}

}
