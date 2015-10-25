package com.ogc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

import java.text.DateFormat;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrboard.ARLayerView;
import com.example.qrboard.LWebView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.GsonHelper;

public class QRChat extends QRSquare {
	
	private List<QRMessage> messages;
	

	private transient QRChatWebPage page;
	
	
	@Override
	public String getCreationChoiseHtml() {
		return "<td height='25%' width='25%' id='"+LWebView.applicationid + ".create." + this.getClass().getSimpleName()+"'  bgcolor='#FF0000' style=\"word-wrap:break-word;\"><div align='center'>"+this.getClass().getSimpleName()+"</div><br><div align='center'><i  class='fa fa-comment'></div></i></td>";
	}

	public QRChat() {
		super();
		page = new QRChatWebPage(this);
		page.setShape(this);
	
	}
	public List<QRMessage> getMessages() {
		return messages;
	}
	@Override
	public void onClose(){
		if(page!=null){
			page.onClose();
		}
	}

	public void setMessages(List<QRMessage> messages) {
		this.messages = messages;
	}
	
	public QRChat(JSONObject jobj) throws JSONException {
		this.setText(jobj.getString("text"));
		this.setCreationDate(GsonHelper.customGson.fromJson(jobj.getString("creationDate"), Date.class));
		this.setVisit(jobj.getLong("visit"));
		this.setAcl(new ACL(jobj.getJSONObject("acl")));
		JSONArray jsonarray = jobj.getJSONArray("messages");
		messages=new ArrayList<QRMessage>();
		for (int i=0;i<jsonarray.length();i++) {
			messages.add(new QRMessage(jsonarray.getJSONObject(i)));
		}
	}
	
	public QRChat(String text, List<QRMessage> messages) {
		super(text);
		setMessages(messages);
	}

	public JSONObject toJSONObject(){
		Map<String, Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("text", this.getText());
		String jsonDate = GsonHelper.customGson.toJson(this.getCreationDate(),Date.class);
		jsonDate=jsonDate.substring(1,jsonDate.length()-1);
		jsonMap.put("creationDate", jsonDate);
		jsonMap.put("visit", this.getVisit());
		jsonMap.put("acl", this.getAcl().toJSON());
		JSONArray array = new JSONArray();
		if (messages!=null && messages.isEmpty()) {
			for(int i = 0 ; i < messages.size();i++){
				array.put(messages.get(i).toJSONObject());
			}
		}
		jsonMap.put("messages", array);
		return new JSONObject(jsonMap);
	}

	public int size() {
		if (messages!=null && !messages.isEmpty())
			return messages.size();
		else
			return 0;
	}

	public QRMessage get(int position) {
		return messages.get(position);
	}

	public void add(QRMessage m) {
		if (messages == null || messages.isEmpty())
			messages = new ArrayList<QRMessage>();
		messages.add(m);
	}

	@Override
	public void draw(Canvas canvas, ARLayerView arview) {
		page.setShape(this);
		page.draw(canvas, arview);
	}
	@Override
	public void onTouch(MotionEvent event) {
		page.onTouch(event);
	}
	
}
