package com.example.projectrpg;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class InventarActivity extends Activity {
	
	//hat noch bugs
	
	
	private Slot testSlot;
	private Slot testSlot2;
	private Slot testSlot3;

	//private TextView statsPlayerlevel;
	//private TextView statsAttack;
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
	
	private ImageButton slot1ItemImage;
	private ImageButton slot2ItemImage;
	private ImageButton slot3ItemImage;
	private ImageButton slot4ItemImage;
	private ImageButton slot5ItemImage;
	private ImageButton slot6ItemImage;
	private ImageButton slot7ItemImage;
	private ImageButton slot8ItemImage;
	private ImageButton slot9ItemImage;
	
	private ImageButton slot1BackgroundSell;
	private ImageButton slot2BackgroundSell;
	private ImageButton slot3BackgroundSell;
	private ImageButton slot4BackgroundSell;
	private ImageButton slot5BackgroundSell;
	private ImageButton slot6BackgroundSell;
	private ImageButton slot7BackgroundSell;
	private ImageButton slot8BackgroundSell;
	private ImageButton slot9BackgroundSell;
	
	private ImageButton slot1ItemImageSell;
	private ImageButton slot2ItemImageSell;
	private ImageButton slot3ItemImageSell;
	private ImageButton slot4ItemImageSell;
	private ImageButton slot5ItemImageSell;
	private ImageButton slot6ItemImageSell;
	private ImageButton slot7ItemImageSell;
	private ImageButton slot8ItemImageSell;
	private ImageButton slot9ItemImageSell;

	private InventarDatabase inventarDatabase;
	
	private ArrayList<Slot> slotList = null;
	private ArrayList<Slot> slotListSell = null;
	private ArrayList<ImageButton> slotBackgroundList = null;
	private ArrayList<ImageButton> slotItemImageList = null;
	private ArrayList<ImageButton> slotBackgroundListSell = null;
	private ArrayList<ImageButton> slotItemImageListSell = null;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupDatabaseConnection();
		setupUI();
		getSavedSlots();
		setItemsOnOwnSlots();
		if(checkNeededInterface()){
			setItemsOnSellSlots();
		}
		else {
//			putEquipedItemsOnSlots();
		}
		setupAllClickListeners();
		
	}

	private boolean checkNeededInterface() {
		boolean isSellInterface = true; 	//irgendwie getNeededInterfaceFromLevelActivity();
		if(isSellInterface){
			for(int i=0; i<9; i++){
				slotBackgroundListSell.get(i).setVisibility(View.VISIBLE);
				slotItemImageListSell.get(i).setVisibility(View.VISIBLE);				
			}	
		} else {
			for(int i=0; i<9; i++){
				slotBackgroundListSell.get(i).setVisibility(View.INVISIBLE);
				slotItemImageListSell.get(i).setVisibility(View.INVISIBLE);
				slotListSell.clear();
			}
		}
		
		return isSellInterface;
	}

	

	private void setupAllClickListeners() {
		for(int i=0; i<9; i++ ){
			setupOnClickListener(slotBackgroundList.get(i), slotItemImageList.get(i), slotList.get(i));
		}
		
		if(checkNeededInterface()){
			for(int i=0; i<9; i++){
				setupOnLongClickListener(slotBackgroundList.get(i), slotItemImageList.get(i), slotList.get(i));
			}
			for(int i=0; i<9; i++ ){
				setupOnClickListener(slotBackgroundListSell.get(i), slotItemImageListSell.get(i), slotListSell.get(i));
			}
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
		
		//hier kriegt man die Infos, welche Items der Sack/Verkäufer/etc. beinhaltet...
				//z.b. slotListSell = givenItemList.getAllItems oder so...
		
		slotListSell.add(testSlot);
		slotListSell.add(testSlot2);
		slotListSell.add(testSlot3);
		slotListSell.add(testSlot3);
		slotListSell.add(testSlot3);
		slotListSell.add(testSlot3);
		slotListSell.add(testSlot3);
		slotListSell.add(testSlot3);
		slotListSell.add(testSlot3);		
	}
	private void setupOnLongClickListener(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground);
					slot.setMarked();
					checkLongClickedItem(slotBackground, slotItem, slot);
				}
				return false;
			}
		});
		
		
	}
	


	private void checkLongClickedItem(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		showSellNotification(slotBackground, slotItem, slot);
	}
	

	private void showSellNotification(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Attention");
		dialogBuilder.setMessage("Do you really want to sell this item?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Yes", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				sellItem(slotBackground, slotItem, slot);
				
			}
		});
		dialogBuilder.setNegativeButton("No", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
	});
		
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();		
	}
	
	private void sellItem(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		Slot tempSlot = new Slot(0, slot.getItemName(), slot.getItemType(), "1");
		boolean putItemInSlot = false;
		if (slot.getNumberOfItems().equalsIgnoreCase("1")){
			slot.eraseSlot();
		} else {
			slot.setNumberOfItems(String.valueOf(Integer.parseInt(slot.getNumberOfItems())-1));
		}
		setSlotsUnmarked();
		for(int i=0; i<9; i++){
			if(tempSlot.getItemName().equalsIgnoreCase(slotListSell.get(i).getItemName()) && putItemInSlot == false){
				if(slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					//do nothing
				} else{
					slotListSell.get(i).setNumberOfItems("" + (Integer.parseInt(slotListSell.get(i).getNumberOfItems())+1));
					putItemInSlot = true;
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			if (slotListSell.get(i).getItemName().equalsIgnoreCase("leer") && putItemInSlot == false) {
				slotListSell.set(i, tempSlot);
				putItemInSlot = true;
			}
		}
		setItemsOnOwnSlots();
		setItemsOnSellSlots();
		setupAllClickListeners();
	}
	
	private void setupOnClickListener(final ImageButton slotBackground, final ImageButton slotItem,
			final Slot slot) {
		slotItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
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
		
		for(int i=0; i<9; i++ ){
			slotBackgroundList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));		
			slotList.get(i).setUnmarked();
		}
		for(int i=0; i<9; i++ ){
			slotBackgroundListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));		
			slotList.get(i).setUnmarked();
		}
	}

	public void onDestroy() {
		super.onDestroy();
		inventarDatabase.close();
	}
	
private void setItemsOnOwnSlots() {
		
		for(int i=0; i<9; i++){
			if (slotList.get(i).getItemName().equalsIgnoreCase("leer")){
				slotItemImageList.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
			}		
			if (slotList.get(i).getItemName().equalsIgnoreCase("Schwert")){
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("1")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_one));
				}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("2")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_two));
				}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("3")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_three));
				}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("4")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_four));
				}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_five));
				}
			}
		}
		
		
	}

	private void setItemsOnSellSlots() {
		
		for(int i=0; i<9; i++){
			if (slotListSell.get(i).getItemName().equalsIgnoreCase("leer")){
				slotItemImageListSell.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			
			if (slotListSell.get(i).getItemName().equalsIgnoreCase("Schwert")){
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("1")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_one));
				}
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("2")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_two));
				}
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("3")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_three));
				}
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("4")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_four));
				}
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_five));
				}
			}
		}
		
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
		
		slot1ItemImage = (ImageButton) findViewById(R.id.inventar_slot1_item_image);
		slot2ItemImage = (ImageButton) findViewById(R.id.inventar_slot2_item_image);
		slot3ItemImage = (ImageButton) findViewById(R.id.inventar_slot3_item_image);
		slot4ItemImage = (ImageButton) findViewById(R.id.inventar_slot4_item_image);
		slot5ItemImage = (ImageButton) findViewById(R.id.inventar_slot5_item_image);
		slot6ItemImage = (ImageButton) findViewById(R.id.inventar_slot6_item_image);
		slot7ItemImage = (ImageButton) findViewById(R.id.inventar_slot7_item_image);
		slot8ItemImage = (ImageButton) findViewById(R.id.inventar_slot8_item_image);
		slot9ItemImage = (ImageButton) findViewById(R.id.inventar_slot9_item_image);
		
		slot1BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot1_sell);
		slot2BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot2_sell);
		slot3BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot3_sell);
		slot4BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot4_sell);
		slot5BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot5_sell);
		slot6BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot6_sell);
		slot7BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot7_sell);
		slot8BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot8_sell);
		slot9BackgroundSell = (ImageButton) findViewById(R.id.inventar_slot9_sell);
		
		slot1ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot1_item_image_sell);
		slot2ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot2_item_image_sell);
		slot3ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot3_item_image_sell);
		slot4ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot4_item_image_sell);
		slot5ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot5_item_image_sell);
		slot6ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot6_item_image_sell);
		slot7ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot7_item_image_sell);
		slot8ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot8_item_image_sell);
		slot9ItemImageSell = (ImageButton) findViewById(R.id.inventar_slot9_item_image_sell);
		

		slotList = new ArrayList<Slot>();
		slotListSell = new ArrayList<Slot>();
		
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
		
		slotBackgroundListSell = new ArrayList<ImageButton>();
		slotBackgroundListSell.add(slot1BackgroundSell);
		slotBackgroundListSell.add(slot2BackgroundSell);
		slotBackgroundListSell.add(slot3BackgroundSell);
		slotBackgroundListSell.add(slot4BackgroundSell);
		slotBackgroundListSell.add(slot5BackgroundSell);
		slotBackgroundListSell.add(slot6BackgroundSell);
		slotBackgroundListSell.add(slot7BackgroundSell);
		slotBackgroundListSell.add(slot8BackgroundSell);
		slotBackgroundListSell.add(slot9BackgroundSell);
		
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
		
		slotItemImageListSell = new ArrayList<ImageButton>();
		slotItemImageListSell.add(slot1ItemImageSell);
		slotItemImageListSell.add(slot2ItemImageSell);
		slotItemImageListSell.add(slot3ItemImageSell);
		slotItemImageListSell.add(slot4ItemImageSell);
		slotItemImageListSell.add(slot5ItemImageSell);
		slotItemImageListSell.add(slot6ItemImageSell);
		slotItemImageListSell.add(slot7ItemImageSell);
		slotItemImageListSell.add(slot8ItemImageSell);
		slotItemImageListSell.add(slot9ItemImageSell);
		
		
		testSlot = new Slot(0, "Schwert", "Waffe", "5");
		testSlot2 = new Slot(0, "Schwert", "Waffe", "4");
		testSlot3 = new Slot(0, "leer", "leer", "0");
		
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
	}
}

 

