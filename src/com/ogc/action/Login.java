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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.renderscript.Type;
import android.util.Log;
import android.view.View;

import com.example.qrboard.ARGUI;
import com.example.qrboard.R;
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
import com.ogc.model.QRWebPage;
import com.ogc.model.special.QRLoginPage;
import com.ogc.model.special.QRLoginPasswordPage;

public class Login extends Action {

	private QRWebPage usersquare;
	private String password;
	private ARGUI argui;
	private Context context;
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
		DialogBuilder.createLoginDialog(context, this);
		this.argui = argui;
		this.context=context;
		argui.dismissActionDialog();
		
	}
	public void addPasswordParameter(String passwordtext){
		this.password = passwordtext;
		execute();
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

					String jsonstring = jsonresponse.getJSONObject("user").toString();
					String jsonaction = jsonresponse.getString("action");
					String jsonsquare = jsonresponse.getJSONObject("QRSquare").toString();
					String jsontype = jsonresponse.getString("type");
					
					QRUser fromJson = (QRUser) gson.fromJson(jsonstring, QRUser.class);
					QRSquare fromJsonSquare;
					try {
						fromJsonSquare = (QRSquare) gson.fromJson(jsonsquare, Class.forName(jsontype));
						QRSquare currentqrsquare = argui.getQRSquare();
						fromJsonSquare.setShape(currentqrsquare);
						argui.setUser(fromJson);
						argui.setUsersquare(fromJsonSquare, jsonaction);
						argui.setQRSquare(fromJsonSquare, true);
						argui.saveState(jsonsquare,jsontype,jsonaction,context);
						Log.d("FROM JSON", fromJson.toString());
						argui.finishAction("Successfully login");
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					argui.finishAction("Unable to login");
				}
			} catch (JSONException | HttpHostConnectException e) {
				Log.d("errore login:",e.getMessage());
				argui.finishAction("Unable to login");
			}

			return null;
		}

	}

	@Override
	public void prepare(ARGUI argui) {
		QRLoginPage qrUserMenager = new QRLoginPage(argui.getQRSquare());
		argui.setQRSquare((QRInternalWebPage)qrUserMenager, true);
	}
	@Override
	public int getColor(ARGUI argui) {
		return Color.GREEN;
	}
	@Override
	public Bitmap getIcon(View view) {
		return BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.actionlogin);
	}
}
