package com.ogc.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import com.example.qrboard.ARGUI;
import com.example.qrboard.R;
import com.ogc.model.QRSquare;

public class Back extends Action{
	ARGUI argui;
	QRSquare square;

	@Override
	public void execute() {
		super.execute();

		if (square != null && argui.getLastqrsquare() != null) {
			square.onClose();
			argui.goToLastQRSquare();
			argui.setActionContext("");
			argui.finishAction(argui.getLastactions());
		}

	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		this.argui = argui;
		this.square = argui.getQRSquare();
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
			return Color.rgb(112, 21, 34);
		}
	}

	@Override
	public Bitmap getIcon(View view) {
		return BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.back32x32);
	}

	
	
}
