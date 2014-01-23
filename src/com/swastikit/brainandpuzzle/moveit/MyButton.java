package com.swastikit.brainandpuzzle.moveit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MyButton {
	Rect rec;
	boolean inTouch;
	boolean disable;
	Bitmap[] bitmap;

	MyButton(Rect r) {
		inTouch = false;
		rec = new Rect(r);
		disable = false;
	}

	void load(Resources res, int[] id) {
		bitmap = new Bitmap[2];
		for (int i = 0; i < 2; i++) {
			bitmap[i] = BitmapFactory.decodeResource(res, id[i]);
			bitmap[i] = Bitmap.createScaledBitmap(bitmap[i], rec.right
					- rec.left, rec.bottom - rec.top, true);
		}
	}

	void disable() {
		disable = true;
	}

	void enable() {
		disable = false;
	}

	void draw(Canvas c) {
		Paint p = new Paint();
		p.setAntiAlias(true);
		if (disable) {
			p.setAlpha(140);
			c.drawBitmap(bitmap[0], rec.left, rec.top, p);
		} else {
			if (inTouch) {
				c.drawBitmap(bitmap[1], rec.left, rec.top, p);
			} else {
				c.drawBitmap(bitmap[0], rec.left, rec.top, p);
			}
		}
	}

	boolean checkIn(int x, int y) {
		inTouch = (rec.left - (rec.right - rec.left) / 3 < x && rec.right
				+ (rec.right - rec.left) / 3 > x)
				&& (rec.top - (rec.bottom - rec.top) / 3 < y && rec.bottom
						+ (rec.bottom - rec.top) > y);
		return inTouch;
	}

}

