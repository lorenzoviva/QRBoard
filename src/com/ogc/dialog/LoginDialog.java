package com.ogc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qrboard.R;
import com.ogc.action.Login;

public class LoginDialog extends Dialog {

	Login login;
	Context context;
	EditText password;
	Button loginButton;
	Button useQRButton;
	
	public LoginDialog(Context context,Login login) {
		super(context,R.style.DialogStyle);
//		super(context);		
		setContentView(R.layout.logindialog);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 128, 128, 128)));
		setTitle("Log in...");
		this.context = context;
		this.login = login;
		// set the custom dialog components - text, image and button
		
		password = (EditText) findViewById(R.id.loginpassword_edit_text);
		loginButton = (Button) findViewById(R.id.login_button);
		useQRButton = (Button) findViewById(R.id.loginuseqr_button);
		// if button is clicked, close the custom dialog
		
		useQRButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				useQRPassword();

			}
		});
		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				login();

			}
		});
		show();
	}


	public void login(){
		if (!password.getText().toString().isEmpty()){
			String passwordtext = password.getText().toString();
			login.addPasswordParameter(passwordtext);
			dismiss();
		}else{
			Toast.makeText(context, "Please fill password field", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	public void useQRPassword(){
		dismiss();
	}


}
