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

	public void draw(Canvas canvas,QRWebPage square) {
		Rect r = new Rect(event.getElementBounds());
		r.offset((int)square.getTwo().x, (int)square.getTwo().y);
		int l = (int)square.getTwo().x-(int)square.getThree().x;
		
		
		Paint p = new Paint();
		p.setColor(Color.RED);
		canvas.drawRect(r, p);
	}

}
