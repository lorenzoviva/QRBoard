package com.example.qrboard;

import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.QRSquare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class QRExplorerRowFooter {
	private int height = 50;
	private int width = 50;

	Paint externalmargin = new Paint();
	Paint internalmargin = new Paint();
	Paint backgroundPaint = new Paint();
	Paint disabled = new Paint();
	Paint textPaint = new Paint();
	int maxusers = -1;
	int listindex = -1;
	int totusers = -1;
	boolean backAllowed = false;
	boolean forwardAllowed = false;
	Rect backRect = null;
	Rect forwardRect = null;
	Bitmap forwardIcon = null;
	Bitmap backIcon = null;

	public QRExplorerRowFooter(int maxusers, int listindex, int totusers, View view) {
		this.maxusers = maxusers;
		this.listindex = listindex;
		this.totusers = totusers;
		if (totusers > listindex + 1) {
			forwardAllowed = true;
			forwardIcon = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.edit32x32);
		}
		if (listindex + 1 > maxusers) {
			backAllowed = true;
			backIcon = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.edit32x32);
		}
	}

	public int getForwardIndex() {
		if (listindex != -1) {
			return listindex + 1;
		} else {
			return 0;
		}
	}
	public int getCurrentIndex() {
		if ((listindex != -1 && maxusers != -1) && (listindex - (maxusers- 1) >= 0)) {
			return listindex - (maxusers - 1);
		} else {
			return 0;
		}

	}
	public int getBackIndex() {
		if ((listindex != -1 && maxusers != -1) && (listindex - (maxusers * 2 - 1) >= 0)) {
			return listindex - (maxusers * 2 - 1);
		} else {
			return 0;
		}

	}

	public int getHeight(Rect bounds) {
		return (bounds.right - bounds.left) / 12;
	}

	public int getWidth(Rect bounds) {
		return (bounds.right - bounds.left) / 5;
	}

	public void draw(Canvas canvas, QRExplorer qrExplorer, Rect bounds, int top) {
		width = getWidth(bounds);
		height = getHeight(bounds);
		externalmargin.setColor(Color.WHITE);
		externalmargin.setStrokeWidth(6);
		internalmargin.setColor(Color.WHITE);
		internalmargin.setStrokeWidth(2);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(height / 2);
		textPaint.setTextAlign(Align.CENTER);
		disabled.setColor(Color.rgb(180, 180, 180));
		backgroundPaint.setColor(Color.rgb(0, 100, 150));
		canvas.drawRect(new Rect((bounds.right - bounds.left) - 3 * width, top + bounds.top, (bounds.right - bounds.left), top + bounds.top + height), backgroundPaint);
		canvas.drawLine((bounds.right - bounds.left) - 3 * width, top + bounds.top + height, (bounds.right - bounds.left) - 3 * width, top + bounds.top, externalmargin);
		canvas.drawLine((bounds.right - bounds.left), top - 2 + bounds.top + height, (bounds.right - bounds.left) - 3 * width, top - 2 + bounds.top + height, externalmargin);
		canvas.drawLine((bounds.right - bounds.left), top + bounds.top, (bounds.right - bounds.left) - 3 * width, top + bounds.top, externalmargin);
		canvas.drawLine((bounds.right - bounds.left), top + bounds.top + height, (bounds.right - bounds.left), top + bounds.top, externalmargin);
		canvas.drawLine((bounds.right - bounds.left) - 2 * width, top + bounds.top + height, (bounds.right - bounds.left) - 2 * width, top + bounds.top, internalmargin);
		canvas.drawLine((bounds.right - bounds.left) - width, top + bounds.top + height, (bounds.right - bounds.left) - width, top + bounds.top, internalmargin);
		if (maxusers != -1 && listindex != -1 && totusers != -1) {
			String text = listindex + 1 + "/" + totusers;
			canvas.drawText(text, (bounds.right - bounds.left) - 2 * width + (width / 2), (top + bounds.top) + (height / 2), textPaint);
		}
		if (backAllowed) {
			canvas.drawBitmap(backIcon, (bounds.right - bounds.left) - 5 * (width / 2) - (backIcon.getWidth() / 2), top + bounds.top + (height / 2) - (backIcon.getHeight() / 2), internalmargin);
			backRect = new Rect((bounds.right - bounds.left) - (3 * width), top + bounds.top, (bounds.right - bounds.left) - (2 * width), top + bounds.top + height);
		}
		if (forwardAllowed) {
			canvas.drawBitmap(forwardIcon, (bounds.right - bounds.left) - (width / 2) - (forwardIcon.getWidth() / 2), top + bounds.top + (height / 2) - (forwardIcon.getHeight() / 2), internalmargin);
			forwardRect = new Rect((bounds.right - bounds.left) - (width), top + bounds.top, (bounds.right - bounds.left), top + bounds.top + height);

		}
	}

	public boolean touchOnRect(MotionEvent event, Rect rect) {
		if (rect != null) {
			Quadrilateral polygon = new Quadrilateral();
			polygon.addVertex(new Point(rect.left, rect.top));
			polygon.addVertex(new Point(rect.right, rect.top));
			polygon.addVertex(new Point(rect.right, rect.bottom));
			polygon.addVertex(new Point(rect.left, rect.bottom));
			return Utility.PointInPolygon(new Point(event.getX(), event.getY()), polygon);
		} else {
			return false;
		}
	}

	public int touched(MotionEvent event) {

		if (touchOnRect(event, backRect)) {
			return getBackIndex();
		}
		if (touchOnRect(event, forwardRect)) {
			return getForwardIndex();
		}
		return -1;
	}
}
