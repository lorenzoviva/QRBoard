package com.ogc.model;

import com.example.qrboard.LWebView;

public class QRChat extends QRSquare {
	@Override
	public String getCreationChoiseHtml() {
		return "<td height='25%' width='25%' id='"+LWebView.applicationid + "create" + this.getClass().getSimpleName()+"'  bgcolor='#FF0000' style=\"word-wrap:break-word;\"><div align='center'>"+this.getClass().getSimpleName()+"</div><br><div align='center'><i  class='fa fa-comment'></div></i></td>";
	}
	
	private String chat;
	
	public String getChat() {
		return chat;
	}
	
	public void setChat(String chat) {
		this.chat = chat;
	}
}
