package com.chiemy.crazyguesspictrue;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class MainActivity extends FragmentActivity{
	private FragmentManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		int no = ((MyApp)getApplication()).readCurrentQuestionNo();
		Bundle bundle = new Bundle();
		bundle.putInt("questionNo", no);
		FirstFragment firstFrag = new FirstFragment();
		firstFrag.setArguments(bundle);
		manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.linearLayout, firstFrag, "first");
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
