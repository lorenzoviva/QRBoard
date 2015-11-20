package com.example.qrboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.GsonHelper;
import com.google.gson.JsonSyntaxException;
import com.ogc.model.QRWebPage;

public class EditWebPageActivity extends Activity implements InvalidableAcivity {
	QRWebPageEditorView editor;
	private ActivityInvalidator invalidator;
	private ListView listViewMessages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listViewMessages = (ListView) findViewById(R.id.list_view_messages);
		setContentView(R.layout.activity_edit_web_page);
		Intent intent = getIntent();
//		String type = intent.getStringExtra("type");
		QRWebPage square = null;
		try {
			square = (QRWebPage) GsonHelper.customGson.fromJson(intent.getStringExtra("QRSquare"), QRWebPage.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageButton addImageButton = (ImageButton) findViewById(R.id.epw_imgbutton);
		ImageButton addDivButton = (ImageButton) findViewById(R.id.epw_divbutton);
		ImageButton addLinkButton = (ImageButton) findViewById(R.id.epw_linkbutton);
		ImageButton addTextButton = (ImageButton) findViewById(R.id.epw_textbutton);
		editor = (QRWebPageEditorView) findViewById(R.id.epw_squaresurfaceView);
		if (square != null) {
			editor.setup(square,addImageButton,addDivButton,addLinkButton,addTextButton);
		}
		
		new ActivityInvalidator(this);


	}

	@Override
	public void invalidate() {
		
		editor.postInvalidate();
		
	}



}
