package de.projectrpg.start;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
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

import de.projectrpg.game.Controller;
import de.projectrpg.game.LevelActivity;
import de.projectrpg.util.OurMusicManager;

public class MainActivity extends Activity {

	private Typeface inhishan;
	private File file2;

	private TextView title;
	private TextView newGame;
	private TextView savedGameSlot1;
	private TextView savedGameSlot2;
	private TextView options;
	private TextView help;
	private TextView startNewGameButton1;
	private TextView startNewGameButton2;
	private TextView loadNewGameButton1;
	private TextView loadNewGameButton2;

	private ImageView newGameImageView;
	private ImageView optionsImageView;
	private ImageView helpImageView;

	private HorizontalScrollView parallaxMidLayer;
	private HorizontalScrollView parallaxFrontLayer;
	
	private TableLayout table;
	private TableLayout startGameTable;

	private int scrolled1 = 0;
	private int scrolled2 = 0;
	
	private boolean isBackButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Controller.initInstance();
		
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

		savedGameSlot2 = (TextView) findViewById(R.id.saved_game_slot2);
		savedGameSlot2.setTypeface(inhishan);

		options = (TextView) findViewById(R.id.options_textview);
		options.setTypeface(inhishan);

		help = (TextView) findViewById(R.id.help_textview);
		help.setTypeface(inhishan);
		
		startNewGameButton1 = (TextView) findViewById(R.id.new_game_button_slot1);
		startNewGameButton1.setTypeface(inhishan);
		startNewGameButton2 = (TextView) findViewById(R.id.new_game_button_slot2);
		startNewGameButton2.setTypeface(inhishan);
		loadNewGameButton1 = (TextView) findViewById(R.id.load_game_button_slot1);
		loadNewGameButton1.setTypeface(inhishan);
		loadNewGameButton2 = (TextView) findViewById(R.id.load_game_button_slot2);	
		loadNewGameButton2.setTypeface(inhishan);	

		newGameImageView = (ImageView) findViewById(R.id.new_game_imageview);
		optionsImageView = (ImageView) findViewById(R.id.options_imageview);
		helpImageView = (ImageView) findViewById(R.id.help_imageview);
		parallaxMidLayer = (HorizontalScrollView) findViewById(R.id.scroll);
		parallaxFrontLayer = (HorizontalScrollView) findViewById(R.id.scroll2);
		
		table = (TableLayout) findViewById(R.id.Tabelle);
		startGameTable = (TableLayout) findViewById(R.id.saved_games_slots);
		
	}
	
	private void setOnClickListenerStartGame(View view){
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				table.setVisibility(View.INVISIBLE);
				newGame.setVisibility(View.INVISIBLE);
				newGameImageView.setVisibility(View.INVISIBLE);
				startGameTable.setVisibility(View.VISIBLE);

				
				File file = new File("/data/data/de.projectrpg/files/slot1.xml");
				
				if(file.exists()){
					loadNewGameButton1.setTextColor(getResources().getColor(R.color.white));

					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

					long today = file.lastModified();      
					String lastModifiedDate = df.format(today);

					setOnClickListenerNewGame(startNewGameButton1, 1);
					setOnClickListenerLoadGame(loadNewGameButton1, 1);
					savedGameSlot1.setText("Slot 1\n\ngespeichert am\n" + lastModifiedDate);
				}
				else {
					loadNewGameButton1.setTextColor(getResources().getColor(R.color.darkgray));
					
					setOnClickListenerNewGame(startNewGameButton1, 1);
					savedGameSlot1.setText("Slot 1\n\nkein Spiel\ngespeichert");
				}
				
				file2 = new File("/data/data/de.projectrpg/files/slot2.xml");
				if(file2.exists()){
					loadNewGameButton2.setTextColor(getResources().getColor(R.color.white));
					
					setOnClickListenerNewGame(startNewGameButton2, 2);
					setOnClickListenerLoadGame(loadNewGameButton2, 2);
					savedGameSlot2.setText("Slot 2\n\ngespeichert am\n" + file2.lastModified());
				}
				else {
					loadNewGameButton2.setTextColor(getResources().getColor(R.color.darkgray));
					
					setOnClickListenerNewGame(startNewGameButton2, 2);
					savedGameSlot2.setText("Slot 2\n\nkein Spiel\ngespeichert");
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
			startGameTable.setVisibility(View.INVISIBLE);
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
			startGameTable.setVisibility(View.INVISIBLE);
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
				showNewGameNotification(slotNumber);
				
			}
		});
	}
	
	private void showNewGameNotification(final int slotNumber) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du wirklich ein neues Spiel beginnen? Zuvor gespeicherte Spielstände auf Slot " + slotNumber + " können überschrieben werden!");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				Intent i = new Intent(MainActivity.this, LevelActivity.class);
				i.putExtra("slot", slotNumber);
				i.putExtra("newGame", true);
				startActivity(i);
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	private void setOnClickListenerLoadGame(View view, final int slotNumber){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showLoadGameNotification(slotNumber);
			}
		});
	}

	private void showLoadGameNotification(final int slotNumber) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du das Spiel auf Slot " + slotNumber + " wirklich laden?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				Intent i = new Intent(MainActivity.this, LevelActivity.class);
				i.putExtra("slot", slotNumber);
				i.putExtra("newGame", false);
				startActivity(i);
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	private void setOnClickListenerOptions(View view){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
				startActivity(intent);
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

