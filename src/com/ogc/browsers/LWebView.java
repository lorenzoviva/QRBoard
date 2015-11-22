package com.ogc.browsers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
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
	private List<BrowserListener> listener = new ArrayList<BrowserListener>();
	ARLayerView arview;
	View mCustomView = null;
	private long pressureTime;
	private transient float tx;
	private transient float ty;
	private transient float dx;
	private transient float dy;
	LWebViewJsParameters jsParameters;

	public LWebView(ARLayerView arview, QRWebPage qrsquare, int width,
			int height, LWebViewJsParameters jsParameters) {
		super(arview.getContext());
		this.arview = arview;
		Context context = arview.getContext();
		this.qrsquare = qrsquare;
		this.width = width;
		maxwidth = width;
		this.height = height;
		maxheight = height;
		this.jsParameters = jsParameters;

		// Log.d("TAG rendering : ", qrsquare.toString());
		ringProgressDialog = ProgressDialog.show(context, "Please wait ...",
				"loading web page ...", true);
		ringProgressDialog.setCancelable(true);
		getSettings().setLoadWithOverviewMode(true);
		getSettings().setPluginState(WebSettings.PluginState.ON);
		getSettings().setAllowContentAccess(true);
		getSettings().setDomStorageEnabled(true);
		getSettings().setFantasyFontFamily("fantasy");
		getSettings().setFixedFontFamily("fantasy");
		setWebChromeClient(new WebChromeClient() {

			public void onShowCustomView(View view,
					WebChromeClient.CustomViewCallback callback) {
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

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				switch (errorCode) {
				case (ERROR_AUTHENTICATION):
					showRepresentationErrorMessage(fcontext,
							"User authentication failed on server");
					break;
				case (ERROR_BAD_URL):
					showRepresentationErrorMessage(fcontext, "Malformed URL");
					break;
				case (ERROR_CONNECT):
					showRepresentationErrorMessage(fcontext,
							"Failed to connect to the server");
					break;
				case (ERROR_FAILED_SSL_HANDSHAKE):
					showRepresentationErrorMessage(fcontext,
							"Failed to perform SSL handshake");
					break;
				case (ERROR_FILE):
					showRepresentationErrorMessage(fcontext,
							"Generic file error");
					break;
				case (ERROR_FILE_NOT_FOUND):
					showRepresentationErrorMessage(fcontext, "File not found");
					break;
				case (ERROR_HOST_LOOKUP):
					showRepresentationErrorMessage(fcontext,
							"Server or proxy hostname lookup failed");
					break;
				case (ERROR_IO):
					showRepresentationErrorMessage(fcontext,
							"Failed to read or write to the server");
					break;
				case (ERROR_PROXY_AUTHENTICATION):
					showRepresentationErrorMessage(fcontext,
							"User authentication failed on proxy");
					break;
				case (ERROR_REDIRECT_LOOP):
					showRepresentationErrorMessage(fcontext,
							"Too many redirects");
					break;
				case (ERROR_TIMEOUT):
					showRepresentationErrorMessage(fcontext,
							"Connection timed out");
					break;
				case (ERROR_TOO_MANY_REQUESTS):
					showRepresentationErrorMessage(fcontext,
							"Too many requests during this load");
					break;
				case (ERROR_UNKNOWN):
					showRepresentationErrorMessage(fcontext, "Generic error");
					break;
				case (ERROR_UNSUPPORTED_AUTH_SCHEME):
					showRepresentationErrorMessage(fcontext,
							"Unsupported authentication scheme (not basic or digest)");
					break;
				case (ERROR_UNSUPPORTED_SCHEME):
					showRepresentationErrorMessage(fcontext,
							"Unsupported URI scheme");
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

				Log.d("LWebViewClient:" + i, "finished loading page url:"
						+ localURL + ", Height:" + computeVertical()
						+ ",width:" + computeHorizontal()
						+ "] , [content Height: " + getContentHeight()
						+ " ,width:" + getRight() + "]" + ",[measured Height:"
						+ getMeasuredHeight() + ", width:" + getMeasuredWidth()
						+ "]");
				i++;
				if (i > maxrequest) {
					showRepresentationErrorMessage(fcontext,
							"Too many requests during this load");
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
		DialogBuilder.createErrorDialog(context,
				"An error occurred while tryng to load web page: " + error);
		destroy();
	}

	public void calculate() {
		measure(View.MeasureSpec.makeMeasureSpec(width,
				View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(
				height, View.MeasureSpec.AT_MOST));// View.MeasureSpec.makeMeasureSpec(5000,
													// //
													// View.MeasureSpec.AT_MOST));
		// Log.d("calculate page dimension:", getMeasuredWidth() + "," +
		// getMeasuredHeight() ); // //
		layout(0, 0, width, height);
	}

	private void notifyListeners(String tagname, String attributes,
			String parents, int eventAction, Rect elementBounds, int w, int h,
			float touchX, float touchY, float scrollY, float scrollX, float f) {
		for (BrowserListener name : listener) {
			name.onBrowserClickEvent(new BrowserClickEvent(tagname, attributes,
					parents, pressureTime, eventAction, elementBounds, w, h,
					touchX, touchY, scrollX, scrollY, f));
		}
	}

	public void addChangeListener(BrowserListener newListener) {
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
			Document doc = Jsoup.parse(html);
			doc.head()
					.append("<meta name='viewport' content='user-scalable=no, width=device-width, target-densitydpi=device-dpi'>");
			loadDataWithBaseURL("file:///android_asset/fonts/", doc.html(),
					"text/html", "charset=UTF-8", null);
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
		Log.d("LWebView", "finished loading page url:"
				+ localURL + ", Height:" + computeVertical()
				+ ",width:" + computeHorizontal()
				+ "] , [content Height: " + getContentHeight()
				+ " ,width:" + getRight() + "]" + ",[measured Height:"
				+ getMeasuredHeight() + ", width:" + getMeasuredWidth()
				+ "]");
		if (getMeasuredHeight() > 0 && getMeasuredWidth() > 0) {
			setVerticalScrollBarEnabled(true);
			setHorizontalScrollBarEnabled(true);
			
			// getDevicePixelRatio();
			arview.setQRSquareScrollable(computeHorizontal()
					- getMeasuredWidth(), computeVertical()
					- getMeasuredHeight());
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
		arview.setQRSquareScrollable(computeHorizontal() - getMeasuredWidth(),
				computeVertical() - getMeasuredHeight());
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
		public void onclick(final String tagname, final String attributes,
				final String parents, final int eventAction, final String rect,
				final int h, final int w, final float touchX,
				final float touchY, final float scrollX, final float scrollY,
				final float f) {
			Log.d("CLICKED ON:", tagname + " " + rect + " h: " + h + " , w: "
					+ w);

			if (attributes != null) {
				runOnUIThread(new Runnable() {
					@Override
					public void run() {

						notifyListeners(tagname, attributes, parents,
								eventAction, getRect(rect), w, h, touchX,
								touchY, scrollX, scrollY, f);
						if (eventAction == MotionEvent.ACTION_UP) {
							onElementTouched(tagname, attributes, parents);
						}

					}
				});
				Log.d("ATTRIBUTES:", attributes);
			}
		}

		public Rect getRect(String rect) {
			if ((rect != null) && !rect.equals("")) {
				String x = rect.split("x:")[1].split(",")[0];
				String y = rect.split("y:")[1].split(",")[0];
				String h = rect.split("h:")[1].split(",")[0];
				String w = rect.split("w:")[1].split(",")[0];
				if (!x.equals("") && !y.equals("") && !w.equals("")
						&& !h.equals("")) {
					int left = (int) Float.parseFloat(x.trim());
					int top = (int) Float.parseFloat(y.trim());
					int right = (int) Float.parseFloat(w.trim()) + left;
					int bottom = (int) Float.parseFloat(h.trim()) + top;
					return new Rect(left, top, right, bottom);
				} else {
					return null;
				}
			} else
				return null;
		}

		@JavascriptInterface
		public void setDevicePixelRatio(final float touchX,
				final float scrollX, final float touchY, final float scrollY,
				final int eventAction, final float f) {
			Log.d("CLICKED ON:", touchX + "," + touchY + "," + f);

			runOnUIThread(new Runnable() {
				@Override
				public void run() {
					clickWebPage(touchX, scrollX, touchY, scrollY, eventAction,
							f);
				}
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

	public void clickWebPage(float touchX, float scrollX, float touchY,
			float scrollY, int eventAction, float f) {
		// if(!isChangeListenerListEmpty()){
		boolean actionUP = eventAction == MotionEvent.ACTION_UP;

		String whilecondition = createWhileCondition();

		String js = "javascript:(function() { "
				// scroll the window
				+ "	window.scrollTo(" + scrollX / f + "," + scrollY / f + "); "
				// get the clicked object
				+ "	var  obj=document.elementFromPoint(" + (touchX / f) + ","
				+ (touchY / f) + ");" + "	var  parents = '';";
		if (actionUP && jsParameters.isExecuteOnClick()
				&& (!jsParameters.isEditPage())) {
			js += "if (obj.fireEvent) {" + "		obj.fireEvent('onclick');"
					+ "	} else {"
					+ "		var evObj = document.createEvent('Events');"
					+ "		evObj.initEvent('click', true, false);"
					+ "		obj.dispatchEvent(evObj);" + "	}";
		} else if (jsParameters.isEditPage()) {
			js += "	var rect = obj.getBoundingClientRect();"
					+ "	var rc = ' x:'+ rect.left+', y: '+rect.top+',  w: '+rect.width+',  h: '+rect.height;";
		}
		js += "	while("
				+ whilecondition
				+ "){"
				// add the courrent object tagName to 'parents' (eg: DIV)
				+ "		parents += obj.tagName + ' ';"
				// add all attributes of parent to 'parents' (eg:
				// color<black>)
				+ "		if(!(obj instanceof HTMLDocument) && obj.hasAttributes()){"
				+ "			for (i = 0; i < obj.attributes.length; i++) {"
				+ "				parents += obj.attributes[i].name + '<' + obj.getAttribute(obj.attributes[i].name) + '>';"
				+ "			}" + "		}" + "		parents += ' ';"
				+ "		obj = obj.parentNode;" + "	} " + "	if(obj!=null) {";
		if (actionUP && jsParameters.isExecuteOnClick()
				&& jsParameters.isFollowOnClick()
				&& (!jsParameters.isEditPage())) {
			js += "	if(obj.onclick != null){" + "	 		obj.onclick();" + "		}";
		}

		if (jsParameters.isEditPage()) {
			js += "     if(rect==null){"
					+ "	 		rect = obj.getBoundingClientRect();"
					+ "	 		rc = ' x:'+ rect.left+', y: '+rect.top+',  w: '+rect.width+',  h: '+rect.height;"
					+ " 	}";
		}
		// add all attributes to the attribute list
		js += "	var att = '';"
				+ "		if(!(obj instanceof HTMLDocument) && obj.hasAttributes()){"
				+ "			for (i = 0; i < obj.attributes.length; i++) {"
				+ "				att += obj.attributes[i].name + '<' + obj.getAttribute(obj.attributes[i].name) + '>';"
				+ "			}" + "		}";
		if (jsParameters.isEditPage()) {
			if (jsParameters.getSelectedId() != null
					&& !jsParameters.getSelectedId().equals("")) {
				js += "    obj = document.getElementById('"
						+ jsParameters.getSelectedId()
						+ "');"
						+ "	   if(obj!=null){"
						+ "	   		rect = obj.getBoundingClientRect();"
						+ "	   		rc = ' x:'+ rect.left+', y: '+rect.top+',  w: '+rect.width+',  h: '+rect.height;"
						+ "     }";
			}
			js += "var h = window.innerHeight;" + "var w = window.innerWidth;"
					+ "	window.clickInterface.onclick(obj.tagName,att,parents,"
					+ eventAction + ",rc,h,w," + touchX + "," + touchY + ","
					+ scrollX + "," + scrollY + "," + f + ");";
		} else {
			js += "	window.clickInterface.onclick(obj.tagName,att,parents,"
					+ eventAction + ",null,0,0,0,0," + scrollX + "," + scrollY
					+ "," + f + ");";
		}
		js += "	}" + "})()";
		loadUrl(js);

		// }
	}

	private String createWhileCondition() {
		String res = "";
		if (jsParameters.isExcludeImages()) {
			res += "(obj.tagName == 'IMG' && obj.parentNode!=null) || (";
		}
		res += "obj.parentNode!=null ";
		if (jsParameters.isFollowId()) {
			res += "&& !obj.hasAttribute('id')";
		}
		if (jsParameters.isFollowExternalLinks()) {
			res += "&& !obj.hasAttribute('src') && !obj.hasAttribute('href')";
		}
		if (jsParameters.isFollowOnClick()) {
			res += "&& obj.onclick == null";
		}
		if (jsParameters.isExcludeImages()) {
			res += ")";
		}

		return res;
	}

	public void setDevicePixelRatio(float f) {
		divicePixelRatio = f;
		height = (int) (maxheight * f);
		width = (int) (maxwidth * f);
	}

	private void getDevicePixelRatio(float touchX, float scrollX, float touchY,
			float scrollY, int eventAction) {
		String js = "javascript:(function(){"
				+ "var  obj=window.devicePixelRatio;" + "if(obj!=null)"
				+ " {window.clickInterface.setDevicePixelRatio(" + touchX + ","
				+ scrollX + "," + touchY + "," + scrollY + "," + eventAction
				+ ",obj);}" + "})()";
		loadUrl(js);
	}

	private void getDevicePixelRatio() {
		String js = "javascript:(function(){"
				+ "var  obj=window.devicePixelRatio;" + "if(obj!=null)"
				+ " {window.clickInterface.setDevicePixelRatio(obj);}" + "})()";
		loadUrl(js);
	}

	public boolean onTouchEvent(MotionEvent event, QRWebPage qrWebPage) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			pressureTime = event.getEventTime() - event.getDownTime();
			Log.d("Clicked", "pressureTime:" + pressureTime);

			if (jsParameters.isOpenOnNewWindow() && pressureTime > 1000
					&& dx < 20 && dy < 20) {
				String url = this.getUrl();

				if (url != null) {
					boolean link = true;
					try {
						new URL(url);
					} catch (MalformedURLException e) {
						link = false;
					}
					if (link) {
						getContext().startActivity(
								new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					}
				}
			} else if (pressureTime < 1000 && dx < 10 && dy < 10) {
				getDevicePixelRatio(event.getX(), getScrollX(), event.getY(),
						getScrollY(), event.getAction());
				performClick();
				super.onTouchEvent(event);
			}

		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			tx = -1;
			ty = -1;
			dx = 0;
			dy = 0;
			getDevicePixelRatio(event.getX(), getScrollX(), event.getY(),
					getScrollY(), event.getAction());

		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (tx != -1) {
				float ddx = tx - event.getX();
				float ddy = ty - event.getY();
				dx += Math.abs(ddx);
				dy += Math.abs(ddy);

				PointF normalvector = new PointF();
				normalvector.x = (float) (qrWebPage.getFour().x - qrWebPage
						.getOne().x);
				normalvector.y = (float) (qrWebPage.getFour().y - qrWebPage
						.getOne().y);
				float normalnorm = (float) Math.sqrt(Math
						.pow(normalvector.x, 2) + Math.pow(normalvector.y, 2));
				float ddnorm = (float) Math.sqrt(Math.pow(ddx, 2)
						+ Math.pow(ddy, 2));
				float theta = (float) Math
						.acos(((float) (normalvector.x * ddx) + (normalvector.y * ddy))
								/ (normalnorm * ddnorm));

				PointF perpendicularvector = new PointF();
				perpendicularvector.x = (float) (qrWebPage.getTwo().x - qrWebPage
						.getOne().x);
				perpendicularvector.y = (float) (qrWebPage.getTwo().y - qrWebPage
						.getOne().y);
				float perpendiculnorm = (float) Math.sqrt(Math.pow(
						perpendicularvector.x, 2)
						+ Math.pow(perpendicularvector.y, 2));
				float beta = (float) Math
						.acos(((float) (perpendicularvector.x * ddx) + (perpendicularvector.y * ddy))
								/ (perpendiculnorm * ddnorm));

				ddy = (float) -(ddnorm * (float) Math.cos(beta));
				ddx = (float) (ddnorm * (float) Math.cos(theta));
				if (qrWebPage.getHorizontalScroll() + ddx < qrWebPage
						.getMaxHorizontalScroll()
						&& qrWebPage.getHorizontalScroll() + ddx > 0) {
					qrWebPage.setHorizontalScroll((int) (qrWebPage
							.getHorizontalScroll() + ddx));
				}
				if (qrWebPage.getVerticalScroll() + ddy < qrWebPage
						.getMaxVerticalScroll()
						&& qrWebPage.getVerticalScroll() + ddy > 0) {
					qrWebPage.setVerticalScroll((int) (qrWebPage
							.getVerticalScroll() + ddy));
				}
				getDevicePixelRatio(event.getX(), getScrollX(), event.getY(),
						getScrollY(), event.getAction());

			}
			tx = event.getX();
			ty = event.getY();

		}
		return false;
	}

	public abstract void onElementTouched(String tagname, String attributes,
			String parents);

	public boolean isChangeListenerListEmpty() {
		return listener.isEmpty();
	}

	public LWebViewJsParameters getJsParameters() {
		return jsParameters;
	}

	public void setJsParameters(LWebViewJsParameters jsParameters) {
		this.jsParameters = jsParameters;
	}

	public void select(String selectedId) {
		String js = "javascript:(function(){"
				+ "var  obj = window.devicePixelRatio;"
				+ "var  selected = document.getElementById('"+selectedId+ "');"
				+ "if(obj!=null && selected!=null){"
				+ " 	var touchX = (selected.getBoundingClientRect().right- selected.getBoundingClientRect().left)/2;"
				+ " 	var touchY = (selected.getBoundingClientRect().bottom- selected.getBoundingClientRect().top)/2;"
				+ "		window.clickInterface.setDevicePixelRatio( touchX ,"+ getScrollX() +", touchY ," + getScrollY() + "," + MotionEvent.ACTION_UP+ ",obj);"
				+ "}"
				+ "})()";
		loadUrl(js);		
	}

}
