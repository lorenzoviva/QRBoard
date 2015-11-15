package com.example.qrboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.google.gson.GsonHelper;
import com.google.gson.JsonSyntaxException;
import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.QRSquare;
import com.ogc.model.QRWebPage;

public class EditWebPageActivity extends Activity implements InvalidableAcivity {
	QRWebPageEditorView editor;
	private ActivityInvalidator invalidator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		editor = (QRWebPageEditorView) findViewById(R.id.epw_squaresurfaceView);
		if (square != null) {
			editor.setup(square);
		}
		invalidator = new ActivityInvalidator(this);


	}

	@Override
	public void invalidate() {
		
		editor.postInvalidate();
		
	}



}
