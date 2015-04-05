package com.innotek.handset;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CustomGridView extends RelativeLayout {
		
	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomGridView(Context context) {
        super(context);
        init(context); 
    }
	
    public CustomGridView(Context context, AttributeSet attrs, int defStyle){
    	super(context, attrs, defStyle);
    	init(context); 
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	private void init(Context context){		

	}
	


}
