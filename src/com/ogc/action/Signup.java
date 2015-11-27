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
import android.renderscript.Type;
import android.util.Log;

import com.example.qrboard.ARGUI;
import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.google.gson.JsonSyntaxException;
import com.ogc.dbutility.DBConst;
import com.ogc.dialog.DialogBuilder;
import com.ogc.model.ACL;
import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.special.QRSignupPage;
import com.ogc.model.special.QRSignupPasswordPage;

public class Signup extends Action {

	private QRSquare usersquare = null;
	private String password = null;
	private String firstname = null;
	private String lastname = null;
	private boolean useQrPassword = false;
	
	ARGUI argui;
	@Override
	public void execute() {
		super.execute();
		new QRSquareAction().execute();
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		QRSquare qrSquare = argui.getQRSquare();
		usersquare = new QRSignupPasswordPage(qrSquare);
		usersquare.setOne(qrSquare.getOne());
		usersquare.setTwo(qrSquare.getTwo());
		usersquare.setThree(qrSquare.getThree());
		usersquare.setFour(qrSquare.getFour());
		argui.setQRSquare(usersquare,true);
		DialogBuilder.createSignupDialog(context, this);
		this.argui = argui;
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
	
	public void addPasswordParameter(String passwordtext) {
		this.password = passwordtext;
		execute();
	}

	public void addCheckQRPassword(boolean checkQRPassword) {
		this.useQrPassword = checkQRPassword;
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
			paramap.put("useQrPassword", useQrPassword);

			JSONObject paramjson = new JSONObject(paramap);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("action", "signup");
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
					String jsonstring = jsonresponse.getJSONObject("user").toString();
					String jsonaction = jsonresponse.getString("action");
					String jsonsquare = jsonresponse.getJSONObject("QRSquare").toString();
					String jsontype = jsonresponse.getString("type");

					QRUser fromJson = (QRUser) gson.fromJson(jsonstring, QRUser.class);
					QRSquare fromJsonSquare;					
					try {
						fromJsonSquare = (QRSquare) gson.fromJson(jsonsquare, Class.forName(jsontype));
						
						
						argui.setUser(fromJson);
						argui.setUsersquare(fromJsonSquare, jsonaction);

						Log.d("FROM JSON", fromJson.toString());
						argui.finishAction("Successfully signup");
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}else{
					argui.finishAction("Unable to signup");

				}
			} catch (JSONException | HttpHostConnectException e) {
				argui.finishAction("Unable to signup");
			}

			return null;
		}

	}

	@Override
	public void prepare(ARGUI argui) {
		QRSignupPage signupPage = new QRSignupPage(argui.getQRSquare());
		argui.setQRSquare((QRInternalWebPage)signupPage, true);
		
	}

	@Override
	public int getColor(ARGUI argui) {
		return Color.GREEN;
	}

}
