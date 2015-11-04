package com.ogc.model;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.qrboard.ARLayerView;
import com.example.qrboard.SquareHolderView;
import com.ogc.browsers.UserMenagerWebView;
import com.ogc.browsers.UsersWebView;

public class QRUsersWebPage extends QRWebPage{
	List<QRSquareUser> squareUsers;
	public QRUsersWebPage(String text, List<QRSquareUser> squareUsers) {
		super(text);
		this.squareUsers = squareUsers;
		String html = "<table><tr><th>Name</th><th>Role</th></tr>";
		for(int i = 0; i < squareUsers.size() ; i++){
			QRSquareUser qrSquareUseri = squareUsers.get(i);
			String id = UsersWebView.applicationid+":user"+qrSquareUseri.getUser().getId() + "role"+ qrSquareUseri.getRole().getId();
			if(qrSquareUseri.getRole().getName().equals("owner")){
				//bgcolor='#FF5050'
				html += "<tr><td style=\"background-color:#ffffff;\" id=\""+id+"td\">" + qrSquareUseri.getUser().getFirstName() + " " + qrSquareUseri.getUser().getLastName() + " : </td><td> <button id=\""+id+"button\">"+qrSquareUseri.getRole().getName()+"</button> </td></tr>";
			}else if(qrSquareUseri.getRole().getName().equals("request")){
				html += "<tr ><td style=\"background-color:#ffffff;\" id=\""+id+"td\">" + qrSquareUseri.getUser().getFirstName() + " " + qrSquareUseri.getUser().getLastName() + " : </td><td> <button  id=\""+id+"button\">"+qrSquareUseri.getRole().getName()+"</button> </td></tr>";
			}else{
				html += "<tr><td style=\"background-color:#ffffff;\" id=\""+id+"td\">" + qrSquareUseri.getUser().getFirstName() + " " + qrSquareUseri.getUser().getLastName() + " : </td><td> <button id=\""+id+"button\">"+qrSquareUseri.getRole().getName()+"</button> </td></tr>";
			}
		}
		html+="</table>";
		setHtml(html);
	}
	@Override
	public void draw(Canvas canvas, SquareHolderView arview) {
//		super.draw(canvas, arview);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		if (webview == null) {
			float density = arview.getContext().getResources().getDisplayMetrics().density;
			webview = new UsersWebView(arview, this, (int)(density*250), (int)( density*250));

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
}
