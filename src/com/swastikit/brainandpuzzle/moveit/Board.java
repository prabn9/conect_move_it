package com.swastikit.brainandpuzzle.moveit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

@SuppressLint("UseSparseArrays")
public class Board {

	MoveitView view;
	int screenWidth, screenHeight;
	int adjus;

	Paint paintBoard;

	Rect gameBoard;

	int rows, cols;

	int gridSize;
	Rect[][] gridPoints;

	// hashmap ball::
	// point=>represents the current position of the ball
	// Integer => represents the color of the ball
	HashMap<Point, Integer> ball;

	// board status:: -1 => blocked grid:: 0 => free space:: greater than 0 => grid color
	int[][] boardStatus;

	Context context;

	List<String> levelFileLists;
	String levelFile;

	int pack = 0;
	String hints;
	int bestMove;

	public Board(int w, int h, MoveitView view) {

		this.view = view;
		screenHeight = h;
		screenWidth = w;

		this.context = view.context;

		this.adjus = view.adjus;

		init();

	}

	public void init() {
		paintBoard = new Paint(Paint.ANTI_ALIAS_FLAG);

		
		levelFileLists = new ArrayList<String>();
		ball = new HashMap<Point, Integer>();
		
		
		
		reset(view.currentLevel);

	}

	public void reset(int level) {
		
		ball.clear();
		
		readLevel();
		setBoard();
		setGridPoints();
		// view.buttonAction.enableAll();

	}

	public void readLevel() {

		if (pack != view.pack) {

			levelFileLists.clear();

			pack = view.pack;
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(context.getAssets().open(
								"moveit/pack" + pack + ".txt")));

				String tmpStr;
				while ((tmpStr = reader.readLine()) != null) {
					levelFileLists.add(tmpStr);
				}

				reader.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		levelFile = levelFileLists.get(view.currentLevel - 1);

		String[] rowElement = levelFile.split(";");

		// set hint file
		hints = rowElement[rowElement.length - 1];

		rows = rowElement.length - 1;
		cols = rowElement[0].split(",").length;
		
		gridSize = screenWidth / cols;
		
		gridPoints = new Rect[rows][cols];
		boardStatus = new int[rows][cols];

		for (int i = 0; i < rows; i++) {
			
			String[] colElement = rowElement[i].split(",");
			for (int j = 0; j < cols; j++) {
				
				if(colElement[j].equals("-1")){//blocked grid
					
					boardStatus[i][j] = -1;
					
					
				} else if(colElement[j].equals("00")){//empty grid
					
					boardStatus[i][j] = 0;
					
				} else if(colElement[j].matches("([1-9])"+"(0)")){//contains object
					
					boardStatus[i][j] = 0;
					ball.put(new Point(i,j), colElement[j].charAt(0) - 48);
					
				} else if(colElement[j].matches("(0)"+"([1-9])")){ //grid filled with given color
					
					boardStatus[i][j] = colElement[j].charAt(1) - 48;
					
				} else if(colElement[j].matches("([1-9])"+"([1-9])")){// contains object and colored grid
					
					boardStatus[i][j] = colElement[j].charAt(1) - 48;
					ball.put(new Point(i,j), colElement[j].charAt(0) - 48);
				}
			}

		}

	}

	public static int randInt(int min, int max) {

		Random rand = new Random();

		int randomNum = rand.nextInt(Math.abs((max - min) + 1)) + min;

		return randomNum;
	}

	public void setBoard() {

		gameBoard = new Rect();
		gameBoard.set(0, view.adjus, screenWidth, (gridSize * cols) + adjus);

	}

	public void setGridPoints() {

		int xFactor = 0, yFactor = adjus;
		gridPoints = new Rect[rows][cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {

				gridPoints[i][j] = new Rect(xFactor, yFactor,
						xFactor += gridSize, yFactor + gridSize);
			}
			yFactor += gridSize;
			xFactor = 0;
		}
	}

	public void draw(Canvas canvas) {

	}

}
