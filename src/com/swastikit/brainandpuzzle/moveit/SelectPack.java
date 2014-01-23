package com.swastikit.brainandpuzzle.moveit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;

public class SelectPack {
	Paint paint;
	int width, height;
	int nemWidth, nemHeight, widthBar, heightBar;

	MyButton mbBack;
	Bitmap WhiteBar;
	Resources r;
	int pack;
	String namePk;
	String[] namepk_grid_level;
	MoveitView view;

	SelectPack(int screenWidth, int screenHeight, MoveitView view) {
		this.view = view;
		width = screenWidth;
		height = screenHeight;

		nemWidth = width / 7;
		nemHeight = (height - width) / 2;
		
		widthBar = width * 5 / 7;
		heightBar = width / 7;

		pack = 0;

		relateY = 0;
	}

	FrameButton[] frameButton;
	int[] colorPack;
	String[][] namePack;

	void load(Resources res) {
		r = res;
		mbBack = new MyButton(new Rect(width / 40, nemHeight - width * 3 / 20,
				width / 40 + width / 10, nemHeight - width / 20));
		mbBack.load(res, new int[] { R.drawable.buttonprevious0,
				R.drawable.buttonprevious0 });

		WhiteBar = BitmapFactory.decodeResource(res, R.drawable.whitebar);
		WhiteBar = Bitmap.createScaledBitmap(WhiteBar, widthBar, heightBar,
				true);
		
		namePk = res.getString(R.string.pack);
		namepk_grid_level = res.getStringArray(R.array.grids_block_puzzle);

		namePack = new String[namepk_grid_level.length][2];

		for (int i = 0; i < namepk_grid_level.length; i++) {
			namePack[i][0] = namePk +" " + (i+1);
			namePack[i][1] = namepk_grid_level[i];
		}

		colorPack = new int[] { 0xffADD8E6,0xff00FF00,0xffFBA0E3, 0xffFFF8DC,
				0xff8a2be2, 0xffffff00, 0xffff0000, 0xff008000, 0xff0000ff,
				0xff800080, 0xffF52887, 0xff48D1CC, 0xff8a2be2, 0xffffff00,
				0xffff0000, 0xff008000, 0xff0000ff, 0xff800080, 0xffF52887,
				0xff48D1CC, 0xff00FF00 };
		
		frameButton = new FrameButton[namepk_grid_level.length];
		for (int i = 0; i < namepk_grid_level.length; i++) {
			frameButton[i] = new FrameButton();
			frameButton[i].set(i, nemWidth, nemHeight + heightBar / 5 + i
					* (heightBar + heightBar / 5));
		}
		paint = new Paint();
		paint.setAntiAlias(true);
	}

	int relateY;
	int widthText;

	void draw(Canvas c) {

		for (int i = 0; i < namepk_grid_level.length; i++) {
			frameButton[i].draw(c, relateY);
		}

		paint.setColor(Color.BLACK);
		c.drawRect(0, 0, width, nemHeight, paint);
		c.drawRect(0, nemHeight * 3 / 2 + width, width, height + 30, paint);

		mbBack.draw(c);
		paint.setColor(Color.WHITE);
		paint.setTextSize(width / 15);
		c.drawText(r.getString(R.string.Select_Pack), width * 3 / 20, nemHeight
				- width / 20 - width / 40, paint);
	}

	class FrameButton {
		int id;
		Rect rec;
		int widthText;
		boolean inTouch;
		Paint p = new Paint();

		void set(int i, int x, int y) {
			id = i;
			rec = new Rect(x, y, x + widthBar, y + heightBar);

			p.setAntiAlias(true);
			p.setTextSize(width / 35);
			p.setStyle(Style.STROKE);
			widthText = (int) p.measureText(namePack[id][1]);
			inTouch = false;
		}

		void draw(Canvas c, int relateY) {
			if (inTouch) {
				c.drawBitmap(WhiteBar, rec.left, rec.top + relateY, p);
				p.setColor(Color.BLACK);
				p.setTextSize(width / 20);
				c.drawText(namePack[id][0], nemWidth + width / 30, relateY
						+ rec.top + heightBar / 2, p);

				p.setTextSize(width / 35);
				c.drawText(namePack[id][1], width - nemWidth - width / 40
						- widthText, relateY + rec.top + heightBar * 3 / 4, p);
			} else {
				//draw pack title
				p.setColor(colorPack[id]);
				p.setTextSize(width / 20);
				c.drawText(namePack[id][0], nemWidth + width / 30, relateY
						+ rec.top + heightBar / 2, p);

				//draw text at right 
				p.setColor(Color.WHITE);
				c.drawRect(rec.left, rec.top + relateY, rec.right, rec.bottom
						+ relateY, p);

				p.setTextSize(width / 35);
				c.drawText(namePack[id][1], width - nemWidth - width / 40
						- widthText, relateY + rec.top + heightBar * 3 / 4, p);
			}
		}

		boolean checkIn(int x, int y, int relateY) {
			inTouch = (x >= rec.left && x < rec.right)
					&& (y >= rec.top + relateY && y < rec.bottom + relateY);
			return inTouch;
		}
	}

	boolean canScroll;
	boolean haveDrag;
	int xDown, yDown;
	int xRel, yRel;

	int touch(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xRel = xDown = (int) event.getX();
			yRel = yDown = (int) event.getY();
			canScroll = false;
			haveDrag = false;
			
			if (mbBack.checkIn(xDown, yDown)) {
				if (view.mediaPlayer.isSound) {
					view.mediaPlayer.checkSound();
					view.mediaPlayer.soundTouch.start();
				}
				return 1;
			} else if (yDown >= nemHeight && yDown < nemHeight * 3 / 2 + width) {
				canScroll = true;
				for (int i = 0; i < namepk_grid_level.length; i++) {
					if (frameButton[i].checkIn(xDown, yDown, relateY)) {
						if (view.mediaPlayer.isSound) {
							view.mediaPlayer.checkSound();
							view.mediaPlayer.soundTouch.start();
						}
						break;
					}
				}
			}
			return 0;
		case MotionEvent.ACTION_MOVE:
			int xMove = (int) event.getX();
			int yMove = (int) event.getY();
			if (mbBack.checkIn(xDown, yDown) && mbBack.checkIn(xMove, yMove)) {
				return 1;
			} else if (canScroll && yMove >= nemHeight
					&& yMove < nemHeight * 3 / 2 + width) {
				if (haveDrag|| (xDown - xMove) * (xDown - xMove) + (yDown - yMove)
								* (yDown - yMove) > (width / 10) * (width / 10)) {
					haveDrag = true;
				} else {
					for (int i = 0; i < namepk_grid_level.length; i++) {
						if (frameButton[i].checkIn(xMove, yMove, relateY)) {
							break;
						}
					}
				}

				relateY += (yMove - yRel);
				relateY = (relateY < -(namepk_grid_level.length
						* (heightBar + heightBar / 5) + heightBar / 5 - width - nemHeight / 2)) ? 
								-(namepk_grid_level.length
						* (heightBar + heightBar / 5) + heightBar / 5 - width - nemHeight / 2)
						: relateY;
				relateY = (relateY > 0) ? 0 : relateY;
				yRel = yMove;
				return 1;
			}
			return 0;
		case MotionEvent.ACTION_UP:
			int xUp = (int) event.getX();
			int yUp = (int) event.getY();
			if (mbBack.checkIn(xDown, yDown) && mbBack.checkIn(xUp, yUp)) {
				mbBack.inTouch = false;
				return 2;
			} else {
				for (int i = 0; i < namepk_grid_level.length; i++) {
					frameButton[i].inTouch = false;
				}
				if (canScroll) {
					if (!haveDrag) {
						for (int i = 0; i < namepk_grid_level.length; i++) {
							if (frameButton[i].checkIn(xUp, yUp, relateY)) {
								pack = i;
								frameButton[i].inTouch = false;
								
								return 3;
							}
						}
					}
				}
				return 1;
			}
		}
		return 0;
	}
}
