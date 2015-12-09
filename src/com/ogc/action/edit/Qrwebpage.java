package com.ogc.action.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.example.qrboard.ARGUI;
import com.ogc.action.Action;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class Qrwebpage extends Action{
	
	private ARGUI argui;
	private Context context;
	
	
	@Override
	public void execute() {
		argui.setActionContext("");
		if(argui.getUser()!=null){
			argui.openEditWebPageActivity(context,null,argui.getUser().getId());
		}else{
			argui.openEditWebPageActivity(context,null,-1);
		}
		
	}
	@Override
	public void perform(ARGUI argui, Context context) {
		this.argui = argui;
		this.context = context;
		execute();

	}
	@Override
	public int getColor(ARGUI argui) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void prepare(ARGUI argui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Bitmap getIcon(View view) {
		return null;
	}

}
