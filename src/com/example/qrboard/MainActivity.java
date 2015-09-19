package com.example.qrboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ogc.dbutility.JSONParser;

public class MainActivity extends Activity {
	TextView tv;
	EditText fn;
	EditText ln;
	JSONParser jParser = new JSONParser();
	JSONObject json;
	private static String url_signup = "http://192.168.42.222:8080/QRWebService/createQR";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.main_text_view);
		tv.setText("lel");
		fn = (EditText) findViewById(R.id.edit_name);
		ln = (EditText) findViewById(R.id.edit_last_name);
	}

	public void scanNow(View view) {
//		new Login().execute();
		Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}

	private class Login extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu
			String username = fn.getText().toString();
			String pass = ln.getText().toString();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("lel", "ohohoh");
			JSONObject json = new JSONObject(map);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("firstName", username));
			params.add(new BasicNameValuePair("lastName", pass));
			params.add(new BasicNameValuePair("json",json.toString()));
			json = jParser.makeHttpRequest(url_signup, "GET", params);
			boolean s = false;

			try {
				
				Log.d("Msg", json.toString());
				s = json.getBoolean("success");
				if (s) {
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

	}

}
