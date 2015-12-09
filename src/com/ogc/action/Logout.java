package com.ogc.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import com.example.qrboard.ARGUI;
import com.example.qrboard.R;
import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;

public class Logout extends Action{

	private ARGUI argui;
	private Context context;

	@Override
	public void execute() {
		super.execute();
		argui.finishAction("Successfully logged out");
		argui.saveStateLogout(context);
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		argui.setUser(null);
		argui.setQRSquare(null, true);
		this.context = context;
		this.argui = argui;
		execute();
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		execute();;
		
	}
	public void prepare(ARGUI argui) {
//		QRUserMenager qrUserMenager = (QRUserMenager) argui.getQRSquare();
//		qrUserMenager.setPassword("");
//		argui.setQRSquare((QRInternalWebPage)qrUserMenager, true);
	}
	@Override
	public int getColor(ARGUI argui) {
		return Color.rgb(250, 110, 110);
	}
	@Override
	public Bitmap getIcon(View view) {
		return BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.actionlogout);
	}
}
