package com.innotek.handset.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ShowPhotoActivity extends Activity {
	private ImageView iv;
	public static final String EXTRA_PATH = "path";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		iv = new ImageView(getApplicationContext());
		
		String path = getIntent().getStringExtra(EXTRA_PATH);
		if (path != null) {
			Bitmap bitMap;
			
		      Bitmap camorabitmap = BitmapFactory.decodeFile(path); 
		      if(null != camorabitmap ){
		         
		         int scale = reckonThumbnail(camorabitmap.getWidth(),camorabitmap.getHeight(), 500, 600);   
		          bitMap = PicZoom(camorabitmap, camorabitmap.getWidth() / scale,camorabitmap.getHeight() / scale);  
		         
		         camorabitmap.recycle();  
		       
		         iv.setVisibility(View.VISIBLE);
		         iv.setImageBitmap(bitMap);   
		         
		      }
			
		} else {
			finish();
		}
		setContentView(iv);
	}
	
	public static int reckonThumbnail(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        if ((oldHeight > newHeight && oldWidth > newWidth)
                || (oldHeight <= newHeight && oldWidth > newWidth)) {
            int be = (int) (oldWidth / (float) newWidth);
            if (be <= 1)
                be = 1;
            return be;
        } else if (oldHeight > newHeight && oldWidth <= newWidth) {
            int be = (int) (oldHeight / (float) newHeight);
            if (be <= 1)
                be = 1;
            return be;
        }
        return 1;
    }
 
    public static Bitmap PicZoom(Bitmap bmp, int width, int height) {
        int bmpWidth = bmp.getWidth();
        int bmpHeght = bmp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);

        return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
    }
}
