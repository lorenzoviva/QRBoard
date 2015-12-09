package com.ogc.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import com.example.qrboard.ARGUI;
import com.example.qrboard.R;
import com.ogc.model.QRSquare;
import com.ogc.model.QRWebPage;

public class Link extends Action{

	private ARGUI argui;
	
	@Override
	public void execute() {
		super.execute();
		argui.finishAction("Back");
		
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		QRSquare qrsquare = argui.getQRSquare();
		QRWebPage qrwebpage = new QRWebPage(qrsquare.getText(), qrsquare.getText());
		qrwebpage.setOne(qrsquare.getOne());
		qrwebpage.setTwo(qrsquare.getTwo());
		qrwebpage.setThree(qrsquare.getThree());
		qrwebpage.setFour(qrsquare.getFour());
		argui.setQRSquare(qrwebpage, true);
		this.argui = argui;
		execute();
		
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(ARGUI argui) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getColor(ARGUI argui) {
		return Color.BLUE;
	}
	@Override
	public Bitmap getIcon(View view) {
		return BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.actionlink);
	}
}
