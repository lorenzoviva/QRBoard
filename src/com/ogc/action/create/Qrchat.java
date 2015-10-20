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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.qrboard.ARGUI;
import com.google.gson.GsonHelper;
import com.ogc.action.Action;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class Qrchat extends Action{

	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
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
			ACL acl = new ACL(true, false);
			
			
			
//			paramap.put("img",GsonHelper.customGson.toJson(byteArray,byte[].class));
			
			paramap.put("name",qrSquare.getText());
			paramap.put("text", qrSquare.getText());
			paramap.put("classname", "QRChat");
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
					argui.finishAction("Unable to create a chat page ");
				}
			} catch (JSONException | HttpHostConnectException e) {
				Log.d("ERROR", e.getMessage());
				argui.finishAction("Unable to create a chat page");
			}

			return null;
		}

	}

}