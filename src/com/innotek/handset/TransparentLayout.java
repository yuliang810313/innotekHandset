package com.innotek.handset;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TransparentLayout extends View {

	private Paint paint;
	private int infoType;
	private int rectHiehgt;
	private int rectWidth;
	private float dryTarget;
	
	
	public static final int ALERT_BACK = 0xFFD84315;
	public static final int ALERT_OVER = 0xFFFF9800;
	public static final int NORMAL_BACK = 0xFF00695C;
	public static final int NORMAL_OVER = 0xFF4CAF50;
	
	
	public static final int DEEP_GREEN = 0xFF4CAF50;
	public static final int SOLID_GEEN = 0xFFA5D6A7;
	
	public static final int SOLID_YELLOW = 0xFFFFF176;
	public static final int YELLOW = 0xFFFFD54F;
	public static final int DEEP_YELLOW = 0xFFFFB300;
	
	public TransparentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setAlpha(0.5f);
		
		if(infoType == 0){
			paint.setColor(ALERT_OVER);
		}else{
			paint.setColor(NORMAL_OVER);
		}
		canvas.drawRect(0, 0, rectWidth, rectHiehgt , paint);
	}
	
	private void init(Context context){
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
		Log.i(TAG, "info type is " + infoType);
	}

	private static final String TAG = "tl";

	public int getRectHiehgt() {
		return rectHiehgt;
	}

	public void setRectHiehgt(int rectHiehgt) {
		this.rectHiehgt = rectHiehgt;
	}

	public int getRectWidth() {
		return rectWidth;
	}

	public void setRectWidth(int rectWidth) {
		this.rectWidth = rectWidth;
	}

	public float getDryTarget() {
		return dryTarget;
	}

	public void setDryTarget(float dryTarget) {
		this.dryTarget = dryTarget;
	}

	
}
