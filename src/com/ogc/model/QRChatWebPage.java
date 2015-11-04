package com.ogc.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.example.qrboard.ARLayerView;
import com.example.qrboard.ChatActivity;
import com.example.qrboard.SquareHolderView;
import com.google.gson.Gson;
import com.ogc.browsers.ChatPageWebView;
import com.ogc.dbutility.DBConst;

public class QRChatWebPage extends QRWebPage {
	private QRChat chat;
	private transient float tx;
	private transient float ty;
	private transient float dx;
	private transient float dy;
	private boolean goToChatActivity = false;

	public QRChat getChat() {
		return chat;
	}

	public void setChat(QRChat chat) {
		this.chat = chat;
	}

	public QRChatWebPage(QRChat qrSquare) {
		super(qrSquare.getText(), DBConst.url + "chat.html");
		this.chat = qrSquare;

	}
	@Override
	public void onClose(){
		if (webview != null) {
			((ChatPageWebView) webview).onClose();
		}
		webview = null;
	}
	@Override
	public void draw(Canvas canvas, SquareHolderView arview) {
		// super.draw(canvas, arview);ù
		if (goToChatActivity) {
			if (webview != null) {
				onClose();
				Intent intent = new Intent(arview.getContext(), ChatActivity.class);
				intent.putExtra("qrchat", chat.toJSONObject().toString());
				if (arview.getUser() != null) {
					intent.putExtra("qruser", ((new Gson()).toJsonTree(arview.getUser(), QRUser.class)).toString());

				}
				arview.getContext().startActivity(intent);
			}
		}
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		if (webview == null) {
			webview = new ChatPageWebView(arview, this, 500, 500);

		} else {
			int w = webview.getMeasuredWidth();
			int h = webview.getMeasuredHeight();
			if (getHorizontalScroll() != webview.getScrollX()) {
				webview.setScrollX(getHorizontalScroll());
			}
			if (getVerticalScroll() != webview.getScrollY()) {

				webview.setScrollY(getVerticalScroll());
			}
			if (h > 0 && w > 0) {

				Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
				Canvas bigcanvas = new Canvas(bm);
				int iHeight = bm.getHeight();
				bigcanvas.drawBitmap(bm, 0, iHeight, paint);

				webview.draw(bigcanvas);
				Matrix matrix = new Matrix();
				float[] dest = { two.x, two.y, three.x, three.y, four.x, four.y, one.x, one.y };
				float[] src = { 0, 0, w, 0, w, h, 0, h };
				matrix.setPolyToPoly(src, 0, dest, 0, 4);
				canvas.drawBitmap(bm, matrix, paint);
			}
		}

	}

	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {

			if (dx < 5 && dy < 5) {

				goToChatActivity = true;

			}

		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			tx = -1;
			ty = -1;
			dx = 0;
			dy = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (tx != -1) {
				float ddx = tx - event.getX();
				float ddy = ty - event.getY();
				dx += Math.abs(ddx);
				dy += Math.abs(ddy);

				if (tx != -1) {
					PointF normalvector = new PointF();
					normalvector.x = (float) (getFour().x - getOne().x);
					normalvector.y = (float) (getFour().y - getOne().y);
					float normalnorm = (float) Math.sqrt(Math.pow(normalvector.x, 2) + Math.pow(normalvector.y, 2));
					float ddnorm = (float) Math.sqrt(Math.pow(ddx, 2) + Math.pow(ddy, 2));
					float theta = (float) Math.acos(((float) (normalvector.x * ddx) + (normalvector.y * ddy)) / (normalnorm * ddnorm));

					PointF perpendicularvector = new PointF();
					perpendicularvector.x = (float) (getTwo().x - getOne().x);
					perpendicularvector.y = (float) (getTwo().y - getOne().y);
					float perpendiculnorm = (float) Math.sqrt(Math.pow(perpendicularvector.x, 2) + Math.pow(perpendicularvector.y, 2));
					float beta = (float) Math.acos(((float) (perpendicularvector.x * ddx) + (perpendicularvector.y * ddy)) / (perpendiculnorm * ddnorm));

					ddy = (float) -(ddnorm * (float) Math.cos(beta));
					ddx = (float) -(ddnorm * (float) Math.cos(theta));
					if (getHorizontalScroll() + ddx < getMaxHorizontalScroll() && getHorizontalScroll() + ddx > 0) {
						setHorizontalScroll((int) (getHorizontalScroll() + ddx));
					}
					if (getVerticalScroll() + ddy < getMaxVerticalScroll() && getVerticalScroll() + ddy > 0) {
						setVerticalScroll((int) (getVerticalScroll() + ddy));
					}
				}

			}
			tx = event.getX();
			ty = event.getY();

		}

	}
}
