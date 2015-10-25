package com.example.qrboard;

import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;
import android.view.View;

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
				+"	document.scrollTo(" + scrollX / f + "," + scrollY / f + "); "
				//	get the clicked object
				+"	var  obj=document.elementFromPoint(" + (touchX / f) + "," + (touchY / f) + ");" 
				+"	var parents = '';"
				//	Throws a click event
				+"	if (obj.fireEvent) {"
				+"		obj.fireEvent('onclick');"
 				+"	} else {"
   				+"		var evObj = document.createEvent('Events');"
   				+"		evObj.initEvent('click', true, false);"
   				+"		obj.dispatchEvent(evObj);"
   				+"	}" 
   				//	find a parent object with an action (url, scr , onclick method, href)
				+"	while((obj.tagName == 'IMG' && obj.parentNode!=null) || (obj.onclick == null && obj.parentNode!=null && !obj.hasAttribute('src') && !obj.hasAttribute('href'))){"
				//		add the courrent object tagName to 'parents' (eg: DIV)
				+"		parents += obj.tagName + ' ';" 
				//		add all attributes of parent to 'parents' (eg: color<black>)
				+"		if(obj.tagName != 'DOCUMENT' && obj.hasAttributes()){"		
				+"			for (i = 0; i < obj.attributes.length; i++) {"
				+"				parents += obj.attributes[i].name + '<' + obj.getAttribute(obj.attributes[i].name) + '>';" 
				+"			}"
				+"		}"
				+"		parents += ' ';" 
				+"		obj = obj.parentNode;" 
				+"	} " 
				+"	if(obj!=null) {"
				+"		if(obj.onclick != null){"
				+"	 		obj.onclick();"
				+"		}"
				+"		var att = '';"
				//		add all attributes to the attribute list
				+"		if(!(obj instanceof HTMLDocument) && obj.hasAttributes()){"		
				+"			for (i = 0; i < obj.attributes.length; i++) {"
				+"				att += obj.attributes[i].name + '<' + obj.getAttribute(obj.attributes[i].name) + '>';" 
				+"			}" 
				+"		}"
				+"		window.clickInterface.onclick(obj.tagName,att,parents);"
				+"	}" 
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

			arview.setQRSquareScrollable(computeHorizontal() - getMeasuredWidth(), computeVertical() - getMeasuredHeight());
			arview.invalidate();
			ringProgressDialog.dismiss();
			String js;
			if(user!=null){
				js = "javascript:(function() {window.join('"+chat.getText()+"','"+String.valueOf(user.getId())+"');})()";
			}else{
				js = "javascript:(function() {window.join('"+chat.getText()+"','anonymous');})()";
			}
			loadUrl(js);
		} else {
			width = maxwidth;
			height = maxheight;
			calculate();
			load();
		}

	}
	

}
