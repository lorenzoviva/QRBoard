package com.ogc.action.edit;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.qrboard.ARGUI;
import com.example.qrboard.ScanActivity;
import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.ogc.action.Action;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRFreeDraw;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;
import com.ogc.model.QRWebPage;
import com.ogc.model.QRWebPageEditor;

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
