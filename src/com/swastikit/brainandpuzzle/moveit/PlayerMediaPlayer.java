package com.swastikit.brainandpuzzle.moveit;




import android.content.Context;
import android.media.MediaPlayer;

public class PlayerMediaPlayer {

	Context context;
	
	boolean isSound= true;
	MediaPlayer soundTouch, soundLeak, soundFlow, soundFinish;
	
	public PlayerMediaPlayer(Context context){
		
		this.context = context;
		
		soundTouch = MediaPlayer.create(context, R.raw.touch);
		soundLeak = MediaPlayer.create(context, R.raw.leak);
		soundFlow = MediaPlayer.create(context, R.raw.flow);
		soundFinish = MediaPlayer.create(context, R.raw.finish);
		
	}
	
	public void checkSound(){
		
		if(soundTouch.isPlaying()){
			soundTouch.stop();
			soundTouch.release();
			soundTouch = MediaPlayer.create(context, R.raw.touch);
			
		}
		
		if(soundLeak.isPlaying()){
			soundLeak.stop();
			soundLeak.release();
			soundLeak = MediaPlayer.create(context, R.raw.leak);
			
		}
		
		if(soundFlow.isPlaying()){
			soundFlow.stop();
			soundFlow.release();
			soundFlow = MediaPlayer.create(context, R.raw.flow);
		}
		
		if(soundFinish.isPlaying()){
			soundFinish.stop();
			soundFinish.release();
			soundFinish = MediaPlayer.create(context, R.raw.finish);
		}
	}
}
