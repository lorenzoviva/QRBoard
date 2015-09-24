package com.example.qrboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.ogc.action.Action;
import com.ogc.dbutility.DBConst;
import com.ogc.dbutility.JSONParser;
import com.ogc.dialog.DialogBuilder;
import com.ogc.model.QRSquare;

public class ScanActivity extends CaptureActivity {

	ARLayerView arview;// the drawable view
	JSONParser jParser = new JSONParser();

	private int state = 0;// 0 scanning, 1 waiting for response, 2 showing
							// response
	Result result = null;
	ActivityInvalidator invalidator;

	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		}

		setContentView(R.layout.activity_scan);

		arview = (ARLayerView) findViewById(R.id.ar_view);
		invalidator = new ActivityInvalidator(this);

		this.addContentView(arview.getButtonView(), arview.getButtonViewLayoutParams());

	}

	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		if (rawResult.getResultPoints().length == 4) {
			if (state == 2 && arview.getQRSquare() == null) {
				state = 0;
			}
			if (state == 0 || !rawResult.getText().equals(result.getText()) || (arview.getQRSquare() != null && !arview.getQRSquare().getText().equals(rawResult.getText()))) {
				if (getActionState() == 1) {// if the courrent actions is
					// waiting for a qr
					result = rawResult;
					arview.setQRSquare(new QRSquare(result.getText()));
					arview.drawCodeResult(result);
					state = 2;
				} else {

					result = rawResult;
					state = 1;
					// Log.d("found bar", "barcode found : points:" +
					// rawResult.getResultPoints().length);
					Log.d("found bar", "barcode found : text : " + rawResult.getText());
					// Log.d("found bar", "state : " + state);
					// Log.d("found bar", "qrcode found : version : " + ((Point)
					// (rawResult.getResultMetadata().get(ResultMetadataType.OTHER))).x);
					// Log.d("found bar", "qrcode found : size : " + ((Point)
					// (rawResult.getResultMetadata().get(ResultMetadataType.OTHER))).y);
					new QRSquareLoader().execute();

				}

			} else if (state == 2) {
				if (rawResult.getResultPoints().length == 4) {
					result = rawResult;
					arview.drawCodeResult(result);
					arview.invalidate();
				} else {
					Log.d("found bar", "barcode found : type:" + rawResult.getBarcodeFormat());
				}
			}
		} else {
			Log.d("found bar", "barcode found : type:" + rawResult.getBarcodeFormat());
		}
		restartPreviewAfterDelay(1L);
	}

	public void createErrorDialog(final String errorMessage) {
		final Context fcontext = this;
		runOnUIThread(new Runnable() {

			@Override
			public void run() {
				DialogBuilder.createErrorDialog(fcontext, errorMessage);

			}
		});

	}

	public void runOnUIThread(Runnable runnable) {
		Handler mainHandler = new Handler(this.getMainLooper());
		mainHandler.post(runnable);
	}

	public int getActionState() {
		Action action = arview.getArgui().getAction();
		if (action != null) {
			return action.getState();
		} else {
			return 0;
		}
	}

	public class QRSquareLoader extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu
			if (state != 2) {
				Map<String, Object> paramap = new HashMap<String, Object>();
				paramap.put("text", result.getText());
				if (arview.getUser() != null) {
					paramap.put("user", arview.getUser().getId());
				}

				JSONObject paramjson = new JSONObject(paramap);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("action", "load");
				map.put("parameters", paramjson);
				JSONObject json = new JSONObject(map);
				Log.d("request:", json.toString());
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("json", json.toString()));
				JSONObject jsonresponse = jParser.makeHttpRequest(DBConst.url_action, "GET", params);
				boolean s = false;

				try {

					Log.d("Msg", jsonresponse.toString());
					s = jsonresponse.getBoolean("success");
					if (s) {
						boolean free = jsonresponse.getBoolean("free");
						if (!free) {
							Gson gson = new Gson();

							String type = jsonresponse.getString("type");
							String jsonstring = jsonresponse.getJSONObject("QRSquare").toString();
							String actions = jsonresponse.getString("action");
							QRSquare fromJson = (QRSquare) gson.fromJson(jsonstring, Class.forName(type));
							// Log.d("result:",
							// result.getResultPoints()[0].toString());
							state = 2;
							arview.setQRSquare(fromJson);
							arview.setActions(actions);
							arview.drawCodeResult(result);
							Log.d("FROM JSON", fromJson.toString());

						} else {
							String actions = jsonresponse.getString("action");
							QRSquare newsquare = new QRSquare();
							newsquare.setText(result.getText());
							state = 2;
							Log.d("result:", result.getResultPoints()[0].toString());
							arview.setQRSquare(newsquare);
							arview.setActions(actions);
							arview.drawCodeResult(result);
							// state = 2;
						}
					}
				} catch (ClassNotFoundException e) {
					createErrorDialog("The servers are down! please try again later");
				} catch (JSONException | NullPointerException e) {
					Log.d("WARNING", "there is some missing information in this qr :" + e.getMessage());
				}
			}
			return null;
		}

	}

	public boolean needToInvalidate() {
		return (arview.getQRSquare() != null && result != null);
	}

	public void invalidate() {
		if (needToInvalidate()) {
			// arview.drawCodeResult(result);
			arview.postInvalidate();

		}
	}

}