package com.ogc.dialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qrboard.FreeDraw;
import com.example.qrboard.R;
import com.ogc.browsers.attrubutes.Image;
import com.ogc.browsers.attrubutes.Text;


/**
 * Created by root on 26/06/15.
 */
public class EwpImageDialog   extends Dialog {
    EditText text;
    Button okButton;
    Image image;
    public EwpImageDialog(Context context,String oldText,Image image) {
        super(context);
        setContentView(R.layout.ewp_imagedialog);
        setTitle("Insert image link");
        text = (EditText) this.findViewById(R.id.ewp_imagedialogtext);
        okButton  = (Button) this.findViewById(R.id.ewp_imagedialogbutton);
        text.setText(oldText);
        this.image = image;

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
    	String style = image.editStyleAttribute("background-image", "url('" + text.getText().toString() + "')");
    	style = image.editStyleAttribute(style, "background-size", "contain");
    	style = image.editStyleAttribute(style, "background-repeat","no-repeat");
    	String html = image.getElement().attr("style",style).html();
    	image.onEdit(html);
    	dismiss();
    }

}
