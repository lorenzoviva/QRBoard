package com.example.qrboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.Button;

import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRSquareUserRepresentation;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.model.QRUserRepresentation;

public class QRExplorerRow {

	private QRUserMenager qrum = null;
	private QRUser qru = null;
	private QRUserRepresentation qrur = null;
	private QRSquare qrs = null;
	private QRSquareUser qrsu = null;
	private QRSquareUserRepresentation qrsur = null;
	private int request;
	private int size = 300;
	Paint externalmargin = new Paint();
	Paint internalmargin = new Paint();

	public QRExplorerRow(QRSquare qrs, QRSquareUser qrsu) {
		this.qrs = qrs;
		this.qrsu = qrsu;
		request = 3;
	}

	public QRExplorerRow(QRUserMenager qrum, QRUser qru, QRSquareUser qrsu, int request) {
		this.qrum = qrum;
		this.qru = qru;
		this.qrur = new QRUserRepresentation(qru);
		this.qrsu = qrsu;
		this.qrsur = new QRSquareUserRepresentation(qrsu);
		this.request = request;

	}

	public QRUserMenager getQrum() {
		return qrum;
	}

	public void setQrum(QRUserMenager qrum) {
		this.qrum = qrum;
	}

	public QRUser getQru() {
		return qru;
	}

	public void setQru(QRUser qru) {
		this.qru = qru;
	}

	public QRSquare getQrs() {
		return qrs;
	}

	public void setQrs(QRSquare qrs) {
		this.qrs = qrs;
	}

	public QRSquareUser getQrsu() {
		return qrsu;
	}

	public void setQrsu(QRSquareUser qrsu) {
		this.qrsu = qrsu;
	}

	public void draw(Canvas canvas, QRExplorer qrExplorer, int i, Rect bounds, int scroll, boolean selected) {
		size = (bounds.right-bounds.left)/5;
		if (!selected) {
			externalmargin.setColor(Color.rgb(200, 200, 200));
			externalmargin.setStrokeWidth(4);
			internalmargin.setColor(Color.rgb(133, 133, 133));
			internalmargin.setStrokeWidth(2);
		} else {
			externalmargin.setColor(Color.rgb(0, 0, 0));
			externalmargin.setStrokeWidth(6);
			internalmargin.setColor(Color.rgb(0, 0, 0));
			internalmargin.setStrokeWidth(2);
		}
		if (request == 1) {
			if (qrum != null) {
				qrum.setOne(new PointF((bounds.right - bounds.left) - 3 * size, -scroll + bounds.top + (i + 1) * size));
				qrum.setTwo(new PointF((bounds.right - bounds.left) - 3 * size, -scroll + bounds.top + i * size));
				qrum.setThree(new PointF((bounds.right - bounds.left) - 2 * size, -scroll + bounds.top + i * size));
				qrum.setFour(new PointF((bounds.right - bounds.left) - 2 * size, -scroll + bounds.top + (i + 1) * size));
				qrum.draw(canvas, qrExplorer);
			}
			if (qrsur != null) {
				qrsur.setOne(new PointF((bounds.right - bounds.left) - 2 * size, -scroll + bounds.top + (i + 1) * size));
				qrsur.setTwo(new PointF((bounds.right - bounds.left) - 2 * size, -scroll + bounds.top + i * size));
				qrsur.setThree(new PointF((bounds.right - bounds.left) - 1 * size, -scroll + bounds.top + i * size));
				qrsur.setFour(new PointF((bounds.right - bounds.left) - 1 * size, -scroll + bounds.top + (i + 1) * size));
				qrsur.draw(canvas, qrExplorer);

			}
			if (qrur != null) {
				qrur.setOne(new PointF((bounds.right - bounds.left) - 1 * size, -scroll + bounds.top + (i + 1) * size));
				qrur.setTwo(new PointF((bounds.right - bounds.left) - 1 * size, -scroll + bounds.top + i * size));
				qrur.setThree(new PointF((bounds.right - bounds.left), -scroll + bounds.top + i * size));
				qrur.setFour(new PointF((bounds.right - bounds.left), -scroll + bounds.top + (i + 1) * size));
				qrur.draw(canvas, qrExplorer);
			}
		}
		canvas.drawLine((bounds.right - bounds.left) - 3 * size, -scroll + bounds.top + (i + 1) * size, (bounds.right - bounds.left) - 3 * size, -scroll + bounds.top + (i) * size, externalmargin);
		// if (selected) {
		canvas.drawLine((bounds.right - bounds.left), -2-scroll + bounds.top + (i + 1) * size, (bounds.right - bounds.left) - 3 * size,-2 -scroll + bounds.top + (i + 1) * size, externalmargin);
		// }
		if (selected) {
			canvas.drawLine((bounds.right - bounds.left), -scroll + bounds.top + (i) * size, (bounds.right - bounds.left) - 3 * size,-scroll + bounds.top + (i) * size, externalmargin);
		}
		canvas.drawLine((bounds.right - bounds.left), -scroll + bounds.top + (i + 1) * size, (bounds.right - bounds.left), -scroll + bounds.top + (i) * size, externalmargin);
		canvas.drawLine((bounds.right - bounds.left) - 2 * size, -scroll + bounds.top + (i + 1) * size, (bounds.right - bounds.left) - 2 * size, -scroll + bounds.top + (i) * size, internalmargin);
		canvas.drawLine((bounds.right - bounds.left) - size, -scroll + bounds.top + (i + 1) * size, (bounds.right - bounds.left) - size, -scroll + bounds.top + (i) * size, internalmargin);

	}

	public boolean touchOnQRSquare(MotionEvent event, QRSquare qrs) {
		if (qrs != null) {
			Quadrilateral polygon = new Quadrilateral();
			polygon.addVertex(new Point(qrs.getTwo().x, qrs.getTwo().y));
			polygon.addVertex(new Point(qrs.getThree().x, qrs.getThree().y));
			polygon.addVertex(new Point(qrs.getFour().x, qrs.getFour().y));
			polygon.addVertex(new Point(qrs.getOne().x, qrs.getOne().y));
			return Utility.PointInPolygon(new Point(event.getX(), event.getY()), polygon);
		} else {
			return false;
		}
	}

	public QRSquare touched(MotionEvent event) {

		if (touchOnQRSquare(event, qrum)) {
			return qrum;
		}
		if (touchOnQRSquare(event, qrsur)) {
			return qrsur;
		}
		if (touchOnQRSquare(event, qrur)) {
			return qrur;
		}
		if (touchOnQRSquare(event, qrs)) {
			return qrs;
		}
		return null;
	}

	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

	}

	public int getSize() {
		return size;
	}

}
