package com.swastikit.brainandpuzzle.moveit;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MoveitView extends View {

	public enum GameMode {
		HOME, PLAY, SETTING, ABOUT, SELECTLEVEL, SELECTPACK, HELP
	}

	public GameMode gameMode;

	public int screenWidth, screenHeight;
	int adjus;

	Home home;
	Play play;
	SelectLevel selectLevel;
	SelectPack selectPack;

	ButtonAction buttonAction;
	Board board;
	Context context;

	PlayerMediaPlayer mediaPlayer;
	
	int pack = 1;
	int currentLevel = 1;
	int maxLevel = 150;
	
	// cache data
	SharedPreferences savedState;
	SharedPreferences.Editor editor;
	
	int	totalHintsLeft;

	public MoveitView(Context context) {
		super(context);

		this.context = context;
		
		init();
	}

	public MoveitView(Context context, AttributeSet aSet) {
		super(context, aSet);

		this.context = context;
		init();
	}

	public MoveitView(Context context, AttributeSet aSet, int defStyle) {
		super(context, aSet, defStyle);

		this.context = context;
	
		init();
	}
	
	public void getScreenResources() {
		screenWidth = this.getResources().getDisplayMetrics().widthPixels;
		screenHeight = this.getResources().getDisplayMetrics().heightPixels;
	}

	public void init() {
		getScreenResources();
		
		savedState = context.getApplicationContext().getSharedPreferences(this.context.getResources().getString(R.string.Moveit), 0);

		editor = savedState.edit();

		
		
		adjus = (screenHeight-screenWidth)/2;
		
		gameMode = GameMode.HOME;
		
		home = new Home(screenWidth, screenHeight, this);
		buttonAction = new ButtonAction(screenWidth, screenHeight, this);
		board = new Board(screenWidth, screenHeight, this);
		
		play = new Play(screenWidth, screenHeight, board, this);
		selectPack = new SelectPack(screenWidth, screenHeight, this);
		selectPack.load(context.getResources());
		selectLevel = new SelectLevel(screenWidth, screenHeight, this);
		selectLevel.load(context.getResources(),context);
	
		mediaPlayer = new PlayerMediaPlayer(context);
		
		new ThreadSlideLevel().start();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		
	
		canvas.drawColor(Color.BLACK);
		
		switch(gameMode){
		case HOME:
			home.draw(canvas);
			invalidate();
			break;
			
		case SELECTPACK:
			selectPack.draw(canvas);
			invalidate();
			break;
			
		case SELECTLEVEL:
			selectLevel.draw(canvas);
			invalidate();
			break;
			
		case PLAY:
			board.draw(canvas);
			play.draw(canvas);
			buttonAction.draw(canvas);
			
			invalidate();
			break;
		default:
			break;
	
		}
	}
	
	@Override
	public synchronized boolean onTouchEvent(MotionEvent event){
		
		postInvalidate();
		
		switch (gameMode) {
		case HOME:
			home.touch(event);
			break;

		case PLAY:
			
			buttonAction.touch(event);
			play.touch(event);
			break;
			
		case HELP:
			break;
			
		case SETTING:
			break;
			
		case SELECTPACK:
			
			switch(selectPack.touch(event)){
			case 0:
				break;
			
			case 1:
				postInvalidate();
				break;
				
			case 2:
				backButton();
				break;
			
			case 3:
				selectLevel.setPack(selectPack.pack);
				this.pack = selectPack.pack+1;
				gameMode = GameMode.SELECTLEVEL;
				postInvalidate();
				break;
			}
			
			break;
			
		case SELECTLEVEL:
			switch (selectLevel.touch(event)) {
			case 0:
				break;

			case 1:
				postInvalidate();
				break;
			
			case 2:
				backButton();
				break;
				
			case 3:

				if (mediaPlayer.isSound) {
					mediaPlayer.checkSound();
					mediaPlayer.soundTouch.start();
				}
				gameMode = GameMode.PLAY;
				currentLevel = selectLevel.level;
				board.reset(currentLevel);
				play.init();
				postInvalidate();
				break;
	
			}

		}
		
		
		return true;
		
	}
	
	public void backButton(){
		switch(gameMode){
		case HOME:
			System.gc();
			((MoveitActivity)getContext()).finish();
			break;
			
		case SELECTPACK:
			gameMode = GameMode.HOME;
			postInvalidate();
			break;
			
		case SELECTLEVEL:
			gameMode = GameMode.SELECTPACK;
			postInvalidate();
			break;
			
		case PLAY:
			gameMode = GameMode.SELECTLEVEL;
			selectLevel.loadStatusMove();
			postInvalidate();
			break;
		}
		
	}
	
	class ThreadSlideLevel extends Thread {

		public void run() {
			try {
				while (true) {
					if (gameMode == GameMode.SELECTLEVEL) {
						if (selectLevel.update()) {
							postInvalidate();
						}
					}
					sleep(3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	

}
