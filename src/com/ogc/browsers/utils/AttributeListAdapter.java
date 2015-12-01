package com.ogc.browsers.utils;

import java.util.List;

import com.example.qrboard.QRWebPageEditorView;
import com.example.qrboard.R;
import com.ogc.browsers.attrubutes.QRAttribute;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AttributeListAdapter extends BaseAdapter {
	
	private Context context;
	private List<QRAttribute> attributes;
	public AttributeListAdapter(Context context, List<QRAttribute> elatt){
		this.context = context;
		this.attributes = elatt;
		
	}
	@Override
	public int getCount() {
		return attributes.size();
	}

	@Override
	public Object getItem(int arg0) {
		return attributes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		QRAttribute qrAttribute = attributes.get(position);
		LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.list_item_attribute, null);
		convertView = ll;
//		TextView lblProperty = (TextView) convertView.findViewById(R.id.li_property);
////		TextView lblAttribute = (TextView) convertView.findViewById(R.id.li_attribute);
////		TextView lblInfo = (TextView) convertView.findViewById(R.id.li_info);
//		lblProperty.setText(qrAttribute.getName());
//		lblAttribute.setText(qrAttribute.getAttribute());
//		lblInfo.setText(qrAttribute.getInfo());addView(attributes.get(position).getView())
		ll.addView(attributes.get(position).getView(getContext()));
		return convertView;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<QRAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<QRAttribute> attributes) {
		this.attributes = attributes;
	}
	public void onTouch(int position, QRWebPageEditorView fa, String id) {
		attributes.get(position).onTouch(fa,id);		
	}

}
