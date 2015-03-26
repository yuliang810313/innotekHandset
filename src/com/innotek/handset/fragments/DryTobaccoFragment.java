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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.innotek.handset.R;
import com.innotek.handset.activities.ArbitrateActivity;
import com.innotek.handset.activities.PreferListActivity;
import com.innotek.handset.activities.ShowPhotoActivity;
import com.innotek.handset.activities.WorkFlowActivity;
import com.innotek.handset.entities.DryTobacco;
import com.innotek.handset.entities.PhotoInfo;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;

public class DryTobaccoFragment extends Fragment implements OnClickListener{

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
	
	
	
	private RadioGroup mGroupQuality;
	private String mQuality;
	
	private EditText mDryWeight;
	private EditText mIssue;
		
	private long preferRoomId;
	
	private DataManager dm;
	private int bakingResult;
	private DatabaseAdapter adapter;
	private long stationId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferRoomId = getArguments().getLong("ROOM_ID");
		stationId = getArguments().getLong("STATION_ID");
		
		dm = new DataManager(getActivity());
		adapter = new DatabaseAdapter(this.getActivity());
	}

	private void cleanTask()
	{
		this.getActivity().finishAffinity();
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
		
		super.onCreateView(inflater, container, savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_dry_tobacco, container, false);
		
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
		
		
		
		mGroupQuality = (RadioGroup) view.findViewById(R.id.id_group_quality);
		mGroupQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mQuality = getValueOnSelect(group, checkedId);
				bakingResult = checkedId;
			}
		});
		
		mDryWeight = (EditText) view.findViewById(R.id.id_dry_weight);
		mIssue = (EditText) view.findViewById(R.id.id_edit_issue);
		
		Button mDone = (Button) view.findViewById(R.id.id_button_done);
		mDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveDryTobacco();
				if(bakingResult == R.id.radio1){
					//启动仲裁
					dm.updateRoomStage(preferRoomId, 7);
					startNewActivity(ArbitrateActivity.class);
				}
				else{
					//结束烘烤流程
					dm.updateRoomStage(preferRoomId, 7);
					startNewActivity(PreferListActivity.class);
					cleanTask();
				}
			}
		});
		
		Button mArbitrate = (Button) view.findViewById(R.id.id_button_arbitrate);
		mArbitrate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveDryTobacco();
				if(bakingResult == R.id.radio1){
					//启动仲裁
					dm.updateRoomStage(preferRoomId, 7);
					adapter.open();
					for(int i = 0; i < photoInfos.length; i++){
						if(photoInfos[i] != null){
							ContentValues cv = new ContentValues();
							cv.put("file_name", filename);
							cv.put("current_path", currentPath);
							cv.put("prefer_room_id", preferRoomId);
							cv.put("photo_type", 3);
							
							
							long k = adapter.insertPhoto(cv);
							
						}
						
					}
					adapter.close();
					startNewActivity(ArbitrateActivity.class);
				}
				else{
					//结束烘烤流程
					dm.updateRoomStage(preferRoomId, 7);
					
					startNewActivity(PreferListActivity.class);
				}
				//startNewActivity(ArbitrateActivity.class);
			}
		});
		
		
		adapter.open();
		Cursor cursor = adapter.findDryTobacco(preferRoomId);
		Cursor  c = adapter.getPhotosByTypeAndPreferRoomId(3, preferRoomId);
		adapter.close();
		
		if(c != null){
			
			do{
				Bitmap bitMap;
				String path = c.getString(c.getColumnIndex("current_path"));
				String fileName = c.getString(c.getColumnIndex("file_name"));
				
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
		
		
		if(cursor != null){
		
			mDryWeight.setText(cursor.getString(cursor.getColumnIndex("dry_weight")));
			mIssue.setText(cursor.getString(cursor.getColumnIndex("issue")));
			String quality = cursor.getString(cursor.getColumnIndex("quality"));
			if(quality.equals("烘烤质量正常")){
				mGroupQuality.check(R.id.radio0);
			}else if(quality.equals("烘烤质量有异议"))
				mGroupQuality.check(R.id.radio1);
		}
		
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
	
	
	//Factory method for create new instance
	public static DryTobaccoFragment newInstance(long roomId, long stationId){
		Bundle args = new Bundle();
		args.putLong("ROOM_ID", roomId);
		args.putLong("STATION_ID", stationId);
		DryTobaccoFragment fragment = new DryTobaccoFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	private void startNewActivity(Class<?> cls){
		Intent intent = new Intent(getActivity(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", preferRoomId);
		intent.putExtra("STATION_ID", stationId);
		getActivity().startActivity(intent);
	}
	
	private void saveDryTobacco(){
		DryTobacco dt = new DryTobacco();
		dt.setDryWeight(Float.valueOf(mDryWeight.getText().toString()));
		dt.setPreferRoomId(preferRoomId);
		dt.setIssue(mIssue.getText().toString());
		dt.setQuality(mQuality);
		
		dm.saveTobaccoAfterBaking(dt);
	}
	
	private String getValueOnSelect(RadioGroup group, int checkedId){
		View view  = group.findViewById(checkedId);
		int radioId = group.indexOfChild(view);
	    RadioButton btn = (RadioButton) group.getChildAt(radioId);
	    return btn.getText().toString();
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
