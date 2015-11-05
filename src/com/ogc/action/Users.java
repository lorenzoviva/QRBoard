
package com.ogc.action;

import java.lang.reflect.Type;
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
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.qrboard.ARGUI;
import com.example.qrboard.ExploreActivity;
import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.google.gson.reflect.TypeToken;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.model.QRUsersWebPage;
import com.ogc.model.special.QRUserRepresentation;

public class Users extends Action {
	private QRSquare qrSquare = null;
	private QRUser qrUser = null;
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
		qrSquare = argui.getQRSquare();
		qrUser = argui.getUser();
		this.argui = argui;
		this.context = context;
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
			if(!(qrSquare instanceof QRUserRepresentation)){
				paramap.put("text", qrSquare.getText());
				paramap.put("request", 1);
			}else{
				paramap.put("QRUser", ((QRUserRepresentation) qrSquare).getUser().getId());
				paramap.put("request", 3);
			}
			if(qrUser!=null){
				paramap.put("user", qrUser.getId());
			}
			JSONObject paramjson = new JSONObject(paramap);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("action", "users");
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
					Intent intent = new Intent(context,ExploreActivity.class);
					intent.putExtra("response", jsonresponse.toString());
					context.startActivity(intent);
				} else {
					argui.finishAction("Unable to load users");
				}
			} catch (JSONException | HttpHostConnectException e) {
				Log.d("ERROR", e.getMessage());
				argui.finishAction("Unable to load users");
			}

			return null;
		}

	}

	@Override
	public void prepare(ARGUI argui) {
//	QRUserMenager qrUserMenager = (QRUserMenager) argui.getQRSquare();
////		qrUserMenager.setPassword("");
//		argui.setQRSquare((QRInternalWebPage)qrUserMenager, true);

	}

	@Override
	public int getColor(ARGUI argui) {
		// TODO Auto-generated method stub
		return Color.YELLOW;
	}

}
