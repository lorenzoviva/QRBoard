package com.ogc.browsers.attrubutes;

import org.jsoup.nodes.Element;
import org.xml.sax.DTDHandler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

import com.example.qrboard.QRWebPageEditorView;
import com.example.qrboard.R;

public class Bounds extends QRAttribute{
	private QRWebPageEditorView fa;
	private String id;
	public Bounds(Element element) {
		super(element);
		setName("Bounds");
		setProperty(".size");
		String attribute = "";
		if(hasStyle()){
			if(hasStyleAttribute("left")){
				attribute += "left :" + getStyleAttribute("left")+"; ";
			}
			if(hasStyleAttribute("top")){
				attribute += "top :" + getStyleAttribute("top")+"; ";
			}
			if(hasStyleAttribute("width")){
				attribute += "width :" + getStyleAttribute("width")+"; ";
			}
			if(hasStyleAttribute("height")){
				attribute += "height :" + getStyleAttribute("height")+"; ";
			}
			
		}
		setInfo("To edit elements bounds just resize and move the selection");
		setAttribute(attribute);
	}

	@Override
	public void onTouch(QRWebPageEditorView fa, String id){
		Toast.makeText(fa.getContext(), (CharSequence) getInfo(), Toast.LENGTH_SHORT).show();
	}


	@Override
	public View getView(Context context) {
		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundResource(R.drawable.attribute_value_background);
		ll.setPadding(2, 2, 2, 2);		
		
		LayoutParams llp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 5, 0, 5);
		ll.setLayoutParams(llp);
		ll.setOrientation(LinearLayout.VERTICAL);
		TextView name = new TextView(context);
		TextView info = new TextView(context);
		TextView value = new TextView(context);
		name.setText(getName());
		info.setText((String)getInfo());
		Drawable drawable = context.getResources().getDrawable(R.drawable.ewpmri);
		float density = context.getResources().getDisplayMetrics().density;
		drawable.setBounds(0, 0, (int)(80*density),(int)(80*density));
		info.setCompoundDrawables(null, null, null, drawable);
		value.setText(((String)(getAttribute())));
		name.setBackgroundResource(R.drawable.attribute_name_background);
		info.setBackgroundResource(R.drawable.attribute_info_background);
		
		LayoutParams nvp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		name.setLayoutParams(nvp);
		value.setLayoutParams(nvp);
		info.setLayoutParams(nvp);
		name.setTextColor(Color.WHITE);
		value.setTextColor(Color.WHITE);
		info.setTextColor(Color.rgb(54, 144, 240));
		ll.addView(name,nvp);
		ll.addView(value,nvp);
		ll.addView(info,nvp);
		return ll;
	}

	@Override
	public void onEdit(String html) {
		
	}

}
