package com.swastikit.brainandpuzzle.moveit;

import java.util.HashMap;

import java.util.Map;

import com.swastikit.brainandpuzzle.moveit.MoveitView.GameMode;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Play {
	
	MoveitView view;
	Board board;
	int screenWidth, screenHeight;
	Paint paint;
	
	int rows, cols;
	Rect[][] gridPoints;
	
	HashMap<Point, Integer> balls;
	HashMap<Point, Point> ballPosition;
	HashMap<Point, Boolean> canBallMove;
	HashMap<Point, Integer> newPoints;
	
	int[][]  boardStatus;
	
	String trigger;
	boolean isMoving;
	
	int gridSize;

	boolean upTriggred;
	boolean gameEnd;
	
	int moves;
	MyDialog mdCompleted;
	
	GameState state;

	Hint hint;

	//string values
	String[] endGame;
	String[] dialogEnd;
	String[] nextLevelPlayAgain;
	String[] playAgainQuit;

	public Play(int w, int h, Board board, MoveitView view){
		this.view=  view;
		this.board = board;
		screenHeight =h;
		screenWidth = w;
		
		
		initStringValues();

	}
	
	public void initStringValues(){
		mdCompleted = new MyDialog(screenWidth, screenHeight);
		endGame = view.context.getResources().getStringArray(R.array.end_game);
		dialogEnd = view.context.getResources().getStringArray(R.array.DialogEnd);
		nextLevelPlayAgain = view.context.getResources().getStringArray(R.array.Next_level_play_again);
		playAgainQuit = view.context.getResources().getStringArray(R.array.Play_again_quit);
	}
	
	@SuppressWarnings("unchecked")
	public void init(){

		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
		this.rows = board.rows;
		this.cols = board.cols;
		
		gridPoints = board.gridPoints;
		balls = (HashMap<Point, Integer>) board.ball.clone();
		boardStatus = board.boardStatus;
		ballPosition = new HashMap<Point, Point>();
		canBallMove = new HashMap<Point, Boolean>();
		newPoints = new HashMap<Point, Integer>();
		
		gridSize = board.gridSize;
		trigger = new String();
		isMoving = false;
		upTriggred = true;
	
		gameEnd = false;
		
		//hints
		view.totalHintsLeft = view.savedState.getInt("totalHintsLeft", 10);
		hint = new Hint(this);
		hint.hints = board.hints;
		
		state = new GameState(this);

		view.buttonAction.mbRedo.disable();
		view.buttonAction.mbUndo.disable();
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void resetPlay(){
		
		balls = (HashMap<Point, Integer>) board.ball.clone();
		ballPosition.clear();
		canBallMove.clear();
		
		boardStatus = board.boardStatus;
		isMoving = false;
		gameEnd = false;
		upTriggred = true;
		moves = 0;
		
		hint.hints = board.hints;
		view.buttonAction.mbRedo.disable();
		view.buttonAction.mbUndo.disable();
		state.clearAll();
		
	}
	
	public void draw(Canvas canvas){
	
		
		drawBoardObjects(canvas);
		
		if(isMoving){
			
			drawMovingObject(canvas);
		}
		drawTexts(canvas);
		drawStatus(canvas);
		if(gameEnd){
			mdCompleted.draw(canvas);
		}
	}
	
	
	public void drawBoardObjects(Canvas canvas){
		
		for (int i = 0; i < rows ; i++) {
			for (int j = 0; j < cols; j++) {
				
				
				//blocks
				if(boardStatus[i][j] == -1){
					paint.setColor(Color.DKGRAY);
					
					canvas.drawRect(gridPoints[i][j], paint);
					
				} 
					
				//colored grids
				if(boardStatus[i][j] > 0){
					paint.setColor(ColorList.getColor(boardStatus[i][j], 0));
					paint.setAlpha(150);
					canvas.drawRect(gridPoints[i][j], paint);
				}
				
				
				//balls
				if(balls.containsKey(new Point(i,j)) && !isMoving){
					paint.setColor(ColorList.getColor(balls.get(new Point(i,j)), 0));
					canvas.drawCircle(gridPoints[i][j].centerX(), gridPoints[i][j].centerY(), gridSize/3, paint);
					
				}
				
				
			}
			
		}
	}
	
	public void drawMovingObject(Canvas canvas){
		int cx = 0, cy = 0;
		boolean flag = true;

		for (Map.Entry<Point, Point> entry : ballPosition.entrySet()) {

			Point key = entry.getKey();
			Point val = entry.getValue();

			if (canBallMove.get(key)) {

				if (trigger.equals("right")) {

					cx = val.x;
					if (cx + gridSize / 4 <= gridPoints[key.x][key.y + 1].centerX()) {
						cx = val.x + gridSize / 4;
						flag = false;
					} else {

						cx = gridPoints[key.x][key.y + 1].centerX();

					}

					cy = val.y;

				} else if (trigger.equals("left")) {

					cx = val.x;
					if (cx - gridSize / 4 >= gridPoints[key.x][key.y - 1].centerX()) {
						cx = val.x - gridSize / 4;
						flag = false;
					} else {

						cx = gridPoints[key.x][key.y - 1].centerX();

					}

					cy = val.y;

				} else if (trigger.equals("bottom")) {

					cy = val.y;
					if (cy + gridSize / 4 <= gridPoints[key.x + 1][key.y].centerY()) {
						cy = val.y + gridSize / 4;
						flag = false;
					} else {

						cy = gridPoints[key.x + 1][key.y].centerY();

					}

					cx = val.x;

				} else if (trigger.equals("top")) {

					cy = val.y;
					if (cy - gridSize / 4 >= gridPoints[key.x - 1][key.y].centerY()) {
						cy = val.y - gridSize / 4;
						flag = false;
					} else {

						cy = gridPoints[key.x - 1][key.y].centerY();

					}

					cx = val.x;

				}
			} else {

				cx = val.x;
				cy = val.y;

			}

			ballPosition.put(key, new Point(cx, cy));

			paint.reset();
			paint.setAntiAlias(true);
			paint.setColor(ColorList.getColor(balls.get(key), 0));
			canvas.drawCircle(cx, cy, gridSize / 3, paint);
		}//end of loop

		if (flag) {

			setNewBallPosition();
			rebuiltMap();

			isMoving = false;

		}

	}
	
	public void setNewBallPosition(){
		
		newPoints.clear();
		
		for(Map.Entry<Point, Integer> entry: balls.entrySet()){
			
			Point key = entry.getKey();
			Integer value = entry.getValue();
			
			if(canBallMove.get(key)){
							
				if(trigger.equals("right")){
				
					
					newPoints.put(new Point(key.x, key.y + 1), value);
					
				} else if(trigger.equals("left")){
					
					
					newPoints.put(new Point(key.x, key.y - 1), value);
					
				} else if(trigger.equals("top")){
					
					newPoints.put(new Point(key.x - 1, key.y), value);
					
				} else if(trigger.equals("bottom")){
		
					newPoints.put(new Point(key.x + 1, key.y), value);
					
					
				}
			
			} else {
				
				newPoints.put(key, value);
			}
			
		}
	}
	
	public void rebuiltMap(){
		
		balls.clear();
		ballPosition.clear();
		canBallMove.clear();
		
		for(Map.Entry<Point, Integer> entry : newPoints.entrySet()){
			
			Point key = entry.getKey();
			int value = entry.getValue();
			
			balls.put(key, value);
			
		}
		gameEnd();
	
	}
	
	public void drawStatus(Canvas canvas){
		if (view.selectLevel.statusMove[view.currentLevel-1] == 1) {
		
			canvas.drawBitmap(view.selectLevel.levelPass, view.buttonAction.mbHint.rec.left, view.buttonAction.mbBack.rec.top, paint);

		} else if (view.selectLevel.statusMove[view.currentLevel-1] == 2) {
			
			canvas.drawBitmap(view.selectLevel.levelPerfect, view.buttonAction.mbHint.rec.left, view.buttonAction.mbBack.rec.top, paint);
		
		}
	}
	
	public void drawTexts(Canvas canvas) {

		// level title
		paint.setTextSize(screenWidth / 15);
		paint.setColor(Color.YELLOW);
		canvas.drawText(view.context.getResources().getString(R.string.Level)
				+ " " + view.currentLevel, screenWidth / 10 + screenWidth / 20,
				(screenHeight - screenWidth) / 2 - screenWidth / 8
						- screenWidth / 40, paint);

		// text below level title
		paint.setTextSize(screenWidth / 30);
		paint.setColor(Color.WHITE);

		String tempHud = view.context.getResources().getString(R.string.Moves)
				+ ":" + moves + "/" + board.bestMove
				+ view.context.getResources().getString(R.string.best);

		int tempHudwidth = (int) paint.measureText(tempHud);
		int gap = (screenWidth - tempHudwidth) / 4;
		int chargap = (int) paint.measureText(" ");
		String spacegap = "";
		for (int i = 0; i < gap; i += chargap) {
			spacegap += " ";
		}
		String HUD = "";

		HUD = HUD + view.context.getResources().getString(R.string.moves) + ":"
				+ moves + "/"+board.bestMove;

		// best user time
		String bestMove = view.savedState.getString(view.pack + "level"
				+ view.currentLevel, "--");
		HUD += spacegap + view.context.getResources().getString(R.string.best) +
				":"+ bestMove;

		int widthText = (int) paint.measureText(HUD);

		canvas.drawText(HUD, (Math.abs(screenWidth - widthText) / 2),
				board.gameBoard.top - screenWidth / 50, paint);

		// for hint text
		int Nem = (screenHeight - screenWidth) / 2 - (screenWidth / 35);
		canvas.drawText("" + view.totalHintsLeft,
				view.buttonAction.mbHint.rec.right - screenWidth / 90, Nem
						+ screenWidth + screenWidth / 20 + (screenWidth / 20)
						* 2 - (screenWidth / 20), paint);
	}
	
	int xMove =-1, yMove= -1;
	int xDown = -1, yDown = -1;
	
	
	public void touch(MotionEvent event){
		
		xMove= (int) event.getX();
		yMove= (int) event.getY();
		
		if(!gameEnd){

			if(board.gameBoard.contains(xMove, yMove) && !isMoving ){
			
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					if(upTriggred){
						
						checkIn("down");
						

					}
					break;
					
				case MotionEvent.ACTION_MOVE:

					if(upTriggred){
						checkIn("move");
						
					}
					break;
					
				case MotionEvent.ACTION_UP:
					
					checkIn("up");
					break;
				}

			}
			
		} else{
			dialog_click_up(xMove, yMove);
		}
	}
	
	int currentBlockId = -1;
	int iPos, jPos;

	public void checkIn(String action){
		
		if(action.equalsIgnoreCase("down")){
			xDown = xMove;
			yDown = yMove;
			
			return;
			
			
		} else if(action.equalsIgnoreCase("move")){
			
			
			
			if(isMoving = inspectMovement()){
				
				
				if(setMapsPosition()){
					if (view.mediaPlayer.isSound) {
						view.mediaPlayer.checkSound();
						view.mediaPlayer.soundFlow.start();
					}
					moves++;
					state.pushUndoState();
					state.clearRedos();
					
					
					
				}else {
					
					if (view.mediaPlayer.isSound) {
						view.mediaPlayer.checkSound();
						view.mediaPlayer.soundTouch.start();
					}
					isMoving = false;
					
				}
				
				upTriggred = false;
				
			}
			
			
		
			return;
			
			
		
		} else if(action.equalsIgnoreCase("up")){
			upTriggred = true;
		}
	}
	
	public boolean setMapsPosition(){
		
		ballPosition.clear();
		canBallMove.clear();
		
		boolean canMove = false;
		
		for(Map.Entry<Point, Integer> entry : balls.entrySet()){
			 
			Point key = entry.getKey();
			int value = entry.getValue();
			
			ballPosition.put(key, new Point(gridPoints[key.x][key.y].centerX(), gridPoints[key.x][key.y].centerY()));
			
			if(canMove(key)){
				canBallMove.put(key, true);
				canMove = true;
			} else {
				canBallMove.put(key, false);
				
			}
			newPoints.put(key, value);
			

		}
		
		return canMove;
	}

	
	public boolean canMove(Point key){
		
		if (trigger.equals("right") && (key.y + 1 < cols)) {
			if (boardStatus[key.x][key.y + 1] != -1) {
				
				if (balls.containsKey(new Point(key.x, key.y + 1))) {

					if (canMove(new Point(key.x, key.y + 1))) {
						return true;
					} else {
						return false;
					}
				}

				return true;
			}

		} else if (trigger.equals("left") && (key.y - 1 >= 0)) {
			if (boardStatus[key.x][key.y - 1] != -1) {

				if (balls.containsKey(new Point(key.x, key.y - 1))) {

					if (canMove(new Point(key.x, key.y - 1))) {
						return true;

					} else {

						return false;
					}
				}

				return true;
			}

		} else if (trigger.equals("bottom") && (key.x + 1 < rows)) {
			if (boardStatus[key.x + 1][key.y] != -1) {

				if (balls.containsKey(new Point(key.x + 1, key.y))) {

					if (canMove(new Point(key.x + 1, key.y))) {

						return true;
					} else {

						return false;
					}
				}

				return true;
			}

		} else if (trigger.equals("top") && (key.x - 1 >= 0)) {
			if (boardStatus[key.x - 1][key.y] != -1) {

				if (balls.containsKey(new Point(key.x - 1, key.y))) {

					if (canMove(new Point(key.x - 1, key.y))) {

						return true;
					} else {
						return false;
					}

				}
				return true;
			}
		}
		
		return false;
	}
	
	
	public boolean inspectMovement(){
		int dx, dy;
		
		for(Map.Entry<Point, Integer> entry : balls.entrySet()){
			Point point = entry.getKey();
			int value = entry.getValue();
			
			if(gridPoints[point.x][point.y].contains(xDown, yDown)){
				dx = xDown - xMove;
				dy = yDown - yMove;
				
				if(Math.abs(dx) - Math.abs(dy) >= 0){
				
					if(dx <= - 50){
						
						trigger = "right";
					
						
						return true;
					}
					
					if(dx >= 50){
						
						trigger = "left";
						return true;
					}
					
				}
				
				if(Math.abs(dx) - Math.abs(dy) <= 0){
					
					if (dy <= -50) {

						trigger = "bottom";
						
						return true;
					}

					if (dy >= 50) {

						trigger = "top";
						return true;
					}
				}
				
				return false;
			}
			
		}
		return false;
		
	}
	

	public void gameEnd(){
		
		gameEnd = true;
	
		for(Map.Entry<Point, Integer> entry: balls.entrySet()){
			
			Point key = entry.getKey();
			int value = entry.getValue();
			
			if(boardStatus[key.x][key.y] != value){
				gameEnd = false;
				
			}
			
		}
		
		if(gameEnd){
			
			setMessageHintsAndMoves();
		}
		
	}
	public void setMessageHintsAndMoves() {

		mdCompleted.active = true;
		// game ends
		if (view.savedState.getInt("hintGained_" + view.pack + "_level_" + view.currentLevel, 0) == 0) {
			// add hint

			view.editor.putInt("hintGained_" + view.pack + "_level_" + view.currentLevel, 1);
			view.editor.commit();

			if (moves <= view.savedState.getInt("bestMove_level_" + view.currentLevel + "_pack_" + view.pack, board.bestMove)) {

				if (view.savedState.getInt("statusMove" + view.pack + "_" + view.currentLevel, 0) != 2) {
					view.editor.putInt("statusMove" + view.pack + "_" + view.currentLevel, 2);
					view.editor.commit();
				}

				view.editor.putInt("bestMove_level_" + view.currentLevel + "_pack_" + view.pack, moves);
				view.totalHintsLeft += 2;
				view.editor.putInt("totalHintsLeft", view.totalHintsLeft);
				view.editor.putInt("pack" + view.pack + "_level" + view.currentLevel + "moves", moves);
				view.editor.putString(view.pack + "level" + view.currentLevel, moves + "");
				view.editor.commit();
				// show dialog after game is completed in best time plus 2 hints

				if (view.currentLevel < view.maxLevel) {
					mdCompleted.set(endGame[0],
							new String[] {endGame[2],view.context.getResources().getString(R.string.in_the_best_move_and_got_2_hints)},
							new String[] { nextLevelPlayAgain[1],nextLevelPlayAgain[0] });
				} else {
					mdCompleted.set(endGame[0],
							new String[] {endGame[2],view.context.getResources().getString(R.string.in_the_best_move_and_got_2_hints) },
							new String[] { playAgainQuit[0],playAgainQuit[1] });

				}
			} else {
				if (view.savedState.getInt("statusMove" + view.pack + "_" + view.currentLevel, 0) < 1) {
					view.editor.putInt("statusMove" + view.pack + "_"+ view.currentLevel, 1);
					view.editor.commit();
				}
				
				view.totalHintsLeft++;
				view.editor.putInt("totalHintsLeft", view.totalHintsLeft);
				view.editor.putInt("pack" + view.pack + "_level" + view.currentLevel + "moves", moves);
				view.editor.putString(view.pack + "level" + view.currentLevel, moves + "");
				view.editor.commit();

				// set total moves in the dialog box plus 1 hint
				String countMove_hint1 = view.context.getResources().getString(
						R.string.countMove_moves_and_got_1_hint);
				countMove_hint1 = countMove_hint1.replace("{COUNT_MOVES}", moves + "");

				// show dialog box after game completion

				if (view.currentLevel < view.maxLevel) {
					mdCompleted.set(view.context.getResources().getString(R.string.Perfect),
							new String[] {view.context.getResources().getString(R.string.You_completed_the_level),countMove_hint1 },
							new String[] {nextLevelPlayAgain[1], nextLevelPlayAgain[0] });
				} else {
					mdCompleted.set(endGame[0],
							new String[] {view.context.getResources().getString(R.string.You_completed_the_level),countMove_hint1 },
							new String[] {playAgainQuit[0], playAgainQuit[1] });
				}

			}

		} else {
			// hint already added
			if (moves <= view.savedState.getInt("bestMove_level_"+ view.currentLevel + "_pack_" + view.pack, board.bestMove)) {

				if (view.savedState.getInt("statusMove" + view.pack + "_" + view.currentLevel, 0) != 2) {
					view.editor.putInt("statusMove" + view.pack + "_" + view.currentLevel, 2);
					view.editor.commit();

				}

				view.editor.putInt("bestMove_level_" + view.currentLevel + "_pack_" + view.pack, moves);
				view.editor.putInt("pack" + view.pack + "_level" + view.currentLevel + "moves", moves);
				view.editor.putString(view.pack + "level" + view.currentLevel, moves + "");
				view.editor.commit();

				// show dialog after game is completed in best time but no hints

				if (view.currentLevel < view.maxLevel) {
					mdCompleted.set(endGame[0],
							new String[] {endGame[2],view.context.getResources().getString(R.string.in_the_best_move) },
							new String[] { nextLevelPlayAgain[1],nextLevelPlayAgain[0] });
				} else {
					mdCompleted.set(endGame[0],
							new String[] {endGame[2],view.context.getResources().getString(R.string.in_the_best_move) },
							new String[] { playAgainQuit[0],playAgainQuit[1] });

				}

			} else {

				// set total moves in the dialog box in random time
				String countMoves = view.context.getResources().getString(
						R.string.in_countMove_moves);
				countMoves = countMoves.replace("{COUNT_MOVES}", moves + "");

				if (view.savedState.getInt("statusMove" + view.pack + "_"+ view.currentLevel, 0) < 1) {
					view.editor.putInt("statusMove" + view.pack + "_"+ view.currentLevel, 1);
					view.editor.commit();
				}

				if (moves <= view.savedState.getInt("pack" + view.pack+ "_level" + view.currentLevel + "moves", moves)) {
					
					view.editor.putInt("pack" + view.pack + "_level"+ view.currentLevel + "moves", moves);
					view.editor.putString(view.pack + "level"+ view.currentLevel, moves + "");
					view.editor.commit();
				}

				if (view.currentLevel < view.maxLevel) {
					mdCompleted.set(view.context.getResources().getString(R.string.Perfect),
							new String[] {view.context.getResources().getString(R.string.You_completed_the_level),countMoves }, 
							new String[] {nextLevelPlayAgain[1],nextLevelPlayAgain[0] });

				} else {
					mdCompleted.set(endGame[0],
							new String[] {view.context.getResources().getString(R.string.You_completed_the_level),countMoves }, 
							new String[] {playAgainQuit[0], playAgainQuit[1] });

				}
			}
		}
	}
	
	
	void dialog_click_up(int x, int y) {

		if (mdCompleted.active) { // level complete show dialogue box

			if (mdCompleted.checkIn(x, y)) {
				if (mdCompleted.inTouch1) { // restart level
					mdCompleted.inTouch1 = false;
					mdCompleted.active = false;

					view.board.reset(view.currentLevel);
					resetPlay();
					view.buttonAction.enableAll();

				} else { // play next level
					mdCompleted.inTouch2 = false;
					mdCompleted.active = false;

					
					if (view.currentLevel == view.maxLevel){

						view.gameMode = GameMode.SELECTPACK;
					} else if (view.currentLevel < view.maxLevel) {// level // < max or game over:: out of movers
							view.currentLevel++;
							view.board.reset(view.currentLevel);
							resetPlay();
							view.buttonAction.enableAll();
						}

					}
				}
			}

	}// end of dialog_click_up
			

}
