package com.example.qrboard;

import com.ogc.model.QRSquare;
import com.ogc.model.special.QRUserRepresentation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;

public class QRSquareUserEditorView extends ARLayerView{
	private QRUserRepresentation qrur = null;
	private QRSquare qrs = null;
	private String qrst = "";
	private int halfmaxwidth = -1;
	public QRSquareUserEditorView(Context context) {
		super(context);
	}
	public QRSquareUserEditorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub
	}

	public QRSquareUserEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TODO Auto-generated constructor stub
	}
	public void setup(QRUserRepresentation qrur,QRSquare qrs,int halfmaxwidth,String qrst){
		this.qrur = qrur;
		this.qrst = qrst;
		this.qrs = qrs;
		this.halfmaxwidth = halfmaxwidth;
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		Paint border = new Paint();
		border.setColor(Color.BLACK);
		border.setStyle(Paint.Style.STROKE);
		border.setStrokeWidth(1);
		RectF rect = new RectF(0, 0, getRight()-getLeft(), getBottom()-getTop());
		canvas.drawRoundRect(rect, 5, 5, paint);
		canvas.drawRoundRect(rect, 5, 5, border);
		float w;
		if(halfmaxwidth > 0){
			w = (getRight()-getLeft())/2-halfmaxwidth;
		}else{
			w = (getRight()-getLeft())/3;
		}
		if(qrs != null && !qrst.equals("")){
			qrs.setOne(new PointF(10, w));
			qrs.setTwo(new PointF(10, 10));
			qrs.setThree(new PointF(w, 10));
			qrs.setFour(new PointF(w, w));
			Class<? extends QRSquare> squareclass;
			try {
				squareclass = (Class<? extends QRSquare>) Class.forName(qrst);
				(squareclass.cast(qrs)).draw(canvas, this);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		if(qrur != null){
			qrur.setOne(new PointF(getRight()-(getLeft()+w+10), w));
			qrur.setTwo(new PointF(getRight()-(getLeft()+w+10), 10));
			qrur.setThree(new PointF(getRight()-(getLeft()+10), 10));
			qrur.setFour(new PointF(getRight()-(getLeft()+10), w));
			qrur.draw(canvas, this);
	
		}
		
//		canvas.drawRoundRect(getLeft(), getTop(), getRight(), getBottom(),15,15, paint);
	}
	
}
