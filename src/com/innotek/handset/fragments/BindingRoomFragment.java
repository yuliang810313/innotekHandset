package com.innotek.handset.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.innotek.handset.R;
import com.innotek.handset.activities.PreferListActivity;
import com.innotek.handset.entities.Room;
import com.innotek.handset.utils.DataManager;
import com.innotek.handset.utils.JSONUtils;

public class BindingRoomFragment extends Fragment{
	
	private TextView mRoomNo;
	private EditText mTobaccoNo;
	private EditText mRoomID;
	private Button mFireButton;
	private TextView mBindingResult;
	
	private Bundle bundle;
	private DataManager dm;
	private String userID;
	
	public static final String TAG = "Binding Fragment";
	
	//Factory method create BindingRoomFargment
	public static BindingRoomFragment newInstance(String roomNo, String tobaccoNo, long stationId){
		Bundle arguments = new Bundle();
		
		arguments.putString("ROOM_NO", roomNo);
		arguments.putString("TOBACCO_NO", tobaccoNo);
		arguments.putLong("STATION_ID", stationId);
		
		BindingRoomFragment fragment = new BindingRoomFragment();
		fragment.setArguments(arguments);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bundle = getArguments();
		
		dm = new DataManager(getActivity());
		
		SharedPreferences sp = getActivity().getSharedPreferences("PREF_USER", Context.MODE_PRIVATE);
		userID = sp.getString("USER_ID", null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_binding_room, container, false);
		
		mRoomNo = (TextView) view.findViewById(R.id.id_room_no);
		mTobaccoNo = (EditText) view.findViewById(R.id.id_tobacco_no);
		mRoomID = (EditText) view.findViewById(R.id.id_room_id);
		mBindingResult = (TextView) view.findViewById(R.id.id_binding_result);
	    
		mRoomNo.setText("øæ∑ø" + bundle.getString("ROOM_NO"));
		mTobaccoNo.setText(bundle.getString("TOBACCO_NO"));
		
		mFireButton = (Button) view.findViewById(R.id.id_fire_btn);
		mFireButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirmFireMissiles();
			}
		});
		
		return view;
	}
	
	private void bindingRoom(){
		String roomID = mRoomID.getText().toString();
		new FetchDevicesStateTask().execute(roomID);			
	}
	
	
	
	private void confirmFireMissiles() {
	    DialogFragment newFragment = new FireMissilesDialogFragment();
	    newFragment.show(getFragmentManager(), "missiles");
	}
	
	
	
    private class FireMissilesDialogFragment extends DialogFragment {
    	@Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_fire_binding)
                   .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	  bindingRoom();
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
    
    private void preferRooms(){
    	Intent intent = new Intent(getActivity(), PreferListActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intent.putExtra("STATION_ID", getArguments().getLong("STATION_ID"));
    	getActivity().startActivity(intent);
    }
    
    private  class FetchDevicesStateTask extends AsyncTask<String, Void, String>{

		
		@Override
		protected String doInBackground(String...params){
			
			String response = JSONUtils.getRoomById(params[0]);
			try{
				JSONObject object = new JSONObject(response);
				JSONObject room = object.getJSONObject("room");
				String roomID = room.getString("address");
				if(roomID == null){
					return null;
				}else{
					String tobaccoNo = bundle.getString("TOBACCO_NO");
					
					if(dm.bindingRoom(userID, roomID, tobaccoNo) > 0 ){
						Room aRoom = JSONUtils.createRoom(room);
						aRoom.setUser_id(userID);
						dm.saveRoomStatus(aRoom);
						
					}
					
				}
				
			}catch(JSONException e){
				e.printStackTrace();
				mBindingResult.setText("∆•≈‰ ß∞‹");
			}
			return "OK";
		}

		@Override
		protected void onPostExecute(String result) {
			if(result.equals("OK")){
				preferRooms();
			}else{
				mBindingResult.setText("∆•≈‰ ß∞‹£¨◊‘øÿ“«ID¥ÌŒÛ");
			}
		}
	}
}
