package com.ogc.action;

import java.lang.reflect.Type;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.model.QRUsersWebPage;
import com.ogc.model.QRWebPage;

public class Users extends Action {
	private QRSquare qrSquare = null;
	private QRUser qrUser = null;
	private ARGUI argui;

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
		// TODO Auto-generated method stub

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
			map.put("action", "users");
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
					Gson gson = new Gson();

					String jsonstring = jsonresponse.getJSONArray("QRSquareUser").toString();
					Type listType = new TypeToken<ArrayList<QRSquareUser>>() {
					}.getType();
					List<QRSquareUser> fromJson = (List<QRSquareUser>) gson.fromJson(jsonstring, listType);
					String html = "";

					if (fromJson != null && !fromJson.isEmpty()) {
						QRUsersWebPage usersquare = new QRUsersWebPage(qrSquare.getText(), fromJson);
						usersquare.setOne(qrSquare.getOne());
						usersquare.setTwo(qrSquare.getTwo());
						usersquare.setThree(qrSquare.getThree());
						usersquare.setFour(qrSquare.getFour());
						argui.setQRSquare(usersquare, true);
						Log.d("FROM JSON", fromJson.toString());
						if(jsonresponse.has("action")){
							argui.finishAction(jsonresponse.getString("action"));
						}else{
							argui.finishAction("Unable to load users");
						}
					} else {
						argui.finishAction("Unable to load users");
					}

				} else {
					argui.finishAction("Unable to load users");
				}
			} catch (JSONException e) {
				Log.d("ERROR", e.getMessage());
				argui.finishAction("Unable to load users");
			}

			return null;
		}

	}

	@Override
	public void prepare(ARGUI argui) {
		QRUserMenager qrUserMenager = (QRUserMenager) argui.getQRSquare();
		qrUserMenager.setPassword("");
		argui.setQRSquare(qrUserMenager, true);

	}

	@Override
	public int getColor(ARGUI argui) {
		// TODO Auto-generated method stub
		return Color.YELLOW;
	}

}
