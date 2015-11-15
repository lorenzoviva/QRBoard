package com.ogc.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

import com.example.qrboard.ARLayerView;
import com.ogc.browsers.BrowserWebView;
import com.ogc.browsers.LWebView;

public class QRWebPage extends QRSquare {

	private String html;
	protected transient LWebView webview = null;
	private transient float tx;
	private transient float ty;
	private transient float dx;
	private transient float dy;
	public QRWebPage(){
		
	}
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public QRWebPage(String text, String html) {
		super(text);
		this.html = html;
	}
	public QRWebPage(String text){
		super(text);
	}

	@Override
	public void draw(Canvas canvas, ARLayerView arview) {
		super.draw(canvas, arview);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		if (webview == null) {
			webview = new BrowserWebView(arview, this, 500, 500);
		} else {

			// webview.layout(0, 0, 500, 500);
			int w = webview.getMeasuredWidth();
			int h = webview.getMeasuredHeight();
			// Log.d("page dimension:", w + "," + h );
			if (getHorizontalScroll() != webview.getScrollX()) {
				webview.setScrollX(getHorizontalScroll());
			}
			// Log.d("Scroll", "scroll (x:" + getHorizontalScroll() + ",y:" +
			// getVerticalScroll() + ") webview.Scroll (x:" +
			// webview.getScrollX() + ",y:" + webview.getScrollY() + ") max:" +
			// webview.getScrollBarSize());
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

		// canvas.drawText(html, one.x, one.y, paint);

	}

	public void onTouch(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP) {

			if (dx < 5 && dy < 5) {
				
					int w = webview.getMeasuredWidth();
					int h = webview.getMeasuredHeight();
					Matrix matrix = new Matrix();
					float[] dest = { two.x, two.y, three.x, three.y, four.x, four.y, one.x, one.y };
					float[] src = { 0, 0, w, 0, w, h, 0, h };
					matrix.setPolyToPoly(src, 0, dest, 0, 4);
					matrix.invert(matrix);
					final float x = event.getX();
					final float y = event.getY();

					float[] pts = { x, y };
					matrix.mapPoints(pts);
					event.setLocation(pts[0], pts[1]);
					if (webview != null) {
						webview.onTouchEvent(event);
					}
			}

		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			tx = -1;
			ty = -1;
			dx = 0;
			dy = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if(tx!=-1){
				float ddx = tx - event.getX();
				float ddy = ty - event.getY();
				dx += Math.abs(ddx);
				dy += Math.abs(ddy);

				if (tx != -1) {
					PointF normalvector = new PointF();
					normalvector.x = (float)(getFour().x-getOne().x);
					normalvector.y = (float)(getFour().y-getOne().y);
					float normalnorm = (float) Math.sqrt(Math.pow(normalvector.x,2)+Math.pow(normalvector.y,2));
					float ddnorm =  (float) Math.sqrt(Math.pow(ddx,2)+Math.pow(ddy,2));
					float theta = (float) Math.acos(((float)(normalvector.x*ddx)+(normalvector.y*ddy))/(normalnorm*ddnorm));
					
					PointF perpendicularvector = new PointF();
					perpendicularvector.x = (float)(getTwo().x-getOne().x);
					perpendicularvector.y = (float)(getTwo().y-getOne().y);
					float perpendiculnorm = (float) Math.sqrt(Math.pow(perpendicularvector.x,2)+Math.pow(perpendicularvector.y,2));
					float beta = (float) Math.acos(((float)(perpendicularvector.x*ddx)+(perpendicularvector.y*ddy))/(perpendiculnorm*ddnorm));
				
					
					
					ddy = (float) -(ddnorm * (float)Math.cos(beta));
					ddx = (float) -(ddnorm * (float)Math.cos(theta));
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

	@Override 
	public String toString() {
		return super.toString() + ", html= " + html + "]";
	}
	
	@Override
	public String getCreationChoiseHtml() {
		return "<td height='25%' id='"+LWebView.applicationid + ".create." + this.getClass().getSimpleName()+"'  width='25%' bgcolor='#FFF000' style=\"word-wrap:break-word;\"><div align='center'>"+this.getClass().getSimpleName()+"</div><br><div align='center'><i class='fa fa-file'></div></i></td>";
	}
	public LWebView getWebview() {
		return webview;
	}
	public void setWebview(LWebView webview) {
		this.webview = webview;
	}
}
