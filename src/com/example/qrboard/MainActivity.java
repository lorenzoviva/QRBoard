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

	

}
