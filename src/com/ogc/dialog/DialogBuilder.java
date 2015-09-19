package com.ogc.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ogc.action.Signup;

public class DialogBuilder {

	public static void createSignupDialog(Context context, Signup signup) {
		new SignupDialog(context, signup);
	}

	public static void createErrorDialog(Context context, String string) {

		new AlertDialog.Builder(context).setTitle("Error").setMessage(string).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// continue with delete
			}
		}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}
}
