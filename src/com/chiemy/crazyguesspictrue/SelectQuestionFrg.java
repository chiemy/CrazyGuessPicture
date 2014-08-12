package com.chiemy.crazyguesspictrue;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chiemy.crazyguesspictrue.MySurfaceView.OnStartListener;

public class SelectQuestionFrg extends Fragment implements OnClickListener{
	private MyApp myApp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApp)(getActivity().getApplication());
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int iconNum = ((MyApp)(getActivity().getApplication())).readIconNo();
		int question = ((MyApp)(getActivity().getApplication())).readCurrentQuestionNo();
		View v = inflater.inflate(R.layout.select_round, container,false);
		TextView icon = (TextView) v.findViewById(R.id.addGold);
		TextView questionNo = (TextView) v.findViewById(R.id.questionNo);
		Button backButton = (Button) v.findViewById(R.id.back);
		backButton.setOnClickListener(this);
		
		icon.setText(""+iconNum);
		
		questionNo.setText(question + "");
		LinearLayout map = (LinearLayout) v.findViewById(R.id.map);
		
		MySurfaceView surfaceView = new MySurfaceView(getActivity());
		surfaceView.setApp(myApp);
		surfaceView.setCurrentIndex(question);
		map.addView(surfaceView);
		surfaceView.setOnStartListener(new OnStartListener() {
			@Override
			public void setIndex(int index) {
				PlayFragment fragment = new PlayFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("questionNo",index);
				fragment.setArguments(bundle);
				getFragmentManager().beginTransaction().replace(R.id.linearLayout, fragment,"play").addToBackStack("play").commit();
			}
		});
		return v;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.back:
			myApp.playEnterSound();
			getFragmentManager().popBackStack();
			break;
		}
	}
}
