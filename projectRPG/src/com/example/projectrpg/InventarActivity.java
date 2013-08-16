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
	
	private Slot testSlot;
	private Slot testSlot2;
	private Slot testSlot3;

	//private TextView statsPlayerlevel;
	private TextView statsAttack;
	//private TextView statsDefense;

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
	
	private ImageButton slot1ItemImage;
	private ImageButton slot2ItemImage;
	private ImageButton slot3ItemImage;
	private ImageButton slot4ItemImage;
	private ImageButton slot5ItemImage;
	private ImageButton slot6ItemImage;
	private ImageButton slot7ItemImage;
	private ImageButton slot8ItemImage;
	private ImageButton slot9ItemImage;
	private ImageButton slot10ItemImage;

	private InventarDatabase inventarDatabase;
	
	private ArrayList<Slot> slotList = null;
	private ArrayList<ImageButton> slotBackgroundList = null;
	private ArrayList<ImageButton> slotItemImageList = null;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupDatabaseConnection();
		setupUI();
		getSavedSlots();
		putItemsOnSlots();
		setupAllClickListeners();
	}

	private void putItemsOnSlots() {

		statsAttack.setText(slotList.get(0).getNumberOfItems());
		
		for(int i=0; i<10; i++){
			if (slotList.get(i).getItemName().equalsIgnoreCase("Schwert")){
			//	if (slotList.get(i).getNumberOfItems() == "1"){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_one));
			//	}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("2")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_two));
				}
			}
		}
		
	}

	private void setupAllClickListeners() {
		for(int i=0; i<10; i++ ){
			setupOnClickListener(slotBackgroundList.get(i), slotItemImageList.get(i), slotList.get(i));
		}
	}

	private void setupDatabaseConnection() {
		inventarDatabase = new InventarDatabase(this);
		inventarDatabase.open();
	}

	

	private void getSavedSlots() {
		try {
			slotList = inventarDatabase.getAllSlots();
			Thread.sleep(2000);
		} catch (Exception e) {
		}
	}

	private void setupOnClickListener(final ImageButton slotBackground, final ImageButton slotItem,
			final Slot slot) {
		slotItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slot.getNumberOfItems() != "0") {
					markThisSlot(slotBackground);
					slot.setMarked();
				}
			}
		});
	}

	private void markThisSlot(ImageButton slotBackground) {
		setSlotsUnmarked();
		slotBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_marked));

	}

	private void setSlotsUnmarked() {
		
		for(int i=0; i<10; i++ ){
			slotBackgroundList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));		
			slotList.get(i).setUnmarked();
		}
	}

	public void onDestroy() {
		super.onDestroy();
		inventarDatabase.close();
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
		
		slot1ItemImage = (ImageButton) findViewById(R.id.inventar_slot1_item_image);
		slot2ItemImage = (ImageButton) findViewById(R.id.inventar_slot2_item_image);
		slot3ItemImage = (ImageButton) findViewById(R.id.inventar_slot3_item_image);
		slot4ItemImage = (ImageButton) findViewById(R.id.inventar_slot4_item_image);
		slot5ItemImage = (ImageButton) findViewById(R.id.inventar_slot5_item_image);
		slot6ItemImage = (ImageButton) findViewById(R.id.inventar_slot6_item_image);
		slot7ItemImage = (ImageButton) findViewById(R.id.inventar_slot7_item_image);
		slot8ItemImage = (ImageButton) findViewById(R.id.inventar_slot8_item_image);
		slot9ItemImage = (ImageButton) findViewById(R.id.inventar_slot9_item_image);
		slot10ItemImage = (ImageButton) findViewById(R.id.inventar_slot10_item_image);

		slotList = new ArrayList<Slot>();
		
		slotBackgroundList = new ArrayList<ImageButton>();
		slotBackgroundList.add(slot1Background);
		slotBackgroundList.add(slot2Background);
		slotBackgroundList.add(slot3Background);
		slotBackgroundList.add(slot4Background);
		slotBackgroundList.add(slot5Background);
		slotBackgroundList.add(slot6Background);
		slotBackgroundList.add(slot7Background);
		slotBackgroundList.add(slot8Background);
		slotBackgroundList.add(slot9Background);
		slotBackgroundList.add(slot10Background);
		
		slotItemImageList = new ArrayList<ImageButton>();
		slotItemImageList.add(slot1ItemImage);
		slotItemImageList.add(slot2ItemImage);
		slotItemImageList.add(slot3ItemImage);
		slotItemImageList.add(slot4ItemImage);
		slotItemImageList.add(slot5ItemImage);
		slotItemImageList.add(slot6ItemImage);
		slotItemImageList.add(slot7ItemImage);
		slotItemImageList.add(slot8ItemImage);
		slotItemImageList.add(slot9ItemImage);
		slotItemImageList.add(slot10ItemImage);
		
		testSlot = new Slot(0, "Schwert", "Waffe", "2");
		testSlot2 = new Slot(1, "Schild", "Waffe", "1");
		testSlot3 = new Slot(0, "Schwert", "Waffe", "1");
		
		inventarDatabase.removeAll();
		inventarDatabase.insertSlotItem(testSlot);
		inventarDatabase.insertSlotItem(testSlot2);
		inventarDatabase.insertSlotItem(testSlot);
		inventarDatabase.insertSlotItem(testSlot2);
		inventarDatabase.insertSlotItem(testSlot);
		inventarDatabase.insertSlotItem(testSlot3);
		inventarDatabase.insertSlotItem(testSlot);
		inventarDatabase.insertSlotItem(testSlot3);
		inventarDatabase.insertSlotItem(testSlot);
		inventarDatabase.insertSlotItem(testSlot2);

		statsAttack = (TextView)findViewById(R.id.text_attack);
	}
	
}
