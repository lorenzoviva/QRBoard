package com.ogc.browsers.attrubutes;

import javax.xml.datatype.Duration;

import org.jsoup.nodes.Element;

import com.example.qrboard.QRWebPageEditorView;
import com.example.qrboard.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Fontsize extends QRAttribute{

	public Fontsize(Element element){
		super(element);
		setName("Font size");
		setInfo("To edit font size just resize the selection!");
		setProperty(".style.fontSize");
		if(element.hasText() && element.hasAttr("style") && element.attr("style").matches(".*font-size.*:.+px.*")){
			setAttribute(element.attr("style").split("font-size")[1].split(";")[0].substring(1));
		}else{
			setAttribute("");
			
		}
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
