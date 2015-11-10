package com.ogc.action.edit;

import org.json.JSONObject;

import android.content.Context;

import com.example.qrboard.ARGUI;
import com.google.gson.GsonHelper;
import com.ogc.action.Action;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class Qrfreedraw extends Action{

	private ARGUI argui;
	private QRSquare qrSquare;
	private QRUser user;
	private Context context;
	
	@Override
	public int getColor(ARGUI argui) {
		return 0;
	}

	@Override
	public void prepare(ARGUI argui) {

	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {

	}
	
	@Override
	public void execute() {
		super.execute();
		try {
			argui.setActionContext("");
			argui.openFreeDrawActivity(new JSONObject(GsonHelper.customGson.toJson(qrSquare)),context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		this.argui = argui;
		this.user = argui.getUser();
		this.context = context;
		qrSquare = argui.getQRSquare();
		execute();
		
	}
	

}
