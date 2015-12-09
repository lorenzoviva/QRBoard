package com.ogc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrboard.R;
import com.ogc.action.Signup;

public class SignupDialog extends Dialog {
	EditText firstname;
	EditText lastname;
	Signup signup;
	Context context;
	CheckBox checkBox;
	EditText password;
	TextView passwordInfo;
	TextView passwordTip;
	
	public SignupDialog(Context context,Signup signup) {
		super(context);
		setContentView(R.layout.signupdialog);
		setTitle("Sign up...");
		this.context = context;
		this.signup = signup;
		// set the custom dialog components - text, image and button
		firstname = (EditText) findViewById(R.id.signupfirstnameedittext);
		lastname = (EditText) findViewById(R.id.signuplastnameedittext);
		checkBox = (CheckBox) findViewById(R.id.useQRPassword_checkbox);
		password = (EditText) findViewById(R.id.signuppassword_edit_text);
		passwordInfo = (TextView) findViewById(R.id.signuppassword_textview);
		passwordTip = (TextView) findViewById(R.id.tips_login_textview);
		Button dialogButton = (Button) findViewById(R.id.signupokbutton);
		// if button is clicked, close the custom dialog
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				switchLoginType(isChecked);
				
			}

			
		});
		dialogButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
		
		show();
	}
	private void switchLoginType(boolean isChecked) {
		if(isChecked){
			password.setVisibility(View.INVISIBLE);
			passwordInfo.setVisibility(View.INVISIBLE);
			passwordTip.setVisibility(View.VISIBLE);
		}else{
			password.setVisibility(View.VISIBLE);
			passwordInfo.setVisibility(View.VISIBLE);
			passwordTip.setVisibility(View.INVISIBLE);
		}
			
	}

	@Override
	public void dismiss() {
		String firstnametext = firstname.getText().toString();
		String lasttnametext = lastname.getText().toString();
		String passwordtext = password.getText().toString();
		boolean checkQRPassword = checkBox.isChecked();
		if (!firstnametext.isEmpty() && !lasttnametext.isEmpty() && !passwordtext.isEmpty()) {
			signup.addFirstnameParameter(firstnametext);
			signup.addLastnameParameter(lasttnametext);
			signup.addCheckQRPassword(checkQRPassword);
			if(!checkQRPassword)
				signup.addPasswordParameter(passwordtext);
			super.dismiss();
		}else{
			Toast.makeText(context, "Please fill all text fields", Toast.LENGTH_SHORT).show();
		}
	}

}
