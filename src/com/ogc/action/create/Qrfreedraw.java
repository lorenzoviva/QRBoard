package com.ogc.action.create;

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
		new QRSquareAction().execute();
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
	
	public class QRSquareAction extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu

			Map<String, Object> paramap = new HashMap<String, Object>();
			ACL acl = new ACL(true, true);
			
			Bitmap whiteBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			for(int x = 0;x<500;x++){
				for(int y = 0;y<500;y++){
					whiteBitmap.setPixel(x, y, Color.WHITE);
				}
			}
			whiteBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    	byte[] byteArray = stream.toByteArray();
			
			
			paramap.put("img",GsonHelper.customGson.toJson(byteArray,byte[].class));
			
			paramap.put("name",qrSquare.getText());
			paramap.put("text", qrSquare.getText());
			paramap.put("classname", "QRFreeDraw");
			paramap.put("acl", acl.toJSON());
			if(user!=null){
				paramap.put("user", user.getId());
				paramap.put("owner", user.getId());
				
			}
			

			JSONObject paramjson = new JSONObject(paramap);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("action", "create");
			map.put("parameters", paramjson);
			JSONObject json = new JSONObject(map);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("json", json.toString()));
			
			try {
				JSONObject jsonresponse = jParser.makeHttpRequest(DBConst.url_action, "POST", params);
				boolean s = false;

				Log.d("Msg", jsonresponse.toString());
				s = jsonresponse.getBoolean("success");
				if (s) {
					argui.openFreeDrawActivity(jsonresponse.getJSONObject("QRSquare"),context);
				} else {
					argui.finishAction("Unable to create a drawing page ");
				}
			} catch (JSONException | HttpHostConnectException e) {
				Log.d("ERROR", e.getMessage());
				argui.finishAction("Unable to create a drawing page");
			}

			return null;
		}

	}


}
