package com.innotek.handset.fragments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.SoundPool;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.activities.PackingTobaccoActivity;
import com.innotek.handset.activities.ShowPhotoActivity;
import com.innotek.handset.entities.PhotoInfo;
import com.innotek.handset.entities.Tobacco;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;
import com.innotek.handset.utils.RFIDReader;
import com.innotek.handset.utils.Tools;
import com.innotek.handset.utils.UidEntity;

public class FreshTobaccoFragment extends Fragment implements OnClickListener  {
	
	public static final String TAG = "New Tobacco";
	
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
	
	private int count = 0;
	
	//烟叶品种
	private RadioGroup mGroupBreed;
	private String mBreed;
	
	//部位
	private RadioGroup mGroupPart;
	private String mPart;
	
	//含水量
	private RadioGroup mGroupWater;
	private String mWater;
	
	//烟叶质地
	private RadioGroup mGroupQuality;
	private String mQuality;
	
	//烟叶类型
	private RadioGroup mGroupType;
	private String mType;
	
	//烟叶成熟度
	private EditText maturityNormal;
	private EditText maturityBelow;
	private EditText maturityAbove;
	
	private EditText mUID;
	private TextView mReadCard;
	
	private DataManager dm;
	private long mPreferRoomId;
	private long stationId;
	
	//Card Reader
	private ReadThread mReadThread ;
	private RFIDReader readerManager ;
	private boolean runflag = true ;
	private boolean readflag = false ;
	private SoundPool sp ;
	private Map<Integer, Integer> suondMap;
	
	private DatabaseAdapter dbAdapter;
	
	private boolean checkMaturity(){
		
		ArrayList<EditText> ls = new ArrayList<EditText>();
		ls.add(maturityNormal);
		ls.add(maturityAbove);
		ls.add(maturityBelow);
		
		for(int i = 0 ; i < ls.size(); i++){
			try{
				Float.parseFloat(ls.get(i).getText().toString());
			}catch(NumberFormatException e){
				ls.get(i).setError("请输入正确的数值");
				return false;
			}
			
		}
		return true;
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dm = new DataManager(getActivity());
		dbAdapter = new DatabaseAdapter(getActivity());
		mPreferRoomId = getArguments().getLong("ROOM_ID");
		stationId = getArguments().getLong("STATION_ID");
		
		readerManager = new RFIDReader();
		initSoundPool();
	
		mReadThread = new ReadThread();
		mReadThread.start();
		
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_fresh_tobacco, container, false);
		
		mTakePhoto = (ImageView) view.findViewById(R.id.id_open_camera);
		
		mTakePhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				takePic();
			}
		});
		
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
		
		mUID = (EditText) view.findViewById(R.id.id_uid);
		
		mReadCard = (TextView) view.findViewById(R.id.id_read_card);
		mReadCard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		
		
		
		//选择品种
		mGroupBreed = (RadioGroup) view.findViewById(R.id.id_group_breed);
		mGroupBreed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mBreed = getValueOnSelect(group, checkedId);
			}
		});
		
		//选择部位
		mGroupPart = (RadioGroup) view.findViewById(R.id.id_group_part);
		mGroupPart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mPart = getValueOnSelect(group, checkedId);
			}
		});
		
		//含水量
		mGroupWater = (RadioGroup) view.findViewById(R.id.id_group_water);
		mGroupWater.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mWater = getValueOnSelect(group, checkedId);
			}
		});
		
		//烟叶质地
		mGroupQuality = (RadioGroup) view.findViewById(R.id.id_group_quality);
		mGroupQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mQuality = getValueOnSelect(group, checkedId);
			}
		});
		
		
		//烟叶类型
		mGroupType = (RadioGroup) view.findViewById(R.id.id_group_type);
		mGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				mType = getValueOnSelect(group, checkedId);
			}
		});
		
		maturityNormal = (EditText) view.findViewById(R.id.id_maturity_normal);
		maturityBelow = (EditText) view.findViewById(R.id.id_maturity_below);
		maturityAbove = (EditText) view.findViewById(R.id.id_maturity_above);
		
		
		Button mSave = (Button) view.findViewById(R.id.id_button_done);
		mSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checkMaturity()){

					Tobacco mTobacco = new Tobacco();
					mTobacco.setBreed(mBreed);
					mTobacco.setMaturity(maturityNormal.getText().toString() + "/" +
										 maturityBelow.getText().toString() + "/" +
										 maturityAbove.getText().toString());
					
					mTobacco.setPart(mPart);
					mTobacco.setQuality(mQuality);
					mTobacco.setTobaccoType(mType);
					mTobacco.setWaterContent(mWater);
					mTobacco.setPreferRoomId(mPreferRoomId);
					
					dm.saveNewTobacco(mTobacco);
					
					dbAdapter.open();
					for(int i = 0; i < photoInfos.length; i++){
						if(photoInfos[i] != null){
							ContentValues cv = new ContentValues();
							cv.put("file_name", filename);
							cv.put("current_path", currentPath);
							cv.put("prefer_room_id", mPreferRoomId);
							cv.put("photo_type", 1);
							Log.i(TAG, "current photo path is " + currentPath);
							
							long k = dbAdapter.insertPhoto(cv);
							Log.i(TAG, "saved photo id: " + k);
						}
						
					}
					dbAdapter.close();
					startActivity();
				}else{
					
				}
				
			}
		});
		
		
		
		dbAdapter.open();
		Cursor cursor = dbAdapter.getFreshTobaccoByPreferId(mPreferRoomId);
		Cursor c = dbAdapter.getPhotosByTypeAndPreferRoomId(1, mPreferRoomId);
		dbAdapter.close();
		
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
			String breed = cursor.getString(cursor.getColumnIndex("breed"));
			if(breed.equals("云烟87")){
				mGroupBreed.check(R.id.radio0);
			}else if(breed.equals("湘烟3号")){
				mGroupBreed.check(R.id.radio1);
			}else if(breed.toLowerCase().equals("k236")){
				mGroupBreed.check(R.id.radio2);
			}else if(breed.equals("其他")){
				mGroupBreed.check(R.id.radio3);
			}
			
			
			String part = cursor.getString(cursor.getColumnIndex("part"));
			if(part.equals("下部叶")){
				mGroupPart.check(R.id.radio4);
			}else if(part.equals("中部叶")){
				mGroupPart.check(R.id.radio5);
			}else if(part.equals("上部叶")){
				mGroupPart.check(R.id.radio6);
			}
			
			String water = cursor.getString(cursor.getColumnIndex("water_content"));
			if(water.equals("较大")){
				mGroupWater.check(R.id.radio7);
			}else if(water.equals("适中")){
				mGroupWater.check(R.id.radio8);
			}else if(water.equals("较小")){
				mGroupWater.check(R.id.radio9);
			}
			
			String quality = cursor.getString(cursor.getColumnIndex("quality"));
			if(quality.equals("手握弹性好")){
				mGroupQuality.check(R.id.radio10);
			}else if(quality.equals("手握易破碎")){
				mGroupQuality.check(R.id.radio11);
			}
			
//			String type = cursor.getString(cursor.getColumnIndex("tobacco_type"));
//			if(type.equals("正常")){
//				mGroupType.check(R.id.radio12);
//			}else if(type.equals("反青")){
//				mGroupType.check(R.id.radio13);
//			}else if(type.equals("干旱")){
//				mGroupType.check(R.id.radio14);
//			}else if(type.equals("黑暴")){
//				mGroupType.check(R.id.radio15);
//			}else if(type.equals("后发")){
//				mGroupType.check(R.id.radio16);
//			}
			
			String maturity = cursor.getString(cursor.getColumnIndex("maturity"));
			String[] ms = maturity.split("/");
			maturityNormal.setText(ms[0]);
			maturityBelow.setText(ms[1]);
			maturityAbove.setText(ms[2]);
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
	
	private void startActivity(){
		Intent intent = new Intent(this.getActivity(), PackingTobaccoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("ROOM_ID", mPreferRoomId);
		intent.putExtra("STATION_ID", stationId);
		getActivity().startActivity(intent);
	}
	
	
	private String getValueOnSelect(RadioGroup group, int checkedId){
		View view  = group.findViewById(checkedId);
		int radioId = group.indexOfChild(view);
	    RadioButton btn = (RadioButton) group.getChildAt(radioId);
	    return btn.getText().toString();
	}
	
	public static FreshTobaccoFragment newInstance(long roomId, long stationId){
		Bundle bundle = new Bundle();
		bundle.putLong("ROOM_ID", roomId);
		bundle.putLong("STATION_ID", stationId);
		
		FreshTobaccoFragment fragment = new FreshTobaccoFragment();
		fragment.setArguments(bundle);
		return fragment;
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
	
	
	@Override
	public void onDestroy() {
		runflag = false ;
		if(readerManager != null){
			readerManager.close();
		}
		super.onDestroy();
	}
	
	
	private class ReadThread extends Thread{
		byte[] uid = null;
		List<byte[]> listUID ;
		@Override
		public void run() {
			
			while(runflag){
				if(readflag){
					
					uid = readerManager.getNFCuid();
					if(uid != null){
						UidEntity uidentity = new UidEntity();
						uidentity.setCardType("NFC or UL card");
						uidentity.setUid(Tools.Bytes2HexString(uid, uid.length));
						reflash(uidentity);
						
					}
					
					uid = readerManager.readID();
					if(uid != null){
						UidEntity identity = new UidEntity();
						identity.setCardType("ID card");
						identity.setUid(Tools.Bytes2HexString(uid, uid.length));
						reflash(identity);
					}
					
					listUID = readerManager.inventory15693();
					if(listUID != null && !listUID.isEmpty()){
						for(byte[] uid15693 : listUID){
							UidEntity identity = new UidEntity();
							identity.setCardType("ISO15693");
							identity.setUid(Tools.Bytes2HexString(uid15693, uid15693.length));
							Log.e("15693", Tools.Bytes2HexString(uid15693, uid15693.length));
							reflash(identity);
						}
					}
					
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			super.run();
		}
	}
	
	
	private void reflash(final UidEntity entity){
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mUID.setText(entity.getUid());
			}
		});
	}
	
	
	
	private void initSoundPool(){
		sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
		suondMap = new HashMap<Integer, Integer>();
		suondMap.put(1, sp.load(this.getActivity(), R.raw.msg, 1));
	}
	
	
	private void play(int sound, int number){
		AudioManager am = (AudioManager)this.getActivity().getSystemService(this.getActivity().AUDIO_SERVICE);
       
        float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        
       
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = audioCurrentVolume/audioMaxVolume;
        sp.play(
        		suondMap.get(sound),  
        		audioCurrentVolume, 
        		audioCurrentVolume, 
                1, 
                number, 
                1);
    }
}
