package com.ogc.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.qrboard.QRSquareUserEditorView;
import com.example.qrboard.R;
import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.ogc.dbutility.DBConst;
import com.ogc.dbutility.JSONParser;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.special.QRUserRepresentation;

public class EditSquareUserDialog extends Dialog{
	private Button okButton;
	private Button cancelButton;
	private Spinner roleSpinner;
	private TextView myRoleTextView;
	private TextView otherTextView;
	private QRSquareUserEditorView editorView;
	private QRSquareUser qrSquareUser;
	private QRSquare square;
	private QRUserRepresentation user;
	private List<String> roleChoises;
	private String lastrequest;
	public EditSquareUserDialog(Context context,String lastrequest, QRSquareUser otherSquareUser, QRSquare square,String qrst, QRUser otheruser, List<String> roleChoises,String myrole) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //before     
		setContentView(R.layout.editsquareuserdialog);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 128, 128, 128)));
		this.lastrequest = lastrequest;
		this.okButton = (Button) findViewById(R.id.esu_ok_button);
		this.cancelButton = (Button) findViewById(R.id.esu_cancel_button);
		this.roleSpinner = (Spinner) findViewById(R.id.esu_rolespinner);
		this.editorView = (QRSquareUserEditorView) findViewById(R.id.esu_squaresurfaceView);
		this.myRoleTextView = (TextView) findViewById(R.id.esu_yourroletextview);
		this.otherTextView = (TextView) findViewById(R.id.esu_othertextview);
		this.user = new QRUserRepresentation(otheruser);
		this.square = square;
		this.qrSquareUser = otherSquareUser;
		roleChoises.add(0, otherSquareUser.getRole().getName());
		this.roleChoises = roleChoises;
		otherTextView.setText(otheruser.getFirstName()+" "+otheruser.getLastName());
		myRoleTextView.setText(myrole);
		android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = LayoutParams.MATCH_PARENT;
        params.width = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, roleChoises); //selected item will look like a spinner set from XML
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		roleSpinner.setAdapter(spinnerArrayAdapter);
		editorView.setup(user, square, getHalfMaxWidth(),qrst);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				save();
			}
		});
		show();
	}
	public void cancel(){
		dismiss();
	}
	public void save(){
		if(roleSpinner.getSelectedItemPosition()!=0 && roleSpinner.getSelectedItemPosition()!=AdapterView.INVALID_POSITION)
		(new QRSquareUserSaver()).execute();
		
	}
	public int getHalfMaxWidth(){
		if(myRoleTextView.getWidth()>otherTextView.getWidth()){
			return myRoleTextView.getWidth();
		}else{
			return otherTextView.getWidth();
		}
	}

	public class QRSquareUserSaver extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			// Getting username and password from user inpu
			JSONParser jParser = new JSONParser();
			Map<String, Object> paramap = new HashMap<String, Object>();
			ACL acl = new ACL(true, true);
			paramap.put("lastrequest", lastrequest);
			paramap.put("newrole", roleSpinner.getSelectedItem());

			JSONObject paramjson = new JSONObject(paramap);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("action", "savesquareuser");
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
					Toast.makeText(getContext(), "Successfully saved!", Toast.LENGTH_SHORT);
				}else{
					Toast.makeText(getContext(), "Unable to save.", Toast.LENGTH_SHORT);
				}
			} catch (JSONException | HttpHostConnectException e) {
				Toast.makeText(getContext(), "Unable to save.", Toast.LENGTH_SHORT);
			}
			dismiss();
			return null;
		}

	}
}
