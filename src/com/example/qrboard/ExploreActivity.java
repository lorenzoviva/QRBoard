package com.example.qrboard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExploreActivity extends Activity implements InvalidableAcivity, OnTouchListener {
	QRExplorer explorer;
	ActivityInvalidator invalidator;
	RelativeLayout instructionLayout;
	private int instructionIndex;
	private List<TextView> instructions;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
		setContentView(R.layout.activity_explore);

		explorer = (QRExplorer) findViewById(R.id.qr_explorer);
		instructionLayout = (RelativeLayout) findViewById(R.id.explore_instruction_layout);
		invalidator = new ActivityInvalidator(this);
		explorer.setExploreButton((Button) findViewById(R.id.explorerbutton));
		explorer.setEditImageInfo((ImageView) findViewById(R.id.explorereditifoimage));
		explorer.setEditTextInfo((TextView) findViewById(R.id.explorereditifo));
		explorer.setBackButton((Button) findViewById(R.id.explorequitbutton));
		explorer.setReadButton((Button) findViewById(R.id.explorereadbutton));
		if (getIntent().hasExtra("response")) {
			String jsonresponse = getIntent().getStringExtra("response");
			setup(jsonresponse);
		}
		explorer.setInstructing(true);
		if (isNotFirstTime()) {
			instructionLayout.setVisibility(View.INVISIBLE);
			explorer.setInstructing(false);
		}else{
//		}
			setupInstructions();
		}
	}

	private void setupInstructions(){
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		float d = metrics.density;
//		Log.d("EXPLORER", "bottom: "+ height + " right :" + width + " mw:" + instructionLayout.getMeasuredWidth());
		instructionIndex = 1;
		instructions = new ArrayList<TextView>();
		instructions.add((TextView) findViewById(R.id.explorer_instructiontextView0));
		instructions.add((TextView) findViewById(R.id.explorer_instructiontextView1));
		TextView tv2 = (TextView) findViewById(R.id.explorer_instructiontextView2);
		Drawable freccia = getResources().getDrawable(R.drawable.arrowright);
		tv2.measure(0, 0);  
		freccia.setBounds(0, 0, tv2.getMeasuredWidth(), tv2.getMeasuredWidth());
		tv2.setCompoundDrawables(null, null, null, freccia);
		instructions.add(tv2);
		TextView tv3 = (TextView) findViewById(R.id.explorer_instructiontextView3);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params1.setMargins((width/5)*2, (width/12), 0, 0);
		tv3.setLayoutParams(params1);
		Drawable row = getResources().getDrawable(R.drawable.instructionrow);
		row.setBounds(0, 0, (width*3)/5, (width*17)/60);
		tv3.setCompoundDrawables(null, row, null, null);
		instructions.add(tv3);
		TextView tv4 = (TextView) findViewById(R.id.explorer_instructiontextView4);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2.setMargins((width/5)*4, (width/12), 0, 0);
		tv4.setLayoutParams(params2);
		Drawable cell = getResources().getDrawable(R.drawable.instructioncell);
		cell.setBounds(0, 0, (width)/5, (width*17)/60);
		tv4.setCompoundDrawables(null, cell, null, null);
		instructions.add(tv4);
		TextView tv5 = (TextView) findViewById(R.id.explorer_instructiontextView5);
		RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params3.setMargins((width/5)*3, (width/12), (width/5), 0);
		tv5.setLayoutParams(params3);
		tv5.setCompoundDrawables(null, cell, null, null);
		instructions.add(tv5);
		TextView tv6 = (TextView) findViewById(R.id.explorer_instructiontextView6);
		RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params4.setMargins((width/5), (width/12), (width/5), 0);
		tv6.setLayoutParams(params4);
		tv6.setCompoundDrawables(null, cell, null, null);
		instructions.add(tv6);
		TextView tv7 = (TextView) findViewById(R.id.explorer_instructiontextView7);
		RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params5.setMargins((width/10), (height/10), 0, 0);
		tv7.setLayoutParams(params5);
		Drawable focused = getResources().getDrawable(R.drawable.instructionfocused);
		focused.setBounds(0, 0, (width*17)/60,(width)/5 );
		tv7.setCompoundDrawables(focused, null, null, null);
		instructions.add(tv7);
		TextView tv8 = (TextView) findViewById(R.id.explorer_instructiontextView8);
		RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params6.setMargins((3*width/10), (height/5) + (width/5)+120, 0, 0);
		tv8.setLayoutParams(params6);
		Drawable arrowleft = getResources().getDrawable(R.drawable.arrowleft);
		arrowleft.setBounds(0,0 ,(width/5),(width)/20);
		tv8.setCompoundDrawables(arrowleft, null, null, null);
		instructions.add(tv8);
		TextView tv9 = (TextView) findViewById(R.id.explorer_instructiontextView9);
		RelativeLayout.LayoutParams params7 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params7.setMargins((3*width/10), (height/5) + (width/5), 0, 0);
		tv9.setLayoutParams(params7);
		Drawable arrowleftbig = getResources().getDrawable(R.drawable.arrowleftbig);
		arrowleftbig.setBounds(0, 0 ,(width/5),(width)/5);
		tv9.setCompoundDrawables(arrowleftbig, null,null , null);
		instructions.add(tv9);
		TextView tv10 = (TextView) findViewById(R.id.explorer_instructiontextView10);
		RelativeLayout.LayoutParams params8 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params8.setMargins((3*width/10), (height/5) + (width/5), 0, 0);
		tv10.setLayoutParams(params8);
		arrowleftbig.setBounds(0, 0 ,(width/5),(width)/5);
		tv10.setCompoundDrawables(arrowleftbig, null, null, null);
		instructions.add(tv10);
		TextView tv11 = (TextView) findViewById(R.id.explorer_instructiontextView11);
		RelativeLayout.LayoutParams params9 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params9.setMargins((3*width/10), (height/5) + (width/5), 0, 0);
		tv11.setLayoutParams(params9);
		arrowleftbig.setBounds(0, 0 ,(width/5),(width)/5);
		tv11.setCompoundDrawables(arrowleftbig, null, null, null);
		instructions.add(tv11);
		TextView tv12 = (TextView) findViewById(R.id.explorer_instructiontextView12);
		RelativeLayout.LayoutParams param10 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		param10.setMargins((3*width/10), (height/5) + (width/5)+60, 0, 0);
		tv12.setLayoutParams(param10);
//		arrowleft.setBounds(0, 0 ,(width/5),(width)/5);
		tv12.setCompoundDrawables(arrowleft, null, null, null);
		instructions.add(tv12);
		TextView tv13 = (TextView) findViewById(R.id.explorer_instructiontextView13);
		RelativeLayout.LayoutParams param11 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		param11.setMargins((width/10), (height/10), 0, 0);
		tv13.setLayoutParams(param11);
		tv13.setCompoundDrawables(focused, null, null, null);
		instructions.add(tv13);

	}
	private void setup(String jsonresponse) {
		explorer.setup(jsonresponse);

	}

	private boolean isNotFirstTime() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean ranBefore = preferences.getBoolean("exploreInstruction", false);
//		if (!ranBefore) {

			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("exploreInstruction", true);
			editor.commit();
			instructionLayout.setVisibility(View.VISIBLE);
			instructionLayout.setOnTouchListener(this);

//		}
		return ranBefore;

	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		instructionIndex++;
		if(instructionIndex>=instructions.size()){
			instructionLayout.setVisibility(View.GONE);
			explorer.setInstructing(false);
		}else{
			for(int i = 1; i< instructions.size(); i++){
				if(i!=instructionIndex){
					instructions.get(i).setVisibility(View.INVISIBLE);
				}else{
					instructions.get(i).setVisibility(View.VISIBLE);
				}
				
			}
		}
		return false;
	}

	@Override
	public void invalidate() {
		if (explorer.getArgui().isRefreshExplorer()) {
			explorer.refresh();
		}
		explorer.postInvalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!explorer.isInstructing()){
			explorer.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		gotoScanActivity();
	}

	public void gotoScanActivity() {
		Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		startActivityForResult(intent, 0);
	}
}
