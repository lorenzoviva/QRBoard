package com.ogc.browsers;

import android.util.Log;

import com.example.qrboard.ARLayerView;
import com.example.qrboard.LWebViewJsParameters;
import com.ogc.model.QRUsersWebPage;

public class UsersWebView extends LWebView{
	

	public UsersWebView(ARLayerView arview, QRUsersWebPage qrsquare, int width, int height) {
		super(arview, qrsquare, width, height,new LWebViewJsParameters(false,false,false,true,false,false));
	}

	@Override
	public void onElementTouched(String tagname, String attributes,String parents) {
		if (attributes.contains("id<")) {
			String[] idattributes = attributes.split("id<");
			if (id != null) {
				lastid = id;
			}
			id = idattributes[1].substring(0, idattributes[1].indexOf(">"));
			Log.d("CLICKED ON ID:", id);
			if (id.startsWith(applicationid)) {
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
