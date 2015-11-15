package com.ogc.browsers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qrboard.ARLayerView;
import com.ogc.dialog.DialogBuilder;
import com.ogc.model.QRWebPage;

public abstract class LWebView extends WebView {
	public static String applicationid = "qrboard";
	protected QRWebPage qrsquare;
	protected int width, height;
	protected int maxwidth, maxheight;
	protected boolean destroy = false;
	protected final ProgressDialog ringProgressDialog;
	private int maxrequest = 50;
	public String id, lastid;
	protected String localURL = "";
	private float divicePixelRatio = -1;
	private List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();
	ARLayerView arview;
	View mCustomView = null;

	public LWebView(ARLayerView arview, QRWebPage qrsquare, int width, int height) {
		super(arview.getContext());
		this.arview = arview;
		Context context = arview.getContext();
		this.qrsquare = qrsquare;
		this.width = width;
		maxwidth = width;
		this.height = height;
		maxheight = height;

		// Log.d("TAG rendering : ", qrsquare.toString());
		ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "loading web page ...", true);
		ringProgressDialog.setCancelable(true);
		getSettings().setLoadWithOverviewMode(true);
		getSettings().setPluginState(WebSettings.PluginState.ON);
		getSettings().setAllowContentAccess(true);
		getSettings().setDomStorageEnabled(true);
		getSettings().setFantasyFontFamily("fantasy");
		getSettings().setFixedFontFamily("fantasy");
		setWebChromeClient(new WebChromeClient() {

			public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
				Log.d("youtube", "youtube");

			}

			public void onHideCustomView() {
				mCustomView = null;
			}

		});
		getSettings().setJavaScriptEnabled(true); // Support JS
		// setLayerType(this.LAYER_TYPE_SOFTWARE, null);
		addJavascriptInterface(new JsClickInterface(context), "clickInterface");
		final Context fcontext = context;
		setWebViewClient(new WebViewClient() {
			private int i = 0;

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

				switch (errorCode) {
				case (ERROR_AUTHENTICATION):
					showRepresentationErrorMessage(fcontext, "User authentication failed on server");
					break;
				case (ERROR_BAD_URL):
					showRepresentationErrorMessage(fcontext, "Malformed URL");
					break;
				case (ERROR_CONNECT):
					showRepresentationErrorMessage(fcontext, "Failed to connect to the server");
					break;
				case (ERROR_FAILED_SSL_HANDSHAKE):
					showRepresentationErrorMessage(fcontext, "Failed to perform SSL handshake");
					break;
				case (ERROR_FILE):
					showRepresentationErrorMessage(fcontext, "Generic file error");
					break;
				case (ERROR_FILE_NOT_FOUND):
					showRepresentationErrorMessage(fcontext, "File not found");
					break;
				case (ERROR_HOST_LOOKUP):
					showRepresentationErrorMessage(fcontext, "Server or proxy hostname lookup failed");
					break;
				case (ERROR_IO):
					showRepresentationErrorMessage(fcontext, "Failed to read or write to the server");
					break;
				case (ERROR_PROXY_AUTHENTICATION):
					showRepresentationErrorMessage(fcontext, "User authentication failed on proxy");
					break;
				case (ERROR_REDIRECT_LOOP):
					showRepresentationErrorMessage(fcontext, "Too many redirects");
					break;
				case (ERROR_TIMEOUT):
					showRepresentationErrorMessage(fcontext, "Connection timed out");
					break;
				case (ERROR_TOO_MANY_REQUESTS):
					showRepresentationErrorMessage(fcontext, "Too many requests during this load");
					break;
				case (ERROR_UNKNOWN):
					showRepresentationErrorMessage(fcontext, "Generic error");
					break;
				case (ERROR_UNSUPPORTED_AUTH_SCHEME):
					showRepresentationErrorMessage(fcontext, "Unsupported authentication scheme (not basic or digest)");
					break;
				case (ERROR_UNSUPPORTED_SCHEME):
					showRepresentationErrorMessage(fcontext, "Unsupported URI scheme");
					break;
				default:
					showRepresentationErrorMessage(fcontext, description);
				}

			}

			public void onPageFinished(WebView view, String url) {
				int indexOf = url.indexOf("/", 8);
				if (indexOf > 0) {
					localURL = url.substring(0, indexOf);
				} else {
					localURL = url;
				}

				Log.d("LWebViewClient:" + i, "finished loading page url:" + localURL);// +
																						// ", Height:"
																						// +
																						// computeVertical()
																						// +
																						// ",width:"+computeHorizontal()+"] , [content Height: "
																						// +
																						// getContentHeight()+
																						// " ,width:"
																						// +
																						// getRight()
																						// +"]"
																						// +
																						// ",[measured Height:"
																						// +getMeasuredHeight()
																						// +
																						// ", width:"
																						// +
																						// getMeasuredWidth()
																						// +
																						// "]");
				i++;
				if (i > maxrequest) {
					showRepresentationErrorMessage(fcontext, "Too many requests during this load");
				} else {
					if (view.getContentHeight() > 0) {
						finished();

					} else {
						load();
					}
				}

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d("URL LOADING", url);
				view.loadUrl(url);
				return false;
			}
		});

		calculate();
		load();

	}

	public void showRepresentationErrorMessage(Context context, String error) {
		ringProgressDialog.dismiss();
		DialogBuilder.createErrorDialog(context, "An error occurred while tryng to load web page: " + error);
		destroy();
	}

	public void calculate() {
		measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST));// View.MeasureSpec.makeMeasureSpec(5000,
																																						// //
																																						// View.MeasureSpec.AT_MOST));
		// Log.d("calculate page dimension:", getMeasuredWidth() + "," +
		// getMeasuredHeight() ); // //
		layout(0, 0, width, height);
	}

	private void notifyListeners(Object object, String property, String oldValue, String newValue) {
		for (PropertyChangeListener name : listener) {
			name.propertyChange(new PropertyChangeEvent(this, object.toString(), oldValue, newValue));
		}
	}

	public void addChangeListener(PropertyChangeListener newListener) {
		listener.add(newListener);
	}

	public int computeHorizontal() {
		return computeHorizontalScrollRange();
	}

	public int computeVertical() {
		return computeVerticalScrollRange();
	}

	@Override
	protected int computeHorizontalScrollRange() {
		return super.computeHorizontalScrollRange();
	}

	@Override
	protected int computeVerticalScrollRange() {

		return super.computeVerticalScrollRange();
	}

	public void load() {
		String html = qrsquare.getHtml();
		boolean link = true;
		try {
			new URL(html);
		} catch (MalformedURLException e) {
			link = false;
		}
		if (link) {
			loadUrl(html);
			qrsquare.setHorizontalScroll(0);
			qrsquare.setVerticalScroll(0);
		} else {
			loadData(html, "text/html", "charset=UTF-8");
		}

		// loadUrl("https://www.google.it/");
	}

	@Override
	public void destroy() {
		destroy = true;
		super.destroy();
	}

	@Override
	public void invalidate() {

		if (getContentHeight() > 0 && !destroy) {

			// Log.d("LWebViewClient", "finished loading page");
			finished();
		}
		super.invalidate();
	}

	public void finished() {
		if (getMeasuredHeight() > 0 && getMeasuredWidth() > 0) {
			setVerticalScrollBarEnabled(true);
			setHorizontalScrollBarEnabled(true);
			// getDevicePixelRatio();
			arview.setQRSquareScrollable(computeHorizontal() - getMeasuredWidth(), computeVertical() - getMeasuredHeight());
			arview.invalidate();
			ringProgressDialog.dismiss();

		} else {
			width = maxwidth;
			height = maxheight;
			// getDevicePixelRatio();
			calculate();
			load();
		}

	}

	@Override
	public void draw(Canvas canvas) {
		arview.setQRSquareScrollable(computeHorizontal() - getMeasuredWidth(), computeVertical() - getMeasuredHeight());
		canvas.translate(-this.getScrollX(), -this.getScrollY());
		if (mCustomView == null) {
			super.draw(canvas);
		} else {
			mCustomView.draw(canvas);
		}

	}

	public class JsClickInterface {
		Context context;

		public JsClickInterface(Context context) {
			this.context = context;
		}

		@JavascriptInterface
		public void onclick(final String tagname, final String attributes, final String parents) {
			Log.d("CLICKED ON:", tagname);

			if (attributes != null) {
				runOnUIThread(new Runnable() {
					@Override
					public void run() {
						notifyListeners(tagname, "attributes", parents, attributes);
						onElementTouched(tagname, attributes, parents);

					}
				});
				Log.d("ATTRIBUTES:", attributes);
			}
		}

		@JavascriptInterface
		public void setDevicePixelRatio(final float touchX, final float scrollX, final float touchY, final float scrollY, final float f) {
			Log.d("CLICKED ON:", touchX + "," + touchY + "," + f);

			runOnUIThread(new Runnable() {
				@Override
				public void run() {
					clickWebPage(touchX, scrollX, touchY, scrollY, f);
				} // This is your code
			});

		}

		@JavascriptInterface
		public void setDevicePixelRatio(final float f) {
			// Log.d("JSINTERFACE","PIXEL RATIO:" + f);

			runOnUIThread(new Runnable() {
				@Override
				public void run() {
					setDevicePixelRatio(f);
				} // This is your code
			});

		}

		public void runOnUIThread(Runnable runnable) {
			Handler mainHandler = new Handler(context.getMainLooper());
			mainHandler.post(runnable);
		}
	}

	public void clickWebPage(float touchX, float scrollX, float touchY, float scrollY, float f){
		if(!isChangeListenerListEmpty()){
		String js = "javascript:(function() { " 
				//	scroll the window
				+"	window.scrollTo(" + scrollX / f + "," + scrollY / f + "); "
				//	get the clicked object
				+"	var  obj=document.elementFromPoint(" + (touchX / f) + "," + (touchY / f) + ");" 
				+"	while(obj.parentNode!=null && !obj.hasAttribute('id')){"
				//		add the courrent object tagName to 'parents' (eg: DIV)
				+"		parents += obj.tagName + ' ';" 
				//		add all attributes of parent to 'parents' (eg: color<black>)
				+"		if(!(obj instanceof HTMLDocument) && obj.hasAttributes()){"		
				+"			for (i = 0; i < obj.attributes.length; i++) {"
				+"				parents += obj.attributes[i].name + '<' + obj.getAttribute(obj.attributes[i].name) + '>';" 
				+"			}"
				+"		}"
				+"		parents += ' ';" 
				+"		obj = obj.parentNode;" 
				+"	} "
				+"	if(obj!=null) {"
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
	}

	public void setDevicePixelRatio(float f) {
		divicePixelRatio = f;
		height = (int) (maxheight * f);
		width = (int) (maxwidth * f);
	}

	private void getDevicePixelRatio(float touchX, float scrollX, float touchY, float scrollY) {
		String js = "javascript:(function(){" + "var  obj=window.devicePixelRatio;" + "if(obj!=null)" + " {window.clickInterface.setDevicePixelRatio(" + touchX + "," + scrollX + "," + touchY + "," + scrollY + ",obj);}" + "})()";
		loadUrl(js);
	}

	private void getDevicePixelRatio() {
		String js = "javascript:(function(){" + "var  obj=window.devicePixelRatio;" + "if(obj!=null)" + " {window.clickInterface.setDevicePixelRatio(obj);}" + "})()";
		loadUrl(js);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		long pressureTime = event.getEventTime() - event.getDownTime();
		Log.d("Clicked", "pressureTime:" + pressureTime);
		if (pressureTime < 1000) {
			getDevicePixelRatio(event.getX(), getScrollX(), event.getY(), getScrollY());
		} else {
			String url = this.getUrl();

			if (url != null) {
				boolean link = true;
				try {
					new URL(url);
				} catch (MalformedURLException e) {
					link = false;
				}
				if (link) {
					getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				}
			}
		}

		performClick();
		super.onTouchEvent(event);
		return false;
	}

	public abstract void onElementTouched(String tagname, String attributes, String parents);

	public boolean isChangeListenerListEmpty() {
		return listener.isEmpty();
	}

}
