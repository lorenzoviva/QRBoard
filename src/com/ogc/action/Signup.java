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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ogc.dbutility.DBConst;
import com.ogc.dialog.DialogBuilder;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.model.QRWebPage;
import com.ogc.model.special.QRSignupPage;
import com.ogc.model.special.QRSignupPasswordPage;

public class Signup extends Action {

	private QRSquare usersquare = null;
	private String password = null;
	private String firstname = null;
	private String lastname = null;
	
	ARGUI argui;
	@Override
	public void execute() {
		new QRSquareAction().execute();
		setState(2);
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		QRSquare qrSquare = argui.getQRSquare();
		usersquare = new QRSignupPasswordPage(qrSquare);
		usersquare.setOne(qrSquare.getOne());
		usersquare.setTwo(qrSquare.getTwo());
		usersquare.setThree(qrSquare.getThree());
		usersquare.setFour(qrSquare.getFour());
		argui.setQRSquare(usersquare,true);
		argui.setUsersquare(usersquare);
		DialogBuilder.createSignupDialog(context, this);
		this.argui = argui;
		setState(1);

	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		this.password = qrsquare.getText();
		execute();
	}

	public void addFirstnameParameter(String firstname) {
		this.firstname = firstname;
	}

	public void addLastnameParameter(String lastname) {
		this.lastname = lastname;
	}

	public class QRSquareAction extends AsyncTask<String, String, String> {

		

		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu

			Map<String, Object> paramap = new HashMap<String, Object>();
			ACL acl = new ACL(true, true);
			paramap.put("firstname", firstname);
			paramap.put("lastname", lastname);
			paramap.put("text", usersquare.getText());
			paramap.put("password", password);

			JSONObject paramjson = new JSONObject(paramap);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("action", "signup");
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
					
					String jsonstring = jsonresponse.getJSONObject("QRUser").toString();
					QRUser fromJson = (QRUser) gson.fromJson(jsonstring, QRUser.class);
					argui.setUser(fromJson);
					
					Log.d("FROM JSON", fromJson.toString());
					argui.finishAction("Successfully signup");
				}else{
					argui.finishAction("Unable to signup");

				}
			} catch (JSONException e) {
				argui.finishAction("Unable to signup");
			}

			return null;
		}

	}

	@Override
	public void prepare(ARGUI argui) {
		QRSignupPage signupPage = new QRSignupPage(argui.getQRSquare());
		argui.setQRSquare(signupPage, true);
		
	}

	@Override
	public int getColor(ARGUI argui) {
		return Color.GREEN;
	}

}
