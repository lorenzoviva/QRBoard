package com.example.qrboard;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ogc.action.Action;
import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.ACL;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;
import com.ogc.model.special.QRAccessDaniedWebPage;
import com.ogc.model.special.QRSquareUserRepresentation;

public class QRExplorer extends ARLayerView implements SurfaceHolder.Callback, OnClickListener {

	private Rect bounds = null;
	private Paint background = new Paint();
	private List<QRExplorerRow> rows = new ArrayList<QRExplorerRow>();
	private QRUser user;
	private QRSquare square;
	private int scroll = 0;
	private int maxScroll = 0;
	private int selectedRow = 0;
	float dx;// difference of x coordinate of touch
	float dy;// difference of y coordinate of touch
	float lx = -1;// last x coordinate of touch
	float ly = -1;// last y coordinate of touch
	float ddx = -1;// displacement on move
	float ddy = -1;// displacement on move
	private Button explorebutton;
	private ImageView image;
	private TextView text;
	private String actionContext = "";
	private Action action;
	private Button backbutton;
	private QRExplorerRowHeader qrExplorerRowHeader;
	private QRExplorerRowFooter qrExplorerRowFooter;
	private String messageRequest;
	private ACL focusedACL;

	public QRExplorer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public QRExplorer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public QRExplorer(Context context) {
		super(context);
		init();
	}

	public void setExploreButton(Button explorebutton) {
		this.explorebutton = explorebutton;
		explorebutton.setOnClickListener(this);

	}

	public void setEditTextInfo(TextView text) {
		this.text = text;
		this.text.setTextColor(Color.WHITE);
	}

	public void setEditImageInfo(ImageView image) {
		this.image = image;
	}

	public void init() {
		getHolder().addCallback(this);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		bounds = new Rect(0, 0, width, height);
		if (!rows.isEmpty() && qrExplorerRowHeader != null && qrExplorerRowFooter != null) {
			if (rows.size() * (bounds.right - bounds.left) / 5 - height > 0) {
				maxScroll = rows.size() * rows.get(0).getSize(bounds) + qrExplorerRowHeader.getHeight(bounds) + qrExplorerRowFooter.getHeight(bounds) - height;
			}
			if(bounds!=null && backbutton != null){
				int xr = (bounds.right - bounds.left) / 10;
				int xl = ((bounds.right - bounds.left) * 3) / 10;
				int yt = ((bounds.bottom - bounds.top)) / 10;
				int yb = yt + (bounds.right - bounds.left) / 5;
				RelativeLayout.LayoutParams backparams = new RelativeLayout.LayoutParams(xl - xr, 100);
				backparams.leftMargin = xr;
				backparams.topMargin = yb + yt + 120;
				backbutton.setLayoutParams(backparams);
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float tx = event.getX();
		float ty = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// Log.d("QRExplorer", "DOWN touch view tx:" + tx + " ty:" + ty +
			// " dx:" + dx + " dy:" + dy + " lx:" + lx + " ly:" +ly);
			lx = tx;
			ly = ty;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// Log.d("QRExplorer", "MOVE touch view tx:" + tx + " ty:" + ty +
			// " dx:" + dx + " dy:" + dy + " lx:" + lx + " ly:" +ly);
			if (lx != -1) {
				dx = lx - tx;
				dy = ly - ty;
				if (scroll + dy > 0 && scroll + dy < maxScroll) {
					scroll += dy;
					ddy += Math.abs(dy);

				}
			}
			lx = tx;
			ly = ty;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// Log.d("QRExplorer", "UP touch view tx:" + tx + " ty:" + ty +
			// " dx:" + dx + " dy:" + dy + " lx:" + lx + " ly:" +ly);
			lx = -1;
			ly = -1;
			if (ddy < 15) {

				 if (getFocusedQRTouched(event)) {
					edit();
				}else if (getQRExplorerRowSquareTouched(event) != -1) {

					selectedRow = getQRExplorerRowSquareTouched(event);
					setFocusedSquare(rows.get(selectedRow).touched(event),rows.get(selectedRow).getAcl());

				} else if (qrExplorerRowFooter != null && qrExplorerRowFooter.touched(event) != -1) {

					onQRExplorerRowFooterTouched(event);

				} else {

					setFocusedSquare(null,null);

				}

				performClick();

			}
			ddy = -1;

		}

		return true;
	}

	public void onQRExplorerRowFooterTouched(MotionEvent event) {

		try {
			setListIndex(qrExplorerRowFooter.touched(event));
			JSONObject requestMessageObj = new JSONObject(messageRequest);
			if (requestMessageObj.has("text")) {
				setQRSquare(new QRSquare(requestMessageObj.getString("text")));
			}
			explore();
		} catch (JSONException e) {
			Toast.makeText(getContext(), "Sorry. Unable to do that!", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == explorebutton.getId()) {
			explore();
		} else if (v.getId() == backbutton.getId()) {
			gotoScanActivity();
		}
	}

	public void explore() {
		if (!(getQRSquare() instanceof QRAccessDaniedWebPage)) {
			performAction("users");
		} else {
			Toast.makeText(getContext(), "You can't explore that!", Toast.LENGTH_SHORT).show();
		}
	}

	public void edit() {
		if(getFocusedSquare() != null && focusedACL != null && focusedACL.isWrite() == true){
			performAction("edit");
		} else {
			Toast.makeText(getContext(), "You can't edit that!", Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressWarnings("unchecked")
	public void setup(String stringjsonresponse) {
		try {
			JSONObject jsonresponse = new JSONObject(stringjsonresponse);
			Gson gson = GsonHelper.customGson;

			Log.d("surface setup", "surface setup");
			String qrsquareuserString = jsonresponse.getJSONArray("QRSquareUser").toString();
			String aclliststring = jsonresponse.getString("ACLList");
			messageRequest = jsonresponse.getString("requestmessage");
			int request = jsonresponse.getInt("request");
			int maxusers = jsonresponse.getInt("maxusers");
			int listindex = jsonresponse.getInt("listindex");
			int totusers = jsonresponse.getInt("totusers");
			if (jsonresponse.has("QRSquare") && jsonresponse.has("type") && jsonresponse.has("acl")) {

				String type = "com.ogc.model." + jsonresponse.getString("type");
				ACL acl = gson.fromJson(jsonresponse.getString("acl"), ACL.class);
				if (acl.isRead()) {
					square = (QRSquare) gson.fromJson(jsonresponse.getString("QRSquare"), Class.forName(type));
				} else {
					square = (QRSquare) gson.fromJson(jsonresponse.getString("QRSquare"), Class.forName(type));
					square = new QRAccessDaniedWebPage(square.getText());
				}
				setFocusedSquare(square, acl);

			}

			if (jsonresponse.has("QRUser")) {
				QRUser user = (GsonHelper.customGson).fromJson(jsonresponse.getString("QRUser"), QRUser.class);
				if (request != 3) {
					setUser(user);
				} else if (jsonresponse.has("user")) {
					QRUser me = (GsonHelper.customGson).fromJson(jsonresponse.getString("user"), QRUser.class);
					setUser(me);
				}
			}
			Type qrsquareUserlistType = new TypeToken<ArrayList<QRSquareUser>>() {
			}.getType();
			Type qrusermenagerlistType = new TypeToken<ArrayList<QRUserMenager>>() {
			}.getType();
			Type qrsquareslistType = new TypeToken<ArrayList<String>>() {
			}.getType();
			Type acllistType = new TypeToken<ArrayList<ACL>>() {
			}.getType();
			List<QRUserMenager> qrusersmenagersfromJson = null;
			List<String> qrsquaresfromJson = null;
			List<QRSquareUser> qrsquareusersfromJson = (List<QRSquareUser>) gson.fromJson(qrsquareuserString, qrsquareUserlistType);
			List<ACL> aclList = (List<ACL>) gson.fromJson(aclliststring, acllistType);
			if (request == 1 || request == 2) {
				String qrusersmenagersString = jsonresponse.getJSONArray("QRUserMenagers").toString();
				qrusersmenagersfromJson = (List<QRUserMenager>) gson.fromJson(qrusersmenagersString, qrusermenagerlistType);
			} else if (request == 3) {
				String qrsquaresString = jsonresponse.getJSONArray("QRSquares").toString();
				qrsquaresfromJson = (List<String>) gson.fromJson(qrsquaresString, qrsquareslistType);
			}
			qrExplorerRowHeader = new QRExplorerRowHeader(request, this);
			qrExplorerRowFooter = new QRExplorerRowFooter(maxusers, listindex, totusers, this);
			boolean qrsquareusersAvaliable = qrsquareusersfromJson != null && !qrsquareusersfromJson.isEmpty();
			boolean qrusermenagersAvaliable = qrusersmenagersfromJson != null && !qrusersmenagersfromJson.isEmpty();
			boolean qrsquaresAvaliable = qrsquaresfromJson != null && !qrsquaresfromJson.isEmpty();

			if (request == 1 && qrsquareusersAvaliable && qrusermenagersAvaliable && qrsquareusersfromJson.size() == qrusersmenagersfromJson.size()) {
				for (int i = 0; i < qrsquareusersfromJson.size(); i++) {
					rows.add(new QRExplorerRow(qrusersmenagersfromJson.get(i), qrsquareusersfromJson.get(i).getUser(), qrsquareusersfromJson.get(i), aclList.get(i), 1));

				}
			}
			if (request == 2 && qrsquareusersAvaliable && qrusermenagersAvaliable && qrsquareusersfromJson.size() == qrusersmenagersfromJson.size()) {
				for (int i = 0; i < qrsquareusersfromJson.size(); i++) {
					if (qrsquareusersfromJson.get(i).getIsnew()) {
						rows.add(new QRExplorerRow(qrusersmenagersfromJson.get(i), qrsquareusersfromJson.get(i).getUser(), null, aclList.get(i), 2));
					} else {
						rows.add(new QRExplorerRow(qrusersmenagersfromJson.get(i), qrsquareusersfromJson.get(i).getUser(), qrsquareusersfromJson.get(i), aclList.get(i), 2));
					}

				}
			}
			if (request == 3 && qrsquareusersAvaliable && qrsquaresAvaliable && qrsquareusersfromJson.size() == qrsquaresfromJson.size()) {
				for (int i = 0; i < qrsquareusersfromJson.size(); i++) {
					Class<? extends QRSquare> squareclass = (Class<? extends QRSquare>) Class.forName("com.ogc.model." + qrsquaresfromJson.get(i));
					String stringjsonsquare = (new JSONObject(jsonresponse.getJSONArray("QRSquareUser").get(i).toString())).getString("square");
					QRSquare cast = (QRSquare) gson.fromJson(stringjsonsquare, squareclass);
					rows.add(new QRExplorerRow(cast, squareclass.getName(), qrsquareusersfromJson.get(i), aclList.get(i)));

				}
			}
			if(bounds!=null && backbutton != null){
				int xr = (bounds.right - bounds.left) / 10;
				int xl = ((bounds.right - bounds.left) * 3) / 10;
				int yt = ((bounds.bottom - bounds.top)) / 10;
				int yb = yt + (bounds.right - bounds.left) / 5;
				RelativeLayout.LayoutParams backparams = new RelativeLayout.LayoutParams(xl - xr, 100);
				backparams.leftMargin = xr;
				backparams.topMargin = yb + yt + 120;
				backbutton.setLayoutParams(backparams);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void draw(Canvas canvas) {

		// super.draw(canvas);
		background.setColor(Color.rgb(93, 181, 224));
		if (bounds == null) {
			canvas.drawRect(new Rect(0, 0, 100, 100), background);
		} else {
			canvas.drawRect(bounds, background);
		}
		if (qrExplorerRowHeader != null && bounds != null && qrExplorerRowFooter != null) {
			int headerheight = qrExplorerRowHeader.getHeight(bounds);

			bounds.top = bounds.top + headerheight;
			int rowsheight = 0;
			if (!rows.isEmpty() && bounds != null) {
				rowsheight = rows.get(0).getSize(bounds) * rows.size();
				for (int i = 0; i < rows.size(); i++) {
					if (selectedRow != i) {
						rows.get(i).draw(canvas, this, i, bounds, scroll, false);
					} else {
						rows.get(i).draw(canvas, this, i, bounds, scroll, true);
					}
				}
			}
			bounds.top = bounds.top - headerheight;
			qrExplorerRowHeader.draw(canvas, this, bounds);

			qrExplorerRowFooter.draw(canvas, this, bounds, headerheight + rowsheight - scroll);
		}

		QRSquare focusedSquare = getQRSquare();
		if (focusedSquare != null) {
			int xr = (bounds.right - bounds.left) / 10;
			int xl = ((bounds.right - bounds.left) * 3) / 10;
			int yt = ((bounds.bottom - bounds.top)) / 10;
			int yb = yt + (bounds.right - bounds.left) / 5;

			Paint backgroundpaint = new Paint();
			backgroundpaint.setColor(Color.rgb(200, 200, 200));
			canvas.drawRect(new RectF(xr - 33, yt - 33, xl + 33, yb + yt), backgroundpaint);

			focusedSquare.setOne(new PointF(xr, yb));
			focusedSquare.setTwo(new PointF(xr, yt));
			focusedSquare.setThree(new PointF(xl, yt));
			focusedSquare.setFour(new PointF(xl, yb));
			setQRSquare(focusedSquare);
			focusedSquare.draw(canvas, this);

			RelativeLayout.LayoutParams exploreparams = new RelativeLayout.LayoutParams(xl - xr, 100);
			exploreparams.leftMargin = xr;
			exploreparams.topMargin = yb + yt;
			explorebutton.setLayoutParams(exploreparams);
			RelativeLayout.LayoutParams backparams = new RelativeLayout.LayoutParams(xl - xr, 100);
			backparams.leftMargin = xr;
			backparams.topMargin = yb + yt + 120;
			backbutton.setLayoutParams(backparams);

			RelativeLayout.LayoutParams imageparams = new RelativeLayout.LayoutParams(50, 50);
			imageparams.leftMargin = xr;
			imageparams.topMargin = yb;
			image.setLayoutParams(imageparams);
			RelativeLayout.LayoutParams textparams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, 50);
			textparams.leftMargin = xr + 51;
			textparams.topMargin = yb;
			text.setLayoutParams(textparams);

		}
	}

	private boolean getFocusedQRTouched(MotionEvent event) {
		QRSquare focusedSquare = getQRSquare();
		if (focusedSquare != null) {
			Quadrilateral polygon = new Quadrilateral();
			polygon.addVertex(new Point(focusedSquare.getTwo().x, focusedSquare.getTwo().y));
			polygon.addVertex(new Point(focusedSquare.getThree().x, focusedSquare.getThree().y));
			polygon.addVertex(new Point(focusedSquare.getFour().x, focusedSquare.getFour().y));
			polygon.addVertex(new Point(focusedSquare.getOne().x, focusedSquare.getOne().y));
			return Utility.PointInPolygon(new Point(event.getX(), event.getY()), polygon);
		} else {
			return false;
		}
	}

	private boolean getExploreButtonTouched(MotionEvent event) {
		if (explorebutton != null && explorebutton.getVisibility() == VISIBLE) {
			int xr = (bounds.right - bounds.left) / 10;
			int xl = ((bounds.right - bounds.left) * 3) / 10;
			int yt = ((bounds.bottom - bounds.top)) / 10;
			int yb = yt + (bounds.right - bounds.left) / 5;

			Quadrilateral polygon = new Quadrilateral();
			polygon.addVertex(new Point(xr, yb + (2 * yt)));
			polygon.addVertex(new Point(xl, yb + (2 * yt)));
			polygon.addVertex(new Point(xl, yb + (2 * yt) + 100));
			polygon.addVertex(new Point(xr, yb + (2 * yt) + 100));
			return Utility.PointInPolygon(new Point(event.getX(), event.getY()), polygon);
		} else {
			return false;
		}
	}

	public int getQRExplorerRowSquareTouched(MotionEvent event) {
		if (!rows.isEmpty()) {
			for (int i = 0; i < rows.size(); i++) {
				if (rows.get(i).touched(event) != null) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public void setQRSquareScrollable(int horizontal, int vertical) {
		// don't need this since clicking on qr must be managed in another way
		// if (square != null) {
		// square.setMaxHorizontalScroll(horizontal);
		// square.setMaxVerticalScroll(vertical);
		// }

	}

	public QRSquare getFocusedSquare() {
		return getQRSquare();
	}

	public void setFocusedSquare(QRSquare focusedSquare, ACL focusedACL) {
		Gson gson = GsonHelper.customGson;
		if (explorebutton != null) {
			this.focusedACL = focusedACL;
			if (focusedSquare == null) {
				Log.d("QRExplorer", "SETTING FOCUSED SQUARE: null");
				setQRSquare(null);
				explorebutton.setVisibility(INVISIBLE);
				text.setVisibility(INVISIBLE);
				image.setVisibility(INVISIBLE);
			} else {
				Log.d("QRExplorer", "TYPE : " + focusedSquare.getClass().getSimpleName() + " ACL : " + gson.toJson(focusedACL) + " SETTING FOCUSED SQUARE:" + (gson.toJson(focusedSquare)).toString());
				// setQRSquare(gson.fromJson(gson.toJson(focusedSquare).toString(),
				// focusedSquare.getClass()));
				setQRSquare(focusedSquare);
				if (!(focusedSquare instanceof QRSquareUserRepresentation)) {
					explorebutton.setVisibility(VISIBLE);
				} else {
					explorebutton.setVisibility(INVISIBLE);
				}

				text.setVisibility(VISIBLE);
				image.setVisibility(VISIBLE);
			}
		}
	}

	public Button getBackButton() {
		return backbutton;

	}

	public void setBackButton(Button backButton) {
		this.backbutton = backButton;
		if(bounds!=null && backButton != null){
			int xr = (bounds.right - bounds.left) / 10;
			int xl = ((bounds.right - bounds.left) * 3) / 10;
			int yt = ((bounds.bottom - bounds.top)) / 10;
			int yb = yt + (bounds.right - bounds.left) / 5;
			RelativeLayout.LayoutParams backparams = new RelativeLayout.LayoutParams(xl - xr, 100);
			backparams.leftMargin = xr;
			backparams.topMargin = yb + yt + 120;
			backbutton.setLayoutParams(backparams);
		}
		backbutton.setOnClickListener(this);
	}

	public void gotoScanActivity() {
		Intent intent = new Intent(getContext(), ScanActivity.class);
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
		getContext().startActivity(intent);
	}

}
