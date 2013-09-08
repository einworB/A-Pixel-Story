
package de.projectrpg.inventory;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectrpg.R;

import de.projectrpg.database.Armor;
import de.projectrpg.database.HealItem;
import de.projectrpg.database.Item;
import de.projectrpg.database.Weapon;
import de.projectrpg.game.Controller;

public class InventarActivity extends Activity {
	
	
	private Controller controller;
	
	private static final String EQUIP_HEAD = "0";
	private static final String EQUIP_UPPER_BODY = "1";
	private static final String EQUIP_HANDS = "2";
	private static final String EQUIP_LOWER_BODY = "3";
	private static final String EQUIP_FEET = "4";
	
	private TextView itemName;
	private TextView titleSellEquip;
	private TextView moneyTextView;
	private TextView itemDetails;
	
	private TextView equipSlotTypeText1;
	private TextView equipSlotTypeText2;
	private TextView equipSlotTypeText3;
	private TextView equipSlotTypeText4;
	private TextView equipSlotTypeText5;
	private TextView weaponSlotTypeText;
	
	private Button removeItemButton;
	private ImageView removeItemBackground;
	
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
	
	private ImageButton slot1BackgroundEquip;
	private ImageButton slot2BackgroundEquip;
	private ImageButton slot3BackgroundEquip;
	private ImageButton slot4BackgroundEquip;
	private ImageButton slot5BackgroundEquip;
	
	private ImageButton slot1ItemImageEquip;
	private ImageButton slot2ItemImageEquip;
	private ImageButton slot3ItemImageEquip;
	private ImageButton slot4ItemImageEquip;
	private ImageButton slot5ItemImageEquip;
	
	private ImageButton weaponSlotBackground;
	
	private ImageButton weaponSlotItemImage;
	
	private ImageButton playerEquip;
	
	private ArrayList<String> sortedItemNames = null;
	private ArrayList<Drawable> sortedItemImages = null;
	private ArrayList<Slot> slotList = null;
	private ArrayList<Slot> slotListSell = null;
	private ArrayList<Slot> slotListEquip = null;
	private ArrayList<ImageButton> slotBackgroundList = null;
	private ArrayList<ImageButton> slotItemImageList = null;
	private ArrayList<ImageButton> slotBackgroundListSell = null;
	private ArrayList<ImageButton> slotItemImageListSell = null;
	private ArrayList<ImageButton> slotBackgroundListEquip = null;
	private ArrayList<ImageButton> slotItemImageListEquip = null;
	
	private Slot weaponSlot = null;
	private String clickedItem = "leer";
	private boolean isInInventory;
	private int healValue = 0;
	private boolean isSellInterface;
	
	private ArrayList<Item> inventoryList = null;
	private ArrayList<Item> inventoryListMerchant = null;
	private ArrayList<Item> tempInventoryList = null;
	private Armor[] equipedArmorList = null;
	private Weapon equippedWeapon;
	
	private boolean removedItem;
	private boolean putItemInSlot;
	
	private Slot tempSlot;
	private Armor tempArmor;
	private Item tempItem;
	private Weapon tempWeapon;
	private HealItem tempHealItem;
	
	private int inventoryUsedSlotCounter = 0;
	private int money;
	private boolean slotsFull = false;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();
		Log.d("sell", "setupUI beendet");
		getSavedItems();
		Log.d("sell", "getSavedItems beendet");
		setItemsOnOwnSlots();
		Log.d("sell", "setItemsOnOwnSlots beendet");
		if(checkNeededInterface()){
			setItemsOnSellSlots();
			setMoneyTextView(controller.getGold());
			Log.d("sell", "setItemsSellSlots beendet");
		}
		else {
			setItemsOnEquipSlots();
			setMoneyTextView(controller.getGold());
			Log.d("sell", "setItemsOnEquipSlots beendet");

		}
		Log.d("sell", "checkNeededInterface beendet");
		setupAllClickListeners();
		Log.d("sell", "setupAllClickListeners beendet");
		
	}

	private boolean checkNeededInterface() {
		isSellInterface = getIntent().getExtras().getBoolean("isMerchant"); 	//irgendwie getNeededInterfaceFromLevelActivity();
		if(isSellInterface){
			Log.d("sell", "in if-abfrage");
			for(int i=0; i<9; i++){
				slotBackgroundListSell.get(i).setVisibility(View.VISIBLE);
				slotItemImageListSell.get(i).setVisibility(View.VISIBLE);
				Log.d("sell", "sell visible gemacht beendet");
				if(i<5){
					slotBackgroundListEquip.get(i).setVisibility(View.INVISIBLE);
					slotItemImageListEquip.get(i).setVisibility(View.INVISIBLE);
					Log.d("sell", "slot invisible gemacht beendet");
				}
			}	
			weaponSlotBackground.setVisibility(View.INVISIBLE);
			weaponSlotItemImage.setVisibility(View.INVISIBLE);
			playerEquip.setVisibility(View.INVISIBLE);
			Log.d("sell", "slot invisible2 gemacht beendet");

			removeItemButton.setVisibility(View.INVISIBLE);
			removeItemBackground.setVisibility(View.INVISIBLE);
			
			equipSlotTypeText1.setVisibility(View.INVISIBLE);
			equipSlotTypeText2.setVisibility(View.INVISIBLE);
			equipSlotTypeText3.setVisibility(View.INVISIBLE);
			equipSlotTypeText4.setVisibility(View.INVISIBLE);
			equipSlotTypeText5.setVisibility(View.INVISIBLE);
			weaponSlotTypeText.setVisibility(View.INVISIBLE);
			
			slotListEquip.clear();
			titleSellEquip.setText(R.string.title_sell);
		} else {
			for(int i=0; i<9; i++){
				slotBackgroundListSell.get(i).setVisibility(View.INVISIBLE);
				slotItemImageListSell.get(i).setVisibility(View.INVISIBLE);
				if(i<5){
					slotBackgroundListEquip.get(i).setVisibility(View.VISIBLE);
					slotItemImageListEquip.get(i).setVisibility(View.VISIBLE);
				}
			}

			weaponSlotBackground.setVisibility(View.VISIBLE);
			weaponSlotItemImage.setVisibility(View.VISIBLE);
			playerEquip.setVisibility(View.VISIBLE);
			
			itemDetails.setVisibility(View.VISIBLE);
			removeItemButton.setVisibility(View.VISIBLE);
			removeItemBackground.setVisibility(View.VISIBLE);
			
			equipSlotTypeText1.setVisibility(View.VISIBLE);
			equipSlotTypeText2.setVisibility(View.VISIBLE);
			equipSlotTypeText3.setVisibility(View.VISIBLE);
			equipSlotTypeText4.setVisibility(View.VISIBLE);
			equipSlotTypeText5.setVisibility(View.VISIBLE);
			weaponSlotTypeText.setVisibility(View.VISIBLE);


			slotListSell.clear();
			titleSellEquip.setText(R.string.title_equip);

		}
		Log.d("sell", "checkNeededInterface beendet");
		return isSellInterface;
	}

	
	private void setupAllClickListeners() {
		for (int i = 0; i < 9; i++) {
			setupOnClickListener(slotBackgroundList.get(i),	slotItemImageList.get(i), slotList.get(i));

			if (checkNeededInterface()) {
				setupOnLongClickListenerSell(slotBackgroundList.get(i),slotItemImageList.get(i), slotList.get(i));
				setupOnLongClickListenerBuy(slotBackgroundListSell.get(i),slotItemImageListSell.get(i), slotListSell.get(i));
				setupOnClickListenerSell(slotBackgroundListSell.get(i),slotItemImageListSell.get(i), slotListSell.get(i));
			} else {
				setupOnLongClickListenerAddEquip(slotBackgroundList.get(i),slotItemImageList.get(i), slotList.get(i));
				
				if(i<5){
					setupOnLongClickListenerRemoveEquip(slotBackgroundListEquip.get(i),slotItemImageListEquip.get(i), slotListEquip.get(i));
					setupOnClickListenerEquip(slotBackgroundListEquip.get(i), slotItemImageListEquip.get(i), slotListEquip.get(i));
				}
				setupOnLongClickListenerRemoveWeapon(weaponSlotBackground, weaponSlotItemImage, weaponSlot);
				setupOnClickListenerEquip(weaponSlotBackground,	weaponSlotItemImage, weaponSlot);
				Log.d("remove", "clickListener ausgelöst");
				removeItemButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(clickedItem.equalsIgnoreCase("leer")){
							
						} else {
							showRemoveItemNotification();
							Log.d("remove", clickedItem);
						}
					}
				});
			}
		}
		
	}

	private void showUseItemNotification() {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dich mit 1x " + clickedItem + " heilen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				useItem();
				
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
	});
		
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	private void showRemoveItemNotification() {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du 1x " + clickedItem + " wirklich entfernen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				removeItem();
				
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
	});
		
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	private void useItem() {
		removedItem = false;	
		tempInventoryList = new ArrayList<Item>();
		for(int i=0; i<inventoryList.size(); i++){
			if(inventoryList.get(i).getName().equalsIgnoreCase(clickedItem) && removedItem == false){
				removedItem = true;
				
				tempHealItem = new HealItem(inventoryList.get(i).getName(), inventoryList.get(i).getLevelNeeded(), "healItem", healValue);
				controller.heal(tempHealItem);
			}
			else{
				tempInventoryList.add(inventoryList.get(i));
			}
		}	
		setSlotsUnmarked();
		controller.setInventory(tempInventoryList);

		slotList.clear();
		for(int i=0; i<9; i++){
			slotList.add(new Slot(0,"leer", "leer", "0"));
		}
		
		resetItemNameTextView();
		
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		setupAllClickListeners();		
	}


	private void removeItem() {
		removedItem = false;
		
		if(isInInventory){
			tempInventoryList = new ArrayList<Item>();
			for(int i=0; i<inventoryList.size(); i++){
				if(inventoryList.get(i).getName().equalsIgnoreCase(clickedItem) && removedItem == false){
					removedItem = true;
				}
				else{
					tempInventoryList.add(inventoryList.get(i));
				}
			}	
			setSlotsUnmarked();
			controller.setInventory(tempInventoryList);
	
			slotList.clear();
			for(int i=0; i<9; i++){
				slotList.add(new Slot(0,"leer", "leer", "0"));
			}	
		} else {
			Log.d("remove", "In else drinnen");
					
			for(int i=0; i<5; i++){
				Log.d("remove", "In for-schleife drinnen" + equipedArmorList.length + clickedItem);
				if(slotListEquip.get(i).getItemName().equalsIgnoreCase(clickedItem)){
					Log.d("remove", "In if-schleife drinnen");
					controller.removeEquippedArmor(i);
					Log.d("remove", "In if2 drinnen");
					removedItem = true;
				}
			}
			Log.d("remove", "item gelöscht");

			if(removedItem == false){
				controller.removeEquippedWeapon(equippedWeapon);
				Log.d("remove", "weapon if-abfrage drinnen");
			}
			Log.d("remove", "weapon if-abfrage übersprungen");

			
			weaponSlot = new Slot(0, "leer", "leer", "0");

			slotList.clear();
			for(int i=0; i<9; i++){
				slotList.add(new Slot(0,"leer", "leer", "0"));
			}
			
			slotListEquip.clear();
			for(int i=0; i<5; i++){
				slotListEquip.add(new Slot(0,"leer", "leer", "0"));
				Log.d("remove", "slots geklärt");

			}
			setSlotsUnmarked();
		}
		
		resetItemNameTextView();
		clickedItem = "leer";
		
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		setupAllClickListeners();
		Log.d("remove", "fertig");

	}

	private void getSavedItems() {
		inventoryUsedSlotCounter = 0;
		money = controller.getGold();

		putItemInSlot = false;
		tempSlot = new Slot(0, "leer", "leer", "0");
		
		
		inventoryList = new ArrayList<Item>();
		
		inventoryList = controller.getInventory();
		Log.d("testArmor", "" + inventoryList.size());
		
		if(checkNeededInterface()){
			inventoryListMerchant = new ArrayList<Item>();
			
			if(controller.getLevel() == 1){
				inventoryListMerchant.add(controller.getItemByName("Holzknüppel"));
				inventoryListMerchant.add(controller.getItemByName("Vertrocknetes Brot"));
				inventoryListMerchant.add(controller.getItemByName("Viermal geflickte Lederhose"));
			}
			if(controller.getLevel() == 2){
				inventoryListMerchant.add(controller.getItemByName("Verstärkter Lederhelm"));
				inventoryListMerchant.add(controller.getItemByName("Stumpfe Kupferaxt"));
				inventoryListMerchant.add(controller.getItemByName("fast frischer Schinken"));
			}
			if(controller.getLevel() == 3){
				inventoryListMerchant.add(controller.getItemByName("Gebratene Hähnchenkeule"));
				inventoryListMerchant.add(controller.getItemByName("bronzener Brustpanzer"));
				inventoryListMerchant.add(controller.getItemByName("Etwas zu schwerer Eisenhammer"));
			}
			if(controller.getLevel() >= 4){
				inventoryListMerchant.add(controller.getItemByName("Schmiedeeisener Streitkolben"));
				inventoryListMerchant.add(controller.getItemByName("Goldene Stiefel"));
				inventoryListMerchant.add(controller.getItemByName("überaus köstlicher Apfelkuchen"));
			}
			
			Log.d("kauf", inventoryListMerchant.get(0).getName());
			Log.d("kauf", inventoryListMerchant.get(1).getName());
			Log.d("kauf", inventoryListMerchant.get(2).getName());
		} else {
			equipedArmorList = new Armor[5];
			equipedArmorList = controller.getArmor();
			
			equippedWeapon = controller.getEquippedWeapon();
		}

	
		
			 
	
			for (int i = 0; i < inventoryList.size(); i++) {
				
				putItemInSlot = false;
				for (int j = 0; j < 9; j++) {
					if (inventoryList.get(i).getName().equalsIgnoreCase(slotList.get(j).getItemName())) {
						if (slotList.get(j).getNumberOfItems()
								.equalsIgnoreCase("5")) {
	
						} else {
							slotList.set(j, new Slot(j, inventoryList.get(i).getName(), inventoryList.get(i).getItemType(), "" + (Integer.parseInt(slotList.get(j).getNumberOfItems())+1)));
							slotList.get(j).setlevelNeeded(inventoryList.get(i).getLevelNeeded());
							if(inventoryList.get(i) instanceof Armor){
								slotList.get(j).setDefenseValue(((Armor)inventoryList.get(i)).getDefenseValue());
								slotList.get(j).setItemType("" + ((Armor)inventoryList.get(i)).getType());
							} 
							if(inventoryList.get(i) instanceof Weapon){
								slotList.get(j).setAttackValue(((Weapon)inventoryList.get(i)).getAttackValue());
								slotList.get(j).setItemType("" + ((Weapon)inventoryList.get(i)).getType());
							}
							putItemInSlot = true;
						}
					}
					if (slotList.get(j).getItemName().equalsIgnoreCase("leer")
							&& putItemInSlot == false) {
						if(inventoryUsedSlotCounter == 9){
							slotsFull = true;
						} else {
						
							if(inventoryList.get(i) instanceof Armor){
								tempSlot = new Slot(j, inventoryList.get(i).getName(),
										"" + ((Armor)inventoryList.get(i)).getType(), "1");
							}	
							if(inventoryList.get(i) instanceof Weapon){
								tempSlot = new Slot(j, inventoryList.get(i).getName(),
										"" + ((Weapon)inventoryList.get(i)).getType(), "1");
							}
							if(inventoryList.get(i) instanceof HealItem){
								tempSlot = new Slot(j, inventoryList.get(i).getName(),
										"5", "1");
							}
							slotList.set(j, tempSlot);
							slotList.get(j).setlevelNeeded(inventoryList.get(i).getLevelNeeded());
							if(inventoryList.get(i) instanceof Armor){
								slotList.get(j).setDefenseValue(((Armor)inventoryList.get(i)).getDefenseValue());
								slotList.get(j).setItemType("" + ((Armor)inventoryList.get(i)).getType());
							} 
							if(inventoryList.get(i) instanceof Weapon){
								slotList.get(j).setAttackValue(((Weapon)inventoryList.get(i)).getAttackValue());
								slotList.get(j).setItemType("" + ((Weapon)inventoryList.get(i)).getType());
							}
							if(inventoryList.get(i) instanceof HealItem){
								slotList.get(j).setHealValue(((HealItem)inventoryList.get(i)).getHeal());
								slotList.get(j).setItemType("6");
							}
							putItemInSlot = true;
							inventoryUsedSlotCounter++;
						}
					}
				}
			}
			
		if(checkNeededInterface()){
			for (int i = 0; i < inventoryListMerchant.size(); i++) {
				
				putItemInSlot = false;
				for (int j = 0; j < 9; j++) {
					if (inventoryListMerchant.get(i).getName().equalsIgnoreCase(slotListSell.get(j).getItemName())) {
						if (slotListSell.get(j).getNumberOfItems()
								.equalsIgnoreCase("5")) {
	
						} else {
							slotListSell.set(j, new Slot(j, inventoryListMerchant.get(i).getName(), inventoryListMerchant.get(i).getItemType(), "" + (Integer.parseInt(slotListSell.get(j).getNumberOfItems())+1)));
							slotListSell.get(j).setlevelNeeded(inventoryListMerchant.get(i).getLevelNeeded());
							if(inventoryListMerchant.get(i) instanceof Armor){
								slotListSell.get(j).setDefenseValue(((Armor)inventoryListMerchant.get(i)).getDefenseValue());
								slotListSell.get(j).setItemType("" + ((Armor)inventoryListMerchant.get(i)).getType());
							} 
							if(inventoryListMerchant.get(i) instanceof Weapon){
								slotListSell.get(j).setAttackValue(((Weapon)inventoryListMerchant.get(i)).getAttackValue());
								slotListSell.get(j).setItemType("" + ((Weapon)inventoryListMerchant.get(i)).getType());
							}
							if(inventoryListMerchant.get(i) instanceof HealItem){
								slotListSell.get(j).setHealValue(((HealItem)inventoryListMerchant.get(i)).getHeal());
								slotListSell.get(j).setItemType("6");
							}
							putItemInSlot = true;
						}
					}
					if (slotListSell.get(j).getItemName().equalsIgnoreCase("leer")
							&& putItemInSlot == false) {
							if(inventoryListMerchant.get(i) instanceof Armor){
								tempSlot = new Slot(j, inventoryListMerchant.get(i).getName(),
										"" + ((Armor)inventoryListMerchant.get(i)).getType(), "1");
							}	
							if(inventoryListMerchant.get(i) instanceof Weapon){
								tempSlot = new Slot(j, inventoryListMerchant.get(i).getName(),
										"" + ((Weapon)inventoryListMerchant.get(i)).getType(), "1");
							}
							if(inventoryListMerchant.get(i) instanceof HealItem){
								tempSlot = new Slot(j, inventoryListMerchant.get(i).getName(),
										"5", "1");
							}
							slotListSell.set(j, tempSlot);
							slotListSell.get(j).setlevelNeeded(inventoryListMerchant.get(i).getLevelNeeded());
							if(inventoryListMerchant.get(i) instanceof Armor){
								slotListSell.get(j).setDefenseValue(((Armor)inventoryListMerchant.get(i)).getDefenseValue());
								slotListSell.get(j).setItemType("" + ((Armor)inventoryListMerchant.get(i)).getType());
							} 
							if(inventoryListMerchant.get(i) instanceof Weapon){
								slotListSell.get(j).setAttackValue(((Weapon)inventoryListMerchant.get(i)).getAttackValue());
								slotListSell.get(j).setItemType("" + ((Weapon)inventoryListMerchant.get(i)).getType());
							}
							if(inventoryListMerchant.get(i) instanceof HealItem){
								slotListSell.get(j).setHealValue(((HealItem)inventoryListMerchant.get(i)).getHeal());
								slotListSell.get(j).setItemType("6");
							}
							putItemInSlot = true;
						}
					}
				}
			} else {
		for (int i = 0; i < equipedArmorList.length; i++) {
			if (equipedArmorList[i] != null){
				slotListEquip.get(equipedArmorList[i].getType()).setItemName(equipedArmorList[i].getName());
				if (equipedArmorList[i].getType() == 0) {
					if(equipedArmorList[i].getName().equalsIgnoreCase("leer")){
					}else{
						slotListEquip.set(equipedArmorList[i].getType(), new Slot(i, equipedArmorList[i].getName(),EQUIP_HEAD, "1"));
						slotListEquip.get(equipedArmorList[i].getType()).setDefenseValue(equipedArmorList[i].getDefenseValue());
						slotListEquip.get(equipedArmorList[i].getType()).setlevelNeeded(equipedArmorList[i].getLevelNeeded());
					}
				}
				if (equipedArmorList[i].getType() == 1) {
					if(equipedArmorList[i].getName().equalsIgnoreCase("leer")){
					}else{
						slotListEquip.set(equipedArmorList[i].getType(), new Slot(i, equipedArmorList[i].getName(),EQUIP_UPPER_BODY, "1"));
						slotListEquip.get(equipedArmorList[i].getType()).setDefenseValue(equipedArmorList[i].getDefenseValue());
						slotListEquip.get(equipedArmorList[i].getType()).setlevelNeeded(equipedArmorList[i].getLevelNeeded());
						}
				}
				if (equipedArmorList[i].getType() == 2) {
					if(equipedArmorList[i].getName().equalsIgnoreCase("leer")){
					}else{
					slotListEquip.set(equipedArmorList[i].getType(), new Slot(i, equipedArmorList[i].getName(),EQUIP_HANDS, "1"));
					slotListEquip.get(equipedArmorList[i].getType()).setDefenseValue(equipedArmorList[i].getDefenseValue());
					slotListEquip.get(equipedArmorList[i].getType()).setlevelNeeded(equipedArmorList[i].getLevelNeeded());
					}
				}
				if (equipedArmorList[i].getType() == 3) {
					if(equipedArmorList[i].getName().equalsIgnoreCase("leer")){
					}else{
						slotListEquip.set(equipedArmorList[i].getType(), new Slot(i, equipedArmorList[i].getName(),EQUIP_LOWER_BODY, "1"));
						slotListEquip.get(equipedArmorList[i].getType()).setDefenseValue(equipedArmorList[i].getDefenseValue());
						slotListEquip.get(equipedArmorList[i].getType()).setlevelNeeded(equipedArmorList[i].getLevelNeeded());
						}
				}
				if (equipedArmorList[i].getType() == 4) {
					if(equipedArmorList[i].getName().equalsIgnoreCase("leer")){
					}else{
						slotListEquip.set(equipedArmorList[i].getType(), new Slot(i, equipedArmorList[i].getName(),EQUIP_FEET, "1"));
						slotListEquip.get(equipedArmorList[i].getType()).setDefenseValue(equipedArmorList[i].getDefenseValue());
						slotListEquip.get(equipedArmorList[i].getType()).setlevelNeeded(equipedArmorList[i].getLevelNeeded());
					}
				}
			}
		}
			if(equippedWeapon != null){
				weaponSlot = new Slot(0, equippedWeapon.getName(), "0", "1");
				weaponSlot.setAttackValue(equippedWeapon.getAttackValue());
				weaponSlot.setlevelNeeded(equippedWeapon.getLevelNeeded());
			}
		}
	}
	private void setupOnLongClickListenerSell(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					slot.setMarked();					
					markThisSlot(slotBackground, slot);
					clickedItem = slot.getItemName();
					healValue = slot.getHealValue();
					checkLongClickedItemSell(slotBackground, slotItem, slot);
				}
				return false;
			}
		});
		
		
	}
	
	private void setupOnLongClickListenerBuy(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground, slot);
					slot.setMarked();
					clickedItem = slot.getItemName();
					healValue = slot.getHealValue();
					if(slotsFull){
						
					} else {
						if(money >= 10){
							checkLongClickedItemBuy(slotBackground, slotItem, slot);
						} else {
							showNotEnoughMoneyNotification();
						}
					}
				}
				return false;
			}
		});
		
		
	}
	
	private void showNotEnoughMoneyNotification() {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Du hast nicht genug Geld, um dir 1x " + clickedItem + " zu kaufen!");
		dialogBuilder.setPositiveButton("OK", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	private void setupOnLongClickListenerAddEquip(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground, slot);
					slot.setMarked();
					clickedItem = slot.getItemName();
					healValue = slot.getHealValue();
					if(healValue != 0){
						showUseItemNotification();
					} else {
						checkLongClickedItemAddEquip(slotBackground, slotItem, slot);
					}
				}
				return false;
			}
		});
		
		
	}
	
	private void setupOnLongClickListenerRemoveEquip(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground, slot);
					slot.setMarked();
					checkLongClickedItemRemoveEquip(slotBackground, slotItem, slot);
				}
				return false;
			}
		});
		
		
	}
	

	private void setupOnLongClickListenerRemoveWeapon(
			final ImageButton weaponSlotBackground,
			final ImageButton weaponSlotItem, final Slot slot) {
		weaponSlotItem.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						if (slot.getNumberOfItems().equals("0")) {
						} else {
							markThisSlot(weaponSlotBackground, slot);
							slot.setMarked();
							checkLongClickedItemRemoveWeapon(weaponSlotBackground, weaponSlotItem, slot);
						}
						return false;
					}
				});		
	}

	private void checkLongClickedItemSell(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		showSellNotification(slotBackground, slotItem, slot);
	}
	
	private void checkLongClickedItemBuy(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		showBuyNotification(slotBackground, slotItem, slot);
	}
	
	private void checkLongClickedItemAddEquip(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		Log.d("addEquip", "drinnen in checkLongClickedItemAddEquip");
		if(slot.getItemType().equalsIgnoreCase(EQUIP_HEAD) || slot.getItemType().equalsIgnoreCase(EQUIP_UPPER_BODY) || slot.getItemType().equalsIgnoreCase(EQUIP_HANDS) || slot.getItemType().equalsIgnoreCase(EQUIP_LOWER_BODY) || slot.getItemType().equalsIgnoreCase(EQUIP_FEET)){
			showAddEquipNotification(slotBackground, slotItem, slot);
		} else {
			showNotEquipableNotification();
		}
	}
	
	private void checkLongClickedItemRemoveEquip(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		showRemoveEquipNotification(slotBackground, slotItem, slot);
	}
	
	private void checkLongClickedItemRemoveWeapon(
			ImageButton weaponSlotBackground, ImageButton weaponSlotItem,
			Slot slot) {
		showRemoveWeaponNotification(weaponSlotBackground, weaponSlotItem, slot);
	}

	private void showSellNotification(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du 1x " + clickedItem + " unwiderruflich verkaufen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				sellItem();
				
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
	});
		
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();		
	}
	
	private void showAddEquipNotification(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich anlegen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				if(slot.getDefenseValue() != 0){
					addEquipItem(slotBackground, slotItem, slot);
				}
				if(slot.getAttackValue() != 0){
					addWeaponItem(slotBackground,slotItem, slot);
				}
				
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
	});
		
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();		
	}
	
	

	private void showRemoveEquipNotification(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich ablegen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				removeEquipItem(slotBackground, slotItem, slot);
				
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
	});
		
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();		
	}
	
	private void showBuyNotification(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du 1x " + clickedItem + " wirklich kaufen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				buyItem();
				
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
	});
		
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();		
	}
	


	private void showRemoveWeaponNotification(
			final ImageButton weaponSlotBackground, final ImageButton weaponSlotItem,
			final Slot slot) {

		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich ablegen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				removeWeaponItem(weaponSlotBackground, weaponSlotItem, slot);
				
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
	});
		
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	private void sellItem() {
		removedItem = false;
		tempInventoryList = new ArrayList<Item>();
			for(int i=0; i<inventoryList.size(); i++){
				Log.d("remove", "clickedItem ist " + clickedItem);
				if(inventoryList.get(i).getName().equalsIgnoreCase(clickedItem) && removedItem == false){
					removedItem = true;
					Log.d("remove", "item gelöscht");
				}
				else{
					tempInventoryList.add(inventoryList.get(i));
				}
			}		
			setSlotsUnmarked();
			controller.setInventory(tempInventoryList);
	
			slotList.clear();
			for(int i=0; i<9; i++){
				slotList.add(new Slot(0,"leer", "leer", "0"));
			}	
			slotListSell.clear();
			for(int i=0; i<9; i++){
				slotListSell.add(new Slot(0,"leer", "leer", "0"));
			}

		clickedItem = "leer";
		addmoney();
		setMoneyTextView(controller.getGold());
		resetItemNameTextView();	
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnSellSlots();
		setupAllClickListeners();
		Log.d("remove", "fertig");
	}
	
	private void addmoney() {
		controller.changeGold(5 * (controller.getLevel()));
	}

	private void buyItem() {
		removedItem = false;
		tempInventoryList = new ArrayList<Item>();
			for(int i=0; i<inventoryListMerchant.size(); i++){
				Log.d("remove", "clickedItem ist " + clickedItem);
				if(inventoryListMerchant.get(i).getName().equalsIgnoreCase(clickedItem) && removedItem == false){
					removedItem = true;
					controller.addItemToInventory(inventoryListMerchant.get(i));
					Log.d("remove", "item geaddet");
				}
				else{
					tempInventoryList.add(inventoryListMerchant.get(i));
				}
			}		
			setSlotsUnmarked();
			inventoryListMerchant = tempInventoryList;
	
			slotList.clear();
			for(int i=0; i<9; i++){
				slotList.add(new Slot(0,"leer", "leer", "0"));
			}
			
			slotListSell.clear();
			for(int i=0; i<9; i++){
				slotListSell.add(new Slot(0,"leer", "leer", "0"));
			}	

		clickedItem = "leer";
		pay();
		setMoneyTextView(controller.getGold());
		resetItemNameTextView();	
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnSellSlots();
		setupAllClickListeners();
		Log.d("remove", "fertig");
	}
	
	private void pay() {
		controller.changeGold(-10*(controller.getLevel()));
	}

	private void addEquipItem(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {

		removedItem = false;
		tempArmor = new Armor(slot.getItemName(), slot.getLevelNeeded(), slot.getDefenseValue(), Integer.parseInt(slot.getItemType()));
		Log.d("testArmor", "" + tempArmor.getName() + tempArmor.getLevelNeeded() + tempArmor.getDefenseValue() + tempArmor.getType());
		tempItem = (Item)tempArmor;
		Log.d("testArmor", "" + tempItem.getName() + tempItem.getLevelNeeded() + ((Armor) tempItem).getDefenseValue() + ((Armor)tempItem).getType());

		tempItem.setItemType("armor");
		tempArmor = (Armor) tempItem;
		Log.d("testArmor", "" + tempArmor.getName() + tempArmor.getLevelNeeded() + tempArmor.getDefenseValue() + tempArmor.getType() + tempArmor.getItemType());

				if(slotListEquip.get(tempArmor.getType()).getNumberOfItems().equalsIgnoreCase("0")){
					controller.addArmor(tempArmor);
					tempInventoryList = new ArrayList<Item>();
					for(int i=0; i<inventoryList.size(); i++){
						removedItem = false;

						if(inventoryList.get(i).getName().equalsIgnoreCase(tempArmor.getName()) && removedItem == false){
							removedItem = true;
						}
						else{
							tempInventoryList.add(inventoryList.get(i));
						}
					}
					
					controller.setInventory(tempInventoryList);
				} else{
					
					showUsedSlotNotification();
				}
				

		slotList.clear();
		for(int i=0; i<9; i++){
			slotList.add(new Slot(0,"leer", "leer", "0"));
		}
			
		resetItemNameTextView();
		setSlotsUnmarked();	
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		setupAllClickListeners();
		Log.d("testArmor", "" + inventoryList.size());
	}
	
	private void addWeaponItem(ImageButton slotBackground,
		ImageButton slotItem, Slot slot) {
		removedItem = false;
		
		tempWeapon = new Weapon(slot.getItemName(), slot.getLevelNeeded(), slot.getAttackValue(), 0);
		tempItem = (Item)tempWeapon;
		tempItem.setItemType("weapon");
		tempWeapon = (Weapon) tempItem;
		setSlotsUnmarked();
		
		
		if(weaponSlot.getNumberOfItems().equalsIgnoreCase("0")){
			
			controller.setWeapon(tempWeapon);
			tempInventoryList = new ArrayList<Item>();
				for(int i=0; i<inventoryList.size(); i++){
					if(inventoryList.get(i).getName().equalsIgnoreCase(tempWeapon.getName()) && removedItem == false){
						removedItem = true;
					}
					else{
						tempInventoryList.add(inventoryList.get(i));
					}
				}			
				controller.setInventory(tempInventoryList);
			} else{
			showUsedSlotNotification();
		}

		slotList.clear();
		for(int i=0; i<9; i++){
			slotList.add(new Slot(0,"leer", "leer", "0"));
		}
		
		resetItemNameTextView();
		
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		setupAllClickListeners();
		
		
	}
	
	
	private void resetItemNameTextView() {
		itemName.setText("Item:");	
		itemDetails.setText("");
	}

	private void removeEquipItem(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		inventoryList.add((Item)equipedArmorList[(Integer.parseInt(slot.getItemType()))]);
		
		controller.setInventory(inventoryList);
		setSlotsUnmarked();
				
		controller.removeEquippedArmor(equipedArmorList[(Integer.parseInt(slot.getItemType()))]);
		
		slotList.clear();
		for(int i=0; i<9; i++){
			slotList.add(new Slot(0,"leer", "leer", "0"));
		}
		
		slotListEquip.clear();
		for(int i=0; i<5; i++){
			slotListEquip.add(new Slot(0,"leer", "leer", "0"));
		}
		
		resetItemNameTextView();
		
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		setupAllClickListeners();
		Log.d("testArmor", "" + inventoryList.size());
	}
	
	private void removeWeaponItem(final ImageButton weaponSlotBackground,
			final ImageButton weaponSlotItem, final Slot slot) {

		inventoryList.add((Item)equippedWeapon);
		
		controller.setInventory(inventoryList);
		setSlotsUnmarked();
				
		controller.removeEquippedWeapon(equippedWeapon);
		
		slotList.clear();
		for(int i=0; i<9; i++){
			slotList.add(new Slot(0,"leer", "leer", "0"));
		}
		
		weaponSlot = new Slot(0, "leer", "leer", "0");
		
		resetItemNameTextView();
		
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		setupAllClickListeners();
	}
	
	
	
	private void showUsedSlotNotification() {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Du bist schon mit einem Item des selben Typs ausgerüstet! Du musst Zuerst dieses Item ablegen!");
		dialogBuilder.setPositiveButton("OK", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	private void showNotEquipableNotification() {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Du kannst dieses Item nicht anlegen!");
		dialogBuilder.setPositiveButton("OK", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	private void setupOnClickListener(final ImageButton slotBackground, final ImageButton slotItem,
			final Slot slot) {
		slotItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground, slot);
					slot.setMarked();
					showItemName(slot);
					clickedItem = slot.getItemName();
					healValue = slot.getHealValue();
					isInInventory = true;
					if(checkNeededInterface()){
						if(slot.getAttackValue() != 0){
							itemDetails.setText("Angriff +" + slot.getAttackValue() + "\nWert: " + (5*(controller.getLevel())) + " Gold");
						}
						if(slot.getDefenseValue() != 0){
							itemDetails.setText("Verteidigung +" + slot.getDefenseValue() + "\nWert: " + (5*(controller.getLevel())) + " Gold");
						}
						if(slot.getHealValue() != 0){
							itemDetails.setText("Leben +" + slot.getHealValue() + "\nWert: " + (5*(controller.getLevel())) + " Gold");
						}
					} else {
						if(slot.getAttackValue() != 0){
							itemDetails.setText("Angriff +" + slot.getAttackValue());
						}
						if(slot.getDefenseValue() != 0){
							itemDetails.setText("Verteidigung +" + slot.getDefenseValue());
						}
						if(slot.getHealValue() != 0){
							itemDetails.setText("Leben +" + slot.getHealValue());
						}
					}
				}
			}
			
		});
	}
	
	private void setupOnClickListenerEquip(final ImageButton slotBackground, final ImageButton slotItem,
			final Slot slot) {
		slotItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground, slot);
					slot.setMarked();
					showItemName(slot);
					clickedItem = slot.getItemName();
					healValue = slot.getHealValue();
					isInInventory = false;
					if(checkNeededInterface()){
						if(slot.getAttackValue() != 0){
							itemDetails.setText("Angriff +" + slot.getAttackValue() + "\nWert: " + (5*(controller.getLevel())) + " Gold");
						}
						if(slot.getDefenseValue() != 0){
							itemDetails.setText("Verteidigung +" + slot.getDefenseValue() + "\nWert: " + (5*(controller.getLevel())) + " Gold");
						}
						if(slot.getHealValue() != 0){
							itemDetails.setText("Leben +" + slot.getHealValue() + "\nWert: " + (5*(controller.getLevel())) + " Gold");
						}
					} else {
						if(slot.getAttackValue() != 0){
							itemDetails.setText("Angriff +" + slot.getAttackValue());
						}
						if(slot.getDefenseValue() != 0){
							itemDetails.setText("Verteidigung +" + slot.getDefenseValue());
						}
						if(slot.getHealValue() != 0){
							itemDetails.setText("Leben +" + slot.getHealValue());
						}
						
					}
				}
			}
			
		});
	}
	
	private void setupOnClickListenerSell(final ImageButton slotBackground, final ImageButton slotItem,
			final Slot slot) {
		slotItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground, slot);
					slot.setMarked();
					showItemName(slot);
					clickedItem = slot.getItemName();
					healValue = slot.getHealValue();
					isInInventory = true;
					if(slot.getAttackValue() != 0){
						itemDetails.setText("Angriff +" + slot.getAttackValue() + "\nPreis: " + (10*(controller.getLevel())) + " Gold");
					}
					if(slot.getDefenseValue() != 0){
						itemDetails.setText("Verteidigung +" + slot.getDefenseValue() + "\nPreis: " + (10*(controller.getLevel())) + " Gold");
					}
					if(slot.getHealValue() != 0){
						itemDetails.setText("Leben +" + slot.getHealValue() + "\nPreis: " + (10*(controller.getLevel())) + " Gold");
					}
				}
			}
			
		});
	}

	private void showItemName(Slot slot) {
		itemName.setText("Item: " + slot.getNumberOfItems() + "x "+ slot.getItemName());
	}

	private void markThisSlot(ImageButton slotBackground, Slot slot) {
		setSlotsUnmarked();
		slotBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_marked));
	}

	private void setSlotsUnmarked() {
		
		for(int i=0; i<9; i++ ){
			slotBackgroundList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));		
			slotList.get(i).setUnmarked();
			if(checkNeededInterface()){
				slotBackgroundListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));		
				slotListSell.get(i).setUnmarked();
			}
			else{
				if(i<5){
					slotBackgroundListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));		
					slotListEquip.get(i).setUnmarked();
					
					weaponSlotBackground.setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));
					weaponSlot.setUnmarked();
				}
			}
		}
		healValue = 0;
	}

	public void onDestroy() {
		super.onDestroy();
	}
	
	private void setItemsOnOwnSlots() {
		
		for(int i=0; i<9; i++){
			for(int j=0; j<sortedItemNames.size(); j++){
				if (slotList.get(i).getItemName().equalsIgnoreCase("leer")){
					slotItemImageList.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
				}	
				if (slotList.get(i).getItemName().equalsIgnoreCase(sortedItemNames.get(j))){
						slotItemImageList.get(i).setBackgroundDrawable(sortedItemImages.get(j));
				}
			}
		}

	}

	private void setItemsOnSellSlots() {
		
		for(int i=0; i<9; i++){
			for(int j=0; j<sortedItemNames.size(); j++){
				if(slotListSell.get(i).getItemName().equalsIgnoreCase("leer")){
					slotItemImageListSell.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
				}
				if (slotListSell.get(i).getItemName().equalsIgnoreCase(sortedItemNames.get(j))){
					slotItemImageListSell.get(i).setBackgroundDrawable(sortedItemImages.get(j));
				}
			}
		}
	}
	
	private void setItemsOnEquipSlots() {

		for(int i=0; i<5; i++){
			for(int j=0; j<sortedItemNames.size(); j++){
				if(slotListEquip.get(i).getItemName().equalsIgnoreCase("leer")){
					slotItemImageListEquip.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
				}
				if (slotListEquip.get(i).getItemName().equalsIgnoreCase(sortedItemNames.get(j))){
					slotItemImageListEquip.get(i).setBackgroundDrawable(sortedItemImages.get(j));
				}
				if(weaponSlot.getItemName().equalsIgnoreCase("leer")){
					weaponSlotItemImage.setBackgroundColor(getResources().getColor(R.color.transparent));
				}
				if(weaponSlot.getItemName().equalsIgnoreCase(sortedItemNames.get(j))){
					weaponSlotItemImage.setBackgroundDrawable(sortedItemImages.get(j));
				}
			}
		}	
	}
	
	private void setMoneyTextView(int i){
		moneyTextView.setText("Gold: " + i);
	}
	
	private void setupUI() {
		setContentView(R.layout.activity_inventar);
				
		sortedItemImages = new ArrayList<Drawable>();
		
		sortedItemImages.add(getResources().getDrawable(R.drawable.abgewetztelederhandschuhe));
		sortedItemImages.add(getResources().getDrawable(R.drawable.axtdestodes));
		sortedItemImages.add(getResources().getDrawable(R.drawable.etwaszuschwerereisenhammer));
		sortedItemImages.add(getResources().getDrawable(R.drawable.holzknueppel));
		sortedItemImages.add(getResources().getDrawable(R.drawable.kupferstreitkolben));
		sortedItemImages.add(getResources().getDrawable(R.drawable.leinenhemd));
		sortedItemImages.add(getResources().getDrawable(R.drawable.loechrigeledersandalen));
		sortedItemImages.add(getResources().getDrawable(R.drawable.nagelneueseisenschwert));
		sortedItemImages.add(getResources().getDrawable(R.drawable.rostigeseisenschwert));
		sortedItemImages.add(getResources().getDrawable(R.drawable.schmiedeeisernerstreitkolben));		
		sortedItemImages.add(getResources().getDrawable(R.drawable.stoffhandschuhe));		
		sortedItemImages.add(getResources().getDrawable(R.drawable.stoffhose));		
		sortedItemImages.add(getResources().getDrawable(R.drawable.stoffschuhe));		
		sortedItemImages.add(getResources().getDrawable(R.drawable.stoffstirnband));
		sortedItemImages.add(getResources().getDrawable(R.drawable.stumpfekupferaxt));
		sortedItemImages.add(getResources().getDrawable(R.drawable.viermalgeflicktelederhose));		
		
		sortedItemNames = new ArrayList<String>();
		
		sortedItemNames.add("abgewetzte Lederhandschuhe");
		sortedItemNames.add("Axt des Todes");
		sortedItemNames.add("Etwas zu schwerer Eisenhammer");
		sortedItemNames.add("Holzknüppel");
		sortedItemNames.add("Kupferstreitkolben");
		sortedItemNames.add("Leinenhemd");
		sortedItemNames.add("löchrige Ledersandalen");
		sortedItemNames.add("Nagelneues Eisenschwert");
		sortedItemNames.add("Rostiges Eisenschwert");
		sortedItemNames.add("Schmiedeeisener Streitkolben");
		sortedItemNames.add("Stoffhandschuhe");
		sortedItemNames.add("Stoffhose");
		sortedItemNames.add("Stoffschuhe");
		sortedItemNames.add("Stoffstirnband");
		sortedItemNames.add("stumpfe Kupferaxt");
		sortedItemNames.add("Viermal geflickte Lederhose");
		
		controller = Controller.getInstance();
	
		
		titleSellEquip = (TextView) findViewById(R.id.title_inventar_sell);
		itemName = (TextView) findViewById(R.id.item_name_and_number);
		moneyTextView = (TextView) findViewById(R.id.money_textView);
		
		equipSlotTypeText1 = (TextView) findViewById(R.id.slot_type_armor1);
		equipSlotTypeText2 = (TextView) findViewById(R.id.slot_type_armor2);
		equipSlotTypeText3 = (TextView) findViewById(R.id.slot_type_armor3);
		equipSlotTypeText4 = (TextView) findViewById(R.id.slot_type_armor4);
		equipSlotTypeText5 = (TextView) findViewById(R.id.slot_type_armor5);
		weaponSlotTypeText = (TextView) findViewById(R.id.slot_type_weapon);
		
		removeItemButton = (Button) findViewById(R.id.remove_item_button);
		itemDetails = (TextView) findViewById(R.id.item_details);
		removeItemBackground = (ImageView) findViewById(R.id.remove_item_background);

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
		
		slot1BackgroundEquip = (ImageButton) findViewById(R.id.player_equip_slot1);
		slot2BackgroundEquip = (ImageButton) findViewById(R.id.player_equip_slot2);
		slot3BackgroundEquip = (ImageButton) findViewById(R.id.player_equip_slot3);
		slot4BackgroundEquip = (ImageButton) findViewById(R.id.player_equip_slot4);
		slot5BackgroundEquip = (ImageButton) findViewById(R.id.player_equip_slot5);
		
		slot1ItemImageEquip = (ImageButton) findViewById(R.id.player_equip_slot1_item_image);
		slot2ItemImageEquip = (ImageButton) findViewById(R.id.player_equip_slot2_item_image);
		slot3ItemImageEquip = (ImageButton) findViewById(R.id.player_equip_slot3_item_image);
		slot4ItemImageEquip = (ImageButton) findViewById(R.id.player_equip_slot4_item_image);
		slot5ItemImageEquip = (ImageButton) findViewById(R.id.player_equip_slot5_item_image);
		
		weaponSlotBackground = (ImageButton) findViewById(R.id.player_weapon_slot);
		weaponSlotItemImage = (ImageButton) findViewById(R.id.player_weapon_slot_item_image);
		
		playerEquip = (ImageButton) findViewById(R.id.player_equip);

		slotList = new ArrayList<Slot>();
		for(int i=0; i<9; i++){
			slotList.add(new Slot(0,"leer", "leer", "0"));
		}
		
		slotListSell = new ArrayList<Slot>();
		for(int i=0; i<9; i++){
			slotListSell.add(new Slot(0,"leer", "leer", "0"));
		}
		
		slotListEquip = new ArrayList<Slot>();
		for(int i=0; i<5; i++){
			slotListEquip.add(new Slot(0,"leer", "leer", "0"));
		}
		weaponSlot = (new Slot(0,"leer", "leer", "0"));
		
		inventoryList = new ArrayList<Item>();
		inventoryListMerchant = new ArrayList<Item>();
		
		
		tempInventoryList = new ArrayList<Item>();
		equipedArmorList = new Armor[5];

		
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
		
		slotBackgroundListEquip = new ArrayList<ImageButton>();
		slotBackgroundListEquip.add(slot1BackgroundEquip);
		slotBackgroundListEquip.add(slot2BackgroundEquip);
		slotBackgroundListEquip.add(slot3BackgroundEquip);
		slotBackgroundListEquip.add(slot4BackgroundEquip);
		slotBackgroundListEquip.add(slot5BackgroundEquip);
		
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
		
		slotItemImageListEquip = new ArrayList<ImageButton>();
		slotItemImageListEquip.add(slot1ItemImageEquip);
		slotItemImageListEquip.add(slot2ItemImageEquip);
		slotItemImageListEquip.add(slot3ItemImageEquip);
		slotItemImageListEquip.add(slot4ItemImageEquip);
		slotItemImageListEquip.add(slot5ItemImageEquip);
	}
	
	public boolean inventoryIsFull(){
		return slotsFull;
	}
}

 

