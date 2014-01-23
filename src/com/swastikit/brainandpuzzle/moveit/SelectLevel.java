package com.swastikit.brainandpuzzle.moveit;





import android.annotation.SuppressLint;

import android.content.Context;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import android.view.MotionEvent;

public class SelectLevel {
	Context context;
	Paint paint;
	int width, height;
	int nemWidth, nemHeight, sizeTile, sizeBorder;
	int pack, level;
	int Page;

	Bitmap levelPass, levelPerfect;

	MyButton mbBack;
	Bitmap bigDot, smallDot;
	
	MoveitView view;

	int[] colorTile;

	SelectLevel(int screenWidth, int screenHeight, MoveitView view) {
		
		this.view = view;
		width = screenWidth;
		height = screenHeight + 50;
		nemWidth = width * 3 / 16;
		nemHeight = (height - width) * 3 / 4;
		sizeBorder = width / 8;
		sizeTile = width / 10;
		Page = 0;
		
	}

	String[][] numPageInPack;
	int[] colorPack;
	String namePack;

	void load(Resources res, Context c) {
		context = c;
		mbBack = new MyButton(new Rect(sizeTile / 4, nemHeight * 2 / 3
				- sizeTile * 3 / 2, sizeTile / 4 + sizeTile, nemHeight * 2 / 3
				- sizeTile / 2));
		mbBack.load(res, new int[] { R.drawable.buttonprevious0,
				R.drawable.buttonprevious0 });

		levelPass = BitmapFactory.decodeResource(res, R.drawable.levelpass);
		levelPass = Bitmap.createScaledBitmap(levelPass, sizeTile, sizeTile,
				true);
		levelPerfect = BitmapFactory.decodeResource(res,
				R.drawable.levelperfect);
		levelPerfect = Bitmap.createScaledBitmap(levelPerfect, sizeTile,
				sizeTile, true);

		bigDot = BitmapFactory.decodeResource(res, R.drawable.circle);
		bigDot = Bitmap.createScaledBitmap(bigDot, sizeTile / 3, sizeTile / 3,
				true);
		smallDot = Bitmap.createScaledBitmap(bigDot, sizeTile / 6,
				sizeTile / 6, true);

		namePack = context.getResources().getString(R.string.pack);
		colorPack = new int[] { 0xff00FF00, 0xffFBA0E3, 0xffADD8E6, 0xffFFF8DC,
				0xff8a2be2, 0xffffff00, 0xffff0000, 0xff008000, 0xff0000ff,
				0xff800080, 0xffff66ff, 0xff48D1CC, 0xffffff00, 0xffff0000, 0xff008000, 0xff0000ff,
				0xff800080, 0xffff66ff, 0xff48D1CC,0xff48D1CC, 0xffffff00, 0xffff0000, 0xff008000, 0xff0000ff,
				0xff800080, 0xffff66ff, 0xff48D1CC};
		
		numPageInPack = new String[][] { 
					{"5","4x4","4x4","4x4","4x4","4x4"},
					{"5","4x5","4x5","4x5","4x5","4x5"},
					{"5","4x6","4x6","4x6","4x6","4x6"},
					{"5","4x7","4x7","4x7","4x7","4x7"},
					{"5","5x4","5x4","5x4","5x4","5x4"},
					{"5","5x5","5x5","5x5","5x5","5x5"},
					{"5","5x6","5x6","5x6","5x6","5x6"},
					{"5","5x7","5x7","5x7","5x7","5x7"},
					{"5","6x4","6x4","6x4","6x4","6x4"},
					{"5","6x5","6x5","6x5","6x5","6x5"},
					{"5","6x6","6x6","6x6","6x6","6x6"},
					{"5","6x7","6x7","6x7","6x7","6x7"},
					{"5","7x4","7x4","7x4","7x4","7x4"},
					{"5","7x5","7x5","7x5","7x5","7x5"},
					{"5","7x6","7x6","7x6","7x6","7x6"},
					{"5","7x7","7x7","7x7","7x7","7x7"},
						};
		colorTile = new int[] { 0xffff0000, 0xff008000, 0xff0000ff, 0xffff8c00,
				0xff8a2be2, 0xffffff00, };
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
	}

	int[] statusMove;

	@SuppressLint("WorldReadableFiles")
	void loadStatusMove() {
		
		statusMove = new int[Integer.parseInt(numPageInPack[pack][0]) * 30];
		for (int i = 0; i < statusMove.length; i++) {
			
			statusMove[i] = view.savedState.getInt("statusMove" + (pack+1) +"_"+ (i+1), 0);
			

		}
	}

	void setPack(int p) {
		pack = p;
		Page = 0;
		relateX = 0;
		loadStatusMove();
	}

	int widthText;

	void draw(Canvas c) {
		
		//back button
		mbBack.draw(c);
		
		//package title
		paint.setColor(colorPack[pack]);
		paint.setTextSize(width / 15);
		c.drawText(namePack +" "+(pack+1), (width/5), nemHeight * 2 / 3
				- sizeTile / 2 - sizeTile / 4, paint);

		//package level lists
		paint.setColor(Color.WHITE);
		for (int k = 0; k < Integer.parseInt(numPageInPack[pack][0]); k++) {
			
			//text above the tiles
			paint.setTextSize(sizeTile * 2 / 3);
			widthText = (int) paint.measureText("(" + (k * 30 + 1) + " - "
					+ (k + 1) * 30 + ")");
			c.drawText("(" + (k * 30 + 1) + " - " + (k + 1) * 30 + ")",
					-relateX + k * 6 * sizeBorder + nemWidth + 5 * sizeBorder
							- widthText, nemHeight - sizeBorder / 3, paint);

			//formatting text inside the tile
			paint.setTextSize(sizeTile / 2);
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 6; j++) {
					widthText = (int) paint.measureText((k * 30 + i + j * 5 + 1) + "");
					
					//draws text column wise
					c.drawText((k * 30 + i + j * 5 + 1) + "", -relateX + k * 6
							* sizeBorder + nemWidth + i * sizeBorder
							+ (sizeBorder - widthText) / 2, nemHeight + j
							* sizeBorder + sizeTile * 3 / 4, paint);
				}
			}
		}

		
		for (int k = 0; k < Integer.parseInt(numPageInPack[pack][0]); k++) {
			paint.setColor(colorTile[k]);
			paint.setTextSize(sizeTile * 2/3);

			//text above the tile eg., 5 X 5
		//	if (numPageInPack[pack][k + 1] != 1) {
//				c.drawText(numPageInPack[pack][k + 1] + "x"
//						+ numPageInPack[pack][k + 1], -relateX + k * 6
//						* sizeBorder + nemWidth, nemHeight - sizeBorder / 3,
//						paint);
	//		}
			
			c.drawText(numPageInPack[pack][k + 1], -relateX + k * 6
					* sizeBorder + nemWidth, nemHeight - sizeBorder / 3,
					paint);
			
			//drawing tiles
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 6; j++) {
					drawRect(c, -relateX + k * 6 * sizeBorder + nemWidth
							+ (sizeBorder - sizeTile) / 2 + i * sizeBorder,
							nemHeight + j * sizeBorder, k * 30 + i + j * 5);
				}
			}
		}

		if (Integer.parseInt(numPageInPack[pack][0]) == 4) {
			c.drawBitmap(smallDot,
					width / 2 - sizeTile * 3 / 2 - smallDot.getWidth() / 2,
					nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2, paint);
			c.drawBitmap(smallDot,
					width / 2 - sizeTile / 2 - smallDot.getWidth() / 2,
					nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2, paint);
			c.drawBitmap(smallDot,
					width / 2 + sizeTile / 2 - smallDot.getWidth() / 2,
					nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2, paint);
			c.drawBitmap(smallDot,
					width / 2 + sizeTile * 3 / 2 - smallDot.getWidth() / 2,
					nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2, paint);
			switch (Page) {
			case 0:
				c.drawBitmap(bigDot,
						width / 2 - sizeTile * 3 / 2 - bigDot.getWidth() / 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			case 1:
				c.drawBitmap(bigDot,
						width / 2 - sizeTile / 2 - bigDot.getWidth() / 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			case 2:
				c.drawBitmap(bigDot,
						width / 2 + sizeTile / 2 - bigDot.getWidth() / 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			case 3:
				c.drawBitmap(bigDot,
						width / 2 + sizeTile * 3 / 2 - bigDot.getWidth() / 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			}
		} else if (Integer.parseInt(numPageInPack[pack][0]) == 5) {
			c.drawBitmap(smallDot,
					width / 2 - sizeTile * 2 - smallDot.getWidth() / 2,
					nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2, paint);
			c.drawBitmap(smallDot, width / 2 - sizeTile - smallDot.getWidth()
					/ 2, nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2,
					paint);
			c.drawBitmap(smallDot, width / 2 - smallDot.getWidth() / 2,
					nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2, paint);
			c.drawBitmap(smallDot, width / 2 + sizeTile - smallDot.getWidth()
					/ 2, nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2,
					paint);
			c.drawBitmap(smallDot,
					width / 2 + sizeTile * 2 - smallDot.getWidth() / 2,
					nemHeight + 7 * sizeBorder - smallDot.getWidth() / 2, paint);
			switch (Page) {
			case 0:
				c.drawBitmap(bigDot,
						width / 2 - sizeTile * 2 - bigDot.getWidth() / 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			case 1:
				c.drawBitmap(bigDot, width / 2 - sizeTile - bigDot.getWidth()
						/ 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			case 2:
				c.drawBitmap(bigDot, width / 2 - bigDot.getWidth() / 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			case 3:
				c.drawBitmap(bigDot, width / 2 + sizeTile - bigDot.getWidth()
						/ 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			case 4:
				c.drawBitmap(bigDot,
						width / 2 + sizeTile * 2 - bigDot.getWidth() / 2,
						nemHeight + 7 * sizeBorder - bigDot.getWidth() / 2,
						paint);
				break;
			}
		}

	}

	void drawRect(Canvas c, int x, int y, int num) {
		if (x >= -sizeTile && x <= width) {

			if (statusMove[num] == 1) {
				paint.setAlpha(120);
				c.drawBitmap(levelPass, x, y, paint);
				paint.setAlpha(255);
			} else if (statusMove[num] == 2) {
				paint.setAlpha(130);
				c.drawBitmap(levelPerfect, x, y, paint);
				paint.setAlpha(255);
			}

//			Path path = new Path();
//			path.moveTo(x, y);
//			path.lineTo(x + sizeTile, y);
//			path.lineTo(x + sizeTile, y + sizeTile);
//			path.lineTo(x, y + sizeTile);
//			path.close();
//			c.drawPath(path, paint);
			c.drawRect(x, y, x + sizeTile, y + sizeTile, paint);
		}
	}

	boolean haveDrag;
	int relateX;
	int oneDistance;
	int xDown, yDown;
	int xRel, yRel;

	int touch(MotionEvent event) {
		if (Moving) {
			return 0;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xRel = xDown = (int) event.getX();
			yRel = yDown = (int) event.getY();
			oneDistance = 0;
			haveDrag = false;

			if (mbBack.checkIn(xDown, yDown)) {
				if (view.mediaPlayer.isSound) {
					view.mediaPlayer.checkSound();
					view.mediaPlayer.soundTouch.start();
				}
				return 1;
			}
			return 0;

		case MotionEvent.ACTION_MOVE:
			int xMove = (int) event.getX();
			int yMove = (int) event.getY();

			if (mbBack.checkIn(xDown, yDown) && mbBack.checkIn(xMove, yMove)) {
				return 1;
			} else {
				if (oneDistance > sizeBorder || oneDistance < -sizeBorder) {
					haveDrag = true;
				}
				relateX -= (xMove - xRel);
				if (relateX > 6 * sizeBorder * (Integer.parseInt(numPageInPack[pack][0]) - 1)) {
					relateX = 6 * sizeBorder * (Integer.parseInt(numPageInPack[pack][0]) - 1);
				} else if (relateX < 0) {
					relateX = 0;
				} else {
					oneDistance -= (xMove - xRel);
				} 
				xRel = xMove;

				if (oneDistance >  sizeBorder) {
					Moving = true;
					moveToLeft = false;
				} else if (oneDistance < -sizeBorder) {
					Moving = true;
					moveToLeft = true;
				}
			}
			return 1;
		case MotionEvent.ACTION_UP:
			int xUp = (int) event.getX();
			int yUp = (int) event.getY();

			oneDistance = 0;
			relateX = Page * 6 * sizeBorder;

			if (mbBack.checkIn(xDown, yDown) && mbBack.checkIn(xUp, yUp)) {
				mbBack.inTouch = false;
				return 2;
			} else if (xUp >= nemWidth && xUp < nemWidth + 5 * sizeBorder
					&& yUp >= nemHeight && yUp < nemHeight + 6 * sizeBorder) {
				if (!haveDrag) {
					level = 30 * Page + ((xUp - nemWidth) / sizeBorder)
							+ ((yUp - nemHeight) / sizeBorder) * 5 + 1;
					
					return 3;
				}
			}
			return 0;
		}
		return 0;
	}

	boolean Moving;
	boolean moveToLeft;
	boolean update() {
		if (Moving) {
			if (moveToLeft) {
				relateX -= 4;
				oneDistance -= 4;
				if (relateX < 0) {
					relateX = 0;
					oneDistance = 0;
					Page = 0;
					Moving = false;
				} else if (relateX > 6 * sizeBorder
						* (Integer.parseInt(numPageInPack[pack][0]) - 1)) {
					relateX = 6 * sizeBorder * (Integer.parseInt(numPageInPack[pack][0]) - 1);
					oneDistance = 0;
					Page = Integer.parseInt(numPageInPack[pack][0]) - 1;
					Moving = false;
				} else if (oneDistance <= -6 * sizeBorder) {
					relateX += (oneDistance + 6 * sizeBorder);
					oneDistance = 0;
					if (--Page < 0) {
						Page = 0;
					}
					Moving = false;
				}
			} else {
				relateX += 4;
				oneDistance += 4;
				if (relateX < 0) {
					relateX = 0;
					oneDistance = 0;
					Page = 0;
					Moving = false;
				} else if (relateX > 6 * sizeBorder
						* (Integer.parseInt(numPageInPack[pack][0]) - 1)) {
					relateX = 6 * sizeBorder * (Integer.parseInt(numPageInPack[pack][0]) - 1);
					oneDistance = 0;
					Page = Integer.parseInt(numPageInPack[pack][0]) - 1;
					Moving = false;
				} else if (oneDistance >= 6 * sizeBorder) {
					relateX -= (oneDistance - 6 * sizeBorder);
					oneDistance = 0;
					if (++Page > Integer.parseInt(numPageInPack[pack][0]) - 1) {
						Page = Integer.parseInt(numPageInPack[pack][0]) - 1;
					}
					Moving = false;
				}
			}
			return true;
		}
		return false;
	}
}

