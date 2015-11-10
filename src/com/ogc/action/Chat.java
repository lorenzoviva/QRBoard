package com.ogc.action;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.example.qrboard.ARGUI;
import com.example.qrboard.ChatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.ogc.model.QRChat;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class Chat extends Action{

	private ARGUI argui;
	private QRUser user;
	private Context context;
	private QRChat qrSquare;

	@Override
	public int getColor(ARGUI argui) {
		return Color.rgb(36,44,250);
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
		qrSquare.onClose();
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra("qrchat", qrSquare.toJSONObject().toString());
		if(user!=null){
			intent.putExtra("qruser",(GsonHelper.customGson.toJsonTree(user,QRUser.class)).toString());

		}
		argui.finishAction("");
		argui.setActionContext("");
		context.startActivity(intent);
		
		argui.setQRSquare(null, true);
		if (argui.getAction() != null) {
			argui.finishAction("");
		}
		((Activity) context).finish();
		
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		this.argui = argui;
		this.user = argui.getUser();
		this.context = context;
		qrSquare = (QRChat) argui.getQRSquare();
		execute();
		
	}
	
	
}
