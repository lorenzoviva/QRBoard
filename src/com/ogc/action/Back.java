package com.ogc.action;

import android.content.Context;
import android.graphics.Color;

import com.example.qrboard.ARGUI;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ogc.model.QRSquare;

public class Back extends Action{
	ARGUI argui;
	
	@Override
	public void execute() {
		setState(2);
		argui.goToLastQRSquare();
		argui.finishAction(argui.getLastactions());
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		this.argui = argui;
		setState(1);
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
