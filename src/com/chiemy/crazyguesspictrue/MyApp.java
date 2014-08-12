package com.chiemy.crazyguesspictrue;

import java.io.IOException;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

public class MyApp extends Application {
	private SharedPreferences.Editor editor;
	private SharedPreferences preference;
	private SoundPool soundPool;
	private int cancelId,coinId,enterId;
	private AssetManager assetMgr;
	private boolean isSoundPoolLoaded = false;
	@Override
	public void onCreate() {
		super.onCreate();
		preference = getSharedPreferences("progress", Context.MODE_WORLD_WRITEABLE);
		editor = preference.edit();
		assetMgr = getAssets();
		
		soundPool = new SoundPool(2, AudioManager.STREAM_NOTIFICATION, 0);
		if (soundPool != null) {
			soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
				@Override
				public void onLoadComplete(SoundPool soundPool, int sampleId,
						int status) {
					isSoundPoolLoaded = status == 0;
				}
			});

			try {
				cancelId = soundPool.load(assetMgr.openFd("sound/cancel.wav"), 1);
				coinId = soundPool.load(assetMgr.openFd("sound/coin.wav"), 1);
				enterId = soundPool.load(assetMgr.openFd("sound/enter.wav"), 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("GameFragment", "创建声音池失败");
		}
	}
	public boolean saveCurrentQuestionNo(int no){
		editor.putInt("quetionNo", no);
		return editor.commit();
	}
	public int readCurrentQuestionNo(){
		return preference.getInt("quetionNo", 1);
	}
	public boolean saveIconNo(int no){
		editor.putInt("iconNo", no);
		return editor.commit();
	}
	public int readIconNo(){
		return preference.getInt("iconNo", 0);
	}
	
	public void playSound(int id) {
		if (isSoundPoolLoaded) {
			soundPool.play(id, 1.0f, 1.0f, 0, 0, 1.0f);
		}
	}

	public void playCancelSound() {
		playSound(cancelId);
	}

	public void playCoinSound() {
		playSound(coinId);
	}

	public void playEnterSound() {
		playSound(enterId);
	}
}
