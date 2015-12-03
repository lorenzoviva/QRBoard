package com.ogc.browsers.attrubutes;

import org.jsoup.nodes.Element;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

import com.example.qrboard.QRWebPageEditorView;
import com.example.qrboard.R;
import com.ogc.dialog.EwpColorPicker;

public class Bordercolor  extends QRAttribute{
	private QRWebPageEditorView fa;
	private String id;
	public Bordercolor(Element element) {
		super(element);
		setName("Border color");
		setProperty(".style.borderColor");
		if(hasStyleAttribute("border-color")){
			setAttribute(getStyleAttribute("border-color"));
		}else{
			setAttribute("");
			
		}
	}

	@Override
	public void onTouch(QRWebPageEditorView fa, String id) {
		this.fa = fa;
		this.id = id;
		final EwpColorPicker cp = new EwpColorPicker(fa.getContext(),(String)getAttribute(),this);
		Button okColor = (Button)cp.findViewById(R.id.okColorButton);
		okColor.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	EditText hex =	(EditText)cp.findViewById(R.id.codHex);
		    	String color = hex.getText().toString();
		    	String style = editStyleAttribute("border-color", "#"+color.toUpperCase());
		    	String html = getElement().attr("style",style).html();
		    	onEdit(html);
		        cp.dismiss();
		    }
		});
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
