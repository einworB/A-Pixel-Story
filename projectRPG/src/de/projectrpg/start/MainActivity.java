package de.projectrpg.start;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.projectrpg.R;

import de.projectrpg.game.LevelActivity;
import de.projectrpg.util.OurMusicManager;

public class MainActivity extends Activity {
	
	//neuer Upload... Kommentare folgen noch

	private Typeface inhishan;
	private File file2;

	private TextView title;
	private TextView newGame;
	private TextView savedGameSlot1;
	private TextView savedGameSlot2;
	private TextView options;
	private TextView help;

	private ImageView newGameImageView;
	private ImageView optionsImageView;
	private ImageView helpImageView;

	private HorizontalScrollView parallaxMidLayer;
	private HorizontalScrollView parallaxFrontLayer;
	
	private TableLayout table;

	private int scrolled1 = 0;
	private int scrolled2 = 0;
	
	private boolean isBackButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();
		setupClickListener();
		new MyAsyncClouds().execute("blub");
		new MyAsyncGround().execute("blub");
	}

	private void setupClickListener() {
		setOnClickListenerStartGame(newGameImageView);
		setOnClickListenerStartGame(newGame);
		setOnClickListenerOptions(optionsImageView);
		setOnClickListenerOptions(options);
		setOnClickListenerHelp(helpImageView);
		setOnClickListenerHelp(help);
		deactivateScrollingbyUser(parallaxMidLayer);
		deactivateScrollingbyUser(parallaxFrontLayer);
	}

	private void setupUI() {
		setContentView(R.layout.activity_main);

		inhishan = Typeface.createFromAsset(getAssets(), "fonts/inhishan.ttf");

		title = (TextView) findViewById(R.id.title_textview);
		title.setTypeface(inhishan);

		newGame = (TextView) findViewById(R.id.new_game_textview);
		newGame.setTypeface(inhishan);

		savedGameSlot1 = (TextView) findViewById(R.id.saved_game_slot1);
		savedGameSlot1.setTypeface(inhishan);
		savedGameSlot1.setVisibility(View.INVISIBLE);

		savedGameSlot2 = (TextView) findViewById(R.id.saved_game_slot2);
		savedGameSlot2.setTypeface(inhishan);
		savedGameSlot2.setVisibility(View.INVISIBLE);

		options = (TextView) findViewById(R.id.options_textview);
		options.setTypeface(inhishan);

		help = (TextView) findViewById(R.id.help_textview);
		help.setTypeface(inhishan);

		newGameImageView = (ImageView) findViewById(R.id.new_game_imageview);
		optionsImageView = (ImageView) findViewById(R.id.options_imageview);
		helpImageView = (ImageView) findViewById(R.id.help_imageview);
		parallaxMidLayer = (HorizontalScrollView) findViewById(R.id.scroll);
		parallaxFrontLayer = (HorizontalScrollView) findViewById(R.id.scroll2);
		
		table = (TableLayout) findViewById(R.id.Tabelle);
		
	}
	
	private void setOnClickListenerStartGame(View view){
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				table.setVisibility(View.INVISIBLE);
				newGame.setVisibility(View.INVISIBLE);
				newGameImageView.setVisibility(View.INVISIBLE);
				savedGameSlot1.setVisibility(View.VISIBLE);
				savedGameSlot2.setVisibility(View.VISIBLE);
				
				File file = new File("/data/data/de.projectrpg/files/slot1.xml");
				
				if(file.exists()){
					// Create an instance of SimpleDateFormat used for formatting 
					// the string representation of date (month/day/year)
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

					// Get the date today using Calendar object.
					long today = file.lastModified();      
					// Using DateFormat format method we can create a string 
					// representation of a date with the defined format.
					String reportDate = df.format(today);

					// Print what date is today!
					System.out.println("Report Date: " + reportDate);
					setOnClickListenerLoadGame(savedGameSlot1, 1);
					savedGameSlot1.setText("\nSlot 1\n\nzuletzt gespeichert am\n " + reportDate);
				}
				else {
					setOnClickListenerNewGame(savedGameSlot1, 1);
					savedGameSlot1.setText("\nSlot 1\n\nneues Spiel\nbeginnen");
				}
				
				file2 = new File("/data/data/de.projectrpg/files/slot2.xml");
				if(file2.exists()){
					setOnClickListenerLoadGame(savedGameSlot2, 2);
					savedGameSlot2.setText("\nSlot 2\n\nzuletzt gespeichert\nam " + file2.lastModified());
				}
				else {
					setOnClickListenerNewGame(savedGameSlot2, 2);
					savedGameSlot2.setText("\nSlot 2\n\nneues Spiel\nbeginnen");
				}
				isBackButton = true;
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if(isBackButton){
			table.setVisibility(View.VISIBLE);
			newGame.setVisibility(View.VISIBLE);
			newGameImageView.setVisibility(View.VISIBLE);
			savedGameSlot1.setVisibility(View.INVISIBLE);
			savedGameSlot2.setVisibility(View.INVISIBLE);
			isBackButton = false;
		} else {
			finish();
		}
	}
	
	/**
	 * called when the activity is brought to the front e.g. on startup or exiting LevelActivity
	 */
	@Override
	public void onResume() {
		super.onResume();
		if(isBackButton){
			table.setVisibility(View.VISIBLE);
			newGame.setVisibility(View.VISIBLE);
			newGameImageView.setVisibility(View.VISIBLE);
			savedGameSlot1.setVisibility(View.INVISIBLE);
			savedGameSlot2.setVisibility(View.INVISIBLE);
			isBackButton = false;
		}
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
	
	private void setOnClickListenerNewGame(View view, final int slotNumber){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, LevelActivity.class);
				i.putExtra("slot", slotNumber);
				i.putExtra("newGame", true);
				startActivity(i);
			}
		});
	}
	
	private void setOnClickListenerLoadGame(View view, final int slotnumber){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, LevelActivity.class);
				i.putExtra("slot", slotnumber);
				i.putExtra("newGame", false);
				startActivity(i);
			}
		});
	}
	
	private void setOnClickListenerOptions(View view){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	private void setOnClickListenerHelp(View view){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void deactivateScrollingbyUser(View view){
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	class MyAsyncClouds extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			publishProgress("b4");
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			timerTask();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

		public void timerTask() {
			Timer timer;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					runOnUiThread(new Runnable() {

						public void run() {
							parallaxMidLayer.scrollBy(1, 0);
							scrolled1++;
							if (scrolled1 == 1920) {
								parallaxMidLayer.scrollBy(-1920, 0);
								scrolled1 = 0;
							}
						}
					});
				}
			}, 0, 45);
		}
	}

	class MyAsyncGround extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			publishProgress("b4");
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			timerTask();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

		public void timerTask() {
			Timer timer;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					runOnUiThread(new Runnable() {

						public void run() {
							parallaxFrontLayer.scrollBy(2, 0);
							scrolled2 = scrolled2 + 2;
							if (scrolled2 == 1920) {
								parallaxFrontLayer.scrollBy(-1920, 0);
								scrolled2 = 0;
							}
						}
					});
				}
			}, 0, 45);
		}

	}
	
	
}

