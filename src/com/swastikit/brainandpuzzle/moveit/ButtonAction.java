package com.swastikit.brainandpuzzle.moveit;


import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.Toast;

public class ButtonAction {
	MyButton mbBack, mbNext, mbPrevious, mbRestart, mbHint, mbUndo, mbRedo;
	int width;
	int height;
	int Nem;
	Context context;

	MoveitView view;

	boolean pressed = false;

	ButtonAction(int sW, int sH, MoveitView view) {
		context = view.getContext();
		this.view = view;
		width = sW;
		height = sH;
		Nem = (height - width) / 2 -(width/35);
		init();
	}

	public void init() {
		load_image_buttons();
		
		enableAll();
		if(view.savedState.getInt("totalHintsLeft", 10) == 0){
			mbHint.disable();
		}
		
		
	}

	public void draw(Canvas canvas) {
		draw_image_buttons(canvas);
	}

	public void disableAll() {
	
		mbNext.disable();
		mbPrevious.disable();
		mbBack.disable();
		mbRestart.disable();
		mbHint.disable();
		mbUndo.disable();
		mbRedo.disable();
		

	}

	public void enableAll() {

		mbRestart.enable();

		mbBack.enable();

		if (view.maxLevel != view.currentLevel) {
			mbNext.enable();
		}

		if (view.currentLevel != 1) {
			mbPrevious.enable();
		}
		
		mbHint.enable();

	}

	public void button_click_down(int x, int y) {
		
		mbHint.checkIn(x, y);
	
		mbNext.checkIn(x, y);
		mbPrevious.checkIn(x, y);
		
		mbBack.checkIn(x, y);
		mbRestart.checkIn(x, y);
		mbUndo.checkIn(x, y);
		mbRedo.checkIn(x, y);
	}

	@SuppressLint("WorldReadableFiles")
	public void button_click_up(int x, int y) {
		if (mbHint.inTouch && mbHint.checkIn(x, y) && !mbHint.disable) {
			mbHint.inTouch = false;
			
			if (view.mediaPlayer.isSound) {
				view.mediaPlayer.checkSound();
				view.mediaPlayer.soundTouch.start();
			}
			
			showConfirmation();
			
			view.editor.putInt("totalHintsLeft", view.totalHintsLeft);
			view.editor.commit();
						
			if (view.totalHintsLeft == 0) {

				mbHint.disable();
				CharSequence text = view.context.getResources().getString(
						R.string.no_hints_left);

				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(view.context, text, duration);
				toast.show();
				return;

			}

		}

		if (mbBack.inTouch && mbBack.checkIn(x, y) && !mbBack.disable) {
			mbBack.inTouch = false;

			if (view.mediaPlayer.isSound) {
				view.mediaPlayer.checkSound();
				view.mediaPlayer.soundTouch.start();
			}

			view.backButton();
			
		

		}
		
		
		if (mbNext.inTouch && mbNext.checkIn(x, y) && !mbNext.disable) {
			mbNext.inTouch = false;
			if (view.mediaPlayer.isSound) {
				view.mediaPlayer.checkSound();
				view.mediaPlayer.soundTouch.start();
			}

			if (view.currentLevel < view.maxLevel) {
				view.currentLevel++;
				
				view.board.reset(view.currentLevel);
				view.play.init();
				
				if(view.currentLevel == view.maxLevel){
					mbNext.disable();
				}
			}
		

		}
		if (mbPrevious.inTouch && mbPrevious.checkIn(x, y)
				&& !mbPrevious.disable) {
			mbPrevious.inTouch = false;

			if (view.mediaPlayer.isSound) {
				view.mediaPlayer.checkSound();
				view.mediaPlayer.soundTouch.start();
			}

			if (view.currentLevel > 1) {
				view.currentLevel--;
				

				view.board.reset(view.currentLevel);
				view.play.init();
				

				if(view.currentLevel == 1){
					mbPrevious.disable();
				}


			}

		}
		
		if (mbRestart.inTouch && mbRestart.checkIn(x, y) && !mbRestart.disable) {
			mbRestart.inTouch = false;

			if (view.mediaPlayer.isSound) {
				view.mediaPlayer.checkSound();
				view.mediaPlayer.soundTouch.start();
			}
			
			view.board.reset(view.currentLevel);
			view.play.resetPlay();


		}
		if (mbUndo.inTouch && mbUndo.checkIn(x, y) && !mbUndo.disable) {
			mbUndo.inTouch = false;
			
			if (view.mediaPlayer.isSound) {
				view.mediaPlayer.checkSound();
				view.mediaPlayer.soundTouch.start();
			}
			
			view.play.state.popUndoState();
			
		}
		
		if (mbRedo.inTouch && mbRedo.checkIn(x, y) && !mbRedo.disable) {
			mbRedo.inTouch = false;

			if (view.mediaPlayer.isSound) {
				view.mediaPlayer.checkSound();
				view.mediaPlayer.soundTouch.start();
			}
			
			view.play.state.popRedoState();

	
		}
	}

	public void gameStart() {

	
		view.invalidate();
	}

	void draw_image_buttons(Canvas c) {
		
		mbPrevious.draw(c);
		mbNext.draw(c);
		mbHint.draw(c);
		mbBack.draw(c);
		mbRestart.draw(c);
		mbUndo.draw(c);
		mbRedo.draw(c);
	}

	void load_image_buttons() {
		mbBack = new MyButton(new Rect(width / 40, Nem - (width / 10) * 2
				, width / 40 + width / 10, Nem - width / 10
				));
		mbUndo = new MyButton(new Rect(width / 2 - width / 40 - (width / 10)
				* 3 - (width / 20) * 2, Nem + width + width / 15, width / 2
				- width / 40 - (width / 10) * 2 - (width / 20) * 2, Nem + width
				+ width / 15 + width / 10));
		mbRedo = new MyButton(new Rect(width / 2 - width / 40 - (width / 10)
				* 2 - (width / 20), Nem + width + width / 15, width / 2 - width
				/ 40 - (width / 10) - (width / 20), Nem + width + width / 15
				+ width / 10));
		mbPrevious = new MyButton(new Rect(width / 2 - width / 40
				- (width / 10), Nem + width + width / 15, width / 2 - width
				/ 40, Nem + width + width / 15 + width / 10));
	
		mbRestart = new MyButton(new Rect(width / 2 + width / 40, Nem + width
					+ width / 15, width / 2 + width / 40 + (width / 10), Nem
					+ width + width / 15 + width / 10));
		
		
		
		mbNext = new MyButton(new Rect(width / 2 + width / 40 + (width / 10)
				+ (width / 20), Nem + width + width / 15, width / 2 + width
				/ 40 + (width / 10) * 2 + (width / 20), Nem + width + width
				/ 15 + width / 10));
		mbHint = new MyButton(new Rect(width / 2 + width / 40 + (width / 10)
				* 2 + (width / 20) * 2, Nem + width + width / 15, width / 2
				+ width / 40 + (width / 10) * 3 + (width / 20) * 2, Nem + width
				+ width / 15 + width / 10));

		mbBack.load(context.getResources(), new int[] {
				R.drawable.buttonprevious0, R.drawable.buttonprevious1 });
		mbHint.load(context.getResources(), new int[] { R.drawable.buttonhint0,
				R.drawable.buttonhint1 });
		mbNext.load(context.getResources(), new int[] { R.drawable.buttonnext0,
				R.drawable.buttonnext1 });
		mbPrevious.load(context.getResources(), new int[] {
				R.drawable.buttonprevious0, R.drawable.buttonprevious1 });
		mbRestart.load(context.getResources(), new int[] {
				R.drawable.buttonrestart0, R.drawable.buttonrestart1 });
		mbUndo.load(context.getResources(), new int[] { R.drawable.buttonundo0,
				R.drawable.buttonundo1 });
		mbRedo.load(context.getResources(), new int[] { R.drawable.buttonredo0,
				R.drawable.buttonredo1 });
	
	}

	public void touch(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			button_click_down((int) event.getX(), (int) event.getY());
			break;

		case MotionEvent.ACTION_MOVE:
			break;

		case MotionEvent.ACTION_UP:
			button_click_up((int) event.getX(), (int) event.getY());
			break;
		}

	}
	
	// view alert box
		public void showConfirmation() {
		

			String dialog = view.context.getString(R.string.need_to_restart_the_game);
			AlertDialog.Builder builder = new AlertDialog.Builder(view.context)
					.setMessage(dialog)
					.setPositiveButton(view.context.getString(R.string.Yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int id) {
									view.play.hint.getHint();
									view.totalHintsLeft--;
								}
							})
					.setNegativeButton(view.context.getString(R.string.Cancel),

					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int id) {

						}
					});

			builder.create();
			builder.show();

		}

}
