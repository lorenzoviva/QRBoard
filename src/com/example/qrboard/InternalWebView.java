package com.example.qrboard;

import android.content.Context;
import android.util.Log;

import com.ogc.action.Action;
import com.ogc.model.QRInternalWebPage;
import com.ogc.model.RoleType;

public class InternalWebView extends LWebView {

	private RoleType role;
	private ARGUI argui;
	private Context context;

	public InternalWebView(ARLayerView arview, QRInternalWebPage qrsquare, int width, int height) {
		super(arview, qrsquare, width, height);
		argui = arview.getArgui();
		context = arview.getContext();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void clickWebPage(float touchX, float scrollX, float touchY, float scrollY, float f) {
		String js = "javascript:(function() { "
		// scroll the window
				+ "	window.scrollTo(" + scrollX / f + "," + scrollY / f + "); "
		// get the clicked object
				+ "	var  obj=document.elementFromPoint(" + (touchX / f) + "," + (touchY / f) + ");" + "	var parents = '';"
				// Throws a click event
				+ "	if (obj.fireEvent) {" + "		obj.fireEvent('onclick');" + "	} else {" + "		var evObj = document.createEvent('Events');" + "		evObj.initEvent('click', true, false);" + "		obj.dispatchEvent(evObj);" + "	}"
				// find a parent object with an id
				+ "	while(obj.parentNode!=null && !obj.hasAttribute('id')){"
				// add the courrent object tagName to 'parents' (eg: DIV)
				+ "		parents += obj.tagName + ' ';"
				// add all attributes of parent to 'parents' (eg: color<black>)
				+ "		if(!(obj instanceof HTMLDocument) && obj.hasAttributes()){" + "			for (i = 0; i < obj.attributes.length; i++) {" + "				parents += obj.attributes[i].name + '<' + obj.getAttribute(obj.attributes[i].name) + '>';" + "			}" + "		}" + "		parents += ' ';" + "		obj = obj.parentNode;" + "	} " + "	if(obj!=null) {" + "		var att = '';"
				// add all attributes to the attribute list
				+ "		if(!(obj instanceof HTMLDocument) && obj.hasAttributes()){" + "			for (i = 0; i < obj.attributes.length; i++) {" + "				att += obj.attributes[i].name + '<' + obj.getAttribute(obj.attributes[i].name) + '>';" + "			}" + "		}" + "		window.clickInterface.onclick(obj.tagName,att,parents);" + "	}" + "})()";
		loadUrl(js);

	}

	@Override
	public void onElementTouched(String tagname, String attributes, String parents) {
		if (attributes.contains("id<")) {
			String[] idattributes = attributes.split("id<");
			if (id != null) {
				lastid = id;
			}
			id = idattributes[1].substring(0, idattributes[1].indexOf(">"));
			Log.d("CLICKED ON ID:", id);
			if (id.startsWith(applicationid)) {
				Log.d("ACTION","id:" + id + " context:" +argui.getActionContext() + "" +applicationid + "." );
				if (id.startsWith(applicationid + ".") && id.split("\\.").length>2) {// action to perform
					Log.d("ACTION","id:" + id + " context:" +argui.getActionContext());
					String action = id.split("\\.")[2].toLowerCase();
					argui.performAction(action, context);
					
				} else if (id.startsWith(applicationid + ":")) {//list item to select
					String js = "";
					if (lastid == null) {
						js = "javascript:(function(){" + "var  obj=document.getElementById(\'" + id + "\'); " + "if(obj!=null)" + " {obj.style.backgroundColor='#FF0000';}" + "})()";
					} else {
						js = "javascript:(function(){" + "var  obj=document.getElementById(\'" + id + "\'); var lastobj=document.getElementById(\'" + lastid + "\'); " + "if(obj!=null && lastobj!=null)" + " {obj.style.backgroundColor='#F0F0F0';lastobj.style.backgroundColor='#FFFFFF';}" + "})()";
					}

					loadUrl(js);
					arview.invalidate();
				}
			}

		}

	}
}
