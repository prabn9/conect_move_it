package com.swastikit.brainandpuzzle.moveit;

import java.util.HashMap;
import java.util.Stack;

import android.R.integer;
import android.graphics.Point;

public class GameState {

	Play play;
	Stack<HashMap<Point, Integer>> undos;
	Stack<HashMap<Point, Integer>> redos;

	public GameState(Play play) {

		this.play = play;
		undos = new Stack<HashMap<Point,Integer>>();
		redos = new Stack<HashMap<Point,Integer>>();

	}

	@SuppressWarnings("unchecked")
	public void pushUndoState() {

		
		undos.push((HashMap<Point, Integer>) play.balls.clone());
		
	
		
		if(!undos.isEmpty()){
			
			play.view.buttonAction.mbUndo.enable();
		}
		
	}

	@SuppressWarnings("unchecked")
	public void popUndoState() {
		pushRedoState();
		play.balls.clear();
		play.balls = (HashMap<Point, Integer>) undos.pop().clone();
		
		
		if(undos.empty()){
			
			play.view.buttonAction.mbUndo.disable();
		}

	}

	@SuppressWarnings("unchecked")
	public void pushRedoState() {

		redos.push((HashMap<Point, Integer>) play.balls.clone());
		
		
		if(!redos.isEmpty()){
			
			play.view.buttonAction.mbRedo.enable();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void popRedoState(){
		
		pushUndoState();
		play.balls.clear();
		play.balls = (HashMap<Point, Integer>) redos.pop().clone();
		
		if(redos.empty()){
			
			play.view.buttonAction.mbRedo.disable();
		}
	}
	
	public void clearUndos(){
		
		undos.clear();
	}
	
	public void clearRedos(){
		redos.clear();
		
	}
	
	public void clearAll(){
		
		undos.clear();
		redos.clear();
	}

}
