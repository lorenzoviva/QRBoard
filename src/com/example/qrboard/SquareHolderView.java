package com.example.qrboard;

import com.ogc.model.QRUser;

import android.content.Context;

public interface SquareHolderView {
	public void setQRSquareScrollable(int horizontal, int vertical);
	public void invalidate();
	public Context getContext();
	public void setActionContext(String string);
	public void performAction(String string);
	public QRUser getUser();
}
