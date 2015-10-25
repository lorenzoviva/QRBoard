package com.ogc.action;

import android.content.Context;
import android.graphics.Color;

import com.example.qrboard.ARGUI;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class Edit extends Action{
	private QRSquare qrSquare;
	private ARGUI argui;
	private Context context;
	@Override
	public void execute() {
		super.execute();
		if(qrSquare!=null){
			argui.setActionContext("edit");
			argui.performAction(qrSquare.getClass().getSimpleName().toLowerCase(), context);
		}
		
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		qrSquare = argui.getQRSquare();
		this.argui = argui;
		this.context = context;
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
