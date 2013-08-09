package com.example.projectrpg;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Typeface inhishan;

	private TextView title;
	private TextView newGame;
	private TextView loadGame;
	private TextView options;
	private TextView help;
	
	private ImageView newGameImageView;
	private ImageView loadGameImageView;
	private ImageView optionsImageView;
	private ImageView helpImageView;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupUI();
		setupClickListener();
	}

	private void setupClickListener() {
		newGameImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		loadGameImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		optionsImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		helpImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
		
		newGameImageView = (ImageView)findViewById(R.id.new_game_imageview);
		loadGameImageView = (ImageView)findViewById(R.id.load_game_imageview);
		optionsImageView = (ImageView)findViewById(R.id.options_imageview);
		helpImageView = (ImageView)findViewById(R.id.help_imageview); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
