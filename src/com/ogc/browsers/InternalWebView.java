package com.ogc.browsers;

import android.util.Log;

import com.example.qrboard.ARLayerView;
import com.ogc.model.QRInternalWebPage;
import com.ogc.model.RoleType;

public class InternalWebView extends LWebView{

		private RoleType role;
		protected ARLayerView arview;
		
		public InternalWebView(ARLayerView arview, QRInternalWebPage qrsquare, int width, int height) {
			super(arview, qrsquare, width, height,new LWebViewJsParameters(false,false,false,true,false,false,false));
			this.arview = arview;
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
