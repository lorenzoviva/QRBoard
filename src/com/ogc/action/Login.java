package com.ogc.action;

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
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.qrboard.ARGUI;
import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;
import com.ogc.model.QRWebPage;
import com.ogc.model.special.QRLoginPage;
import com.ogc.model.special.QRLoginPasswordPage;

public class Login extends Action {

	private QRWebPage usersquare;
	private String password;
	private ARGUI argui;
	
	@Override
	public void execute() {
		super.execute();
		new QRSquareAction().execute();
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		QRSquare qrSquare = argui.getQRSquare();
		usersquare = new QRLoginPasswordPage(qrSquare);
		usersquare.setOne(qrSquare.getOne());
		usersquare.setTwo(qrSquare.getTwo());
		usersquare.setThree(qrSquare.getThree());
		usersquare.setFour(qrSquare.getFour());
		argui.setQRSquare(usersquare, true);
		this.argui = argui;
		
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		this.password = qrsquare.getText();
		execute();

	}

	public class QRSquareAction extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu

			Map<String, Object> paramap = new HashMap<String, Object>();
			ACL acl = new ACL(true, true);
			paramap.put("text", usersquare.getText());
			paramap.put("password", password);

			JSONObject paramjson = new JSONObject(paramap);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("action", "login");
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
					Gson gson = GsonHelper.customGson;

					String jsonstring = jsonresponse.getJSONObject("QRUser").toString();
					QRUser fromJson = (QRUser) gson.fromJson(jsonstring, QRUser.class);
					argui.setUser(fromJson);

					Log.d("FROM JSON", fromJson.toString());
					argui.finishAction("Successfully login");
				}else{
					argui.finishAction("Unable to login");
				}
			} catch (JSONException | HttpHostConnectException e) {
				argui.finishAction("Unable to login");
			}

			return null;
		}

	}

	@Override
	public void prepare(ARGUI argui) {
		argui.setUsersquare(argui.getQRSquare());
		QRLoginPage qrUserMenager = new QRLoginPage(argui.getQRSquare());
		argui.setQRSquare((QRInternalWebPage)qrUserMenager, true);
		
		
	}
	@Override
	public int getColor(ARGUI argui) {
		return Color.GREEN;
	}
}
