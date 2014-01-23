package com.swastikit.brainandpuzzle.moveit;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class Hint {

	Play play;
	String[] hintIndex;
	HashMap<Integer, ArrayList<String>> hintPoint;
	int blockIndex = 0;

	boolean hintActive;

	String hints;

	public Hint(Play play) {
		this.play = play;
		hintActive = false;
		hintPoint = new HashMap<Integer, ArrayList<String>>();

	}

	public void getHint() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				
				

				for (int p = 0; p < hints.length(); p++) {

					char hintChar = hints.charAt(p);

					switch (hintChar) {
					case 'r':

						play.trigger = "right";
						break;

					case 'l':
						play.trigger = "left";
						break;

					case 't':
						play.trigger = "top";
						break;

					case 'b':
						play.trigger = "bottom";
						break;

					default:
						break;
					}

					play.isMoving = true;
					play.setMapsPosition();
					play.view.postInvalidate();
					

					Log.d("hint",hints.length()+ play.trigger + " "+hintChar);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}

				}

			}
		}).start();

	}

	public class HintTimerThread extends Thread {

		public void run() {

			try {
				sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			hintActive = false;

		}
	}

}
