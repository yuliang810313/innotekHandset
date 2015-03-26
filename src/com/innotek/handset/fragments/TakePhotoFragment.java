package com.innotek.handset.fragments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.innotek.handset.R;
import com.innotek.handset.activities.ShowPhotoActivity;
import com.innotek.handset.entities.PhotoInfo;
import com.innotek.handset.utils.DatabaseAdapter;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TakePhotoFragment extends Fragment implements OnClickListener{

	private ImageView mTakePhoto;
	
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ImageView mImageView3;
	private ImageView mImageView4;
	
	private PhotoInfo[] photoInfo = new PhotoInfo[]{
			new PhotoInfo(null, null),
			new PhotoInfo(null, null),
			new PhotoInfo(null, null),
			new PhotoInfo(null, null)
	};
	
	private List<ImageView> mImageViewList = new ArrayList<ImageView>();
	
	private static final int GO_TO_CEMARA = 1;
	
	private static final int GO_TO_IMAGEVIEW = 2;
	
	private File currentImageFile = null;
	
	private String currentPath;
	
	private String filename;
	
	private int count;
	
	private DatabaseAdapter dbAdapter;
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_image_1:
		case R.id.id_image_2:
		case R.id.id_image_3:
		case R.id.id_image_4:
			Intent i = new Intent(getActivity(), ShowPhotoActivity.class);
			i.putExtra(ShowPhotoActivity.EXTRA_PATH, photoInfo[v.getId() - R.id.id_image_1].getFilePath());
			startActivity(i);
			break;

		default:
			break;
		}
	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbAdapter = new DatabaseAdapter(getActivity());
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.take_photo, container, false);
		
		mImageView1 = (ImageView) view.findViewById(R.id.id_image_1);
		mImageView2 = (ImageView) view.findViewById(R.id.id_image_2);
		mImageView3 = (ImageView) view.findViewById(R.id.id_image_3);
		mImageView4 = (ImageView) view.findViewById(R.id.id_image_4);
		
		mImageView1.setOnClickListener(this);
		mImageView2.setOnClickListener(this);
		mImageView3.setOnClickListener(this);
		mImageView4.setOnClickListener(this);
		
		mImageViewList.add(mImageView1);
		mImageViewList.add(mImageView2);
		mImageViewList.add(mImageView3);
		mImageViewList.add(mImageView4);
		
		mTakePhoto = (ImageView) view.findViewById(R.id.id_open_camera);
		
		mTakePhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				File dir = new File(Environment.getExternalStorageDirectory(), "photos");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				filename = System.currentTimeMillis()+".jpg";
				currentImageFile = new File(dir, filename);
				if (!currentImageFile.exists()) {
					try {
						currentImageFile.createNewFile();
						currentPath = currentImageFile.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
				startActivityForResult(i, 1);
			}
		});
		
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GO_TO_CEMARA) {
			
			Bitmap bitMap;
			
		    Bitmap camorabitmap = BitmapFactory.decodeFile(currentPath); 
		    if(null != camorabitmap ){
		       
		         int scale = reckonThumbnail(camorabitmap.getWidth(),camorabitmap.getHeight(), 500, 600);   
		         bitMap = PicZoom(camorabitmap, camorabitmap.getWidth() / scale,camorabitmap.getHeight() / scale);  
		        
		         camorabitmap.recycle();  
		      
		         mImageViewList.get(count).setVisibility(View.VISIBLE);
		         mImageViewList.get(count).setImageBitmap(bitMap);   
		        
		         
		         ContentValues cv = new ContentValues();
					cv.put("file_name", filename);
					cv.put("current_path", currentPath);
					cv.put("photo_type", 1);
					
					photoInfo[count].setFileName(filename);
					photoInfo[count].setFilePath(currentPath);
					count++;
					
					if (count == 4) {
						count = 0;
					}
							
					dbAdapter.open();
					dbAdapter.insertPhoto(cv);
					dbAdapter.close();
					
		      }
		      
	
		}
		else if (requestCode == GO_TO_IMAGEVIEW) {
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
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
