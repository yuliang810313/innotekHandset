package com.innotek.handset.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


public class CurveLines extends View {
	private int screenWidth;
	private int screenHeight;
	
	private float[] drys;
	private float[] wets;
	private int[] stageTimes;
	private int[] durationTimes;
	
	private int stage;
	private int currentStage;
	
	private Paint paint;
	private Paint rectPaint;
	private int radius;
	
	private int dHeight;
	private int dWidth;
	
	private float[][] timePositions;
	
	private int style = 0;
	
	private static final int DRY_COLOR = 0xFF01579B;
	private static final int WET_COLOR = 0xFF2E7D32;
	private static final int RED = 0xFFB71C1C;
	
	public static final String TAG = "CurveLines";
	
	public CurveLines(Context context, AttributeSet attrs) {
		super(context, attrs);	
		
	    paint = new Paint();
	    
		DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		
		float density = dm.density;
		radius = Math.round(2 * density);
		
		rectPaint = new Paint();
 		rectPaint.setStyle(Paint.Style.STROKE);
 		rectPaint.setStrokeWidth(3);
 		rectPaint.setColor(RED);
 		
	}
	
	public void setDrys(float[] drys){
		this.drys = drys;
	}
	
	public int[] createTimes(){
		
		if(stageTimes == null || durationTimes == null ){
			return null;
		}else{
			int length = durationTimes.length + stageTimes.length;
			int[] timePoints = new int[length]; 
			int j = 0;
			for(int i = 0; i < stageTimes.length ; i++){
				timePoints[j] = durationTimes[i];
				j++;
				timePoints[j] = stageTimes[i];
				j++;
			}
			
			return timePoints;
		}	
	}
	
	

	
	/**
	 * 
	 * @param canvas
	 * @param color
	 * @param line
	 * @param text
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param offsetX
	 * @param offsetY
	 */
	public void drawCurve(Canvas canvas, int color, float[] line,String text,
							float startX, float startY, float endX, float endY, float offsetX , float offsetY){
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(6);
		paint.setColor(color);
		
		canvas.drawLines(line, paint);
		
			for(int i = 0; i < line.length / 2; i++){
				canvas.drawCircle(line[i*2], line[i*2+1], radius, paint);
			}
		
		paint.setStrokeWidth(10);
		
		canvas.drawLine(startX, startY, endX, endY, paint);
		
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(28);
		paint.setFakeBoldText(true);
		
		canvas.drawText(text, endX + offsetX, endY + offsetY, paint);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawContent(canvas);
	 }

	
	private void drawContent(Canvas canvas){
		float[] dryLine = createLines(20, 400);
		float[] wetLine = createLines(20, 450);
		
		timePositions = createTimePoints(wetLine);
		
		
		 drawCoordinates(paint, canvas);
		 
		 drawCurve(canvas, DRY_COLOR, dryLine,  "干球设定", 50, 50, 150, 50, 10 , 10);
		 drawCurve(canvas, WET_COLOR, wetLine, "湿球设定", 50, 80, 150, 80, 10 , 10);

		 paint.setTextSize(25);
		 
		 //Draw wet Text
		 if(wets != null){
			 paint.setColor(WET_COLOR);
				 for(int i = 0; i < wets.length; i++){
					 canvas.drawText(String.valueOf(wets[i]), timePositions[i*2][0]-20, timePositions[i*2][1] - 10, paint);
		 		}	 
		 }
		 
		 //Draw dry Text
		 if(drys != null){
			 paint.setColor(DRY_COLOR);
			
				 for(int i = 0; i < drys.length; i++){ 
					 canvas.drawText(String.valueOf(drys[i]), timePositions[i*2][0]-20, timePositions[i*2][1] -65 , paint);
				 }
			 	
		 }	
		 
		 //Draw times points
		 int[] timeLines = createTimes();
		 if(timeLines != null){
			
		 	for(int i = 0; i < timeLines.length-1  ; i++){
		 		float x =  timePositions[i][0];
		 		float y =  timePositions[i][1];
		 		
		 		canvas.drawText(String.valueOf(timeLines[i]), x, y + 25, paint);
		 	}
		 	
		 		canvas.drawRect(timePositions[currentStage][0] - dWidth / 2,
	 				    timePositions[currentStage][1] - 200,
	 				    timePositions[currentStage][0] + dWidth / 2, 
	 				    timePositions[currentStage][1] + 50, 
	 				    rectPaint);
		 }
		 if(style != 0)
			 drawDelta(canvas);
	}
	/**
	 * 
	 * @param paint
	 * @param canvas
	 */
	 private void drawCoordinates(Paint paint, Canvas canvas){
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(0xFFE0E0E0);
		for(int i = 0; i <= screenWidth ; i += 50){
			 canvas.drawLine(i, 700, i, 0, paint);
		}
	 }
	 
	 
	public float[] createXs(int startX, int screenWidth){
		dWidth = (screenWidth - startX - 20) / 20;
		float [] xs = new float[38];
		
		for(int i = 0; i < xs.length; i++){
			if(i % 2 == 0){
				if(i == 0){
					xs[i] = startX;
				}else
					xs[i] = xs[i-1];
			}else
				xs[i] = xs[i-1] + dWidth;
		}
		return xs;
	}
	 
	public float[] createYs(int startY, int screenHeight){
		dHeight = 20;
		float[] ys = new float[38];
		for(int i = 0; i < ys.length; i++){
			if(i < 3)
				ys[i] = startY;
			else if(i > 34){
				ys[i] = ys[33] - dHeight;
			}
			else{
				float temp = startY - dHeight * ((i+1)/4);
				
				for(int j = 0; j < 4; j++){
					ys[i] = temp;
					if(j<3)
						i=i+1;
				}	
			}
		}
		
		return ys;
	}
	
	
	private float[] createLines(int startX, int startY){
		
		float[] linePoints = new float[19 * 4];
		float[] xs = createXs(startX, screenWidth);
		float[] ys = createYs(startY, screenHeight);
		
		for(int i = 0; i < linePoints.length; i++){
			if(i % 2 == 0)
				linePoints[i] = xs[i/2];
			else
				linePoints[i] = ys[(i - 1)/2];
		}
		
		return linePoints;
	}
	
	private float[][] createTimePoints(float[] line){
		float[][] points = new float[line.length / 4][2];
		int j = 0;
		
		for(int i = 0; i < points.length; i++){
			
			points[i][0] = line[j*2] + (line[(j + 1) * 2] - line[j*2])/2;
			j += 2;
			
			points[i][1] = line[1] - i * (dHeight/2);
			
		}
		
		return points;
	}
	
	
	
	public void setWets(float[] wets) {
		this.wets = wets;
	}

	public void setStageTimes(int[] stageTimes) {
		this.stageTimes = stageTimes;
	}

	public void setDurationTimes(int[] durationTimes) {
		this.durationTimes = durationTimes;
	}

	public void setCurrentStage(int currentStage){
		this.currentStage = currentStage;
	}

	

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public void setStyle(int style) {
		this.style = style;
	}
	
	
	public void drawDelta(Canvas canvas){
		paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        
		Path path2=new Path();
		
		float x = timePositions[stage ][0];
		float y = timePositions[stage ][1] + 50;
		
        path2.moveTo(x, y);
        path2.lineTo(x - dWidth / 2, y + 50);
        path2.lineTo(x + dWidth / 2, y + 50);
        path2.close();
        
        canvas.drawPath(path2, paint);
		
	}
	
}
