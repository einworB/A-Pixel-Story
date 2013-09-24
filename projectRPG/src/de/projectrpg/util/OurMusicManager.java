package de.projectrpg.util;

import android.content.Context;
import android.media.MediaPlayer;
import de.projectrpg.R;
import de.projectrpg.game.Controller;

/**
 * Class responsible for playing Music
 */
public class OurMusicManager {
	
	/** constant for the music title to play */
	private static final int MUSIC = R.raw.music;
	
	/** the MediaPlayer playing the music */
	private static MediaPlayer player;

	/**
	 * starts the music player
	 * @param context the context of the calling activity
	 */
	public static void start(Context context){
		Controller controller = Controller.getInstance();
		if(!controller.isMusicMuted()){
			if(player!=null){
				if(!player.isPlaying()) player.start();
			} else{
				player = MediaPlayer.create(context, MUSIC);
				player.setLooping(true);
				player.start();
			}
		}
	}
	
	/**
	 * pauses the music
	 */
	public static void pause() {
		if(player!=null){
			if(player.isPlaying()){
				player.pause();
			}
		}
	}
	
	/**
	 * stops the music and releases the musicplayer
	 */
	public static void release() {
		if(player!=null){
			if(player.isPlaying()){
				player.stop();
			}
		player.release();
		}		
	}

}
