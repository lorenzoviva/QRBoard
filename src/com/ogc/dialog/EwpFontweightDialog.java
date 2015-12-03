package com.ogc.dialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.qrboard.R;
import com.ogc.browsers.attrubutes.Fontweight;


/**
 * Created by root on 26/06/15.
 */
public class EwpFontweightDialog   extends Dialog {
    TextView info;
    Button okButton;
    Fontweight fontweight;
    SeekBar bwSeekbar;
    int oldbfw;
    public EwpFontweightDialog(Context context,int oldbw,Fontweight fontweight) {
        super(context);
        setContentView(R.layout.ewp_fontweightdialog);
        setTitle("Edit text");
         info = (TextView) this.findViewById(R.id.fontweightdialoginfotext);
        okButton  = (Button) this.findViewById(R.id.fontweightdialogbutton);
        bwSeekbar = (SeekBar) this.findViewById(R.id.fontweightdialogseekbar);
        if(oldbw!=-1){
        	String bw = String.valueOf(oldbw);
        	info.setText("Weight:" + bw );
        }else{
        	String bw = "0";
        	info.setText("Weight:" + bw );
        }
        this.fontweight = fontweight;
        this.oldbfw = oldbw;
        bwSeekbar.setProgress(oldbw);
        okButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                confirmDialog();
                return false;
            }
        });
        bwSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				editInfo(progress);
				
			}
		});
        show();
    }
    public void editInfo(int progress){
    	info.setText("Weight:" + (progress+1)*100);
    }
    public void confirmDialog(){
    	int newfw = (bwSeekbar.getProgress()+1)*100;
    	if(oldbfw!=-1 || (newfw!=oldbfw)){
    		String style = fontweight.editStyleAttribute("font-weight", newfw+"");
    		String html = fontweight.getElement().attr("style",style).html();
    		fontweight.onEdit(html);
    	}
    	dismiss();
    }

}
