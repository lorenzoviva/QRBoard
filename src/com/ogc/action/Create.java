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
import com.ogc.action.Request.QRSquareAction;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.model.special.QRCreationChooserPage;
import com.ogc.model.special.QRFreePage;
import com.ogc.model.special.QRSignupPage;


public class Create extends Action {
	private QRSquare qrSquare = null;
	private QRUser qrUser = null;
	private ARGUI argui;
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
		execute();
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(ARGUI argui) {
		if(!(argui.getQRSquare() instanceof QRSignupPage)){
			QRFreePage freeQRWebPage = new QRFreePage(argui.getQRSquare());
			argui.setQRSquare(freeQRWebPage, true);
		}
	}
	@Override
	public int getColor(ARGUI argui) {
		return Color.MAGENTA;
	}
	
	public class QRSquareAction extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu

			Map<String, Object> paramap = new HashMap<String, Object>();
			ACL acl = new ACL(true, true);
			paramap.put("from", "create");
			paramap.put("text", qrSquare.getText());
			if(qrUser!=null){
				paramap.put("user", qrUser.getId());
			}

			JSONObject paramjson = new JSONObject(paramap);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("action", "choice");
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
					QRCreationChooserPage qrChooser = new QRCreationChooserPage(qrSquare,jsonresponse.getString("choises"));
					argui.setQRSquare(qrChooser, false);
					argui.finishAction(jsonresponse.getString("action"));
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
