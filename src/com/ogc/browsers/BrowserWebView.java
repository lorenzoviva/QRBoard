package com.ogc.browsers;

import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.example.qrboard.ARLayerView;
import com.ogc.model.QRWebPage;

public class BrowserWebView extends LWebView {

	public BrowserWebView(ARLayerView arview, QRWebPage qrsquare, int width, int height) {
		super(arview, qrsquare, width, height,new LWebViewJsParameters(true,true,true,false,true,true,false));
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

	
}
