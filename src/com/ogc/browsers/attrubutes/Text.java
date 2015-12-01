package com.ogc.browsers.attrubutes;

import org.jsoup.nodes.Element;

import com.example.qrboard.QRWebPageEditorView;
import com.example.qrboard.R;
import com.ogc.dialog.EwpTextDialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

public class Text extends QRAttribute{
	private QRWebPageEditorView fa;
	private String id;
	public Text(Element element){
		super(element);
		setName("Text");
		setProperty("");
		if(element.hasText()){
			setAttribute(element.text());
		}else{
			setAttribute("");
		}
		
	}
	@Override
	public void onTouch(QRWebPageEditorView fa, String id){
		this.fa = fa;
		this.id = id;
		new EwpTextDialog(fa.getContext(), getElement().text(), this);
	}
	@Override
	public void onEdit(String html) {
		
		fa.edit(html, id);
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
		name.setText(getName());
		String text = ((String)(getAttribute()));
		if(text.length()>9){
			text = text.substring(0,9);
			text += "...";
		}
		TextView value = new TextView(context);
		value.setText(text);
		name.setBackgroundResource(R.drawable.attribute_name_background);
		name.setTextColor(Color.WHITE);
		value.setTextColor(Color.WHITE);
		LayoutParams nvp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		value.setLayoutParams(nvp);
		name.setLayoutParams(nvp);
		ll.addView(name,nvp);
		ll.addView(value,nvp);
		
		return ll;
	}
	

}
