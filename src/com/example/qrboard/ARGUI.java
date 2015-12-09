package com.example.qrboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.ogc.action.Action;
import com.ogc.dialog.EditSquareUserDialog;
import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;
import com.ogc.model.special.QRSquareUserRepresentation;

public class ARGUI {

	private Result result;
	private QRSquare qrsquare;
	private QRSquare lastqrsquare = null;
	private QRSquare usersquare;
	private String useractions;
	private String lastactions;
	private List<String> actions = new ArrayList<String>();
	private int listindex = 0; // in actions witch needs more pages of data to
								// load
	private boolean refreshExplorer = false;
	// this index tells us what is the currently last page index.
	private int hover = -1;
	private String clickedaction = "";
	private QRUser user = null;
	private Action action = null;
	private List<Action> allActions = new ArrayList<Action>();
	private String actionContext = "";
	private ProgressDialog ringProgressDialog = null;

	public ARGUI() {

	}

	public void openFreeDrawActivity(JSONObject jsonObject, Context context) {
		Intent intent = new Intent(context, FreeDraw.class);
		intent.putExtra("jsonFreeDraw", jsonObject.toString());
		if (user != null) {
			intent.putExtra("userid", user.getId());
		}
		context.startActivity(intent);
	}

	public void openEditWebPageActivity(Context context, JSONObject square,
			long userid) {
		if (square == null) {
			Intent intent = new Intent(context, EditWebPageActivity.class);
			intent.putExtra("type", qrsquare.getClass().getName());
			Log.d("TYPE:", qrsquare.getClass().getName());
			intent.putExtra("QRSquare", GsonHelper.customGson.toJson(qrsquare)
					.toString());
			if (userid != -1) {
				intent.putExtra("userid", userid);
			}
			context.startActivity(intent);
		} else {
			Intent intent = new Intent(context, EditWebPageActivity.class);
			intent.putExtra("type", square.getClass().toString());
			intent.putExtra("QRSquare", square.toString());
			if (userid != -1) {
				intent.putExtra("userid", userid);
			}
			context.startActivity(intent);
		}

	}

	public void openSquareUserEditorDialog(final Context context,
			final JSONObject jsonresponse, final String lastrequest) {

		final QRSquare fsquare = this.qrsquare;
		final ARGUI fargui = this;
		setActionContext("");
		runOnUIThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					QRSquareUserRepresentation rep = (QRSquareUserRepresentation) fsquare;
					QRUser user = GsonHelper.customGson.fromJson(
							jsonresponse.getString("user"), QRUser.class);
					String type = jsonresponse.getString("qrst");
					Class<? extends QRSquare> squareclass = (Class<? extends QRSquare>) Class
							.forName(type);
					QRSquare square = GsonHelper.customGson.fromJson(
							jsonresponse.getString("square"), squareclass);
					String role = jsonresponse.getString("role");
					if (square != null && user != null
							&& rep.getSquareuser() != null) {
						List<String> roleChoises = new ArrayList<String>();
						String[] rolechoises = jsonresponse
								.getString("choises").split(",");
						for (int i = 0; i < rolechoises.length; i++) {
							roleChoises.add(rolechoises[i].replace(",", ""));
						}
						new EditSquareUserDialog(fargui, context, lastrequest,
								rep.getSquareuser(), square, type, user,
								roleChoises, role);
					} else {
						Toast.makeText(
								context,
								"You don't have the permission to edit this role.",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JsonSyntaxException | JSONException
						| ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, context);

	}

	public void runOnUIThread(Runnable runnable, Context context) {
		Handler mainHandler = new Handler(context.getMainLooper());
		mainHandler.post(runnable);
	}

	public void draw(Canvas canvas, ARLayerView arview) {
		if (qrsquare != null && (!actions.isEmpty() && actions.size() > 0)) {
			drawCircle(canvas, arview);
			qrsquare.draw(canvas, arview);
		}

	}

	public void drawCircle(Canvas canvas, View view) {
		try {
			float cx = (qrsquare.getOne().x + qrsquare.getTwo().x
					+ qrsquare.getThree().x + qrsquare.getFour().x) / 4;
			float cy = (qrsquare.getOne().y + qrsquare.getTwo().y
					+ qrsquare.getThree().y + qrsquare.getFour().y) / 4;
			float radius = (float) (1.618d * Math.max(Math.max(
					Math.sqrt(Math.pow(cx - qrsquare.getOne().x, 2)
							+ Math.pow(cy - qrsquare.getOne().y, 2)),
					Math.sqrt(Math.pow(cx - qrsquare.getTwo().x, 2)
							+ Math.pow(cy - qrsquare.getTwo().y, 2))), Math
					.max(Math.sqrt(Math.pow(cx - qrsquare.getThree().x, 2)
							+ Math.pow(cy - qrsquare.getThree().y, 2)),
							Math.sqrt(Math.pow(cx - qrsquare.getFour().x, 2)
									+ Math.pow(cy - qrsquare.getFour().y, 2)))));

			RectF rect = new RectF(cx - radius, cy - radius, cx + radius, cy
					+ radius);
			RectF rectext = new RectF((float) (cx - radius * 0.9),
					(float) (cy - radius * 0.9), (float) (cx + radius * 0.9),
					(float) (cy + radius * 0.9));
			float angle = (float) (360 / actions.size());
			for (int i = 0; i < actions.size(); i++) {
				Paint semicircle = new Paint();

				int color = getColor(actions.get(i));

				semicircle.setColor(color);
				if (i != hover) {
					semicircle.setAlpha(128);
					canvas.drawArc(rect, angle * i, angle, true, semicircle);
				} else {
					rect.inset((float) (-radius * 0.2), (float) (-radius * 0.2));
					canvas.drawArc(rect, angle * i, angle, true, semicircle);
					rect.inset((float) (radius * 0.2), (float) (radius * 0.2));
				}

				Path path = new Path();
				float offset = angle / 4;
				path.arcTo(rectext, (angle * i) + offset, angle - (offset * 2));
				Paint paint = new Paint();
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.BLACK);
				float textSize = (float) (radius * 0.15);
				paint.setTextSize(textSize);
				if (!actions.isEmpty() && actions.size() > 0) {
					canvas.drawTextOnPath(actions.get(i), path, 0, textSize,
							paint);
				}
				canvas.drawPath(path, paint);

				Action realaction = null;
				try {
					realaction = getRealAction(actions.get(i));
				} catch (InstantiationException | IllegalAccessException
						| ClassNotFoundException | IndexOutOfBoundsException e) {
					/*
					 * non è un vero errore
					 */
					// Log.d("ERROR", "unable to get icon from action :" +
					// actions.get(i));
				}
				if (realaction != null && realaction.getIcon(view) != null) {
					Paint iconpaint = new Paint();
					Bitmap icon = realaction.getIcon(view);
					angle = (float) ((angle * Math.PI) / 180);
					float ty = (float) (cy + (radius / 4) * 3
							* Math.sin((angle * i) + (angle / 5) * 4) - (icon
							.getHeight() / 2));
					float lx = (float) (cx + (radius / 4) * 3
							* Math.cos((angle * i) + (angle / 5) * 4) - (icon
							.getWidth() / 2));
					angle = (float) ((angle * 180) / Math.PI);
					canvas.drawBitmap(icon, lx, ty, iconpaint);
				}
			}
		} catch (NullPointerException e) {
			// -.-
		}

	}

	public void setQRSquare(QRSquare qrsquare, boolean forced) {
		if (action == null || forced) {
			lastqrsquare = this.qrsquare;
			// && !lastqrsquare.equals(qrsquare) && (lastactions == null ||
			// !lastactions.equals(action))
			if (lastqrsquare != null) {
				lastqrsquare.setOne(this.qrsquare.getOne());
				lastqrsquare.setTwo(this.qrsquare.getTwo());
				lastqrsquare.setThree(this.qrsquare.getThree());
				lastqrsquare.setFour(this.qrsquare.getFour());
			}
			if (lastqrsquare != null) {
				String lastactions = "";
				for (int i = 0; i < actions.size(); i++) {
					lastactions += actions.get(i) + ",";
				}
				if (lastactions != null && lastactions.endsWith(",")) {
					lastactions = lastactions.substring(0,
							lastactions.length() - 1);
				}
				if (lastactions != null) {// && this.lastactions != null &&
											// !lastactions.equals(this.lastactions))
											// {
					this.lastactions = lastactions;
				}
			}
			if (qrsquare == null) {
				Log.d("EXCEPTION", "qrsquare is null");
			}
			this.qrsquare = qrsquare;
		} else {
			// if(qrsquare == null){
			// Log.d("EXCEPTION", "not forced, action:" +
			// action.getClass().getSimpleName());
			// }

			action.addQRParameter(qrsquare);
			this.qrsquare = qrsquare;
		}

	}

	public void setShape(Result rawResult) {
		if (lastqrsquare != null && result != null) {
			lastqrsquare.setShape(result.getResultPoints(),
					((android.graphics.Point) (result.getResultMetadata()
							.get(ResultMetadataType.OTHER))).y);
		}
		this.result = rawResult;
		qrsquare.setShape(result.getResultPoints(),
				((android.graphics.Point) (rawResult.getResultMetadata()
						.get(ResultMetadataType.OTHER))).y);

	}

	public void setActions(String action) {
		if (this.action == null) {
			if (lastqrsquare != null) {
				String lastactions = "";
				for (int i = 0; i < actions.size(); i++) {
					lastactions += actions.get(i) + ",";
				}
				if (lastactions != null && lastactions.endsWith(",")) {
					lastactions = lastactions.substring(0,
							lastactions.length() - 1);
				}
				if (lastactions != null) {// && this.lastactions != null &&
											// !lastactions.equals(this.lastactions))
											// {
					this.lastactions = lastactions;
				}
			}
			this.actions = new ArrayList<String>();
			String[] actions = action.split(",");
			for (int i = 0; i < actions.length; i++) {
				Action realAction;
				try {
					realAction = getRealAction(actions[i]);
					realAction.prepare(this);
				} catch (InstantiationException | IllegalAccessException
						| ClassNotFoundException e) {
					/*
					 * non è un vero errore
					 */
					Log.d("ERROR", "unable to prepare action :" + actions[i]);
				}
				this.actions.add(actions[i]);
			}
		}
	}

	public void goToLastQRSquare() {
		if (lastqrsquare != null && lastactions != null) {
			setQRSquare(lastqrsquare, false);
		}
	}

	public void onTouch(MotionEvent event, Context context) {
		if (qrsquare != null) {
			if (touchOnQRSquare(event)) {
				qrsquare.onTouch(event);
			} else if (!actions.isEmpty()) {

				float cx = (qrsquare.getOne().x + qrsquare.getTwo().x
						+ qrsquare.getThree().x + qrsquare.getFour().x) / 4;
				float cy = (qrsquare.getOne().y + qrsquare.getTwo().y
						+ qrsquare.getThree().y + qrsquare.getFour().y) / 4;

				float tx = event.getX();
				float ty = event.getY();
				float ax = tx - cx;
				float ay = ty - cy;

				double theta_radians = 360 - (((Math.atan2(ay, -ax) / Math.PI) * 180) + 180);
				float angle = (float) (360 / actions.size());
				for (int i = 0; i < actions.size(); i++) {
					if (theta_radians < angle * (i + 1)
							&& theta_radians > angle * i) {

						if (event.getAction() == MotionEvent.ACTION_DOWN
								|| event.getAction() == MotionEvent.ACTION_MOVE) {
							hover = i;
							clickedaction = actions.get(i);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							if (hover != -1) {
								hover = -1;
								performAction(clickedaction, context);
							}
						}
					}
				}
			}
		}
	}

	public boolean touchOnQRSquare(MotionEvent event) {
		if (qrsquare != null) {
			Quadrilateral polygon = new Quadrilateral();
			polygon.addVertex(new Point(qrsquare.getTwo().x,
					qrsquare.getTwo().y));
			polygon.addVertex(new Point(qrsquare.getThree().x, qrsquare
					.getThree().y));
			polygon.addVertex(new Point(qrsquare.getFour().x, qrsquare
					.getFour().y));
			polygon.addVertex(new Point(qrsquare.getOne().x,
					qrsquare.getOne().y));
			return Utility.PointInPolygon(
					new Point(event.getX(), event.getY()), polygon);
		} else {
			return false;
		}
	}

	public void performAction(String action, Context context) {
		try {
			this.action = getRealAction(action);
			Log.d("ACTION", "performing:" + actionContext + action);
			
			this.action.perform(this, context);

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			/*
			 * non è un vero errore
			 */
			Log.d("ERROR", "unable to perform action :" + clickedaction);
		}

	}
	public void showActionDialog(Context context){
		ringProgressDialog = ProgressDialog.show(context,
				"Please wait ...", "Performing "
						+ this.action.getClass().getSimpleName()
								.toLowerCase() + "", true);
		ringProgressDialog.setCancelable(true);
	}
	public void dismissActionDialog() {
		if (ringProgressDialog != null) {
			ringProgressDialog.dismiss();
			ringProgressDialog = null;
		}
	}

	public void finishAction(String actions) {
		if (action != null) {
			dismissActionDialog();
			this.action = null;
			this.setActions(actions);

		}

	}

	public void goToUserSquare() {
		this.qrsquare = usersquare;

		setActionContext("");
		setActions(useractions);

	}

	public void centerSquare(int width, int height) {
		qrsquare.setTwo(new PointF((width - (height / 2)) / 2, height / 4));
		qrsquare.setThree(new PointF(width - ((width - (height / 2)) / 2),
				height / 4));
		qrsquare.setFour(new PointF(width - ((width - (height / 2)) / 2),
				3 * height / 4));
		qrsquare.setOne(new PointF((width - (height / 2)) / 2, 3 * height / 4));
	}

	public void centerLastSquare(int width, int height) {
		lastqrsquare.setTwo(new PointF((width - (height / 2)) / 2, height / 4));
		lastqrsquare.setThree(new PointF(width - ((width - (height / 2)) / 2),
				height / 4));
		lastqrsquare.setFour(new PointF(width - ((width - (height / 2)) / 2),
				3 * height / 4));
		lastqrsquare.setOne(new PointF((width - (height / 2)) / 2,
				3 * height / 4));
	}

	public void centerUserSquare(int width, int height) {
		usersquare.setTwo(new PointF((width - (height / 2)) / 2, height / 4));
		usersquare.setThree(new PointF(width - ((width - (height / 2)) / 2),
				height / 4));
		usersquare.setFour(new PointF(width - ((width - (height / 2)) / 2),
				3 * height / 4));
		usersquare
				.setOne(new PointF((width - (height / 2)) / 2, 3 * height / 4));
	}

	public void setQRSquareScrollable(int horizontal, int vertical) {
		if (qrsquare != null) {
			qrsquare.setMaxHorizontalScroll(horizontal);
			qrsquare.setMaxVerticalScroll(vertical);
		}

	}

	public void setUsersquare(QRSquare usersquare, String useractions) {
		this.usersquare = usersquare;
		this.useractions = useractions;
		// for (int i = 0; i < actions.size(); i++) {
		// if (!actions.get(i).equals("login")) {
		// this.useractions += actions.get(i) + ",";
		// } else {
		// this.useractions += "users,links,edit,logout,";
		// }

		// }
		if (this.useractions.endsWith(",")) {
			this.useractions = this.useractions.substring(0,
					this.useractions.length() - 1);
		}
	}

	public Action getRealAction(String action) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Action realAction;
		if (!actionContext.equals("")) {
			realAction = (Action) Class.forName(
					"com.ogc.action." + actionContext + "."
							+ Action.correctActionName(action)).newInstance();
		} else {
			realAction = (Action) Class.forName(
					"com.ogc.action." + Action.correctActionName(action))
					.newInstance();
		}
		return realAction;
	}

	public int getColor(String action) {
		Action realAction;
		try {
			realAction = getRealAction(action);
			return realAction.getColor(this);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			/*
			 * non è un vero errore
			 */
			// Log.d("ERROR", "cannot get color from action: " + action);
			return Color.GRAY;
		}

	}

	public QRSquare getQRSquare() {
		return qrsquare;
	}

	public Result getResult() {
		return result;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public QRSquare getUsersquare() {
		return usersquare;
	}

	public QRUser getUser() {
		return user;
	}

	public void setUser(QRUser user) {
		this.user = user;
	}

	public String getLastactions() {
		return lastactions;
	}

	public void setLastactions(String lastactions) {
		this.lastactions = lastactions;
	}

	public QRSquare getLastqrsquare() {
		return lastqrsquare;
	}

	public void setLastqrsquare(QRSquare lastqrsquare) {
		this.lastqrsquare = lastqrsquare;
	}

	public String getActionContext() {
		return actionContext;
	}

	public int getListindex() {
		return listindex;
	}

	public void setListindex(int listindex) {
		this.listindex = listindex;
	}

	public void setActionContext(String actionContext) {
		this.actionContext = actionContext;
	}

	public boolean isRefreshExplorer() {
		return refreshExplorer;
	}

	public void setRefreshExplorer(boolean refreshExplorer) {
		this.refreshExplorer = refreshExplorer;
	}

	public void saveStateLogout(Context context) {
		SharedPreferences app_preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = app_preferences.edit();
		editor.remove("userid");
		editor.commit(); // Very important

	}

	public void saveState(String json, String type, String actions,
			Context context) {
		SharedPreferences app_preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = app_preferences.edit();
		Gson gson = GsonHelper.customGson;

		if (user != null) {
			editor.putString("user", gson.toJsonTree(user, QRUser.class)
					.toString());
		}
		editor.putString("current", json);
		editor.putString("actions", actions);
		editor.putString("currenttype", type);
		if (lastqrsquare != null && lastactions != null
				&& !lastactions.equals("")) {
			String last = gson.toJsonTree(lastqrsquare).toString();
			editor.putString("last", last);
			editor.putString("lastactions", lastactions);
			editor.putString("lastype", lastqrsquare.getClass().getName()
					.toString());
		}
		if (usersquare != null && useractions != null
				&& !useractions.equals("")) {
			String users = gson.toJsonTree(usersquare).toString();
			editor.putString("usersquare", users);
			editor.putString("useractions", useractions);
			editor.putString("usertype", usersquare.getClass().getName()
					.toString());

		}
		editor.commit(); // Very important

	}

	public boolean resumeState(Context context, int width, int height) {
		Gson gson = GsonHelper.customGson;
		SharedPreferences app_preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		// Get the value for the run counter
		String user = app_preferences.getString("user", "error");
		if (!user.equals("error")) {
			setUser(gson.fromJson(user, QRUser.class));
		}

		String lastjson = app_preferences.getString("last", "error");
		String lasttype = app_preferences.getString("lastype", "error");
		String lastactions = app_preferences.getString("lastactions", "error");
		if (!lastjson.equals("error") && !lasttype.equals("error")
				&& !lastactions.equals("error")) {

			try {
				QRSquare fromJson = (QRSquare) gson.fromJson(lastjson,
						Class.forName(lasttype));

				this.lastqrsquare = fromJson;
				this.lastactions = lastactions;
				centerLastSquare(width, height);
			} catch (JsonSyntaxException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String userjson = app_preferences.getString("usersquare", "error");
		String usertype = app_preferences.getString("usertype", "error");
		String useractions = app_preferences.getString("useractions", "error");
		if (!userjson.equals("error") && !usertype.equals("error")
				&& !useractions.equals("error")) {

			try {
				QRSquare fromJson = (QRSquare) gson.fromJson(userjson,
						Class.forName(usertype));

				this.usersquare = fromJson;
				this.useractions = useractions;
				centerUserSquare(width, height);
			} catch (JsonSyntaxException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String json = app_preferences.getString("current", "error");
		String type = app_preferences.getString("currenttype", "error");
		String actions = app_preferences.getString("actions", "error");
		Log.d("RESUME", "type:" + type + " json:" + json);
		if (!json.equals("error") && !type.equals("error")
				&& !actions.equals("error")) {

			try {
				QRSquare fromJson = (QRSquare) gson.fromJson(json,
						Class.forName(type));

				setQRSquare(fromJson, true);
				setActionContext("");
				setActions(actions);
				centerSquare(width, height);
				return true;
			} catch (JsonSyntaxException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}
