package com.chiemy.crazyguesspictrue;

import java.io.IOException;
import java.text.DecimalFormat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.chiemy.crazyguesspictrue.question.IQuestion;
import com.chiemy.crazyguesspictrue.question.QuestionFactory;

public class PlayFragment extends Fragment implements OnClickListener{
	private int windowWidth;
	private int index;
	private StringBuffer result;
	//答案单词个数
	private int intputTvCount;
	private int currentQuestionNo;
	private int hasPassedQNum;
	private int iconNo;
	private IQuestion currentQuestion;
	//输入框父布局
	private LinearLayout inputLayout;
	//音效，getActivity().getAssets();
	private AssetManager astManager;
	//GirdView适配器
	private TextViewAdapter adapter;
	private GridView gridView;
	private ValueAnimator colorAnimation;
	//图片，地址格式化，new DecimalFormat("格式")，0代表此位没有数字，用0代替
	private DecimalFormat decimalFormat;
	private Dialog answerRightdialog;
	private TextView questionNoTv;
	private TextView iconNoTv;
	private ImageView pictrue;
	private MyApp myApp;
	
	private TextView dialogQNo;
	private TextView dialogRewardIcon;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Integer colorFrom = Color.RED;
		Integer colorTo = Color.WHITE;
		colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
		result = new StringBuffer();
		windowWidth = getResources().getDisplayMetrics().widthPixels;
		astManager = getActivity().getAssets();
		decimalFormat = new DecimalFormat("__00000.png");
		
		answerRightdialog = new Dialog(getActivity(),R.style.dialog);
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
		Button share = (Button) v.findViewById(R.id.button1);
		Button next = (Button) v.findViewById(R.id.button2);
		dialogQNo = (TextView) v.findViewById(R.id.textView3);
		dialogRewardIcon = (TextView) v.findViewById(R.id.textView5);
		
		
		share.setOnClickListener(this);
		next.setOnClickListener(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(windowWidth, getResources().getDisplayMetrics().heightPixels);
		answerRightdialog.addContentView(v, lp);
		answerRightdialog.setCancelable(false);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//获取题号，金币
		myApp = (MyApp)(getActivity().getApplication());
		hasPassedQNum = myApp.readCurrentQuestionNo();
		currentQuestionNo = getArguments().getInt("questionNo");
		iconNo = myApp.readIconNo();
		
		View v = inflater.inflate(R.layout.begin, container,false);
		
		questionNoTv = (TextView) v.findViewById(R.id.questionNo);
		iconNoTv = (TextView) v.findViewById(R.id.addGold);
		
		questionNoTv.setText(currentQuestionNo+"");
		dialogQNo.setText(""+currentQuestionNo);
		iconNoTv.setText("" + iconNo);
		
		setImageSectionHeightWidth(v);
		
		inputLayout = (LinearLayout) v.findViewById(R.id.input);
		
		TextView addGold = (TextView) v.findViewById(R.id.addGold);
		addGold.setOnClickListener(this);
		Button backBtn = (Button) v.findViewById(R.id.back);
		backBtn.setOnClickListener(this);
		
		gridView = (GridView) v.findViewById(R.id.gridView);
		adapter = new TextViewAdapter();
		
		int height = (int) (windowWidth / 8.0f * 3);
		LayoutParams lp = (LayoutParams) gridView.getLayoutParams();
		lp.height = height;
		gridView.setLayoutParams(lp);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				myApp.playEnterSound();
				TextView tv = (TextView)view;
				String word = (String) tv.getText();
				if(index < intputTvCount){
					view.setVisibility(View.INVISIBLE);
					for(int i = 0 ; i < intputTvCount ; i++){
						TextView inputTv = (TextView) inputLayout.getChildAt(i);
						String text = (String) inputTv.getText();
						if(text == null || text.equals("")){
							inputTv.setText(word);
							inputTv.setTag(position);
							result.insert(i, word);
							if(result.length() == intputTvCount){
								if(isAnswerCorrect()){
									currentQuestionNo++;
									if(hasPassedQNum < currentQuestionNo){
										myApp.playCoinSound();
										myApp.saveCurrentQuestionNo(currentQuestionNo);
										iconNo = iconNo + 10;
										myApp.saveIconNo(iconNo);
										dialogRewardIcon.setText("奖励：10");
									}else{
										dialogRewardIcon.setVisibility(View.INVISIBLE);
									}
									questionNoTv.setText(currentQuestionNo+"");
									iconNoTv.setText(iconNo + "");
									answerRightdialog.show();
									return;
								}else{
									for(int j = 0 ; j < intputTvCount ; j++){
										textColorChange(j);
									}
									colorAnimation.setRepeatCount(2);
									colorAnimation.setDuration(500);
									colorAnimation.start();
								}
							}
							break;
						}
					}
					index++;
				}
			}
		});
		setQuestionUi(currentQuestionNo);
		
		return v;
	}
	public void setImageSectionHeightWidth(View v) {
		pictrue = (ImageView) v.findViewById(R.id.pictrue);
		RelativeLayout.LayoutParams rLayoutParams = (RelativeLayout.LayoutParams) pictrue.getLayoutParams();
		rLayoutParams.height = (int) (windowWidth / 10.0f * 6);
		rLayoutParams.width = (int) (windowWidth / 10.0f * 6);
		rLayoutParams.setMargins(0, 30, 0, 0);
		pictrue.setLayoutParams(rLayoutParams);
		Button share = (Button) v.findViewById(R.id.share);
		Button clear = (Button) v.findViewById(R.id.clearOne);
		Button prompt = (Button) v.findViewById(R.id.prompt);
		
		int imageBtnSize = (int) (windowWidth / 10.0f * 1.5);
		
		share.getLayoutParams().height = imageBtnSize;
		share.getLayoutParams().width = imageBtnSize;
		clear.getLayoutParams().height = imageBtnSize;
		clear.getLayoutParams().width = imageBtnSize;
		prompt.getLayoutParams().height = imageBtnSize;
		prompt.getLayoutParams().width = imageBtnSize;
	}
	
	
	//更新界面与题目相关，清空StringBuffer
	private void setQuestionUi(int no) {
		result.delete(0, result.length());
		inputLayout.removeAllViews();
		index = 0;
		setImage(no);
		
		currentQuestion = QuestionFactory.getQuestion(no);
		String answer = currentQuestion.getAnswer();
		intputTvCount = answer.length();
		initInputTextView();
		
		String confuseString = currentQuestion.getConfuseString();
		char [] data = confuseString.toCharArray();
		adapter.setData(data);
		gridView.setAdapter(adapter);
		gridView.startLayoutAnimation();
		adapter.notifyDataSetChanged();
		
	}
	
	//设置题目相关（与题号，相关）图片
	private void setImage(int no){
		String pictrueName = decimalFormat.format(no);
		try {
			BitmapDrawable drawable = new BitmapDrawable(getResources(),astManager.open("image/"+pictrueName));
			pictrue.setImageDrawable(drawable);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isAnswerCorrect(){
		String rightAnswer = currentQuestion.getAnswer();
		if(rightAnswer.equals(result.toString())){
			return true;
		}
		return false;
		
	}
	
	public void initInputTextView() {
		int btnSize = (int)(windowWidth /10.0f);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btnSize,btnSize);
		params.setMargins(5, 0, 5, 0);
		for(int i = 0 ; i < intputTvCount ; i++){
			TextView inputTv = new TextView(getActivity());
			inputTv.setLayoutParams(params);
			inputTv.setBackgroundResource(R.drawable.btn_word_02);
			inputTv.setGravity(Gravity.CENTER);
			inputTv.setTextSize(25);
			inputTv.setTextColor(Color.WHITE);
			inputLayout.addView(inputTv,i);
			
			inputTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					myApp.playEnterSound();
					String word = (String) ((TextView)v).getText();
					if(word != null && !word.equals("")){
						result.deleteCharAt(result.indexOf(word));
						int position = (Integer) v.getTag();
						View view = gridView.getChildAt(position);
						view.setVisibility(View.VISIBLE);
						((TextView)v).setText(null);
						index--;
					}
				}
			});
		}
	}
	

	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.addGold:
			myApp.playEnterSound();
			
			break;
		case R.id.back:
			myApp.playCancelSound();
			getFragmentManager().popBackStack();
			break;
		case R.id.button1:
			myApp.playEnterSound();
			answerRightdialog.dismiss();
			break;
		case R.id.button2:
			myApp.playEnterSound();
			setQuestionUi(currentQuestionNo);
			answerRightdialog.dismiss();
			break;
		}
	}
	public class TextViewAdapter extends BaseAdapter{
		private char [] arr;
		private LayoutInflater inflater;
		public void setData(char [] arr){
			this.arr = arr;
		}
		public TextViewAdapter(){
			inflater = LayoutInflater.from(getActivity());
		}
		@Override
		public int getCount() {
			return arr.length;
		}

		@Override
		public Object getItem(int arg0) {
			return arr[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup group) {
			if(convertView == null){
				convertView = new TextView(getActivity());
			}
			int btnSize = (int)(windowWidth /8.0f);
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(btnSize,btnSize);
			convertView.setLayoutParams(lp);
			convertView.setBackgroundResource(R.drawable.gridview_item);
			((TextView)convertView).setText(arr[position]+"");
			((TextView)convertView).setGravity(Gravity.CENTER);
			((TextView)convertView).setTextSize(30);
			((TextView)convertView).setTextColor(Color.BLACK);
			return convertView;
		}
	}
	private void textColorChange(final int i){
		colorAnimation.addUpdateListener(new AnimatorUpdateListener() {
		    @Override
		    public void onAnimationUpdate(ValueAnimator animator) {
		    	((TextView)inputLayout.getChildAt(i)).setTextColor((Integer)animator.getAnimatedValue());
		    }
		});
	}
}
