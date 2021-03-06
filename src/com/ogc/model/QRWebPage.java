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
import com.ogc.browsers.LWebViewJsParameters;

public class QRWebPage extends QRSquare {

	private String html;
	protected transient LWebView webview = null;
	private String selectedId = "";

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
			float density = arview.getContext().getResources().getDisplayMetrics().density;
			webview = new BrowserWebView(arview, this, (int) (250*density), (int) (250*density));

		} else {
			if(!selectedId.equals("") && webview.getJsParameters()!=null){
				if(webview.getJsParameters().isEditPage()){
					if(webview.getJsParameters().getSelectedId()==null || webview.getJsParameters().getSelectedId().equals("") || !webview.getJsParameters().getSelectedId().equals(selectedId)){
						webview.select(selectedId);
					}else{
						selectedId = "";
					}
				}
			}
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

		if (webview != null) {
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
			webview.onTouchEvent(event,this);
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
	public void setJsParameters(LWebViewJsParameters jsParameters) {
		if(webview!=null){
			webview.setJsParameters(jsParameters);
		}
		
	}
	public void select(String selectedId) {
		this.selectedId = selectedId;
	}
	public String getSelectedId() {
		// TODO Auto-generated method stub
		return selectedId;
	}
}
