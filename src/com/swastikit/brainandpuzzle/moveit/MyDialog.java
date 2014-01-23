package com.swastikit.brainandpuzzle.moveit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class MyDialog {

	public boolean active;

	int width, height;

	Paint p;
	boolean have2Button;
	Rect rDialog, rButton1, rButton2;

	public boolean inTouch1;

	public boolean inTouch2;

	String title;
	String[] content;
	String button1, button2;

	public MyDialog(int w, int h) {
		width = w;
		height = h;

		inTouch1 = inTouch2 = false;
		have2Button = false;

		p = new Paint();
		p.setAntiAlias(true);
		active = false;
	}

	public void set(String[] allText) {
		title = allText[0];
		content = new String[2];
		content[0] = allText[1];
		content[1] = allText[2];
		button1 = allText[3];
		button2 = allText[4];

		have2Button = true;
		createRec();
	}

	
	public void set(String t, String[] c, String sButton) {
		title = t;
		content = c;
		button1 = sButton;

		have2Button = false;
		createRec();
	}

	public void set(String t, String[] c, String[] sButton) {
		title = t;
		content = c;
		button1 = sButton[0];
		button2 = sButton[1];

		have2Button = true;
		createRec();
	}

	void createRec() {
		rDialog = new Rect(width / 6, height / 2 - width / 3,
				width * 5 / 6, height / 2 + width / 4);
		if (have2Button) {
			rButton1 = new Rect(width / 6 + width / 12, rDialog.bottom - (width / 4) - (width / 30),
					width * 5 / 6 - width / 12,  rDialog.bottom - (width / 6));
		} else {
			rButton1 = new Rect(width / 6 + width / 12, rDialog.bottom - (width / 4) - (width / 30),
					width * 5 / 6 - width / 12,  rDialog.bottom - (width / 6));
		}
		
		rButton2 = new Rect(width / 6 + width / 12, rDialog.bottom - (width / 7), width * 5
				/ 6 - width / 12, rDialog.bottom - (width / 40));
	}

	int widthText;

	public void draw(Canvas c) {
		if (active) {
			p.setStyle(Style.FILL);
			p.setColor(0xdd000000);
			c.drawRect(rDialog, p);

			p.setStyle(Style.STROKE);
			p.setColor(0xffffffff);
			c.drawRect(rDialog, p);

			p.setTextSize(width / 15);
			widthText = (int) p.measureText(title);
			c.drawText(title, width / 2 - widthText / 2,rDialog.top + (width / 15), p);

			if(content[1].length() > (width / 25)){
				p.setTextSize(width / 30);
			}else{
				p.setTextSize(width / 25);
			}
			widthText = (int) p.measureText(content[0]);
			c.drawText(content[0], width / 2 - widthText / 2, rDialog.top + (width / 6), p);
			widthText = (int) p.measureText(content[1]);
			c.drawText(content[1], width / 2 - widthText / 2, rDialog.top + (width / 6) + (width / 25), p);

			if (have2Button) {
				p.setTextSize(width / 20);
				if (inTouch1) {
					p.setStyle(Style.FILL);
					p.setColor(0xffffffff);
					c.drawRect(rButton1, p);
					p.setStyle(Style.STROKE);

					p.setColor(0xff000000);
					widthText = (int) p.measureText(button1);
					c.drawText(button1, width / 2 - widthText / 2, rButton1.bottom - (rButton1.height() / 3), p);
					p.setColor(0xffffffff);
				} else {
					c.drawRect(rButton1, p);
					widthText = (int) p.measureText(button1);
					c.drawText(button1, width / 2 - widthText / 2,rButton1.bottom - (rButton1.height() / 3) , p);
				}
				if (inTouch2) {
					p.setStyle(Style.FILL);
					p.setColor(0xffffffff);
					c.drawRect(rButton2, p);
					p.setStyle(Style.STROKE);

					p.setColor(0xff000000);
					widthText = (int) p.measureText(button2);
					c.drawText(button2, width / 2 - widthText / 2, rButton2.bottom - (rButton2.height() / 3), p);
					p.setColor(0xffffffff);
				} else {
					c.drawRect(rButton2, p);
					widthText = (int) p.measureText(button2);
					c.drawText(button2, width / 2 - widthText / 2,  rButton2.bottom - (rButton2.height() / 3), p);
				}

			} else {
				if (inTouch1) {
					p.setStyle(Style.FILL);
					p.setColor(0xffffffff);
					c.drawRect(rButton1, p);
					p.setStyle(Style.STROKE);

					p.setColor(0xff000000);
					widthText = (int) p.measureText(button1);
					c.drawText(button1, width / 2 - widthText / 2,rButton1.bottom - (rButton1.height() / 3), p);
					p.setColor(0xffffffff);
				} else {
					c.drawRect(rButton1, p);
					widthText = (int) p.measureText(button1);
					c.drawText(button1, width / 2 - widthText / 2, rButton1.bottom - (rButton1.height() / 3), p);
				}
			}
		}

	}

	public boolean checkIn(int x, int y) {
		if (active) {
			inTouch1 = (rButton1.left <= x && rButton1.right > x) && (rButton1.top <= y && rButton1.bottom > y);
			inTouch2 = (rButton2.left <= x && rButton2.right > x) && (rButton2.top <= y && rButton2.bottom > y);
			return inTouch1 || inTouch2;
		}
		return false;
	}
}
