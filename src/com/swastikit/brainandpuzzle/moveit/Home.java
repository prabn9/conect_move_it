package com.swastikit.brainandpuzzle.moveit;

import com.swastikit.brainandpuzzle.moveit.MoveitView.GameMode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.widget.Toast;

public class Home {

	Paint paint;
	MoveitView view;
	Context context;
	Resources resources;
	int screenWidth, screenHeight;

	Rect playRec, helpRec, resetRec;
	MyButton mbBack;
	String menuName;
	int nemHeight;
	public Home(int sw, int sh, MoveitView view) {

		this.view = view;

		screenWidth = sw;
		screenHeight = sh;
		
		nemHeight = (this.screenHeight - this.screenWidth) / 2;

		init();

	}

	int touchIndex = 0;

	public void init() {

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		context = view.context;
		resources = context.getResources();

		setRectangleValues();
		
		mbBack = new MyButton(new Rect(screenWidth / 40, nemHeight
				- screenWidth * 3 / 20, screenWidth / 40 + screenWidth / 10,
				nemHeight - screenWidth / 20));
		mbBack.load(resources, new int[] {
				R.drawable.buttonprevious0, R.drawable.buttonprevious0 });

	}

	int textWidth;
	int adjus;

	public void setRectangleValues() {

		adjus = (int) (screenWidth * 13 / 30);

		// play
		playRec = new Rect();

		adjus = adjus + screenWidth / 5;
		paint.setTextSize(screenWidth / 15);

		menuName = resources.getString(R.string.play);
		textWidth = (int) paint.measureText(menuName);

		playRec.set(screenWidth / 2 - textWidth / 2, adjus - screenWidth / 30,
				(screenWidth / 2) + (textWidth / 2), adjus + (screenWidth / 20));

		// help
		helpRec = new Rect();
		adjus = adjus + (screenWidth / 10);
		menuName = resources.getString(R.string.Help);
		textWidth = (int) paint.measureText(menuName);
		helpRec.set((screenWidth / 2) - (textWidth / 2), adjus,
				(screenWidth / 2) + (textWidth / 2), adjus + (screenWidth / 20));

		// reset
		resetRec = new Rect();
		adjus = adjus + (screenWidth / 10);
		menuName = resources.getString(R.string.Reset_progress);
		textWidth = (int) paint.measureText(menuName);
		resetRec.set((screenWidth / 2) - (textWidth / 2), adjus,
				(screenWidth / 2) + (textWidth / 2), adjus + (screenWidth / 10));

	}

	public void draw(Canvas canvas) {

		mbBack.draw(canvas);
		String subStr;

		paint.setTextSize(screenWidth / 8);
		adjus = (screenWidth * 13) / 30;
		menuName = context.getResources().getString(R.string.Moveit);
		textWidth = (int) paint.measureText(menuName);

		for (int i = 0; i < menuName.length(); i++) {
			subStr = menuName.substring(i, i + 1);
			if (subStr.equals(" ")) {
				continue;
			}
			paint.setColor(ColorList.getColor(i + 1, 0));
			int a = (int) paint
					.measureText((String) menuName.subSequence(0, i));
			canvas.drawText(subStr, (screenWidth / 2) - (textWidth / 2) + a,
					adjus, paint);
		}
		
		if (touchIndex == 1) {
			paint.setColor(Color.RED);
			
		} else {
			paint.setColor(Color.WHITE);
		}

		paint.setTextSize(screenWidth / 15);
		menuName = context.getResources().getString(R.string.play);
		canvas.drawText(menuName, playRec.left, playRec.bottom, paint);
		if (touchIndex == 2) {
			paint.setColor(Color.RED);
		} else {
			paint.setColor(Color.WHITE);
		}

		menuName = context.getResources().getString(R.string.Help);
		canvas.drawText(menuName, helpRec.left, helpRec.bottom, paint);
		if (touchIndex == 3) {
			paint.setColor(Color.RED);
		} else {
			paint.setColor(Color.WHITE);
		}

		menuName = context.getResources().getString(R.string.Reset_progress);
		canvas.drawText(menuName, resetRec.left, resetRec.bottom, paint);
	}

	int xDown = 0, yDown = 0;

	public void touch(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			xDown = (int) event.getX();
			yDown = (int) event.getY();
			if (mbBack.checkIn(xDown, yDown)) {
				if (view.mediaPlayer.isSound) {
					view.mediaPlayer.checkSound();
					view.mediaPlayer.soundTouch.start();
					view.invalidate();
				}
			}else{
				checkIn(xDown, yDown, "down");
			}
			break;
	
		case MotionEvent.ACTION_UP:

			touchIndex = 0;
			if (mbBack.checkIn(xDown, yDown)
					&& mbBack.checkIn((int) event.getX(), (int) event.getY())) {
				mbBack.inTouch = false;
				view.backButton();
			} else{
			
				checkIn((int) event.getX(), (int) event.getY(), "up");
			}
			
			break;
	

		case MotionEvent.ACTION_MOVE:
			break;
		default:
			break;
		}

	}

	public void checkIn(int x, int y, String action) {

		if (playRec.contains(x, y)) {

			if (action.equals("down")) {

				if (view.mediaPlayer.isSound) {
					view.mediaPlayer.checkSound();
					view.mediaPlayer.soundTouch.start();
				}

				touchIndex = 1;

			} else if (action.equals("up")) {
				
				view.gameMode = GameMode.SELECTPACK;

			}
		} else if (helpRec.contains(x, y)) {

			if (action.equals("down")) {

				if (view.mediaPlayer.isSound) {
					view.mediaPlayer.checkSound();
					view.mediaPlayer.soundTouch.start();
				}
				touchIndex = 2;
			} else if (action.equals("up")) {
				view.gameMode = GameMode.HELP;
			}
		} else if (resetRec.contains(x, y)) {
			if (action.equals("down")) {

				if (view.mediaPlayer.isSound) {
					view.mediaPlayer.checkSound();
					view.mediaPlayer.soundTouch.start();
				}
				touchIndex = 3;
			} else if (action.equals("up")) {
				showResetDialog();
			}

		}
	}
	
	public void showResetDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(view.context)
				.setMessage(view.context.getString(R.string.Reset_setting_line))
				.setPositiveButton(view.context.getString(R.string.Yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								Editor saver = view.context
										.getSharedPreferences(view.context.getResources().getString(R.string.Moveit), 0)
										.edit();
								saver.clear();
								saver.commit();
								Toast toast = Toast.makeText(
										view.context,
										view.context
												.getString(R.string.All_progess_resetted),
										Toast.LENGTH_SHORT);
								toast.show();
							}
						})
				.setNegativeButton(view.context.getString(R.string.Cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Nothing
							}
						});
		builder.create();
		builder.show();
	}
}
