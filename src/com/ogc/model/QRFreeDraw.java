package com.ogc.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.example.qrboard.ARLayerView;
import com.example.qrboard.LWebView;
import com.example.qrboard.SquareHolderView;
import com.google.gson.annotations.Expose;

public class QRFreeDraw extends QRSquare {

	// per serializzare/deserializzare personalmete l'immagine che viene
	// inserita nel json
	// @Expose(serialize = false, deserialize = false)
	private transient byte[] img;
	private String name;

	public QRFreeDraw() {
	}

	public QRFreeDraw(byte[] im, String nome, String text) {
		super(text);
		this.img = im;
		this.name = nome;
	}

	@Override
	public String getCreationChoiseHtml() {
		return "<td height='25%' width='25%' id='" + LWebView.applicationid + ".create." + this.getClass().getSimpleName() + "'  bgcolor='#FF0000' style=\"word-wrap:break-word;\"><div align='center'>" + this.getClass().getSimpleName() + "</div><br><div align='center'><i  class='fa fa-pencil'></div></i></td>";
	}

	/**
	 * @return the img
	 */
	public byte[] getImg() {
		return img;
	}

	/**
	 * @param img
	 *            the img to set
	 */
	public void setImg(byte[] img) {
		this.img = img;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void draw(Canvas canvas, SquareHolderView arview) {
		if (img != null) {
			Paint wallpaint = new Paint();
			wallpaint.setColor(Color.WHITE);
//			wallpaint.setAlpha(128);
			wallpaint.setStyle(Paint.Style.FILL);
			Path wallpath = new Path();
			wallpath.reset(); // only needed when reusing this path for a new build
			wallpath.moveTo(one.x, one.y); // used for first point
			wallpath.lineTo(two.x, two.y);
			wallpath.lineTo(three.x, three.y);
			wallpath.lineTo(four.x,four.y);
			wallpath.lineTo(one.x, one.y); // there is a setLastPoint action but i found it not to work as expected
			
			canvas.drawPath(wallpath, wallpaint);
			
			
			Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
			float[] po = { two.x, two.y, three.x, three.y, one.x, one.y, four.x, four.y };
			canvas.drawBitmapMesh(bitmap, 1, 1, po, 0, null, 0, null);
			
		}else{
			Log.d("ERROR", "img = null");
		}

	}

}
