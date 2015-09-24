package com.ogc.action;

import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;

import com.example.qrboard.ARGUI;
import com.google.gson.JsonObject;
import com.ogc.dbutility.JSONParser;
import com.ogc.model.QRSquare;

public abstract class Action {
	private int state = 0;// 0 : null , 1 : performing , 2 : executing
	protected JSONParser jParser = new JSONParser();
	
	public abstract int getColor(ARGUI argui);
	public void execute(){
		setState(2);
	}
	public void perform(ARGUI argui, Context context){
		setState(1);
	}
	public abstract void prepare(ARGUI argui);
	public static String correctActionName(String string) {
		if (string.length() > 1) {
			return string.substring(0, 1).toUpperCase(Locale.ROOT).concat(string.substring(1, string.length()));
		} else {
			return string.toUpperCase(Locale.ROOT);

		}
	}

	public abstract void addQRParameter(QRSquare qrsquare);

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
