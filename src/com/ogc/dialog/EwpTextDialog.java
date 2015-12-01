package com.ogc.dialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qrboard.FreeDraw;
import com.example.qrboard.R;
import com.ogc.browsers.attrubutes.Text;


/**
 * Created by root on 26/06/15.
 */
public class EwpTextDialog   extends Dialog {
    EditText text;
    Button okButton;
    Text eltext;
    public EwpTextDialog(Context context,String oldText,Text eltext) {
        super(context);
        setContentView(R.layout.ewp_textdialog);
        setTitle("Edit text");
         text = (EditText) this.findViewById(R.id.ewp_textdialogtext);
        okButton  = (Button) this.findViewById(R.id.ewp_textdialogbutton);
        text.setText(oldText);
        this.eltext = eltext;

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
    	String html = eltext.getElement().text(text.getText().toString()).html();
    	eltext.onEdit(html);
    	dismiss();
    }

}
