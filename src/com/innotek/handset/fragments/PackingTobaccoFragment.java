package com.innotek.handset.fragments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.innotek.handset.R;
import com.innotek.handset.activities.SelectCurveActivity;
import com.innotek.handset.activities.ShowPhotoActivity;
import com.innotek.handset.entities.Packing;
import com.innotek.handset.entities.PhotoInfo;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;

public class PackingTobaccoFragment extends Fragment implements OnClickListener{

	private ImageView mTakePhoto;
	
	private ImageView mImageView1;
	private ImageView mImageView2;
	private ImageView mImageView3;
	private ImageView mImageView4;
	
	private PhotoInfo[] photoInfos = new PhotoInfo[4];
	
	private List<ImageView> mImageViewList = new ArrayList<ImageView>();
	
	private static final int GO_TO_CEMARA = 1;
	
	private static final int GO_TO_IMAGEVIEW = 2;
	
	private File currentImageFile = null;
	
	private String currentPath;
	
	private String filename;
	
	private int count;

	private long mRoomId;
	private long stationId;
	
	private DataManager dm;
	//
	private RadioGroup mGroupCategory;
	private String mCategory;
	
	private EditText mPackingAmount;
	
	private RadioGroup mGroupCategoryState;
	private String mCategoryState;
	
	private RadioGroup mGroupUniformity;
	private String mUniformity;
	
	private RadioGroup mGroupPackingType;
	private String mPackingType;
	
	private EditText mOther;
	private DatabaseAdapter adapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		mRoomId = getArguments().getLong("ROOM_ID");
		stationId = getArguments().getLong("STATION_ID");
		dm = new DataManager(getActivity());
		adapter = new DatabaseAdapter(getActivity());
	}
	
	private void takePic(){
		
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
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_packing_tobacco, container, false);
		
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
				takePic();
			}
		});
		
		
		mOther = (EditText) view.findViewById(R.id.id_edit_other);
		
		mGroupCategory = (RadioGroup) view.findViewById(R.id.id_group_category);
		mGroupCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mCategory = getValueOnSelect(group, checkedId);
			}
		});
		
		
		mGroupCategoryState = (RadioGroup) view.findViewById(R.id.id_group_category_state);
		mGroupCategoryState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mCategoryState = getValueOnSelect(group, checkedId);
			}
		});
		
		
		mGroupUniformity = (RadioGroup) view.findViewById(R.id.id_group_uniformity);
		mGroupUniformity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mUniformity = getValueOnSelect(group, checkedId);
			}
		});
		
		mGroupPackingType = (RadioGroup) view.findViewById(R.id.id_group_packing_type);
		mGroupPackingType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mPackingType = getValueOnSelect(group, checkedId);
			}
		});
		
		mPackingAmount = (EditText) view.findViewById(R.id.id_packing_amount);
		
		
		Button mDone = (Button) view.findViewById(R.id.id_button_done);
		mDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("Packing", "Amount :" + mPackingAmount);
				if(mPackingAmount.getText().toString().equals("")){
					mPackingAmount.setError("请填写装烟量");
				}else{
					Packing packing = new Packing();
					packing.setCategoryState(mCategoryState);
					packing.setCategory(mCategory);
					packing.setPackingAmount(Float.valueOf(mPackingAmount.getText().toString()));
					packing.setPackingType(mPackingType);
					packing.setPreferRoomId(mRoomId);
					packing.setUniformity(mUniformity);
					packing.setOther(mOther.getText().toString());
					dm.savePacking(packing);
					
					adapter.open();
					adapter.updatePreferRoomStage(mRoomId, 3);
					for(int i = 0; i < photoInfos.length; i++){
						if(photoInfos[i] != null){
							ContentValues cv = new ContentValues();
							cv.put("file_name", filename);
							cv.put("current_path", currentPath);
							cv.put("prefer_room_id", mRoomId);
							cv.put("photo_type", 2);

							adapter.insertPhoto(cv);
							
						}
						
					}
					adapter.close();
					
					startNewActivity();
				}	
			}
		});
		
		adapter.open();
		Cursor c = adapter.findPacking(mRoomId);
		Cursor cursor = adapter.getPhotosByTypeAndPreferRoomId(2, mRoomId);
		adapter.close();
		
		if(cursor != null){
			
			do{
				Bitmap bitMap;
				String path = cursor.getString(cursor.getColumnIndex("current_path"));
				String fileName = cursor.getString(cursor.getColumnIndex("file_name"));
				
				PhotoInfo photoInfo = new PhotoInfo(fileName, path);
		        photoInfos[count] = photoInfo;
		        
			    Bitmap camorabitmap = BitmapFactory.decodeFile(path); 
			    if(null != camorabitmap ){
			       
			         int scale = reckonThumbnail(camorabitmap.getWidth(),camorabitmap.getHeight(), 500, 600);   
			         bitMap = PicZoom(camorabitmap, camorabitmap.getWidth() / scale,camorabitmap.getHeight() / scale);  
			        
			         camorabitmap.recycle();  
			      
			         mImageViewList.get(count).setVisibility(View.VISIBLE);
			         mImageViewList.get(count).setImageBitmap(bitMap);   
			         count++;
			    }
			}while(c.moveToNext());
		}
		
		
		
		
		if(c != null){
			String category = c.getString(c.getColumnIndex("category"));
			if(category.equals("同杆/夹同质")){
				mGroupCategory.check(R.id.radio0);
			}else if(category.equals("混编")){
				mGroupCategory.check(R.id.radio1);
			}else if(category.equals("同竿/夹均匀")){
				mGroupCategory.check(R.id.radio2);
			}else if(category.equals("同竿/夹不匀")){
				mGroupCategory.check(R.id.radio3);
			}
			
			String packageType = c.getString(c.getColumnIndex("packing_type"));
			if(packageType.equals("各竿/夹量基本一致")){
				mGroupPackingType.check(R.id.radio4);
			}else if(packageType.equals("各竿/夹量不一致")){
				mGroupPackingType.check(R.id.radio5);
			}
			
			String packageState = c.getString(c.getColumnIndex("category_state"));
			if(packageState.equals("同层同质")){
				mGroupCategoryState.check(R.id.radio9);
			}else if(packageState.equals("按气流方式正确分层")){
				mGroupCategoryState.check(R.id.radio10);
			}
			
			String uniformity = c.getString(c.getColumnIndex("uniformity"));
			if(uniformity.equals("前密后稀")){
				mGroupUniformity.check(R.id.radio11);
			}else if(uniformity.equals("前稀后密")){
				mGroupUniformity.check(R.id.radio12);
			}else if(uniformity.equals("均匀")){
				mGroupUniformity.check(R.id.radio13);
			}
			
			mPackingAmount.setText(String.valueOf(c.getFloat(c.getColumnIndex("packing_amount"))));
			mOther.setText(c.getString(c.getColumnIndex("other")));
		}
		
		return view;
	}
	
	
	private String getValueOnSelect(RadioGroup group, int checkedId){
		View view  = group.findViewById(checkedId);
		int radioId = group.indexOfChild(view);
	    RadioButton btn = (RadioButton) group.getChildAt(radioId);
	    return btn.getText().toString();
	}
	
	public static PackingTobaccoFragment newInstance(long roomId, long stationId){
		Bundle bundle = new Bundle();
		bundle.putLong("ROOM_ID", roomId);
		bundle.putLong("STATION_ID", stationId);
		
		PackingTobaccoFragment fragment = new PackingTobaccoFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	public void startNewActivity(){
		Intent intent = new Intent(getActivity(), SelectCurveActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", mRoomId);
		intent.putExtra("STATION_ID", stationId);
		
		getActivity().startActivity(intent);
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
		      
				  if (count == 4) {
					  count = 0;
				  }	
				  
		         mImageViewList.get(count).setVisibility(View.VISIBLE);
		         mImageViewList.get(count).setImageBitmap(bitMap);   
		        
		         PhotoInfo photoInfo = new PhotoInfo(filename, currentPath);
		         photoInfos[count] = photoInfo;

				 count++;
					
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
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_image_1:
		case R.id.id_image_2:
		case R.id.id_image_3:
		case R.id.id_image_4:
			Intent i = new Intent(getActivity(), ShowPhotoActivity.class);
			i.putExtra(ShowPhotoActivity.EXTRA_PATH, photoInfos[v.getId() - R.id.id_image_1].getFilePath());
			startActivity(i);
			break;

		default:
			break;
		}
		
	}

}
