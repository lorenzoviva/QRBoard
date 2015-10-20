package com.ogc.model;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.qrboard.LWebView;
import com.google.gson.Gson;

public class QRChat extends QRSquare {
	
	private List<QRMessage> messages;
	
	@Override
	public String getCreationChoiseHtml() {
		return "<td height='25%' width='25%' id='"+LWebView.applicationid + "create" + this.getClass().getSimpleName()+"'  bgcolor='#FF0000' style=\"word-wrap:break-word;\"><div align='center'>"+this.getClass().getSimpleName()+"</div><br><div align='center'><i  class='fa fa-comment'></div></i></td>";
	}

	public List<QRMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<QRMessage> messages) {
		this.messages = messages;
	}
	
	public QRChat(JSONObject jobj) throws JSONException{
		
		this.setText(jobj.getString("text"));
		ACL act = new ACL(jobj.getJSONObject("acl"));
	}
	public JSONObject toJSONObject(){
		Map<String, Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("text", this.getText());
		jsonMap.put("creationDate", (new Gson()).toJson(this.getCreationDate(),Date.class));
		jsonMap.put("visit", this.getVisit());
		jsonMap.put("acl", this.getAcl().toJSON());
		JSONArray array = new JSONArray();
		for(int i = 0 ; i < messages.size();i++){
			array.put(messages.get(i).toJSONObject());
		}
		jsonMap.put("messages", array);
		return new JSONObject(jsonMap);
	}
}
