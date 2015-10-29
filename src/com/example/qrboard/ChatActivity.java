package com.example.qrboard;

import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codebutler.android_websockets.WebSocketClient;
import com.google.gson.Gson;
import com.ogc.dbutility.DBConst;
import com.ogc.model.MessagesListAdapter;
import com.ogc.model.QRChat;
import com.ogc.model.QRMessage;
import com.ogc.model.QRUser;

public class ChatActivity extends Activity {

	private MessagesListAdapter adapter;
	private QRChat qrchat = null;
	private QRUser qruser = null;
	private ListView listViewMessages;
	private WebSocketClient client = null;
	private static final String TAG_SELF = "self", TAG_NEW = "new", TAG_MESSAGE = "message", TAG_EXIT = "exit";
	private Button sendButton;
	private EditText inputMessage;
	String sessionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		listViewMessages = (ListView) findViewById(R.id.list_view_messages);
		sendButton = (Button) findViewById(R.id.btnSend);
		inputMessage = (EditText) findViewById(R.id.inputMsg);
		if (client == null || !client.isConnected()) {
			try {
				qrchat = new QRChat(new JSONObject(getIntent().getStringExtra("qrchat")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (qrchat != null) {
				if (getIntent().hasExtra("qruser")) {
					String stringExtra = getIntent().getStringExtra("qruser");
					qruser = (new Gson()).fromJson(stringExtra, QRUser.class);
				}
				adapter = new MessagesListAdapter(this, qrchat, qruser);
				listViewMessages.setAdapter(adapter);

				String userid = "anonymous";
				if (qruser != null) {

					userid = String.valueOf(qruser.getId());

				}
				/**
				 * Creating web socket client. This will have callback methods
				 * */
				URI create = URI.create(DBConst.url_webSocket + "?chat=" + qrchat.getText() + "&name=" + userid);
				Log.d("CONNECTING", create.toString());
				client = new WebSocketClient(create, new WebSocketClient.Listener() {
					@Override
					public void onConnect() {

					}

					/**
					 * On receiving the message from web socket server
					 * */
					@Override
					public void onMessage(String message) {
						Log.d("TAG", String.format("Got string message! %s", message));

						parseMessage(message);

					}

					@Override
					public void onMessage(byte[] data) {
						Log.d("TAG", String.format("Got binary message! %s", bytesToHex(data)));

						// Message will be in JSON format
						parseMessage(bytesToHex(data));
					}

					/**
					 * Called when the connection is terminated
					 * */
					@Override
					public void onDisconnect(int code, String reason) {

						String message = String.format(Locale.US, "Disconnected! Code: %d Reason: %s", code, reason);

						showToast(message);

						// clear the session id from shared preferences
						// utils.storeSessionId(null);
					}

					@Override
					public void onError(Exception error) {
						Log.e("TAG", "Error! : " + error);

						showToast("Error! : " + error);
					}

				}, null);
				sendButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onSend();
					}
				});
				client.connect();
			} else {
				sendButton.setEnabled(false);
				Log.d("messaggio qrchat", "errore");
			}
		}

	}

	public void onSend() {
		String string = inputMessage.getText().toString();
		if (!string.trim().equals("") && !sessionId.equals("")) {
			Map<String, Object> paramap = new HashMap<String, Object>();
			paramap.put("flag", "message");
			paramap.put("sessionId", sessionId);
			paramap.put("message", string);
			sendMessageToServer(new JSONObject(paramap).toString());
			inputMessage.setText("");
		} else {
			showToast("please fill the input box before!");
		}
	}

	/**
	 * Method to send message to web socket server
	 * */
	private void sendMessageToServer(String message) {
		if (client != null && client.isConnected()) {
			client.send(message);
		}
	}

	/**
	 * Parsing the JSON message received from server The intent of message will
	 * be identified by JSON node 'flag'. flag = self, message belongs to the
	 * person. flag = new, a new person joined the conversation. flag = message,
	 * a new message received from server. flag = exit, somebody left the
	 * conversation.
	 * */
	private void parseMessage(final String msg) {

		try {
			JSONObject jObj = new JSONObject(msg);

			// JSON node 'flag'
			String flag = jObj.getString("flag");

			// if flag is 'self', this JSON contains session id
			if (flag.equalsIgnoreCase(TAG_SELF)) {

				sessionId = jObj.getString("sessionId");
				String qrchatstring = jObj.getString("chat");
				JSONObject chatObject = new JSONObject(qrchatstring);
				JSONArray messagesJsonArray = chatObject.getJSONArray("messages");
				for (int i = 0; i < messagesJsonArray.length(); i++) {
					QRMessage m = new QRMessage(new JSONObject(messagesJsonArray.get(i).toString()));
					appendMessage(m, false);
				}
				// Save the session id in shared preferences
				// utils.storeSessionId(sessionId);

				// Log.e(TAG, "Your session id: " + utils.getSessionId());

			} else if (flag.equalsIgnoreCase(TAG_NEW)) {
				// If the flag is 'new', new person joined the room
				String name = jObj.getString("name");
				String message = jObj.getString("message");

				// number of people online
				String onlineCount = jObj.getString("onlineCount");

				showToast(name + message + ". Currently " + onlineCount + " people online!");

			} else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
				// if the flag is 'message', new message received
				String fromName = "";
				JSONObject jmessage = new JSONObject(jObj.getString("message"));
				String sessionId = jObj.getString("sessionId");
				boolean isSelf = true;

				// Checking if the message was sent by you
				if (!sessionId.equals(this.sessionId)) {
					fromName = jObj.getString("name");
					isSelf = false;
					if (!fromName.equals("anonymous")) {
						String firstName = fromName.split("&")[0];
						String lastName = fromName.substring(firstName.length() + 1);
						QRUser sender = new QRUser(firstName, lastName);
						QRMessage m = new QRMessage(jmessage.getString("text"), sender);
						appendMessage(m, true);
					} else {
						// not my message
						QRMessage m = new QRMessage(jmessage.getString("text"), null);
						appendMessage(m, true);

					}

				} else {

					QRMessage m = new QRMessage(jmessage.getString("text"), qruser);

					// Appending the message to chat list
					appendMessage(m, true);
				}

			} else if (flag.equalsIgnoreCase(TAG_EXIT)) {
				// If the flag is 'exit', somebody left the conversation
				String name = jObj.getString("name");
				String message = jObj.getString("message");

				showToast(name + message);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (client != null & client.isConnected()) {
			client.disconnect();
		}
	}

	/**
	 * Appending message to list view
	 * */
	private void appendMessage(final QRMessage m, final boolean playbeep) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				qrchat.add(m);

				adapter.notifyDataSetChanged();

				// Playing device's notification
				if (playbeep) {
					playBeep();
				}
			}
		});
	}

	private void showToast(final String message) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
		});

	}

	/**
	 * Plays device's default notification sound
	 * */
	public void playBeep() {

		try {
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
			r.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}
