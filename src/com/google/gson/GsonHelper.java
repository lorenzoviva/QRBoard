package com.google.gson;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.ogc.model.QRFreeDraw;

import android.util.Base64;
import android.util.Log;

public class GsonHelper {
	public static final Gson customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter()).registerTypeHierarchyAdapter(QRFreeDraw.class, new QRFreeDrawTypeAdapter()).registerTypeHierarchyAdapter(Date.class, new DateTypeAdapter()).create();

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

	private static class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
			// Gson gson = new
			// GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
			// JsonElement json = gson.toJsonTree(src);

			DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
			iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
			String dateFormatAsString = iso8601Format.format(src);
			return new JsonPrimitive(dateFormatAsString);
		}

		@Override
		public Date deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
			DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
			iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = null;
			try {
				date = iso8601Format.parse(json.toString());
			} catch (ParseException e) {
			}

			// Gson gson = new
			// GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
			// Date date = gson.fromJson(json, Date.class);
			return date;
		}
	}
}