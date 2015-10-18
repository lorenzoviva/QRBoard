package com.google.gson;

import java.lang.reflect.Type;

import com.ogc.model.QRFreeDraw;

import android.util.Base64;
import android.util.Log;

public class GsonHelper {
	public static final Gson customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter()).registerTypeHierarchyAdapter(QRFreeDraw.class, new QRFreeDrawTypeAdapter()).create();

	// Using Android's base64 libraries. This can be replaced with any base64
	// library.
	private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
		  public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
	        }

	        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
	            return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
	        }
	}
	private static class QRFreeDrawTypeAdapter implements JsonSerializer<QRFreeDraw>, JsonDeserializer<QRFreeDraw> {
		

        public JsonElement serialize(QRFreeDraw src, Type typeOfSrc, JsonSerializationContext context) {
        	JsonElement json = (new Gson()).toJsonTree(src);
        	JsonObject asJsonObject = json.getAsJsonObject();
			asJsonObject.remove("img");
        	JsonElement img = customGson.toJsonTree(src.getImg());
        	asJsonObject.add("img", img);
            return asJsonObject;
        }

		@Override
		public QRFreeDraw deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
			JsonElement img = json.getAsJsonObject().get("img");
			Log.d("ELEMENTS", "json:" + json.toString() + "img:" + img.toString());
			QRFreeDraw qrfreedraw = (new Gson()).fromJson(json.toString(), QRFreeDraw.class);
			qrfreedraw.setImg(customGson.fromJson(img, byte[].class));
			return qrfreedraw;
		}
}
}