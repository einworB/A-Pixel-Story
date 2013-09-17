package de.projectrpg.start;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import de.projectrpg.R;
import de.projectrpg.util.OurMusicManager;

/**
 * The activity to help the user to play the game.
 */
public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}
	
	/**
	 * called when the activity is brought to the front
	 */
	@Override
	protected synchronized void onResume() {
		super.onResume();
		OurMusicManager.start(this);		
	}
	
	/**
	 * called when the activity is paused
	 */
	@Override
	protected void onPause() {
		super.onPause();
		OurMusicManager.pause();
	}

}
