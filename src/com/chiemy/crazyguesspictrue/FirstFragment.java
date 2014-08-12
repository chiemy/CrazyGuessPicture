package com.chiemy.crazyguesspictrue;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class FirstFragment extends Fragment {
	private ImageView play;
	private FragmentManager manager;
	private int questionNo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = getActivity().getSupportFragmentManager();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.first, container,false);
		Bundle bundle = getArguments();
		questionNo = bundle.getInt("questionNo");
		TextView questionNoTv = (TextView) v.findViewById(R.id.questionNo);
		questionNoTv.setText(questionNo + "");
		play = (ImageView) v.findViewById(R.id.play);
		
		MyClickListener clickListener = new MyClickListener();
		play.setOnClickListener(clickListener);
		return v;
	}
	class MyClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.play:
				MyApp myApp = (MyApp)(getActivity().getApplication());
				myApp.playEnterSound();
				myApp.playEnterSound();
				SelectQuestionFrg fragment = new SelectQuestionFrg();
				manager.beginTransaction().replace(R.id.linearLayout, fragment,"select").addToBackStack("select").commit();
				break;
			}
		}
	}
}
