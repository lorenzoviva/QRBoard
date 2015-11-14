package com.ogc.action.edit;

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
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.qrboard.ARGUI;
import com.google.gson.GsonHelper;
import com.ogc.action.Action;
import com.ogc.dbutility.DBConst;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.special.QRSquareUserRepresentation;

public class Qrsquareuserrepresentation extends Action{

	private ARGUI argui;
	private QRSquareUserRepresentation squareSquareUser;
	private QRSquareUser squareUser;
	private String text;
	private long yourid = -1;
	private long otherid;
	private Context context;
	@Override
	public int getColor(ARGUI argui) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void prepare(ARGUI argui) {
		
		
		
	}
	@Override
	public void execute() {
		super.execute();
		(new QRSquareAction()).execute();
	}

	@Override
	public void perform(ARGUI argui, Context context) {
		this.argui = argui;
		this.squareSquareUser = (QRSquareUserRepresentation) argui.getQRSquare();
		this.squareUser = squareSquareUser.getSquareuser();
		QRUser you = argui.getUser();
		this.yourid = you.getId();
		this.otherid = squareUser.getUser().getId();
		this.text = squareSquareUser.getSquareText();
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
			paramap.put("from", "editrole");
			paramap.put("user", yourid);
			
			paramap.put("otheruser", otherid);
			paramap.put("text", text);

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
					String roles = jsonresponse.getString("choises");
					if(roles.equals("")){
						runOnUIThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context,"You don't have the permission to edit this role.", Toast.LENGTH_SHORT).show();
							}
						}, context);
						argui.setActionContext("");
					}else{
						argui.openSquareUserEditorDialog(context,jsonresponse, paramjson.toString());
					}
				} else {
					runOnUIThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(context,"You don't have the permission to edit this role.", Toast.LENGTH_SHORT).show();
						}
					}, context);
					argui.setActionContext("");
				}
			} catch (JSONException | HttpHostConnectException e) {
				Log.d("ERROR", e.getMessage());
				argui.finishAction("Unable to request");
			}

			return null;
		}
		public void runOnUIThread(Runnable runnable, Context context) {
			Handler mainHandler = new Handler(context.getMainLooper());
			mainHandler.post(runnable);
		}
	}

}
