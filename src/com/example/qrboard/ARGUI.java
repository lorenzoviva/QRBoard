package com.example.qrboard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.ogc.action.Action;
import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.QRSquare;
import com.ogc.model.QRUser;

public class ARGUI {

	private Result result;
	private QRSquare qrsquare;
	private QRSquare lastqrsquare = null;
	private QRSquare usersquare;
	private String useractions;
	private String lastactions;
	private List<String> actions = new ArrayList<String>();

	private int hover = -1;
	private String clickedaction = "";
	private QRUser user = null;
	private Action action = null;
	private List<Action> allActions = new ArrayList<Action>();
	private String actionContext = "";

	public QRUser getUser() {
		return user;
	}

	public void setUser(QRUser user) {
		this.user = user;
	}

	public ARGUI() {

	}

	public void openFreeDrawActivity(JSONObject jsonObject, Context context) {
		Intent intent = new Intent(context, FreeDraw.class);
		intent.putExtra("jsonFreeDraw", jsonObject.toString());
		context.startActivity(intent);
	}

	public void draw(Canvas canvas, ARLayerView arview) {
		if (qrsquare != null && (!actions.isEmpty() && actions.size() > 0)) {
			drawCircle(canvas, arview);
			qrsquare.draw(canvas, arview);
		}

	}

	public void drawCircle(Canvas canvas, View view) {

		float cx = (qrsquare.getOne().x + qrsquare.getTwo().x + qrsquare.getThree().x + qrsquare.getFour().x) / 4;
		float cy = (qrsquare.getOne().y + qrsquare.getTwo().y + qrsquare.getThree().y + qrsquare.getFour().y) / 4;
		float radius = (float) (1.618d * Math.max(Math.max(Math.sqrt(Math.pow(cx - qrsquare.getOne().x, 2) + Math.pow(cy - qrsquare.getOne().y, 2)), Math.sqrt(Math.pow(cx - qrsquare.getTwo().x, 2) + Math.pow(cy - qrsquare.getTwo().y, 2))), Math.max(Math.sqrt(Math.pow(cx - qrsquare.getThree().x, 2) + Math.pow(cy - qrsquare.getThree().y, 2)), Math.sqrt(Math.pow(cx - qrsquare.getFour().x, 2) + Math.pow(cy - qrsquare.getFour().y, 2)))));

		RectF rect = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
		RectF rectext = new RectF((float) (cx - radius * 0.9), (float) (cy - radius * 0.9), (float) (cx + radius * 0.9), (float) (cy + radius * 0.9));
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
			float textSize = (float) (radius * 0.2);
			paint.setTextSize(textSize);
			if (!actions.isEmpty() && actions.size() > 0) {
				canvas.drawTextOnPath(actions.get(i), path, 0, textSize, paint);
			}
			canvas.drawPath(path, paint);

			Action realaction = null;
			try {
				realaction = getRealAction(actions.get(i));
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				/*
				 * non è un vero errore
				 */
				// Log.d("ERROR", "unable to get icon from action :" +
				// actions.get(i));
			}
			if (realaction != null && realaction.getIcon(view) != null) {
				Bitmap icon = realaction.getIcon(view);
				float ty = (float) (cy + radius * Math.cos((angle * i) + (angle / 2)) - (icon.getHeight() / 2));
				float lx = (float) (cx + radius * Math.sin((angle * i) + (angle / 2)) - (icon.getWidth() / 2));
				canvas.drawBitmap(icon, lx, ty, new Paint());
			}
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
					lastactions = lastactions.substring(0, lastactions.length() - 1);
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
			Log.d("EXCEPTION", "not forced, action:" + action.getClass().getSimpleName());
			// }

			action.addQRParameter(qrsquare);
			this.qrsquare = qrsquare;
		}

	}

	public QRSquare getQRSquare() {
		return qrsquare;
	}

	public void setShape(Result rawResult) {
		if (lastqrsquare != null && result != null) {
			lastqrsquare.setShape(result.getResultPoints(), ((android.graphics.Point) (result.getResultMetadata().get(ResultMetadataType.OTHER))).y);
		}
		this.result = rawResult;
		qrsquare.setShape(result.getResultPoints(), ((android.graphics.Point) (rawResult.getResultMetadata().get(ResultMetadataType.OTHER))).y);

	}

	public Result getResult() {
		return result;
	}

	public void setActions(String action) {
		if (this.action == null) {
			if (lastqrsquare != null) {
				String lastactions = "";
				for (int i = 0; i < actions.size(); i++) {
					lastactions += actions.get(i) + ",";
				}
				if (lastactions != null && lastactions.endsWith(",")) {
					lastactions = lastactions.substring(0, lastactions.length() - 1);
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
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					/*
					 * non è un vero errore
					 */
					Log.d("ERROR", "unable to prepare action :" + actions[i]);
				}
				this.actions.add(actions[i]);
			}
			if (usersquare != null && usersquare.equals(qrsquare)) {
				this.useractions = action;
			}

		}
	}

	public Action getRealAction(String action) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Action realAction;
		if (!actionContext.equals("")) {
			realAction = (Action) Class.forName("com.ogc.action." + actionContext + "." + Action.correctActionName(action)).newInstance();
		} else {
			realAction = (Action) Class.forName("com.ogc.action." + Action.correctActionName(action)).newInstance();
		}
		return realAction;
	}

	public int getColor(String action) {
		Action realAction;
		try {
			realAction = getRealAction(action);
			return realAction.getColor(this);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			/*
			 * non è un vero errore
			 */
			// Log.d("ERROR", "cannot get color from action: " + action);
			return Color.GRAY;
		}

	}

	public void goToLastQRSquare() {
		if (lastqrsquare != null && lastactions != null) {
			setQRSquare(lastqrsquare, false);
		}
	}

	public void onTouch(MotionEvent event, Context context) {
		if (qrsquare != null) {

			float cx = (qrsquare.getOne().x + qrsquare.getTwo().x + qrsquare.getThree().x + qrsquare.getFour().x) / 4;
			float cy = (qrsquare.getOne().y + qrsquare.getTwo().y + qrsquare.getThree().y + qrsquare.getFour().y) / 4;

			float tx = event.getX();
			float ty = event.getY();
			float ax = tx - cx;
			float ay = ty - cy;

			double theta_radians = 360 - (((Math.atan2(ay, -ax) / Math.PI) * 180) + 180);
			float angle = (float) (360 / actions.size());
			for (int i = 0; i < actions.size(); i++) {
				if (theta_radians < angle * (i + 1) && theta_radians > angle * i) {
					if (touchOnQRSquare(event)) {
						qrsquare.onTouch(event);
					} else {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
			polygon.addVertex(new Point(qrsquare.getTwo().x, qrsquare.getTwo().y));
			polygon.addVertex(new Point(qrsquare.getThree().x, qrsquare.getThree().y));
			polygon.addVertex(new Point(qrsquare.getFour().x, qrsquare.getFour().y));
			polygon.addVertex(new Point(qrsquare.getOne().x, qrsquare.getOne().y));
			return Utility.PointInPolygon(new Point(event.getX(), event.getY()), polygon);
		} else {
			return false;
		}
	}

	public void performAction(String action, Context context) {
		try {
			this.action = getRealAction(action);
			Log.d("ACTION", "performing:" + action);
			this.action.perform(this, context);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			/*
			 * non è un vero errore
			 */
			Log.d("ERROR", "unable to perform action :" + clickedaction);
		}

	}

	public void finishAction(String actions) {
		if (action != null) {
			this.action = null;
			this.setActions(actions);
		}

	}

	public void setQRSquareScrollable(int horizontal, int vertical) {
		if (qrsquare != null) {
			qrsquare.setMaxHorizontalScroll(horizontal);
			qrsquare.setMaxVerticalScroll(vertical);
		}

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

	public void setUsersquare(QRSquare usersquare) {
		this.usersquare = usersquare;
		this.useractions = "";
		for (int i = 0; i < actions.size(); i++) {
			if (!actions.get(i).equals("login")) {
				this.useractions += actions.get(i) + ",";
			} else {
				this.useractions += "users,links,edit,logout,";
			}

		}
		if (useractions.endsWith(",")) {
			useractions = useractions.substring(0, useractions.length() - 1);
		}
	}

	public void showUsers(int width, int height) {
		this.qrsquare = usersquare;
		qrsquare.setTwo(new PointF((width - (height / 2)) / 2, height / 4));
		qrsquare.setThree(new PointF(width - ((width - (height / 2)) / 2), height / 4));
		qrsquare.setFour(new PointF(width - ((width - (height / 2)) / 2), 3 * height / 4));
		qrsquare.setOne(new PointF((width - (height / 2)) / 2, 3 * height / 4));
		setActionContext("");
		setActions(useractions);

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

	public void setActionContext(String actionContext) {
		this.actionContext = actionContext;
	}
}
