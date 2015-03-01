package com.innotek.handset.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.innotek.handset.R;
import com.innotek.handset.activities.MainActivity;
import com.innotek.handset.activities.PreferListActivity;

public class HomeFragment extends Fragment {

	private ArrayAdapter<String> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		Button test = (Button)view.findViewById(R.id.id_btn_roomview);
		
		test.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goMonitor();
				
			}
		});
//		ArrayList<ActionsItem> list = new ArrayList<ActionsItem>();
//		list.add(new ActionsItem("烤房监控", R.drawable.ic_flow_chart_50));
//		list.add(new ActionsItem("我的烤房", R.drawable.ic_like_outline_50));
		
//		String [] list = new String[]{"烘烤技术监管", "烘烤业务管理", "烘烤技术咨询", "查询"};
//;		adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_with_textview, 0, list);
//		setListAdapter(adapter);
		
		return view;
	}
	
	
//	@Override
//	public void onListItemClick(ListView listView, View view, int position, long id) {
//
//		switch(position){
//		case 0:
//			goMonitor();
//			break;
//		case 1:
//			goToPrefer();
//			break;
//		}
//		
//	}
	
	private void goToPrefer(){
		Intent intent = new Intent(getActivity(), PreferListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(intent);
	}
	
	private void goMonitor(){
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(intent);
	}
	
/*	private class ActionsAdapter extends ArrayAdapter<ActionsItem>{

		public ActionsAdapter(ArrayList<ActionsItem> actions){
			super(getActivity(), 0, actions);
		}



		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
			}
//			ActionsItem action = this.getItem(position);
//			TextView mAction = (TextView)convertView.findViewById(R.id.id_actions);
//			mAction.setText(action.getTitle());
//			ImageView mImage = (ImageView)convertView.findViewById(R.id.id_actions_image);
//			mImage.setImageResource(action.getImage());
			
			return convertView;
		}
		
		

		
		
	}
	*/
}
