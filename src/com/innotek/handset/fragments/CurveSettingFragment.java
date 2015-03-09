package com.innotek.handset.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.DatabaseAdapter;


public class CurveSettingFragment extends Fragment{
	
	public static final String ARG_PAGE = "page";
	public static final String CURVE_ID = "curve_id";
	public static final String TAG = "CurveParamsFragment";
	
	private EditText mDry;
	private EditText mWet;
	private EditText mDTime;
	private EditText mSTime;
	private TextView mStageNumber;
	private Button mSaveButton;
	
	private static int mPageNumber;
	private static long mCurveId;
	
	private int j;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
		mCurveId = getArguments().getLong(CURVE_ID);
	}

	private void saveCurveParams(){
		float dry = Float.valueOf(mDry.getText().toString());
		float wet = Float.valueOf(mWet.getText().toString());
		int sTime = Integer.valueOf(mSTime.getText().toString());
		int dTime = Integer.valueOf(mDTime.getText().toString());
		
		DataManager curveManager = new DataManager(getActivity());
		
		int result = curveManager.modifyCurveParams(dry, wet, sTime, dTime, mCurveId, j);
		if(result > 0){
			//Send curve params
			Log.i(TAG, "Send curve Params to device");
		}else{
			
		}
	}
	
	private void confirmFireMissiles() {
	    DialogFragment newFragment = new FireMissilesDialogFragment();
	    newFragment.show(getFragmentManager(), "missiles");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container,											
			Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.fragment_scrollview_curve_params, container, false);

		
		mDry = (EditText)view.findViewById(R.id.edit_dry_temperature);
		mWet = (EditText)view.findViewById(R.id.edit_wet_temperature);
		mDTime = (EditText)view.findViewById(R.id.edit_duration_time);
		mSTime = (EditText)view.findViewById(R.id.edit_stage_time);
		
		mSaveButton = (Button)view.findViewById(R.id.id_save_btn);
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirmFireMissiles();
			}
		});
		
		
		mStageNumber = (TextView)view.findViewById(R.id.id_stage_number);
		mStageNumber.setText("½×¶Î " + (mPageNumber+1));
		
		DatabaseAdapter dbAdapter = new DatabaseAdapter(getActivity());
		dbAdapter.open();
		j = mPageNumber+1;
		Cursor data =  dbAdapter.getCurveParamsByStageAndCurve(j, mCurveId);
		dbAdapter.close();
		
		if(data != null){
			float dry;
			float wet;
			int sTime;
			int dTime;
			do{
				dry = data.getFloat(data.getColumnIndex("dry_value"));
				wet = data.getFloat(data.getColumnIndex("wet_value"));
				sTime = data.getInt(data.getColumnIndex("stage_time"));
				dTime = data.getInt(data.getColumnIndex("duration_time"));
				
				
			}while(data.moveToNext());
			mDry.setText(String.valueOf(dry));
			mWet.setText(String.valueOf(wet));
			mSTime.setText(String.valueOf(sTime));
			mDTime.setText(String.valueOf(dTime));
		}
		
		return view;
	}
	
    public static CurveSettingFragment create(int stage, long curve_id) {
    	CurveSettingFragment fragment = new CurveSettingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, stage);
        args.putLong(CURVE_ID, curve_id);
        fragment.setArguments(args);
        return fragment;
    }
	
    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    private class FireMissilesDialogFragment extends DialogFragment {
    	@Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_fire_missiles)
                   .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	   saveCurveParams();
                       }
                   })
                   .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           // User cancelled the dialog
                       }
                   });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
