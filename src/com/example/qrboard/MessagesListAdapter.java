package com.example.qrboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qrboard.R;
import com.ogc.model.QRChat;
import com.ogc.model.QRMessage;
import com.ogc.model.QRUser;

public class MessagesListAdapter extends BaseAdapter {

	private Context context;
	private QRChat qrchat;
	private QRUser user;

	public MessagesListAdapter(Context context, QRChat navDrawerItems, QRUser user) {
		this.context = context;
		this.qrchat = navDrawerItems;
		this.user = user;
	}

	@Override
	public int getCount() {
		return qrchat.size();
	}

	@Override
	public Object getItem(int position) {
		return qrchat.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/**
		 * The following list not implemented reusable list items as list items
		 * are showing incorrect data Add the solution if you have one
		 * */

		QRMessage m = qrchat.get(position);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		// Identifying the message owner

		if (user != null && m.getSender() != null && m.getSender().equals(user)) {
			// message belongs to you, so load the right aligned layout
			convertView = mInflater.inflate(R.layout.list_item_message_right, null);
		} else {
			// message belongs to other person, load the left aligned layout
			convertView = mInflater.inflate(R.layout.list_item_message_left, null);
		}

		TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
		TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

		txtMsg.setText(m.getText());
		if (m.getSender() != null) {
			lblFrom.setText(m.getSender().getFirstName() + " " + m.getSender().getLastName());
		} else {
			lblFrom.setText("anonymous");
		}
		if(m.getDate()!=null){
			DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
			String dateFormatAsString = iso8601Format.format(m.getDate());
			lblFrom.append(" " + dateFormatAsString);
		}

		return convertView;
	}
}
