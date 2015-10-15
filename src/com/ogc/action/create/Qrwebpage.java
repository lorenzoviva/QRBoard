package com.ogc.action.create;

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
import android.os.AsyncTask;
import android.util.Log;

import com.example.qrboard.ARGUI;
import com.google.gson.Gson;
import com.ogc.action.Action;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;
import com.ogc.model.QRWebPage;
import com.ogc.model.QRWebPageEditor;

public class Qrwebpage extends Action{
	ARGUI argui;
	QRSquare qrSquare;
	QRUser qrUser;
	
	@Override
	public void execute() {
		super.execute();
		new QRSquareAction().execute();
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		this.argui = argui;
		qrUser = argui.getUser();
		qrSquare = argui.getQRSquare();
		execute();
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(ARGUI argui) {
		
	}

	@Override
	public int getColor(ARGUI argui) {
		return 0;
	}

	public class QRSquareAction extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu

			Map<String, Object> paramap = new HashMap<String, Object>();
			ACL acl = new ACL(true, true);
			paramap.put("html", "new page");
			paramap.put("text", qrSquare.getText());
			paramap.put("classname", "QRWebPage");
			paramap.put("acl", acl.toJSON());
			if(qrUser!=null){
				paramap.put("user", qrUser.getId());
				paramap.put("owner", qrUser.getId());
				
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
					QRWebPage newsquare = (new Gson()).fromJson(jsonresponse.getJSONObject("QRSquare").toString(), QRWebPage.class);
					QRWebPageEditor qrChooser = new QRWebPageEditor(newsquare);
					argui.setQRSquare(qrChooser, false);
					argui.setActionContext("create.qrwebpage");
					argui.finishAction("save,access,add,edit,remove,exit,");
				} else {
					argui.finishAction("Unable to request choises ");

				}
			} catch (JSONException | HttpHostConnectException e) {
				Log.d("ERROR", e.getMessage());
				argui.finishAction("Unable to request");
			}

			return null;
		}

	}
	
}
