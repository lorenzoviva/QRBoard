package com.example.qrboard;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.zxing.Result;
import com.ogc.action.Action;
import com.ogc.graphics.Point;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class ARLayerView extends SurfaceView {

	private ARGUI argui = null;

	private ImageButton removeSquareButton;
	private ImageButton userButton;

	public void setListIndex(int index) {
		argui.setListindex(index);
	}

	public ARLayerView(Context context) {
		super(context);
		setWillNotDraw(false);
		argui = new ARGUI();

		// TODO Auto-generated constructor stub
	}

	public ARLayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setWillNotDraw(false);
		argui = new ARGUI();

		// TODO Auto-generated constructor stub
	}

	public ARLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		argui = new ARGUI();

		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (argui != null) {
			argui.draw(canvas, this);
			if (removeSquareButton != null) {
				if (argui.getQRSquare() == null) {
					removeSquareButton.setVisibility(View.GONE);
				} else {
					removeSquareButton.setVisibility(View.VISIBLE);
				}
			}
			if (userButton != null) {
				if (argui.getUser() == null) {
					userButton.setVisibility(View.GONE);
				} else {
					userButton.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		argui.onTouch(event, this.getContext());
		if (event.getAction() == MotionEvent.ACTION_UP) {
			performClick();
		}
		invalidate();
		return true;
	}

	public void setQRSquareScrollable(int horizontal, int vertical) {
		argui.setQRSquareScrollable(horizontal, vertical);
	}

	public void setQRSquare(QRSquare qrSquare, boolean b) {
		argui.setQRSquare(qrSquare, b);
	}

	public void runOnUIThread(Runnable runnable) {
		Handler mainHandler = new Handler(getContext().getMainLooper());
		mainHandler.post(runnable);
	}

	public void setQRSquare(QRSquare qrSquare) {
		argui.setQRSquare(qrSquare, false);
	}

	public QRSquare getQRSquare() {
		return argui.getQRSquare();
	}

	public void drawCodeResult(Result rawResult) {
		this.argui.setShape(rawResult);
		postInvalidate();
		// invalidate();
	}

	public ARGUI getArgui() {
		return argui;
	}

	public void setArgui(ARGUI argui) {
		this.argui = argui;
	}

	public void setUser(QRUser user) {
		this.argui.setUser(user);
	}

	public QRUser getUser() {
		return argui.getUser();
	}

	public void setActions(String actions) {
		argui.setActions(actions);
	}

	public void removeSquare() {
		argui.setQRSquare(null, true);
		if (argui.getAction() != null) {
			argui.finishAction("");
		}
		argui.setActionContext("");
		invalidate();
	}

	public View getButtonView() {
		removeSquareButton = new ImageButton(getContext());
		removeSquareButton.setImageResource(R.drawable.removesquarebutton);
		removeSquareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeSquare();
			}
		});
		userButton = new ImageButton(getContext());
		userButton.setImageResource(R.drawable.userbutton);
		userButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showUser();
			}
		});
		final LinearLayout ll = new LinearLayout(getContext());
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.addView(removeSquareButton, getButtonViewLayoutParams());
		ll.addView(userButton, getButtonViewLayoutParams());
		return ll;
	}

	public void showUser() {
		int width = this.getWidth();
		int height = this.getHeight();
		argui.showUsers(width, height);
	}

	public void performAction(String action) {
		argui.performAction(action, getContext());
	}

	public LinearLayout.LayoutParams getButtonViewLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(100, 100, 100, 100);
		return layoutParams;
	}

	public void setActionContext(String actionContext) {
		argui.setActionContext(actionContext);
	}

	public Action getRealAction(String action) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return argui.getRealAction(action);
	}

}
