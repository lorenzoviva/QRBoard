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
		int left = (int)((square.getThree().x-square.getTwo().x)*(r.left/(event.getWindowWidth())));
		int top = (int)((square.getFour().y-square.getThree().y)*(r.top/(event.getWindowHeight())));
		int right = (int)((square.getThree().x-square.getTwo().x)*((r.right+r.left)/(event.getWindowWidth())));
		int bottom = (int)((square.getFour().y-(int)square.getThree().y)*((r.bottom+r.top)/(event.getWindowHeight())));
		r.set(left, top, right, bottom);
		r.offset((int)square.getTwo().x, (int)square.getTwo().y);
		
		Paint p = new Paint();
		p.setColor(Color.RED);
		canvas.drawRect(r, p);
	}

}
