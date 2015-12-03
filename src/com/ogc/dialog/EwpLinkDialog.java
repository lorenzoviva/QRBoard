package com.ogc.dialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qrboard.R;
import com.ogc.browsers.attrubutes.Link;


/**
 * Created by root on 26/06/15.
 */
public class EwpLinkDialog   extends Dialog {
    EditText text;
    Button okButton;
    Link link;
    public EwpLinkDialog(Context context,String oldText,Link link) {
        super(context);
        setContentView(R.layout.ewp_linkdialog);
        setTitle("Edit link");
        text = (EditText) this.findViewById(R.id.ewp_linkdialogtext);
        okButton  = (Button) this.findViewById(R.id.ewp_linkdialogbutton);
        text.setText(oldText);
        this.link = link;

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
    	String html = link.getElement().attr("href",text.getText().toString()).html();
    	link.onEdit(html);
    	dismiss();
    }

}
