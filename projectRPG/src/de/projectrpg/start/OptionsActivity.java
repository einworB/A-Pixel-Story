package de.projectrpg.start;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.projectrpg.R;
import de.projectrpg.game.Controller;
import de.projectrpg.util.OurMusicManager;

public class OptionsActivity extends Activity {
	CheckBox checkBox;
	private Controller controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		setupUI();
		OurMusicManager.start(this);
		controller = Controller.getInstance();
		checkBox.setChecked(!controller.isMusicMuted());
	}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
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
