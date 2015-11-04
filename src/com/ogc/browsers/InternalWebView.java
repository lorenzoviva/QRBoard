package com.ogc.browsers;

import android.util.Log;

import com.example.qrboard.SquareHolderView;
import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRUserMenager;
import com.ogc.model.RoleType;

public class InternalWebView extends LWebView{

		private RoleType role;
		protected SquareHolderView arview;
		
		public InternalWebView(SquareHolderView arview, QRInternalWebPage qrsquare, int width, int height) {
			super(arview, qrsquare, width, height);
			this.arview = arview;
		}

		@Override
		public void clickWebPage(float touchX, float scrollX, float touchY, float scrollY, float f) {
			String js = "javascript:(function() { " 
					//	scroll the window
					+"	window.scrollTo(" + scrollX / f + "," + scrollY / f + "); "
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
	   				//	find a parent object with an id
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
					String[] splitid = id.split("\\.");
			
					if(id.startsWith(applicationid +".") && splitid.length>2){//do action
						arview.setActionContext(splitid[1]);
						arview.performAction((splitid[2]));											
					}else if(id.startsWith(applicationid+":")){//select from a list
						String js = "";
						if (lastid == null) {
							js = "javascript:(function(){" + "var  obj=document.getElementById(\'" + id + "\'); " + "if(obj!=null)" + " {obj.style.backgroundColor='#FF0000';}" + "})()";
						} else {
							js = "javascript:(function(){" + "var  obj=document.getElementById(\'" + id + "\'); var lastobj=document.getElementById(\'" + lastid + "\'); " + "if(obj!=null && lastobj!=null)" + " {obj.style.backgroundColor='#FF0000';lastobj.style.backgroundColor='#FFFFFF';}" + "})()";
						}

						loadUrl(js);
						arview.invalidate();
					}
					
				}
			}
			
		}
}