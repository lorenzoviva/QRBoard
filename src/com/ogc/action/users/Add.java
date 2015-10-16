package com.ogc.action.users;

import android.content.Context;
import android.graphics.Color;

import com.example.qrboard.ARGUI;
import com.ogc.action.Action;
import com.ogc.model.QRSquare;

public class Add extends Action{
ARGUI argui;
	
	@Override
	public void execute() {
		super.execute();
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		this.argui = argui;
		execute();
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(ARGUI argui) {
		//we have a qrsquare		
	}

	@Override
	public int getColor(ARGUI argui) {
		return Color.RED;
	}
}
