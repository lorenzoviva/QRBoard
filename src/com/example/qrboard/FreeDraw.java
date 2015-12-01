package com.example.qrboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.ogc.dbutility.DBConst;
import com.ogc.dbutility.JSONParser;
import com.ogc.dialog.DialogBuilder;
import com.ogc.dialog.FreeDrawTextDialog;
import com.ogc.dialog.TextSizeDialog;
import com.ogc.model.QRFreeDraw;
import com.ogc.model.QRSquare;

public class FreeDraw extends Activity {

	private DrawingView drawView;
	private ImageButton currPaint;
	private QRFreeDraw qrEntity;
	private ImageButton saveButton;
	private ImageButton moveButton;
	private ImageButton drawButton;
	private ImageButton eraseButton;
	private ImageButton textButton;
	private ImageButton editTextSizeButton;
	private ImageButton confirmTextButton;
	private ImageButton discardTextButton;
	private BrushSizeDialog brushDialog;
	private FreeDrawTextDialog textDialog;
	private TextSizeDialog textSizeDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_free_draw);
		drawView = (DrawingView) findViewById(R.id.drawing);
		LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors1);
		currPaint = (ImageButton) findViewById(R.id.blackbutton);
		currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
		saveButton = (ImageButton) findViewById(R.id.save_btn);
		moveButton = (ImageButton) findViewById(R.id.move_btn);
		drawButton = (ImageButton) findViewById(R.id.draw_btn);
		eraseButton = (ImageButton) findViewById(R.id.erase_btn);
		textButton = (ImageButton) findViewById(R.id.text_btn);
		editTextSizeButton = (ImageButton) findViewById(R.id.editTextSize_btn);
		confirmTextButton = (ImageButton) findViewById(R.id.confirm_btn);
		discardTextButton = (ImageButton) findViewById(R.id.discard_btn);

		Intent intent = getIntent();
		String json = intent.getStringExtra("jsonFreeDraw");
		long userid = -1;
		Log.e("JSONNNN", json);
		if(intent.hasExtra("userid")){
			userid = intent.getLongExtra("user", -1);
		}
		Gson gson = com.google.gson.GsonHelper.customGson;
		qrEntity = gson.fromJson(json, QRFreeDraw.class);
		Bitmap bitmap = BitmapFactory.decodeByteArray(qrEntity.getImg(), 0, qrEntity.getImg().length);
		Log.d("BITMAP DECODED", bitmap.getWidth() + "," + bitmap.getHeight());
		drawView.requestLayout();
		drawView.setup(qrEntity,userid);
		drawView.requestLayout();
		resetAllButtonsColors();

		drawButton.setBackgroundColor(Color.DKGRAY);
		saveButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				resetAllButtonsColors();
				saveButton.setBackgroundColor(Color.DKGRAY);
				drawView.saveBitmap();
				goBackToScanActivity();
				return false;
			}
		});
		moveButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				resetAllButtonsColors();
				moveButton.setBackgroundColor(Color.DKGRAY);
				drawView.setTool(3);
				return false;
			}
		});
		eraseButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				resetAllButtonsColors();
				eraseButton.setBackgroundColor(Color.DKGRAY);
				drawView.setTool(2);
				openBrushSizeDialog(2);
				return false;
			}
		});
		textButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				resetAllButtonsColors();
				textButton.setBackgroundColor(Color.DKGRAY);
				drawView.setTool(4);
				showTextButtons();
				return false;
			}
		});
		drawButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				resetAllButtonsColors();
				drawButton.setBackgroundColor(Color.DKGRAY);
				drawView.setTool(1);
				openBrushSizeDialog(1);
				return false;
			}
		});
		confirmTextButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (drawView.getTextDrawer() != null) {
					drawView.confirmText();
				}
				resetAllButtonsColors();
				moveButton.setBackgroundColor(Color.DKGRAY);
				drawView.setTool(3);
				return false;
			}
		});
		discardTextButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (drawView.getTextDrawer() != null) {
					drawView.discardText();
				}
				resetAllButtonsColors();
				moveButton.setBackgroundColor(Color.DKGRAY);
				drawView.setTool(3);
				return false;
			}
		});
		editTextSizeButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				openTextSizeDialog();

				return false;
			}
		});
	}

	public void openBrushSizeDialog(int i) {
		if (brushDialog == null || !brushDialog.isShowing()) {
			brushDialog = new BrushSizeDialog(this, i, drawView.getBrushSize());
		}

	}

	public void confirmBrushSizeDialog(int s) {
		drawView.setBrushSize(s);
		brushDialog.hide();
		brushDialog.dismiss();
		brushDialog = null;
	}

	public void openTextDialog() {
		if (textDialog == null || !textDialog.isShowing()) {
			textDialog = new FreeDrawTextDialog(this);
		}

	}

	public void openTextSizeDialog() {
		if (textSizeDialog == null || !textSizeDialog.isShowing()) {
			textSizeDialog = new TextSizeDialog(this);
		}

	}

	public void confirmTextDialog(String t) {
		drawView.setText(t);
		textDialog.hide();
		textDialog.dismiss();
		textDialog = null;
	}

	public void confirmTextSizeDialog(int s) {
		drawView.setTextSize(s);
		textSizeDialog.hide();
		textSizeDialog.dismiss();
		textSizeDialog = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// String key = KeyEvent.keyCodeToString(keyCode);
		// // key will be something like "KEYCODE_A" - extract the "A"
		//
		// // use pattern to convert int keycode to some character
		// Matcher matcher = KEYCODE_PATTERN.matcher(key);
		// if (matcher.matches()) {
		// // append character to textview
		// mTextView.append(matcher.group(1));
		// }
		// // let the default implementation handle the event
		return super.onKeyDown(keyCode, event);
	}

	public void showTextButtons() {
		editTextSizeButton.setVisibility(View.VISIBLE);
		confirmTextButton.setVisibility(View.VISIBLE);
		discardTextButton.setVisibility(View.VISIBLE);
	}

	public void hideTextButtons() {
		editTextSizeButton.setVisibility(View.INVISIBLE);
		confirmTextButton.setVisibility(View.INVISIBLE);
		discardTextButton.setVisibility(View.INVISIBLE);
	}

	public void resetAllButtonsColors() {
		resetAllExceptTextButtonsColors();
		hideTextButtons();
	}

	public void resetAllExceptTextButtonsColors() {
		moveButton.setBackgroundColor(Color.GRAY);
		saveButton.setBackgroundColor(Color.GRAY);
		drawButton.setBackgroundColor(Color.GRAY);
		eraseButton.setBackgroundColor(Color.GRAY);
		textButton.setBackgroundColor(Color.GRAY);
	}

	public void paintClicked(View view) {
		// use chosen color
		if (view != currPaint) {
			// update color
			ImageButton imgView = (ImageButton) view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currPaint = (ImageButton) view;
		}
	}

	public void goBackToScanActivity() {
		Intent intent = new Intent(this, ScanActivity.class);
		startActivity(intent);
	}

	public DrawingView getDrawView() {
		return drawView;
	}

	@Override
	public void onBackPressed() {
		goBackToScanActivity();
	}
}
