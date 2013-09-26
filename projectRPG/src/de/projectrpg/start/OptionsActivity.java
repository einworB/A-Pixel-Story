package de.projectrpg.start;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.projectrpg.R;
import de.projectrpg.game.Controller;
import de.projectrpg.util.OurMusicManager;

/**
 * Option Activity to switch the music on and off.
 */
public class OptionsActivity extends Activity {
	/** checkbox to switch the music on and off*/
	CheckBox checkBox;
	/** the controller*/
	private Controller controller;
	
	/**
	 * setup the UI and the get an instance of the controller.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		setupUI();
		OurMusicManager.start(this);
		controller = Controller.getInstance();
		checkBox.setChecked(!controller.isMusicMuted());
	}

	/**
	 * setup the UI. If the checkbox is clicked check whether the box is checked or not.
	 * If it is checked start the music else pause it.
	 */
	private void setupUI() {
		checkBox = (CheckBox) findViewById(R.id.checkbox_options);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(!isChecked) {
					controller.setMusicMuted(true);
					OurMusicManager.pause();
				} else {
					controller.setMusicMuted(false);
					OurMusicManager.start(OptionsActivity.this);
				}
				
			}
		});
	}

	/**
	 * called when the activity is brought to the front e.g. on startup or exiting LevelActivity
	 */
	@Override
	public void onResume() {
		super.onResume();
		OurMusicManager.start(this);		
	}
}
