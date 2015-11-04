package com.example.qrboard;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExploreActivity extends Activity implements InvalidableAcivity {
	QRExplorer explorer;
	ActivityInvalidator invalidator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT < 16) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
		setContentView(R.layout.activity_explore);
		
		explorer = (QRExplorer) findViewById(R.id.qr_explorer);
		invalidator = new ActivityInvalidator(this);
		explorer.setExploreButton((Button) findViewById(R.id.explorerbutton));
		explorer.setEditImageInfo((ImageView) findViewById(R.id.explorereditifoimage));
		explorer.setEditTextInfo((TextView)findViewById(R.id.explorereditifo));
		if (getIntent().hasExtra("response")) {
			String jsonresponse = getIntent().getStringExtra("response");
			setup(jsonresponse);
		}
		
	}

	private void setup(String jsonresponse) {
		explorer.setup(jsonresponse);

	}

	@Override
	public void invalidate() {
		explorer.postInvalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		explorer.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
