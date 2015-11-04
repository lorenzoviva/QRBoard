package com.example.qrboard;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ogc.graphics.Point;
import com.ogc.graphics.Quadrilateral;
import com.ogc.graphics.Utility;
import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRSquareUserRepresentation;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;

public class QRExplorer extends SurfaceView implements SurfaceHolder.Callback, SquareHolderView {

	private Rect bounds = null;
	private Paint background = new Paint();
	private List<QRExplorerRow> rows = new ArrayList<QRExplorerRow>();
	private QRUser user;
	private QRSquare square;
	private int scroll = 0;
	private int maxScroll = 0;
	private int selectedRow = 0;
	private QRSquare focusedSquare;
	float dx;// difference of x coordinate of touch
	float dy;// difference of y coordinate of touch
	float lx = -1;// last x coordinate of touch
	float ly = -1;// last y coordinate of touch
	float ddx = -1;// displacement on move
	float ddy = -1;// displacement on move
	private Button explorebutton;
	private ImageView image;
	private TextView text;

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
		this.explorebutton.setVisibility(INVISIBLE);

	}

	public void setEditTextInfo(TextView text) {
		this.text = text;
		this.text.setVisibility(INVISIBLE);
		this.text.setTextColor(Color.WHITE);
	}

	public void setEditImageInfo(ImageView image) {
		this.image = image;
		this.image.setVisibility(INVISIBLE);
	}

	public void init() {
		setWillNotDraw(false);
		getHolder().addCallback(this);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		bounds = new Rect(0, 0, width, height);
		if (!rows.isEmpty()) {
			if (rows.size() * (bounds.right - bounds.left) / 5 - height > 0) {
				maxScroll = rows.size() * (bounds.right - bounds.left) / 5 - height;
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

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
			if (ddy < 5) {
				selectedRow = getQRExplorerRowSquareTouched(event);
				if (selectedRow != -1) {
					rows.get(selectedRow).onTouchEvent(event);
				} else if (getFocusedQRTouched(event)) {
					Log.d("QRExplorer", "QRFocusedSquare has been touched");
					Toast.makeText(getContext(), "YEEEE CLICKED ON SQUARE", Toast.LENGTH_SHORT).show();
//					focusedSquare.setOne(new PointF(focusedSquare.getOne().x - 20, focusedSquare.getOne().y));
				} else if (getExploreButtonTouched(event)) {
					Log.d("QRExplorer", "Explore button has been touched");
					Toast.makeText(getContext(), "YEEEE CLICKED ON BUTTON", Toast.LENGTH_SHORT).show();
//					explorebutton.setBackgroundColor(Color.BLUE);
				} else {
					setFocusedSquare(null);
				}

			}
			ddy = -1;
		}

		return super.onTouchEvent(event);
	}

	public void setup(String stringjsonresponse) {
		try {
			JSONObject jsonresponse = new JSONObject(stringjsonresponse);
			Gson gson = GsonHelper.customGson;

			Log.d("surface setup", "surface setup");
			String qrsquareuserString = jsonresponse.getJSONArray("QRSquareUser").toString();

			int request = jsonresponse.getInt("request");
			if (jsonresponse.has("QRSquare") && jsonresponse.has("type")) {
				String type = "com.ogc.model." + jsonresponse.getString("type");
				square = (QRSquare) gson.fromJson(jsonresponse.getString("QRSquare"), Class.forName(type));
				if (request != 3) {
					setFocusedSquare(square);
				}
			}

			Type qrsquareUserlistType = new TypeToken<ArrayList<QRSquareUser>>() {
			}.getType();
			Type qrusermenagerlistType = new TypeToken<ArrayList<QRUserMenager>>() {
			}.getType();
			Type qrsquareslistType = new TypeToken<ArrayList<QRSquare>>() {
			}.getType();
			List<QRUserMenager> qrusersmenagersfromJson = null;
			List<QRSquare> qrsquaresfromJson = null;
			List<QRSquareUser> qrsquareusersfromJson = (List<QRSquareUser>) gson.fromJson(qrsquareuserString, qrsquareUserlistType);
			if (request == 1 || request == 2) {
				String qrusersmenagersString = jsonresponse.getJSONArray("QRUserMenagers").toString();
				qrusersmenagersfromJson = (List<QRUserMenager>) gson.fromJson(qrusersmenagersString, qrusermenagerlistType);
			} else if (request == 3) {
				String qrsquaresString = jsonresponse.getJSONArray("QRSquares").toString();
				qrsquaresfromJson = (List<QRSquare>) gson.fromJson(qrsquaresString, qrsquareslistType);
			}
			boolean qrsquareusersAvaliable = qrsquareusersfromJson != null && !qrsquareusersfromJson.isEmpty();
			boolean qrusermenagersAvaliable = qrusersmenagersfromJson != null && !qrusersmenagersfromJson.isEmpty();
			if (request == 1 && qrsquareusersAvaliable && qrusermenagersAvaliable && qrsquareusersfromJson.size() == qrusersmenagersfromJson.size()) {
				for (int i = 0; i < qrsquareusersfromJson.size(); i++) {
					Log.d("QRExplorer", "adding a QRExplorerRow user:" + qrsquareusersfromJson.get(i).getUser());
					rows.add(new QRExplorerRow(qrusersmenagersfromJson.get(i), qrsquareusersfromJson.get(i).getUser(), qrsquareusersfromJson.get(i), 1));
				}
			}
			// if (qrsquareusersfromJson != null &&
			// !qrsquareusersfromJson.isEmpty()) {
			// for (QRSquareUser qrsu : qrsquareusersfromJson) {
			// towrite.add(qrsu.getJSON().toString()) ;
			// Log.d("QRSUS", towrite.get(towrite.size()-1));
			// }
			// // QRUsersWebPage usersquare = new
			// // QRUsersWebPage(qrSquare.getText(), qrsquareusersfromJson);
			// // usersquare.setOne(qrSquare.getOne());
			// // usersquare.setTwo(qrSquare.getTwo())d;
			// // usersquare.setThree(qrSquare.getThree());
			// // usersquare.setFour(qrSquare.getFour());
			// // argui.setQRSquare(usersquare, true);
			// // Log.d("FROM JSON", qrsquareusersfromJson.toString());
			// // if (stringjsonresponse.has("action")) {
			// // argui.setActionContext("users");
			// // argui.finishAction(stringjsonresponse.getString("action"));
			// // } else {
			// // argui.finishAction("Unable to load users");
			// // }
			// }
			// if(qrusersmenagersfromJson != null &&
			// !qrusersmenagersfromJson.isEmpty()){
			// for(QRUserMenager qrum : qrusersmenagersfromJson){
			// towrite.add(gson.toJson(qrum));
			// }
			// }
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

		super.draw(canvas);
		background.setColor(Color.rgb(93, 181, 224));
		if (bounds == null) {
			canvas.drawRect(new Rect(0, 0, 100, 100), background);
		} else {
			canvas.drawRect(bounds, background);
		}
		if (!rows.isEmpty() && bounds != null) {
			for (int i = 0; i < rows.size(); i++) {
				if (selectedRow != i) {
					rows.get(i).draw(canvas, this, i, bounds, scroll, false);
				} else {
					rows.get(i).draw(canvas, this, i, bounds, scroll, true);
				}
			}
		}
		if (focusedSquare != null) {
			int xr = (bounds.right - bounds.left) / 10;
			int xl = ((bounds.right - bounds.left) * 3) / 10;
			int yt = ((bounds.bottom - bounds.top)) / 10;
			int yb = yt + (bounds.right - bounds.left) / 5;

			
			Paint backgroundpaint = new Paint();
			backgroundpaint.setColor(Color.rgb(200,200,200));
			canvas.drawRect(new RectF(xr-33,yt-33,xl+33,yb+yt), backgroundpaint);
			
			focusedSquare.setOne(new PointF(xr, yb));
			focusedSquare.setTwo(new PointF(xr, yt));
			focusedSquare.setThree(new PointF(xl, yt));
			focusedSquare.setFour(new PointF(xl, yb));
			focusedSquare.draw(canvas, this);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(xl - xr, 100);
			params.leftMargin = xr;
			params.topMargin = yb + (2 * yt);
			explorebutton.setLayoutParams(params);
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

			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) explorebutton.getLayoutParams();
			Quadrilateral polygon = new Quadrilateral();
			polygon.addVertex(new Point(layoutParams.leftMargin, layoutParams.topMargin));
			polygon.addVertex(new Point(layoutParams.leftMargin + layoutParams.width, layoutParams.topMargin));
			polygon.addVertex(new Point(layoutParams.leftMargin + layoutParams.width, layoutParams.topMargin + layoutParams.height));
			polygon.addVertex(new Point(layoutParams.leftMargin, layoutParams.topMargin + layoutParams.height));
			return Utility.PointInPolygon(new Point(event.getX(), event.getY()), polygon);
		} else {
			return false;
		}
	}

	public int getQRExplorerRowSquareTouched(MotionEvent event) {
		if (!rows.isEmpty()) {
			for (int i = 0; i < rows.size(); i++) {
				if (rows.get(i).touched(event) != null) {
					setFocusedSquare(rows.get(i).touched(event));
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public void setQRSquareScrollable(int horizontal, int vertical) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActionContext(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performAction(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public QRUser getUser() {
		return user;
	}

	public QRSquare getFocusedSquare() {
		return focusedSquare;
	}

	public void setFocusedSquare(QRSquare focusedSquare) {
		this.focusedSquare = focusedSquare;
		if (explorebutton != null) {
			if (focusedSquare == null) {
				explorebutton.setVisibility(INVISIBLE);
				text.setVisibility(INVISIBLE);
				image.setVisibility(INVISIBLE);
			} else {
				if (!(focusedSquare instanceof QRSquareUserRepresentation)) {
					explorebutton.setVisibility(VISIBLE);
				}else{
					explorebutton.setVisibility(INVISIBLE);
				}

				text.setVisibility(VISIBLE);
				image.setVisibility(VISIBLE);
			}
		}
	}

}
