package com.ogc.dbutility;

public class DBConst {
	public static final String ip = "192.168.1.8";
	public static final String ip_port = ip + ":8080";
	public static final String app_name = "QRWebService";
	public static final String url = "http://" + ip_port + "/"+app_name+"/";
	public  static final String url_action = url+"action";
	public static final String url_webSocket = "ws://" + ip_port + "/"+app_name+"/chat";
}
