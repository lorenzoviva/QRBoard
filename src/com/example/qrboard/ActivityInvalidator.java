package com.example.qrboard;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

public class ActivityInvalidator{
	InvalidableAcivity activity;
//	int i = 0;
	public ActivityInvalidator(InvalidableAcivity context) {
		activity = context;
		final Runnable invalidation = new Runnable() {
		    public void run() {
		    	activity.invalidate();
		    }
		  };
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) { // probably want some other challenge here
					try {
						Thread.currentThread();
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					invalidation.run();
				}
				
				
			}
		};
		thread.start();
		
	}

	

}