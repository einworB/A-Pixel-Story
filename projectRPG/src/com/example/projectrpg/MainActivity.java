package com.example.projectrpg;

import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
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

	private HorizontalScrollView parallaxMidLayer;
	private HorizontalScrollView parallaxFrontLayer;
	
	private int scrolled1 = 0;
	private int scrolled2 = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();
		setupClickListener();
		new MyAsyncClouds().execute("blub");
		new MyAsyncGround().execute("blub");
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

		parallaxMidLayer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		parallaxFrontLayer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	private void setupUI() {
		setContentView(R.layout.activity_main);

		inhishan = Typeface.createFromAsset(getAssets(), "fonts/inhishan.ttf");

		title = (TextView) findViewById(R.id.title_textview);
		title.setTypeface(inhishan);

		newGame = (TextView) findViewById(R.id.new_game_textview);
		newGame.setTypeface(inhishan);

		loadGame = (TextView) findViewById(R.id.load_game_textview);
		loadGame.setTypeface(inhishan);

		options = (TextView) findViewById(R.id.options_textview);
		options.setTypeface(inhishan);

		help = (TextView) findViewById(R.id.help_textview);
		help.setTypeface(inhishan);

		newGameImageView = (ImageView) findViewById(R.id.new_game_imageview);
		loadGameImageView = (ImageView) findViewById(R.id.load_game_imageview);
		optionsImageView = (ImageView) findViewById(R.id.options_imageview);
		helpImageView = (ImageView) findViewById(R.id.help_imageview);
		parallaxMidLayer = (HorizontalScrollView) findViewById(R.id.scroll);
		parallaxFrontLayer = (HorizontalScrollView) findViewById(R.id.scroll2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
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
							if (scrolled1 == 1280) {
								parallaxMidLayer.scrollBy(-1280, 0);
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
							if (scrolled2 == 1280) {
								parallaxFrontLayer.scrollBy(-1280, 0);
								scrolled2 = 0;
							}
						}
					});
				}
			}, 0, 45);
		}

	}

}
