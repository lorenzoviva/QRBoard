package com.ogc.dialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.qrboard.R;
import com.ogc.browsers.attrubutes.Borderradius;


/**
 * Created by root on 26/06/15.
 */
public class EwpBroderradiusDialog   extends Dialog {
    TextView info;
    Button okButton;
    Borderradius borderradius;
    SeekBar bwSeekbar;
    int oldbw;
    public EwpBroderradiusDialog(Context context,int oldbw,Borderradius borderradius) {
        super(context);
        setContentView(R.layout.ewp_borderradiusdialog);
        setTitle("Edit text");
         info = (TextView) this.findViewById(R.id.borderradiusdialoginfotext);
        okButton  = (Button) this.findViewById(R.id.borderradiusdialogbutton);
        bwSeekbar = (SeekBar) this.findViewById(R.id.borderradiusdialogseekbar);
        if(oldbw!=-1){
        	String bw = String.valueOf(oldbw);
        	info.setText("Radius:" + bw );
        }else{
        	String bw = "0";
        	info.setText("Radius:" + bw );
        }
        this.borderradius = borderradius;
        this.oldbw = oldbw;
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
    	info.setText("Radius:" + progress);
    }
    public void confirmDialog(){
    	int newbw = bwSeekbar.getProgress();
    	if(oldbw!=-1 || (newbw!=oldbw)){
    		String style = borderradius.editStyleAttribute("border-radius", newbw+"px");
    		style = borderradius.editStyleAttribute(style, "border-style", "solid");
    		String html = borderradius.getElement().attr("style",style).html();
    		Log.d("NEWHTML", html);
    		borderradius.onEdit(html);
    	}
    	dismiss();
    }

}
