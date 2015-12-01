package com.ogc.dialog;
import com.example.qrboard.FreeDraw;
import com.example.qrboard.R;
import com.example.qrboard.R.id;
import com.example.qrboard.R.layout;

import android.app.Dialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by root on 26/06/15.
 */
public class FreeDrawTextDialog   extends Dialog {
    EditText text;
    Button okButton;
    FreeDraw drawwindow;
    public FreeDrawTextDialog(FreeDraw context) {
        super(context);
        setContentView(R.layout.freedraw_textdialog);
        setTitle("Edit text");
         text = (EditText) this.findViewById(R.id.freedraw_textdialogtext);
        okButton  = (Button) this.findViewById(R.id.freedraw_textdialogbutton);
        text.setText(context.getDrawView().getTextDrawer().getMessage());

        drawwindow = context;

        okButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                confirmDialog();
                return false;
            }
        });
        show();
    }
    public void confirmDialog(){
        drawwindow.confirmTextDialog(text.getText().toString());
    }

}
