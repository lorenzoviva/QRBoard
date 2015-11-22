package com.example.qrboard;

import com.ogc.browsers.BrowserClickEvent;
import com.ogc.model.QRWebPage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class CustomSelector {
	BrowserClickEvent event;

	public CustomSelector(BrowserClickEvent event) {
		this.event = event;
	}

	public void draw(Canvas canvas, QRWebPage square) {
		Rect r = new Rect(event.getElementBounds());
		int left = (int) ((((double) square.getThree().x - (double) square.getTwo().x) * ((double) r.left ))/ (double) (event.getWindowWidth()));
		int top = (int) ((((double) square.getFour().y - (double) square.getThree().y) * ((double) r.top ))/ (double) (event.getWindowHeight()));
		int right = (int) ((((double) square.getThree().x - (double) square.getTwo().x) * ((double) (r.right ))) / (double) (event.getWindowWidth()));
		int bottom = (int) ((((double) square.getFour().y - (double) square.getThree().y) * ((double) (r.bottom))) / (double) (event.getWindowHeight()));
		r.set(left, top, right, bottom);
		r.offset((int) square.getTwo().x, (int) square.getTwo().y);

		Paint borderPaint = new Paint();
		borderPaint.setARGB(255, 93, 181, 224);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(1);
		for (int i = 0; i < 10; i++) {
			borderPaint.setAlpha(255 - (i * 20));
			r.set(r.left - +1, r.top - 1, r.right + 1, r.bottom + 1);
			canvas.drawRect(r, borderPaint);

		}
	}

	public BrowserClickEvent getEvent() {
		return event;
	}

	public void setEvent(BrowserClickEvent event) {
		this.event = event;
	}

}
