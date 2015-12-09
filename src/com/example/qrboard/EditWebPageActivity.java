package com.example.qrboard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.GsonHelper;
import com.google.gson.JsonSyntaxException;
import com.ogc.model.QRWebPage;

public class EditWebPageActivity extends Activity implements InvalidableAcivity, OnTouchListener {
	private QRWebPageEditorView editor;
	private ListView listViewMessages;
	private TextView selectTextView;
	private LinearLayout instructionLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// listViewMessages.setAdapter(new AttributeListAdapter(this));
		setContentView(R.layout.activity_edit_web_page);
		Intent intent = getIntent();
		// String type = intent.getStringExtra("type");
		QRWebPage square = null;
		String type = "com.ogc.model.QRWebPage";//will be owerwritten
		type = intent.getStringExtra("type");
		try {
			
			square = (QRWebPage) GsonHelper.customGson.fromJson(
					intent.getStringExtra("QRSquare"), Class.forName(type));
			
		} catch (JsonSyntaxException | ClassNotFoundException e) {
			square = (QRWebPage) GsonHelper.customGson.fromJson(
					intent.getStringExtra("QRSquare"), QRWebPage.class);
		}
		instructionLayout = (LinearLayout) findViewById(R.id.ewp_instruction_layout);
		listViewMessages = (ListView) findViewById(R.id.ewp_attribute_list);
		selectTextView = (TextView) findViewById(R.id.ewp_select_textview);
		ImageButton addImageButton = (ImageButton) findViewById(R.id.epw_imgbutton);
		ImageButton addDivButton = (ImageButton) findViewById(R.id.epw_divbutton);
		ImageButton addLinkButton = (ImageButton) findViewById(R.id.epw_linkbutton);
		ImageButton addTextButton = (ImageButton) findViewById(R.id.epw_textbutton);
		ImageButton removeButton = (ImageButton) findViewById(R.id.ewp_removebutton);
		ImageButton saveButton = (ImageButton) findViewById(R.id.epw_savebutton);
		editor = (QRWebPageEditorView) findViewById(R.id.epw_squaresurfaceView);
		if (square != null) {
			if (intent.hasExtra("userid")) {
				editor.setup(square,intent.getStringExtra("QRSquare"),type,intent.getLongExtra("userid", -1), saveButton, addImageButton, addDivButton,
						addLinkButton, addTextButton, removeButton,
						listViewMessages, selectTextView);
			} else {
				editor.setup(square,intent.getStringExtra("QRSquare"),type,-1, saveButton, addImageButton, addDivButton,
						addLinkButton, addTextButton, removeButton,
						listViewMessages, selectTextView);
			}
		}

		new ActivityInvalidator(this);
		editor.setInstructing(true);
		if (isNotFirstTime()) {
			instructionLayout.setVisibility(View.INVISIBLE);
			editor.setInstructing(false);
		}
	}
	private boolean isNotFirstTime() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean ranBefore = preferences.getBoolean("ewpInstruction", false);
		if (!ranBefore) {

			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("ewpInstruction", true);
			editor.commit();
			instructionLayout.setVisibility(View.VISIBLE);
			instructionLayout.setOnTouchListener(this);

		}
		return ranBefore;

	}
	@Override
	public void invalidate() {

		editor.postInvalidate();

	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		instructionLayout.setVisibility(View.GONE);
		editor.setInstructing(false);
		return false;
	}

}
