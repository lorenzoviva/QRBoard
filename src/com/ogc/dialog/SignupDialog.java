package com.ogc.dialog;

import com.example.qrboard.R;
import com.ogc.action.Signup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupDialog extends Dialog {
	EditText firstname;
	EditText lastname;
	Signup signup;
	Context context;
	public SignupDialog(Context context,Signup signup) {
		super(context);
		setContentView(R.layout.signupdialog);
		setTitle("Sign up...");
		this.context = context;
		this.signup = signup;
		// set the custom dialog components - text, image and button
		firstname = (EditText) findViewById(R.id.signupfirstnameedittext);
		lastname = (EditText) findViewById(R.id.signuplastnameedittext);

		Button dialogButton = (Button) findViewById(R.id.signupokbutton);
		// if button is clicked, close the custom dialog
		
		dialogButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
		
		show();
	}


	@Override
	public void dismiss() {
		String firstnametext = firstname.getText().toString();

		String lasttnametext = lastname.getText().toString();
		if (!firstnametext.isEmpty() && !lasttnametext.isEmpty()) {
			signup.addFirstnameParameter(firstnametext);
			signup.addLastnameParameter(lasttnametext);
			super.dismiss();
		}else{
			Toast.makeText(context, "please fill both text fields", Toast.LENGTH_SHORT).show();
		}
	}

}
