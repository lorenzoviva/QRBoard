package com.ogc.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.qrboard.ARLayerView;
import com.example.qrboard.ChatPageWebView;
import com.example.qrboard.InternalWebView;
import com.example.qrboard.PageEditorWebView;
import com.ogc.dbutility.DBConst;

public class QRChatWebPage extends QRWebPage{
	private QRChat chat;
	
	public QRChat getChat() {
		return chat;
	}
	public void setChat(QRChat chat) {
		this.chat = chat;
	}
	public QRChatWebPage(QRChat qrSquare) {
		super(qrSquare.getText(),DBConst.url+"chat.html");
		this.chat=qrSquare;
		
	}
	@Override
	public void draw(Canvas canvas, ARLayerView arview) {
//		super.draw(canvas, arview);
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
}
