package com.ogc.browsers;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.example.qrboard.ARLayerView;
import com.ogc.model.QRChat;
import com.ogc.model.QRChatWebPage;
import com.ogc.model.QRUser;

public class ChatPageWebView extends LWebView{

	private QRChat chat;
	private QRUser user;
	
	public ChatPageWebView(ARLayerView arview, QRChatWebPage qrsquare, int width, int height) {
		super(arview, qrsquare, width, height);
		this.chat=qrsquare.getChat();
		this.user = arview.getUser();
		this.addJavascriptInterface(new JsScollInterface(arview.getContext()), "scrollInterface");
	}

	@Override
	public void onElementTouched(String tagname, String attributes, String parents) {
		Log.d("Parents:", parents);
		if (attributes.contains("src<")) {
			String[] scrattributes = attributes.split("src<");
			String url = scrattributes[1].substring(0, scrattributes[1].indexOf(">"));
			if (!url.startsWith("data:")) {
				boolean link = true;
				try {
					new URL(url);
				} catch (MalformedURLException e) {
					link = false;
				}
				if (link) {
					qrsquare.setHtml(url);
					qrsquare.setHorizontalScroll(0);
					qrsquare.setVerticalScroll(0);
					calculate();
					load();
				} else {
					link = true;
					url = localURL + url;
					try {
						new URL(url);
					} catch (MalformedURLException e) {
						link = false;
					}
					if (link) {
						qrsquare.setHtml(url);
						qrsquare.setHorizontalScroll(0);
						qrsquare.setVerticalScroll(0);
						calculate();
						load();
					}
				}
			}
		}
		if (attributes.contains("href<")) {
			String[] hrefattributes = attributes.split("href<");
			String url = hrefattributes[1].substring(0, hrefattributes[1].indexOf(">"));
			if (!url.startsWith("data:")) {
				boolean link = true;
				try {
					new URL(url);
				} catch (MalformedURLException e) {
					link = false;
				}
				if (link) {
					qrsquare.setHtml(url);
					qrsquare.setHorizontalScroll(0);
					qrsquare.setVerticalScroll(0);
					calculate();
					load();
				} else {
					link = true;
					url = localURL + url;
					try {
						new URL(url);
					} catch (MalformedURLException e) {
						link = false;
					}
					if (link) {
						qrsquare.setHtml(url);
						qrsquare.setHorizontalScroll(0);
						qrsquare.setVerticalScroll(0);
						calculate();
						load();
					}
				}
			}
		}
	}

	@Override
	public void calculate() {
		width = 500;
		height = 500;
		measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));// View.MeasureSpec.makeMeasureSpec(5000,
																																						// //
																																						// View.MeasureSpec.AT_MOST));
		// Log.d("calculate page dimension:", getMeasuredWidth() + "," +
		// getMeasuredHeight() ); // //
		layout(0, 0, width, height);
	}
	@Override
	public void clickWebPage(float touchX, float scrollX, float touchY, float scrollY, float f) {

		String js = "javascript:(function() { " 
				//	scroll the window
				+"	document.getElementById('messages').scrollTo(" + scrollX / f + "," + scrollY / f + "); "
				//	get the clicked object
				+"	var  obj=document.elementFromPoint(" + (touchX / f) + "," + (touchY / f) + ");" 
				+"})()";
		loadUrl(js);
	}
	
	public void onClose(){
		String js = "javascript:(function() {window.closeSocket();})()";
		loadUrl(js);
	}
	@Override
	public void finished() {
		if (getMeasuredHeight() > 0 && getMeasuredWidth() > 0) {
			setVerticalScrollBarEnabled(true);
			setHorizontalScrollBarEnabled(true);

			
			arview.invalidate();
			ringProgressDialog.dismiss();
			String js;
			if(user!=null){
				js = "javascript:(function() {window.join('"+chat.getText()+"','"+String.valueOf(user.getId())+"');var ul = document.getElementById('messages');window.scrollInterface.setScroll(ul.lastChild.offsetTop - ul.firstChild.offsetTop	+ ul.lastChild.outerHeight + parseFloat(ul.get('padding-top'))+ parseFloat(ul.get('padding-bottom')));})()";
			}else{
				js = "javascript:(function() {window.join('"+chat.getText()+"','anonymous');	var ul = document.getElementById('messages');window.scrollInterface.setScroll(ul.lastChild.offsetTop - ul.firstChild.offsetTop	+ ul.lastChild.outerHeight + parseFloat(ul.get('padding-top'))+ parseFloat(ul.get('padding-bottom')));})()";
			}
			loadUrl(js);
		} else {
			width = maxwidth;
			height = maxheight;
			calculate();
			load();
		}

		

	}
	public class JsScollInterface {
		private Context context;
		public JsScollInterface(Context context) {
			this.context = context;
		}
		@JavascriptInterface
		public void setScroll(final float scrollY) {

			if (scrollY != 0) {
				runOnUIThread(new Runnable() {
					@Override
					public void run() {
						setScroll(scrollY);

					}
				});
			}
		}
		public void runOnUIThread(Runnable runnable) {
			Handler mainHandler = new Handler(context.getMainLooper());
			mainHandler.post(runnable);
		}
	}
	public void setScroll(float scrollY){
		Log.d("OMFG","scrollY :" + scrollY);
		arview.setQRSquareScrollable(0, (int)scrollY);
	}


}
