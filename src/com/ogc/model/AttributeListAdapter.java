package com.ogc.model;

import java.util.List;
import com.example.qrboard.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AttributeListAdapter extends BaseAdapter {
	
	private Context context;
	private List<QRAttribute> attributes;
	
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
		convertView = mInflater.inflate(R.layout.list_item_attribute, null);
		TextView lblProperty = (TextView) convertView.findViewById(R.id.li_property);
		TextView lblAttribute = (TextView) convertView.findViewById(R.id.li_attribute);
		TextView lblInfo = (TextView) convertView.findViewById(R.id.li_info);
		lblProperty.setText(qrAttribute.getProperty());
		lblAttribute.setText(qrAttribute.getAttribute());
		lblInfo.setText(qrAttribute.getInfo());
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

}
