package com.ogc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.qrboard.ARGUI;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.model.QRWebPage;

public class Request extends Action {
	private QRSquare qrSquare = null;
	private QRUser qrUser = null;
	private ARGUI argui;
	private static String accessDaniedHtml = "You cant see this qr!";
	
	@Override
	public void execute() {
		new QRSquareAction().execute();
		setState(2);
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		qrSquare = argui.getQRSquare();
		qrUser = argui.getUser();
		this.argui = argui;
		setState(1);
		execute();
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {

	}

	public class QRSquareAction extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu

			Map<String, Object> paramap = new HashMap<String, Object>();
			ACL acl = new ACL(true, true);
			paramap.put("text", qrSquare.getText());
			paramap.put("user", qrUser.getId());

			JSONObject paramjson = new JSONObject(paramap);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("action", "request");
			map.put("parameters", paramjson);
			JSONObject json = new JSONObject(map);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("json", json.toString()));
			JSONObject jsonresponse = jParser.makeHttpRequest(DBConst.url_action, "POST", params);
			boolean s = false;

			Log.d("Msg", jsonresponse.toString());
			try {
				s = jsonresponse.getBoolean("success");
				if (s) {
					argui.finishAction("Successfully requested");
				} else {
					argui.finishAction("Unable to request");

				}
			} catch (JSONException e) {
				Log.d("ERROR", e.getMessage());
				argui.finishAction("Unable to request");
			}

			return null;
		}

	}

	@Override
	public void prepare(ARGUI argui) {
		QRWebPage accessDaniedWebPage = new QRWebPage(argui.getQRSquare().getText(), accessDaniedHtml);
		argui.setQRSquare(accessDaniedWebPage, true);
		
	}

	@Override
	public int getColor(ARGUI argui) {
		
		return Color.rgb(244, 102, 2);
	}
}