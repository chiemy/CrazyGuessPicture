package com.chiemy.crazyguesspictrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	private RendererThread renderer;
	private float distance;
	private List<Map<String,Float>> pointCoordinate;
	private int passStoneHeight;
	private int passStoneWidth;
	private int screenWidth;
	private int screenHeight;
	private Bitmap backBm;
	private Bitmap passStone;
	private Bitmap pressPassStone;
	private Bitmap mountain;
	private Bitmap honey;
	private Bitmap cloud;
	private Bitmap play;
	private Bitmap pressPlay;
	private Path path;
	private List<PointF> passStoneCoordinate;
	private float [] linePath;
	private int pointNo;
	//按下的位置
	private int currentPressIndex = -1;
	//已经按下的位置
	private int prePressIndex = -1;
	//按下开始按钮后默认传递的关数，即上次玩到的关数
	private int translateIndex;
	private boolean showMaxIndexBg = true;
	private boolean showPressIndexBg;
	private int maxIndex;
	//是否点击了管卡
	private boolean isPressStone;
	private boolean showStart = true;
	//是否点击了开始游戏按钮
	private boolean start;
	//起始偏移量，距离屏幕左侧和底部
	float xOffset;
	float yOffset;
	//行数
	int rowNo;
	//列数
	int columnsNo;
	//行间隔,列间隔
	float rowGap;
	float columnsGap;
	float maxHeight;
	
	private MyApp myApp;
	public void setApp(MyApp app){
		myApp = app;
	}
	
	public MySurfaceView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}
	
	public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setCurrentIndex(int index){
		translateIndex = index;
		maxIndex = index;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		backBm = Bitmap.createBitmap(480,800,Config.ARGB_8888);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		passStone = BitmapFactory.decodeResource(getResources(), R.drawable.guanka_stone).copy(Bitmap.Config.ARGB_8888, true);
		passStoneHeight = passStone.getHeight();
		passStoneWidth = passStone.getWidth();
		
		pressPassStone = BitmapFactory.decodeResource(getResources(), R.drawable.guanka_stone02).copy(Bitmap.Config.ARGB_8888, true);
		mountain = BitmapFactory.decodeResource(getResources(), R.drawable.moutain,options).copy(Bitmap.Config.ARGB_8888, true);
		honey  = BitmapFactory.decodeResource(getResources(), R.drawable.honey).copy(Bitmap.Config.ARGB_8888, true);
		cloud = BitmapFactory.decodeResource(getResources(), R.drawable.cloud).copy(Bitmap.Config.ARGB_8888, true);
		play = BitmapFactory.decodeResource(getResources(), R.drawable.startgame01).copy(Bitmap.Config.ARGB_8888, true);
		pressPlay = BitmapFactory.decodeResource(getResources(), R.drawable.startgame02).copy(Bitmap.Config.ARGB_8888, true);
		
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight = getResources().getDisplayMetrics().heightPixels;
		
		pointCoordinate = new ArrayList<Map<String,Float>>();
		//起始偏移量，距离屏幕左侧和底部
		xOffset = 40;
		yOffset = 200;
		//行数
		rowNo = 17;
		//列数
		columnsNo = 6;
		//行间隔,列间隔
		rowGap = 100;
		
		maxHeight = (rowNo-7)*rowGap;
		
		float columnsGap = (screenWidth - 2*xOffset)/(columnsNo-1);
		for(int i = 0 ; i < rowNo ; i++){
			for(int j = 0 ; j < columnsNo ; j++){
				Map<String,Float> map = new HashMap<String,Float>();
				map.put("xCoordinate", j*columnsGap+xOffset);
				map.put("yCoordinate",screenHeight - i*rowGap - yOffset);
				pointCoordinate.add(map);
			}
		}
		//path = new Path();
		//路径点
		//initPath();
		pointNo = 32;
		linePath = new float[(pointNo-1)*4];
		
		
		passStoneCoordinate = new ArrayList<PointF>();
		drawBitmapAtPoint(0);
		drawBitmapAtPoint(1);
		drawBitmapAtPoint(2);
		drawBitmapAtPoint(9);
		drawBitmapAtPoint(10);
		drawBitmapAtPoint(11);
		drawBitmapAtPoint(17);
		drawBitmapAtPoint(16);
		drawBitmapAtPoint(15);
		drawBitmapAtPoint(14);
		drawBitmapAtPoint(13);
		drawBitmapAtPoint(12);
		drawBitmapAtPoint(18);
		drawBitmapAtPoint(19);
		drawBitmapAtPoint(20);
		drawBitmapAtPoint(21);
		drawBitmapAtPoint(22);
		drawBitmapAtPoint(23);
		drawBitmapAtPoint(29);
		drawBitmapAtPoint(35);
		drawBitmapAtPoint(34);
		drawBitmapAtPoint(33);
		drawBitmapAtPoint(26);
		drawBitmapAtPoint(25);
		drawBitmapAtPoint(24);
		drawBitmapAtPoint(30);
		drawBitmapAtPoint(31);
		drawBitmapAtPoint(38);
		drawBitmapAtPoint(39);
		drawBitmapAtPoint(40);
		drawBitmapAtPoint(41);
		drawBitmapAtPoint(47);
		drawBitmapAtPoint(53);
		drawBitmapAtPoint(52);
		drawBitmapAtPoint(51);
		drawBitmapAtPoint(50);
		drawBitmapAtPoint(43);
		drawBitmapAtPoint(48);
		drawBitmapAtPoint(54);
		drawBitmapAtPoint(55);
		drawBitmapAtPoint(56);
		drawBitmapAtPoint(57);
		drawBitmapAtPoint(58);
		drawBitmapAtPoint(59);
		drawBitmapAtPoint(65);
		drawBitmapAtPoint(70);
		drawBitmapAtPoint(69);
		drawBitmapAtPoint(68);
		drawBitmapAtPoint(61);
		drawBitmapAtPoint(60);
		drawBitmapAtPoint(66);
		drawBitmapAtPoint(72);
		drawBitmapAtPoint(73);
		drawBitmapAtPoint(80);
		drawBitmapAtPoint(87);
		drawBitmapAtPoint(94);
		drawBitmapAtPoint(95);
		drawBitmapAtPoint(101);
		
		
		renderer = new RendererThread(holder);
		renderer.start();
		
	}
	
	boolean isFirstPoint = true;
	public void initPath(){
		toPoint(0, 2);
		toPoint(2, 9);
		toPoint(9, 11);
		toPoint(11, 17);
		toPoint(17, 12);
		toPoint(12, 18);
		toPoint(18, 23);
		toPoint(23, 35);
		toPoint(35, 33);
		toPoint(33, 26);
		toPoint(26, 24);
		toPoint(24, 30);
		toPoint(30, 31);
		toPoint(31, 38);
		toPoint(38, 41);
		toPoint(41, 53);
		toPoint(53, 50);
		toPoint(50, 43);
		toPoint(43, 48);
		toPoint(48, 54);
		toPoint(54, 59);
		toPoint(59, 65);
		toPoint(65, 70);
		toPoint(70, 68);
		toPoint(68, 61);
		toPoint(61, 60);
		toPoint(60, 72);
		toPoint(72, 73);
		toPoint(73, 94);
		toPoint(94, 95);
		toPoint(95, 101);
		index = 0;
	}
	/*public void initPath(){
		toPoint(0);
		toPoint(2);
		toPoint(9);
		toPoint(11);
		toPoint(17);
		toPoint(12);
		toPoint(18);
		toPoint(23);
		toPoint(35);
		toPoint(33);
		toPoint(26);
		toPoint(24);
		toPoint(30);
		toPoint(31);
		toPoint(38);
		toPoint(41);
		toPoint(53);
		toPoint(50);
	}*/
	int index;
	public void toPoint(int firstPoint,int secondPoint){
		
		Map<String,Float> map = pointCoordinate.get(firstPoint);
		Map<String,Float> map2 = pointCoordinate.get(secondPoint);
		linePath[index++] = map.get("xCoordinate");
		linePath[index++] = map.get("yCoordinate")+distance;
		linePath[index++] = map2.get("xCoordinate");
		linePath[index++] = map2.get("yCoordinate")+distance;

		/*if(isFirstPoint){
			path.moveTo(map.get("xCoordinate"),map.get("yCoordinate")+distance);
			isFirstPoint = false;
		}else{
			path.lineTo(map.get("xCoordinate"),map.get("yCoordinate")+distance);
		}*/
		
	}
	
	public void drawBitmapAtPoint(int index){
		Map<String,Float> map = pointCoordinate.get(index);
		PointF point = new PointF(map.get("xCoordinate"),map.get("yCoordinate"));
		passStoneCoordinate.add(point);
 	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		renderer.shutDown();
	}
	
	int radius;
	int radius2;
	int alpha;
	public class RendererThread extends Thread{
		private SurfaceHolder mHolder;
		private boolean running;
		private Paint linePaint;
		private Paint textPaint;
		private Paint branchLinePaint;
		private Paint circlePaint;
		private Paint circlePaint2;
		
		public RendererThread(SurfaceHolder holder){
			mHolder = holder;
			running = true;
			linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			linePaint.setColor(0xffEEEE00);
			linePaint.setStrokeWidth(15);
			//linePaint.setStyle(Paint.Style.STROKE);
			
			branchLinePaint =new Paint(Paint.ANTI_ALIAS_FLAG);
			branchLinePaint.setColor(0xff63B8FF);
			branchLinePaint.setStrokeWidth(15);
			//branchLinePaint.setStyle(Paint.Style.STROKE);
			
			textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			textPaint.setColor(Color.WHITE);
			textPaint.setTextSize(20);
			textPaint.setStrokeWidth(5);
			
			circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			circlePaint.setColor(Color.RED);
			circlePaint.setStrokeWidth(12);
			circlePaint.setStyle(Paint.Style.STROKE);
			radius = 0;
			radius2 = 0;
			alpha = 150;
			
			circlePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
			circlePaint2.setColor(Color.GREEN);
			circlePaint2.setStrokeWidth(10);
			circlePaint2.setStyle(Paint.Style.STROKE);
			circlePaint2.setAlpha(50);
			
			Timer t = new Timer();
			t.schedule(new MyTimerTask(t), 0,50);
			
		} 	 	
		@Override
		public void run() {
			Canvas canvas1 = new Canvas(backBm);
			canvas1.drawColor(0xff6C7B8B);
			PointF point1 = passStoneCoordinate.get(11);
			PointF point2 = passStoneCoordinate.get(11);
			PointF point3 = passStoneCoordinate.get(51);
			//分支1
			Map<String,Float> map1 = pointCoordinate.get(28);
			Map<String,Float> map2 = pointCoordinate.get(29);
			//开始按钮
			Map<String,Float> map3 = pointCoordinate.get(19);
			
			while(running){
				circlePaint.setAlpha(alpha);
				
				Canvas canvas = mHolder.lockCanvas();
				if(backBm != null && mountain != null && honey!= null){
					canvas.drawBitmap(backBm, 0, 0, null);
					canvas.drawBitmap(mountain,point1.x+xOffset,point1.y+100+distance, null);
					canvas.drawBitmap(honey,point2.x,point2.y+20+distance, null);
				}
				canvas.drawLine(map1.get("xCoordinate"), 
						map1.get("yCoordinate")+distance,
						map2.get("xCoordinate"), 
						map2.get("yCoordinate")+distance, branchLinePaint);
				initPath();
				//分支
				canvas.drawLines(linePath, linePaint);
				//画关卡
				for(int i = 0 ; i < passStoneCoordinate.size() ; i++){
					PointF point = passStoneCoordinate.get(i);
					if(i == maxIndex-1){
						//玩到的最高关卡后的红点
						canvas.drawCircle(point.x, point.y+distance, radius, circlePaint);
						canvas.drawCircle(point.x, point.y+distance, radius2, circlePaint);
					}
					if(i == prePressIndex && i != maxIndex-1){
						canvas.drawCircle(point.x, point.y+distance, 30, circlePaint2);
					}
					if(i == currentPressIndex){
						canvas.drawBitmap(pressPassStone, point.x -passStoneWidth/2 , point.y-passStoneHeight/2+distance, null);
					}else{
						canvas.drawBitmap(passStone, point.x -passStoneWidth/2 , point.y-passStoneHeight/2+distance, null);
					}
					
					canvas.drawText(i+1 +"", point.x-passStoneWidth/4, point.y+passStoneHeight/4+distance, textPaint);
				}
				canvas.drawBitmap(cloud,point3.x-20,point3.y- 5*rowGap +distance, null);
				if(isPressStone||showStart){
					//如果为点击了某关卡，显示准备开始按钮
					canvas.drawBitmap(play,map3.get("xCoordinate"),map3.get("yCoordinate"),null);
					if(start){
						canvas.drawBitmap(pressPlay,map3.get("xCoordinate"),map3.get("yCoordinate"),null);
					}
				}
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
		public void shutDown(){
			running = false;
		}
	}
	
	public class MyTimerTask extends TimerTask{
		private Timer t;
		public MyTimerTask(Timer t){
			this.t = t;
		}
		@Override
		public void run() {
			if(radius <= 49){
				radius++;
				if(radius >= 15){
					radius2++;
				}
				alpha -= 2;
			}else{
				radius = 0;
				radius2 = 0;
				alpha = 150;
			}
		}
	}
	
	float newY = 0;
	float oldDistance;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			distance = oldDistance + event.getY() - newY;
			if(distance < 0){
				distance = 0;
			}else if(distance >= maxHeight){
				distance = maxHeight;
			}
		}else if(event.getAction() == MotionEvent.ACTION_DOWN){
			newY = event.getY();
			float touchXCoordinate = event.getX();
			float touchYCoordinate = event.getY();
			if(showStart){
				Map<String,Float> map = pointCoordinate.get(19);
				float rangeX = map.get("xCoordinate") + play.getWidth()/2;
				float rangeY = map.get("yCoordinate") + play.getHeight();
				if(touchXCoordinate > map.get("xCoordinate") &&
						touchXCoordinate < rangeX && 
						touchYCoordinate > map.get("yCoordinate")&&
						touchYCoordinate < rangeY){
					start = true;
				}else{
					showStart = false;
					start = false;
				}
			} 
			if(passStoneCoordinate != null && !passStoneCoordinate.isEmpty()){
				for(int i = 0 ; i < passStoneCoordinate.size() ; i++){
					PointF point = passStoneCoordinate.get(i);
					float xCoordinate = point.x -passStoneWidth/2;
					float yCoordinate = point.y-passStoneHeight/2+distance;
					float xRange = xCoordinate + passStoneWidth;
					float yRange = yCoordinate + passStoneHeight;
					if(touchXCoordinate > xCoordinate && 
							touchXCoordinate < xRange &&
							touchYCoordinate > yCoordinate && touchYCoordinate < yRange){
						
						if(i+1 <= maxIndex){
							//准备传递
							translateIndex = i+1;
							//为实现点击效果
							currentPressIndex = i;
							prePressIndex = i;
						}
						break;
					}
				}
			}
			
			
			
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			oldDistance = distance;
			float touchXCoordinate = event.getX();
			float touchYCoordinate = event.getY();
			//此判断说明，不是在滑动
			if(Math.abs(event.getY() - newY) < passStoneHeight){
				if(passStoneCoordinate != null && !passStoneCoordinate.isEmpty()){
					for(int i = 0 ; i < passStoneCoordinate.size() ; i++){
						PointF point = passStoneCoordinate.get(i);
						float xCoordinate = point.x -passStoneWidth/2;
						float yCoordinate = point.y-passStoneHeight/2+distance;
						float xRange = xCoordinate + passStoneWidth;
						float yRange = yCoordinate + passStoneHeight;
						if(touchXCoordinate > xCoordinate && 
								touchXCoordinate < xRange &&
								touchYCoordinate > yCoordinate && 
								touchYCoordinate < yRange){
							//点击玩的最高关卡之前的关卡，才显示start按钮
							if(i+1 <= maxIndex){
								if(!showStart){
									showStart = true;
									myApp.playEnterSound();
									break;
								}
							}
						}
					}
				}
				if(start){
					myApp.playEnterSound();
					Message msg = handler.obtainMessage();
					msg.arg1 = translateIndex;
					handler.sendMessage(msg);
				}
			}else{
				start = false;
				showStart = false;
			}
			currentPressIndex=-1;
		}
		
		return true;
	}
	private Handler handler;
	public void setOnStartListener(final OnStartListener listener){
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				renderer.shutDown();
				listener.setIndex(msg.arg1);
			}
		};
	}
	public interface OnStartListener{
		public void setIndex(int index);
	}
	
}
