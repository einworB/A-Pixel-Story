package com.example.projectrpg;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Typeface inhishan;

	private TextView title;
	private TextView newGame;
	private TextView loadGame;
	private TextView options;
	private TextView help;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupUI();
	}

	private void setupUI() {
		setContentView(R.layout.activity_main);
		
		inhishan = Typeface
				.createFromAsset(getAssets(), "fonts/inhishan.ttf");

		title = (TextView)findViewById(R.id.title_textview);
		title.setTypeface(inhishan);
		
		newGame = (TextView)findViewById(R.id.new_game_textview);
		newGame.setTypeface(inhishan);
		
		loadGame = (TextView)findViewById(R.id.load_game_textview);
		loadGame.setTypeface(inhishan);
		
		options = (TextView)findViewById(R.id.options_textview);
		options.setTypeface(inhishan);
		
		help = (TextView)findViewById(R.id.help_textview);
		help.setTypeface(inhishan);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
