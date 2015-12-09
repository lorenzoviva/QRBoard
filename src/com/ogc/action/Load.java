package com.ogc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.qrboard.ARGUI;
import com.example.qrboard.R;
import com.example.qrboard.ScanActivity;
import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.ogc.action.Login.QRSquareAction;
import com.ogc.dbutility.DBConst;
import com.ogc.dialog.DialogBuilder;
import com.ogc.model.QRSquare;
import com.ogc.model.special.QRAccessDaniedWebPage;

public class Load extends Action{

	private ARGUI argui;
	private Context context;
	@Override
	public int getColor(ARGUI argui) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void prepare(ARGUI argui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addQRParameter(QRSquare qrsquare) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void perform(ARGUI argui, Context context) {
		super.perform(argui, context);
		this.argui = argui;
		this.context = context;
		execute();
	}
	@Override
	public void execute() {
		super.execute();
		new QRSquareLoader().execute();
	}
	public void gotoScanActivity() {
		Intent intent = new Intent(context, ScanActivity.class);
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
		context.startActivity(intent);
	}
	public class QRSquareLoader extends AsyncTask<String, String, String> {

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu
		
				Map<String, Object> paramap = new HashMap<String, Object>();
				String text = argui.getQRSquare().getText();
				if(text.contains("%")){
					text = (text.replace("%", "x"));
				}
				paramap.put("text", text);
				if (argui.getUser() != null) {
					paramap.put("user", argui.getUser().getId());
				}

				JSONObject paramjson = new JSONObject(paramap);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("action", "load");
				map.put("parameters", paramjson);
				JSONObject json = new JSONObject(map);
				Log.d("request:", json.toString());
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("json", json.toString()));

				JSONObject jsonresponse = null;
				try {
					jsonresponse = jParser.makeHttpRequest(DBConst.url_action, "GET", params);
					boolean s = false;
					Log.d("Msg", jsonresponse.toString());
					s = jsonresponse.getBoolean("success");
					if (s) {
						boolean free = jsonresponse.getBoolean("free");
						if (!free) {
							Gson gson = GsonHelper.customGson;

							String type = jsonresponse.getString("type");
							String jsonstring = jsonresponse.getJSONObject("QRSquare").toString();
							String actions = jsonresponse.getString("action");
							QRSquare fromJson =  null;
							if(!actions.toLowerCase(Locale.ROOT).contains("read,")){
								
								fromJson = new QRAccessDaniedWebPage(((QRSquare) gson.fromJson(jsonstring, Class.forName(type))).getText());
							}else{
								actions = actions.replace("read,", "");
								fromJson = (QRSquare) gson.fromJson(jsonstring, Class.forName(type));
							}
//							jsonstring = gson.toJsonTree(fromJson).toString();
//							argui.saveState(jsonstring,type,actions,context);
							argui.setQRSquare(fromJson,true);
							argui.setActionContext("");
							argui.setActions(actions);
							jsonstring = gson.toJsonTree(argui.getQRSquare()).toString();
							argui.saveState(jsonstring,type,actions,context);
							gotoScanActivity();

						} else {
							String actions = jsonresponse.getString("action");
							QRSquare newsquare = new QRSquare();
							newsquare.setText(text);
							argui.setQRSquare(newsquare,true);
							argui.setActionContext("");
							argui.setActions(actions);
							gotoScanActivity();
						}
					}
				} catch (HttpHostConnectException | ClassNotFoundException e) {
					createErrorDialog("The servers are down! please try again later");
				} catch (JSONException | NullPointerException e) {
					if (jsonresponse == null) {
						createErrorDialog("The servers are down! please try again later");
					}
					Log.d("WARNING", "there is some missing information in this qr :" + e.getMessage());
				}
			
			return null;
		}

	
		

	}
	public void createErrorDialog(final String errorMessage) {
		final Context fcontext = context;
		runOnUIThread(new Runnable() {

			@Override
			public void run() {
				DialogBuilder.createErrorDialog(fcontext, errorMessage);

			}
		});

	}
	public void runOnUIThread(Runnable runnable) {
		Handler mainHandler = new Handler(context.getMainLooper());
		mainHandler.post(runnable);
	}
	@Override
	public Bitmap getIcon(View view) {
		return null;
	}
}
