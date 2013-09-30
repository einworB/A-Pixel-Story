package de.apixelstory.start;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import de.apixelstory.R;
import de.apixelstory.game.Controller;
import de.apixelstory.game.LevelActivity;
import de.apixelstory.util.OurMusicManager;

/**
 * This is the Activity where the player starts this application. 
 * From this mainmenu, the player can start the game, go to the
 * options activity or go to the help activity
 */
public class MainActivity extends Activity {

	
	//=======================================================================FIELDS========================================================================================	
	
	/** typeface, which is used for the text */
	private Typeface inhishan;

	
	/** textView for title of the game */
	private TextView title;
	
	/** textView for startGame button */
	private TextView startGame;
	
	/** textView for savedGame slot 1 */
	private TextView savedGameSlot1;
	
	/** textView for savedGame slot 2 */
	private TextView savedGameSlot2;
	
	/** textView for options button */
	private TextView options;
	
	/** textView for help button */
	private TextView help;
	
	/** textView for startNewGame button 1 */
	private TextView startNewGameButton1;
	
	/** textView for startNewGame button 2 */
	private TextView startNewGameButton2;
	
	/** textView for loadGame button 1 */
	private TextView loadGameButton1;
	
	/** textView for loadGame button 2 */
	private TextView loadGameButton2;

	
	/** imageView for the startGame button */
	private ImageView startGameImageView;
	
	/** imageView for the options button */
	private ImageView optionsImageView;
	
	/** imageView for the help button */
	private ImageView helpImageView;

	
	/** scrollView for the mid layer of the parallax background */
	private HorizontalScrollView parallaxMidLayer;
	
	/** scrollView for the front layer of the parallax background */
	private HorizontalScrollView parallaxFrontLayer;
	
	
	/** tableLayout for the the buttons startGame, options and help */
	private TableLayout table;
	
	/** tableLayout for the other buttons (newGame & loadGame) */
	private TableLayout startGameTable;

	/** counts the amount of pixels, the scrollView for the mid layer is scrolling */
	private int scrolled1;
	
	/** counts the amount of pixels, the scrollView for the front layer is scrolling */
	private int scrolled2;
	
	/** boolean, which decides, if the back button leaves the 
	 * application or just goes one step back in the menu */
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

	/** sets up all OnClickListeners */
	private void setupClickListener() {
		setOnClickListenerStartGame(startGameImageView);
		setOnClickListenerStartGame(startGame);
		setOnClickListenerOptions(optionsImageView);
		setOnClickListenerOptions(options);
		setOnClickListenerHelp(helpImageView);
		setOnClickListenerHelp(help);
		deactivateScrollingbyUser(parallaxMidLayer);
		deactivateScrollingbyUser(parallaxFrontLayer);
	}

	/** sets up the whole user interface */
	private void setupUI() {
		setContentView(R.layout.activity_main);

		inhishan = Typeface.createFromAsset(getAssets(), "fonts/inhishan.ttf");

		title = (TextView) findViewById(R.id.title_textview);
		title.setTypeface(inhishan);

		startGame = (TextView) findViewById(R.id.new_game_textview);
		startGame.setTypeface(inhishan);

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
		loadGameButton1 = (TextView) findViewById(R.id.load_game_button_slot1);
		loadGameButton1.setTypeface(inhishan);
		loadGameButton2 = (TextView) findViewById(R.id.load_game_button_slot2);	
		loadGameButton2.setTypeface(inhishan);	

		startGameImageView = (ImageView) findViewById(R.id.new_game_imageview);
		optionsImageView = (ImageView) findViewById(R.id.options_imageview);
		helpImageView = (ImageView) findViewById(R.id.help_imageview);
		parallaxMidLayer = (HorizontalScrollView) findViewById(R.id.scroll);
		parallaxFrontLayer = (HorizontalScrollView) findViewById(R.id.scroll2);
		
		table = (TableLayout) findViewById(R.id.Tabelle);
		startGameTable = (TableLayout) findViewById(R.id.saved_games_slots);
		
	}
	
	/** sets OnClickListener to show the slots to start a game*/
	private void setOnClickListenerStartGame(View view){
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				table.setVisibility(View.INVISIBLE);
				startGame.setVisibility(View.INVISIBLE);
				startGameImageView.setVisibility(View.INVISIBLE);
				startGameTable.setVisibility(View.VISIBLE);

				
				File file = new File("/data/data/de.projectrpg/files/slot1.xml");
				
				if(file.exists()){
					loadGameButton1.setTextColor(getResources().getColor(R.color.white));

					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

					long today = file.lastModified();      
					String lastModifiedDate = df.format(today);

					setOnClickListenerNewGame(startNewGameButton1, 1);
					setOnClickListenerLoadGame(loadGameButton1, 1);
					savedGameSlot1.setText("Slot 1\n\ngespeichert am\n" + lastModifiedDate);
				}
				else {
					loadGameButton1.setTextColor(getResources().getColor(R.color.darkgray));
					
					setOnClickListenerNewGame(startNewGameButton1, 1);
					savedGameSlot1.setText("Slot 1\n\nkein Spiel\ngespeichert");
				}
				
				File file2 = new File("/data/data/de.projectrpg/files/slot2.xml");
				if(file2.exists()){
					loadGameButton2.setTextColor(getResources().getColor(R.color.white));

					DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

					long today = file2.lastModified();      
					String lastModifiedDate = df.format(today);

					setOnClickListenerNewGame(startNewGameButton2, 2);
					setOnClickListenerLoadGame(loadGameButton2, 2);
					savedGameSlot2.setText("Slot 2\n\ngespeichert am\n" + lastModifiedDate);
				}
				else {
					loadGameButton2.setTextColor(getResources().getColor(R.color.darkgray));
					
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
			startGame.setVisibility(View.VISIBLE);
			startGameImageView.setVisibility(View.VISIBLE);
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
			startGame.setVisibility(View.VISIBLE);
			startGameImageView.setVisibility(View.VISIBLE);
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
	
	/** sets up OnClickListener, which starts a new game */
	private void setOnClickListenerNewGame(View view, final int slotNumber){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNewGameNotification(slotNumber);
				
			}
		});
	}
	
	/** shows Notification, which checks, if the player really wants to start a new Game */
	private void showNewGameNotification(final int slotNumber) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		final File file = new File("/data/data/de.projectrpg/files/slot" + slotNumber + ".xml");
		
		if(file.exists()){
			dialogBuilder.setMessage("Willst du wirklich ein neues Spiel beginnen? Zuvor gespeicherte Spielstände auf Slot " + slotNumber + " können überschrieben werden!");
		} else {
			dialogBuilder.setMessage("Willst du ein neues Spiel beginnen?");
		}
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				file.delete();
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

	/** sets up OnClickListener, which loads a previously saved game */
	private void setOnClickListenerLoadGame(View view, final int slotNumber){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showLoadGameNotification(slotNumber);
			}
		});
	}

	/** shows Notification, which checks, if the player really wants to load a saved game */
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

	/** sets up OnClickListener, which shows the options activity */
	private void setOnClickListenerOptions(View view){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/** sets up OnClickListener, which shows the help activity */
	private void setOnClickListenerHelp(View view){
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, HelpActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/** deactivates scrolling of a View by User */
	private void deactivateScrollingbyUser(View view){
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	/** AsyncTask, which scrolls the mid layer of the parallax background */
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

	/** AsyncTask, which scrolls the front layer of the parallax background */
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

