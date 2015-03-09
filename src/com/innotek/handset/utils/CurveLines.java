package com.innotek.handset.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class CurveLines extends View {
	
	private float[] drys;
	private float[] wets;
	private int[] stageTimes;
	private int[] durationTimes;
	private int currentStage;
	//private int style;
	
	private Paint paint;
	private Paint rectPaint;
	private int radius;
	private static final int DRY_COLOR = 0xFF01579B;
	private static final int WET_COLOR = 0xFF2E7D32;
	private static final int RED = 0xFFB71C1C;
	

	public CurveLines(Context context, AttributeSet attrs) {
		super(context, attrs);	
		
	    paint = new Paint();
	    
		DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
		float density = dm.density;
		radius = Math.round(2*density);
		
		
		rectPaint = new Paint();
 		rectPaint.setStyle(Paint.Style.STROKE);
 		rectPaint.setStrokeWidth(3);
 		rectPaint.setColor(RED);
	}
	
	public void setDrys(float[] drys){
		this.drys = drys;
	}
	
//	public void setStyle(int style){
//		this.style = style;
//	}
	
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
	
	
	protected void onDraw(Canvas canvas) {
		 drawCoordinates(paint, canvas);
		 
		 drawCurve(canvas, DRY_COLOR, dryLine,  "干球设定", 50, 50, 150, 50, 10 , 10);
		 drawCurve(canvas, WET_COLOR, wetLine, "湿球设定", 50, 80, 150, 80, 10 , 10);

		 paint.setTextSize(25);
		 
		 

		 
		 //Draw wet Text
		 if(wets != null){
			 paint.setColor(WET_COLOR);
			
				 for(int i = 0; i < wets.length; i++){
					 canvas.drawText(String.valueOf(wets[i]), timePositions[i*2][0]-20, timePositions[i*2][1] - 140, paint);
		 		}
			 
		 }
		 
		 //Draw dry Text
		 if(drys != null){
			 paint.setColor(DRY_COLOR);
			
				 for(int i = 0; i < drys.length; i++){ 
					 canvas.drawText(String.valueOf(drys[i]), timePositions[i*2][0]-20, timePositions[i*2][1] - 210, paint);
				 }
			 	
		 }	
		 
		 //Draw times points
		 int[] timeLines = createTimes();
		 if(timeLines != null){
			
		 	for(int i = 0; i < timeLines.length - 1; i++){
		 		int x =  timePositions[i][0];
		 		int y =  timePositions[i][1];
		 		if(i == currentStage){
		 			
		 			canvas.drawRect(x-25, y-250, x+25, y-80, rectPaint);
		 		}
	
		 		canvas.drawText(String.valueOf(timeLines[i]), x, y-100, paint);
		 	}
		 }
		
	 }

	 private void drawCoordinates(Paint paint, Canvas canvas){
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(0xFFE0E0E0);
		for(int i = 0; i <= 1025; i+= 50){
			 canvas.drawLine(i, 700, i, 0, paint);
		}
	 }
	 
	 
		private float[] dryLine = new float[]{
				 50, 500, 100, 500,
				 100,500, 150, 470,
				 150,470, 200, 470,
				 200,470, 250, 440,
				 250,440, 300, 440,
				 300,440, 350, 410,
				 350,410, 400, 410,
				 400,410, 450, 380,
				 450,380, 500, 380,
				 500,380, 550, 350,
				 550,350, 600, 350,
				 600,350, 650, 320,
				 650,320, 700, 320,
				 700,320, 750, 290,
				 750,290, 800, 290,
				 800,290, 850, 250,
				 850,250, 900, 250,
				 900,250, 950, 210,
				 950,210, 1000, 210
				 
		 };
		
		private float[] wetLine = new float[]{
				 50, 560, 100, 560,
				 100, 560, 150, 540,
				 150,540, 200, 540,
				 200,540, 250, 510,
				 250,510, 300, 510,
				 300,510, 350, 480,
				 350,480, 400, 480,
				 400,480, 450, 450,
				 450,450, 500, 450,
				 500,450, 550, 420,
				 550,420, 600, 420,
				 600,420, 650, 390,
				 650,390, 700, 390,
				 700,390, 750, 360,
				 750,360, 800, 360,
				 800,360, 850, 320,
				 850,320, 900, 320,
				 900,320, 950, 290,
				 950,290, 1000, 290	 
		 };
		
		 int[][] timePositions = new int[][]{
			{75, 690}, {125, 680}, {175, 670},{225, 650},{275, 640},
			{325, 620},{375, 610},{425, 595},{475, 580},{525, 560},
			{575, 545},{625, 530},{675, 515},{725, 490},{775, 485},
			{825, 465},{875, 450},{925,425},{975,415},{1025,380}
		 };

}
