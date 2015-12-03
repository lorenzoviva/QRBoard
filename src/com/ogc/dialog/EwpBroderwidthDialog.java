package com.ogc.dialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.qrboard.R;
import com.ogc.browsers.attrubutes.Borderwidth;
import com.ogc.browsers.attrubutes.Text;


/**
 * Created by root on 26/06/15.
 */
public class EwpBroderwidthDialog   extends Dialog {
    TextView info;
    Button okButton;
    Borderwidth borderwidth;
    SeekBar bwSeekbar;
    int oldbw;
    public EwpBroderwidthDialog(Context context,int oldbw,Borderwidth borderwidth) {
        super(context);
        setContentView(R.layout.ewp_borderwidthdialog);
        setTitle("Edit text");
         info = (TextView) this.findViewById(R.id.borderwidthdialoginfotext);
        okButton  = (Button) this.findViewById(R.id.borderwidthdialogbutton);
        bwSeekbar = (SeekBar) this.findViewById(R.id.borderwidthdialogseekbar);
        if(oldbw!=-1){
        	String bw = String.valueOf(oldbw);
        	info.setText("Border:" + bw );
        }else{
        	String bw = "0";
        	info.setText("Border:" + bw );
        }
        this.borderwidth = borderwidth;
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
    	info.setText("Border:" + progress);
    }
    public void confirmDialog(){
    	int newbw = bwSeekbar.getProgress();
    	if(oldbw!=-1 || (newbw!=oldbw)){
    		String style = borderwidth.editStyleAttribute("border-width", newbw+"px");
    		style = borderwidth.editStyleAttribute(style, "border-style", "solid");
    		String html = borderwidth.getElement().attr("style",style).html();
    		borderwidth.onEdit(html);
    	}
    	dismiss();
    }

}
