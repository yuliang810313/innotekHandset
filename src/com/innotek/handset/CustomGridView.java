package com.innotek.handset;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CustomGridView extends RelativeLayout {
	
//	private Paint paint;
//	private static final int WET_COLOR = 0xFF2E7D32;
//	private static final String TAG = "Custom RelativeLayout";
	
	
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
//		Log.i(TAG, "draw rect");
//		paint = new Paint();
//		paint.setStyle(Paint.Style.FILL);
//		paint.setColor(WET_COLOR);	
	}
	


}
