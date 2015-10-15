package com.ogc.action.create;

import android.content.Context;
import android.graphics.Color;

import com.example.qrboard.ARGUI;
import com.ogc.action.Action;
import com.ogc.model.QRSquare;

public class Back extends Action{
	ARGUI argui;
	
	@Override
	public void execute() {
		super.execute();
		argui.goToLastQRSquare();
		argui.setActionContext("");
		argui.finishAction(argui.getLastactions());
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
		if(argui.getQRSquare()!=null && argui.getLastqrsquare()!=null){
			return Color.GRAY;
		}else{
			return Color.rgb(112, 21, 54);
		}
	}

	
	
}
