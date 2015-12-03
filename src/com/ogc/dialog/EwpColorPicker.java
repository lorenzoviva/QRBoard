package com.ogc.dialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.qrboard.R;
import com.ogc.browsers.attrubutes.QRAttribute;
import com.ogc.browsers.attrubutes.Textcolor;

/**
 * This is the only class of the project. Consist in a costum dialog that show
 * the GUI for choose the color.
 *
 * @author Simone Pessotto
 *
 */
public class EwpColorPicker extends Dialog implements SeekBar.OnSeekBarChangeListener {

    public Activity c;
    public Dialog d;

    View colorView;
    SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    TextView redToolTip, greenToolTip, blueToolTip;
    EditText codHex;
    private int red = -1, green = -1, blue = -1;
    int seekBarLeft;
    Rect thumbRect;
    private QRAttribute attribute;

    private String colorHex;

    public EwpColorPicker(Context context, String color, QRAttribute attribute) {
    	super(context);
    	this.c = (Activity) context;
    	if(color.equals("")){
    		this.red = 0;
    		this.green = 0;
    		this.blue = 0;
    	}
    	this.colorHex = color;
    	this.attribute = attribute;
		show();
	}


	/**
     * Simple onCreate function. Here there is the init of the GUI.
     *
     * @param savedInstanceState As usual ...
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.materialcolorpicker__layout_color_picker);
//        } else {
//            setContentView(R.layout.materialcolorpicker__layout_color_picker_old_android);
//        }

        colorView = findViewById(R.id.colorView);

        redSeekBar = (SeekBar)findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar)findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar)findViewById(R.id.blueSeekBar);

        seekBarLeft = redSeekBar.getPaddingLeft();

        redToolTip = (TextView)findViewById(R.id.redToolTip);
        greenToolTip = (TextView)findViewById(R.id.greenToolTip);
        blueToolTip = (TextView)findViewById(R.id.blueToolTip);

        codHex = (EditText)findViewById(R.id.codHex);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        colorView.setBackgroundColor(Color.rgb(red, green, blue));
        if(red!=-1 && blue != -1 && green !=-1){
        	codHex.setText(String.format("%02x%02x%02x", red, green, blue));
        }else{
        	updateColorView(colorHex);
        }

        codHex.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            updateColorView(v.getText().toString());
                            InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(codHex.getWindowToken(), 0);

                            return true;
                        }
                        return false;
                    }
                });


    }


    /**
     * Method that syncrhonize the color between the bars, the view and the HEC code text.
     *
     * @param s HEX Code of the color.
     */
    private void updateColorView(String s) {
        if(s.matches("#?[0-9a-fA-F]+")){
        	s = s.replace("#", "");
            int color = (int)Long.parseLong(s, 16);
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = (color >> 0) & 0xFF;
            Log.d("RGB", "r:" + red + "g: " + green + " b: " + blue);
            colorView.setBackgroundColor(Color.rgb(red, green, blue));
            redSeekBar.setProgress(red);
            greenSeekBar.setProgress(green);
            blueSeekBar.setProgress(blue);
        }
        else{
            codHex.setError(c.getResources().getText(R.string.materialcolorpicker__errHex));
        }
    }



    /**
     * Method called when the user change the value of the bars. This sync the colors.
     *
     * @param seekBar SeekBar that has changed
     * @param progress The new progress value
     * @param fromUser If it coem from User
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.redSeekBar) {

            red = progress;
           
        }
        else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;
           
        }
        else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;
          

        }

        colorView.setBackgroundColor(Color.rgb(red, green, blue));

        //Setting the inputText hex color
        codHex.setText(String.format("%02x%02x%02x", red, green, blue));

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }


    /**
     * Getter for the RED value of the RGB selected color
     * @return RED Value Integer (0 - 255)
     */
    public int getRed() {
        return red;
    }

    /**
     * Getter for the GREEN value of the RGB selected color
     * @return GREEN Value Integer (0 - 255)
     */
    public int getGreen() {
        return green;
    }


    /**
     * Getter for the BLUE value of the RGB selected color
     * @return BLUE Value Integer (0 - 255)
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Getter for the color as Android Color class value.
     *
     * From Android Reference: The Color class defines methods for creating and converting color ints.
     * Colors are represented as packed ints, made up of 4 bytes: alpha, red, green, blue.
     * The values are unpremultiplied, meaning any transparency is stored solely in the alpha
     * component, and not in the color components.
     *
     * @return Selected color as Android Color class value.
     */
    public int getColor(){
        return Color.rgb(red,green, blue);
    }
}