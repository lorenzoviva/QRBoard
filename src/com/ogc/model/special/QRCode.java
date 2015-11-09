package com.ogc.model.special;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.example.qrboard.ARLayerView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ogc.model.QRSquare;

public class QRCode extends QRSquare {
	private Bitmap qrcode;

	public QRCode(String text) {
		super(text);
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		// create qr code matrix
		QRCodeWriter writer = new QRCodeWriter();
		try {
			BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE,500, 500, hints);
			qrcode = toBitmapColour(matrix,Color.BLACK);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw(Canvas canvas, ARLayerView arview) {
		// TODO Auto-generated method stub
		if (qrcode != null) {
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
			float[] po = { two.x, two.y, three.x, three.y, one.x, one.y, four.x, four.y };
			canvas.drawBitmapMesh(qrcode, 1, 1, po, 0, null, 0, null);
			
		}else{
			Log.d("ERROR", "img = null");
		}

	}

	public static Bitmap toBitmapColour(BitMatrix matrix, int colour){
	    int height = matrix.getHeight();
	    int width = matrix.getWidth();
	    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    for (int x = 0; x < width; x++){
	        for (int y = 0; y < height; y++){
	            bmp.setPixel(x, y, matrix.get(x,y) ? colour : Color.WHITE);
	        }
	    }
	    return bmp;
	}
}
