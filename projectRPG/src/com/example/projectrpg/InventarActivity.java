package com.example.projectrpg;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class InventarActivity extends Activity {
	
	//hat noch bugs

	private TextView statsPlayerlevel;
	private TextView statsAttack;
	private TextView statsDefense;

	private ImageButton slot1Background;
	private ImageButton slot2Background;
	private ImageButton slot3Background;
	private ImageButton slot4Background;
	private ImageButton slot5Background;
	private ImageButton slot6Background;
	private ImageButton slot7Background;
	private ImageButton slot8Background;
	private ImageButton slot9Background;
	private ImageButton slot10Background;

	private Slot slot1;
	private Slot slot2;
	private Slot slot3;
	private Slot slot4;
	private Slot slot5;
	private Slot slot6;
	private Slot slot7;
	private Slot slot8;
	private Slot slot9;
	private Slot slot10;

	private InventarDatabase inventarDatabase;
	private ArrayList<Slot> slotList = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupDatabaseConnection();
		setupUI();
		getSavedSlots();
		setupAllClickListeners();
	}

	private void setupAllClickListeners() {
		setupOnClickListener(slot1Background, slot1);
		setupOnClickListener(slot2Background, slot2);
		setupOnClickListener(slot3Background, slot3);
		setupOnClickListener(slot4Background, slot4);
		setupOnClickListener(slot5Background, slot5);
		setupOnClickListener(slot6Background, slot6);
		setupOnClickListener(slot7Background, slot7);
		setupOnClickListener(slot8Background, slot8);
		setupOnClickListener(slot9Background, slot9);
		setupOnClickListener(slot10Background, slot10);
	}

	private void setupDatabaseConnection() {
		inventarDatabase = new InventarDatabase(this);
		inventarDatabase.open();
	}

	private void setupUI() {
		setContentView(R.layout.activity_inventar);

		slot1Background = (ImageButton) findViewById(R.id.inventar_slot1);
		slot2Background = (ImageButton) findViewById(R.id.inventar_slot2);
		slot3Background = (ImageButton) findViewById(R.id.inventar_slot3);
		slot4Background = (ImageButton) findViewById(R.id.inventar_slot4);
		slot5Background = (ImageButton) findViewById(R.id.inventar_slot5);
		slot6Background = (ImageButton) findViewById(R.id.inventar_slot6);
		slot7Background = (ImageButton) findViewById(R.id.inventar_slot7);
		slot8Background = (ImageButton) findViewById(R.id.inventar_slot8);
		slot9Background = (ImageButton) findViewById(R.id.inventar_slot9);
		slot10Background = (ImageButton) findViewById(R.id.inventar_slot10);

		slot1 = new Slot();
		slot2 = new Slot();
		slot3 = new Slot();
		slot4 = new Slot();
		slot5 = new Slot();
		slot6 = new Slot();
		slot7 = new Slot();
		slot8 = new Slot();
		slot9 = new Slot();
		slot10 = new Slot();

		slotList = new ArrayList<Slot>();

	}

	private void getSavedSlots() {
		try {
			slotList = inventarDatabase.getAllSlots();
			Thread.sleep(2000);
		} catch (Exception e) {
		}
		runOnUiThread(notifyList);
	}

	private Runnable notifyList = new Runnable() {

		@Override
		public void run() {
			if (slotList != null && slotList.size() > 0) {
				for (int i = 0; i < slotList.size(); i++) {
					if (i == 0) {
						slot1 = slotList.get(i);
					}
					if (i == 1) {
						slot2 = slotList.get(i);
					}
					if (i == 2) {
						slot3 = slotList.get(i);
					}
					if (i == 3) {
						slot4 = slotList.get(i);
					}
					if (i == 4) {
						slot5 = slotList.get(i);
					}
					if (i == 5) {
						slot6 = slotList.get(i);
					}
					if (i == 6) {
						slot7 = slotList.get(i);
					}
					if (i == 7) {
						slot8 = slotList.get(i);
					}
					if (i == 8) {
						slot9 = slotList.get(i);
					}
					if (i == 9) {
						slot10 = slotList.get(i);
					}
				}
			}
		}
	};

	private void setupOnClickListener(final ImageButton slotImage,
			final Slot slot) {
		slotImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slot.getNumberOfItems() != 0) {
					markThisSlot(slotImage);
					slot.setMarked();
				}
			}
		});
	}

	private void markThisSlot(ImageButton slotBackground) {
		setSlotsUnmarked();
		slotBackground
				.setBackgroundColor(getResources().getColor(R.color.blue));

	}

	private void setSlotsUnmarked() {
		slot1Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot2Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot3Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot4Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot5Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot6Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot7Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot8Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot9Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
		slot10Background.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_slot));
	}

	public void onDestroy() {
		super.onDestroy();
		inventarDatabase.close();
	}
}
