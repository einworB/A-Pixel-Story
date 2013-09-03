




//hinweis für mich: armor/inventar zurückgeben an controller


package de.projectrpg.inventory;

import java.util.ArrayList;

import com.example.projectrpg.R;

import de.projectrpg.database.Armor;
import de.projectrpg.database.Weapon;
import de.projectrpg.database.Item;
import de.projectrpg.game.Controller;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class InventarActivity extends Activity {
	
	
	private Intent intent;
	private Controller controller;
	
	private static final String EQUIP_HEAD = "kopf";
	private static final String EQUIP_UPPER_BODY = "oberkoerper";
	private static final String EQUIP_HANDS = "haende";
	private static final String EQUIP_LOWER_BODY = "beine";
	private static final String EQUIP_FEET = "fuesse";
	
	//hat noch bugs
	
	
	private Slot testSlot;
	private Slot testSlot2;
	private Slot testSlot3;

	private TextView titleSellEquip;
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
	
	private ImageButton playerEquip;
	
	private ArrayList<Slot> slotList = null;
	private ArrayList<Slot> slotListSell = null;
	private ArrayList<Slot> slotListEquip = null;
	private ArrayList<ImageButton> slotBackgroundList = null;
	private ArrayList<ImageButton> slotItemImageList = null;
	private ArrayList<ImageButton> slotBackgroundListSell = null;
	private ArrayList<ImageButton> slotItemImageListSell = null;
	private ArrayList<ImageButton> slotBackgroundListEquip = null;
	private ArrayList<ImageButton> slotItemImageListEquip = null;
	
	private ArrayList<Item> inventoryList = null;
	private Armor[] equipedArmorList = null;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();
		getSavedItems();
		setItemsOnOwnSlots();
		if(checkNeededInterface()){
			setItemsOnSellSlots();
		}
		else {
			setItemsOnEquipSlots();
		}
		setupAllClickListeners();
		
	}

	private boolean checkNeededInterface() {
		boolean isSellInterface = false; 	//irgendwie getNeededInterfaceFromLevelActivity();
		if(isSellInterface){
			for(int i=0; i<9; i++){
				slotBackgroundListSell.get(i).setVisibility(View.VISIBLE);
				slotItemImageListSell.get(i).setVisibility(View.VISIBLE);
				if(i<5){
					slotBackgroundListEquip.get(i).setVisibility(View.INVISIBLE);
					slotItemImageListEquip.get(i).setVisibility(View.INVISIBLE);
				}
				playerEquip.setVisibility(View.INVISIBLE);
				
				slotListEquip.clear();
				titleSellEquip.setText(R.string.title_sell);
			}	
		} else {
			for(int i=0; i<9; i++){
				slotBackgroundListSell.get(i).setVisibility(View.INVISIBLE);
				slotItemImageListSell.get(i).setVisibility(View.INVISIBLE);
				if(i<5){
					slotBackgroundListEquip.get(i).setVisibility(View.VISIBLE);
					slotItemImageListEquip.get(i).setVisibility(View.VISIBLE);
				}
				playerEquip.setVisibility(View.VISIBLE);

				slotListSell.clear();
				titleSellEquip.setText(R.string.title_equip);
			}
		}
		
		return isSellInterface;
	}

	
	private void setupAllClickListeners() {
		for (int i = 0; i < 9; i++) {
			setupOnClickListener(slotBackgroundList.get(i),	slotItemImageList.get(i), slotList.get(i));

			if (checkNeededInterface()) {
				setupOnLongClickListenerSell(slotBackgroundList.get(i),slotItemImageList.get(i), slotList.get(i));
				setupOnLongClickListenerBuy(slotBackgroundListSell.get(i),slotItemImageListSell.get(i), slotListSell.get(i));
				setupOnClickListener(slotBackgroundListSell.get(i),slotItemImageListSell.get(i), slotListSell.get(i));
			} else {
				setupOnLongClickListenerAddEquip(slotBackgroundList.get(i),slotItemImageList.get(i), slotList.get(i));
				
				if(i<5){
					setupOnLongClickListenerRemoveEquip(slotBackgroundListEquip.get(i),slotItemImageListEquip.get(i), slotListEquip.get(i));
					setupOnClickListener(slotBackgroundListEquip.get(i), slotItemImageListEquip.get(i), slotListEquip.get(i));
				}
			}
		}
	}
	
	private void getSavedItems() {
		boolean putItemInSlot = false;
		Slot tempSlot = new Slot(0, "leer", "leer", "0");

		try{
		 inventoryList = controller.getInventory();
		}
		catch(NullPointerException e){
			//do nothing
		}
		try{
			 equipedArmorList = controller.getArmor();
			}
			catch(NullPointerException e){
				//do nothing
			}
		
	/*	Item tempItem = new Item("Schwert", 1, EQUIP_HEAD);
		Armor tempArmor = (Armor) tempItem;
		tempArmor.setDefenseValue(1);
		tempArmor.setType(0);
		tempArmor.set
		
		Item tempItem2 = new Item("Pizza", 1, EQUIP_HANDS);
		Armor tempArmor2 = (Armor) tempItem2;
		tempArmor2.setDefenseValue(1);
		tempArmor2.setType(2);
		
		Item tempItem3 = new Item("leer", 1, "leer");
		Armor tempArmor3 = (Armor) tempItem3;
		tempArmor3.setDefenseValue(0);
		tempArmor3.setType(5);
		
	
		
		equipedArmorList[0] = tempArmor;
		equipedArmorList[1] = tempArmor2;
		equipedArmorList[2] = tempArmor3;
		equipedArmorList[3] = tempArmor3;
		equipedArmorList[4] = tempArmor3;
		
		 
		inventoryList.add(tempArmor);
		inventoryList.add(tempArmor);
		inventoryList.add(tempArmor);
		inventoryList.add(tempArmor);
		inventoryList.add(tempArmor2);
		inventoryList.add(tempArmor);
		inventoryList.add(tempArmor);
		
		**/
		if(inventoryList != null){
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
							} 
							if(inventoryList.get(i) instanceof Weapon){
								slotList.get(j).setAttackValue(((Weapon)inventoryList.get(i)).getAttackValue());
							}
							putItemInSlot = true;
						}
					}
					if (slotList.get(j).getItemName().equalsIgnoreCase("leer")
							&& putItemInSlot == false) {
						tempSlot = new Slot(j, inventoryList.get(i).getName(),
								inventoryList.get(i).getItemType(), "1");
						slotList.set(j, tempSlot);
						slotList.get(j).setlevelNeeded(inventoryList.get(i).getLevelNeeded());
						if(inventoryList.get(i) instanceof Armor){
							slotList.get(j).setDefenseValue(((Armor)inventoryList.get(i)).getDefenseValue());
						} 
						if(inventoryList.get(i) instanceof Weapon){
							slotList.get(j).setAttackValue(((Weapon)inventoryList.get(i)).getAttackValue());
						}
						putItemInSlot = true;
					}
				}
			}
		}
		
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
	private void setupOnLongClickListenerSell(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground);
					slot.setMarked();
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
					markThisSlot(slotBackground);
					slot.setMarked();
					checkLongClickedItemBuy(slotBackground, slotItem, slot);
				}
				return false;
			}
		});
		
		
	}
	
	private void setupOnLongClickListenerAddEquip(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground);
					slot.setMarked();
					checkLongClickedItemAddEquip(slotBackground, slotItem, slot);
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
					markThisSlot(slotBackground);
					slot.setMarked();
					checkLongClickedItemRemoveEquip(slotBackground, slotItem, slot);
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
		if(slot.getItemType().equals(EQUIP_HEAD) || slot.getItemType().equals(EQUIP_UPPER_BODY) || slot.getItemType().equals(EQUIP_HANDS) || slot.getItemType().equals(EQUIP_LOWER_BODY) || slot.getItemType().equals(EQUIP_FEET)){
			showAddEquipNotification(slotBackground, slotItem, slot);
		} else {
			showNotEquipableNotification();
		}
	}
	
	private void checkLongClickedItemRemoveEquip(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		showRemoveEquipNotification(slotBackground, slotItem, slot);
	}
	

	private void showSellNotification(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		AlertDialog.Builder dialogBuilder = new  AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich verkaufen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				sellItem(slotBackground, slotItem, slot);
				
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
				
				addEquipItem(slotBackground, slotItem, slot);
				
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
		dialogBuilder.setMessage("Willst du dieses Item wirklich kaufen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				//if this button is clicked, the item will be sold
				
				buyItem(slotBackground, slotItem, slot);
				
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
	
	private void buyItem(final ImageButton slotBackground,
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
			if(tempSlot.getItemName().equalsIgnoreCase(slotList.get(i).getItemName()) && putItemInSlot == false){
				if(slotList.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					//do nothing
				} else{
					slotList.get(i).setNumberOfItems("" + (Integer.parseInt(slotList.get(i).getNumberOfItems())+1));
					putItemInSlot = true;
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			if (slotList.get(i).getItemName().equalsIgnoreCase("leer") && putItemInSlot == false) {
				slotList.set(i, tempSlot);
				putItemInSlot = true;
			}
		}
		setItemsOnOwnSlots();
		setItemsOnSellSlots();
		setupAllClickListeners();
	}
	
	private void addEquipItem(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		Slot tempSlot = new Slot(0, slot.getItemName(), slot.getItemType(), "1");
		Item tempItem = new Item(tempSlot.getItemName(), tempSlot.getLevelNeeded(), tempSlot.getItemType());
		Armor tempArmor = (Armor) tempItem;
		setSlotsUnmarked();
			if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_HEAD)){
				if(slotListEquip.get(0).getNumberOfItems().equalsIgnoreCase("0")){
				//	slotListEquip.set(0, tempSlot);
					tempArmor.setDefenseValue(tempSlot.getDefenseValue());
					tempArmor.setType(0);
					controller.addArmor(tempArmor);
					
					if (slot.getNumberOfItems().equalsIgnoreCase("1")){
						slot.eraseSlot();
					} else {
						slot.setNumberOfItems(String.valueOf(Integer.parseInt(slot.getNumberOfItems())-1));
					}
					controller.removeItemFromInventory(tempArmor);

				} else{
					showUsedSlotNotification();
				}
			}
			if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_UPPER_BODY)){
				if(slotListEquip.get(1).getNumberOfItems().equalsIgnoreCase("0")){
				//	slotListEquip.set(1, tempSlot);
					tempArmor.setDefenseValue(tempSlot.getDefenseValue());
					tempArmor.setType(0);
					controller.addArmor(tempArmor);
					if (slot.getNumberOfItems().equalsIgnoreCase("1")){
						slot.eraseSlot();
					} else {
						slot.setNumberOfItems(String.valueOf(Integer.parseInt(slot.getNumberOfItems())-1));
					}
					controller.removeItemFromInventory(tempArmor);
					
				} else{
					showUsedSlotNotification();
				}
			}
			if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_HANDS)){
				if(slotListEquip.get(2).getNumberOfItems().equalsIgnoreCase("0")){
				//	slotListEquip.set(2, tempSlot);
					tempArmor.setDefenseValue(tempSlot.getDefenseValue());
					tempArmor.setType(0);
					controller.addArmor(tempArmor);
					
					if (slot.getNumberOfItems().equalsIgnoreCase("1")){
						slot.eraseSlot();
					} else {
						slot.setNumberOfItems(String.valueOf(Integer.parseInt(slot.getNumberOfItems())-1));
					}
					controller.removeItemFromInventory(tempArmor);
				} else{
					showUsedSlotNotification();
				}
			}
			if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_LOWER_BODY)){
				if(slotListEquip.get(3).getNumberOfItems().equalsIgnoreCase("0")){
				//	slotListEquip.set(3, tempSlot);
					tempArmor.setDefenseValue(tempSlot.getDefenseValue());
					tempArmor.setType(0);
					controller.addArmor(tempArmor);
					
					if (slot.getNumberOfItems().equalsIgnoreCase("1")){
						slot.eraseSlot();
					} else {
						slot.setNumberOfItems(String.valueOf(Integer.parseInt(slot.getNumberOfItems())-1));
					}
					controller.removeItemFromInventory(tempArmor);
				} else{
					showUsedSlotNotification();
				}
			}
			if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_FEET)){
				if(slotListEquip.get(4).getNumberOfItems().equalsIgnoreCase("0")){
			//		slotListEquip.set(4, tempSlot);
					tempArmor.setDefenseValue(tempSlot.getDefenseValue());
					tempArmor.setType(0);
					controller.addArmor(tempArmor);
					if (slot.getNumberOfItems().equalsIgnoreCase("1")){
						slot.eraseSlot();
					} else {
						slot.setNumberOfItems(String.valueOf(Integer.parseInt(slot.getNumberOfItems())-1));
					}
					controller.removeItemFromInventory(tempArmor);
				} else{
					showUsedSlotNotification();
				}
			}
			
			
		getSavedItems();	
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		setupAllClickListeners();
	}
	
	private void removeEquipItem(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		Slot tempSlot = new Slot(0, slot.getItemName(), slot.getItemType(), "1");
		Armor tempArmor = new Armor("", 1, 1, 1);
		boolean putItemInSlot = false;
	/*	if (slot.getNumberOfItems().equalsIgnoreCase("1")){
			slot.eraseSlot();
		} else {
			slot.setNumberOfItems(String.valueOf(Integer.parseInt(slot.getNumberOfItems())-1));
		} **/
		if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_HEAD)){
			tempArmor = equipedArmorList[0];
			controller.removeEquippedArmor(tempArmor);
		}
		if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_UPPER_BODY)){
			tempArmor = equipedArmorList[1];
			controller.removeEquippedArmor(tempArmor);
			}
		if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_HANDS)){
			tempArmor = equipedArmorList[2];
			controller.removeEquippedArmor(tempArmor);
			}
		if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_LOWER_BODY)){
			tempArmor = equipedArmorList[3];
			controller.removeEquippedArmor(tempArmor);
			}
		if(tempSlot.getItemType().equalsIgnoreCase(EQUIP_FEET)){
			tempArmor = equipedArmorList[4];
			controller.removeEquippedArmor(tempArmor);
			}
		
		setSlotsUnmarked();
	/*	for(int i=0; i<9; i++){
			if(tempSlot.getItemName().equalsIgnoreCase(slotList.get(i).getItemName()) && putItemInSlot == false){
				if(slotList.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					//do nothing
				} else{
					slotList.get(i).setNumberOfItems("" + (Integer.parseInt(slotList.get(i).getNumberOfItems())+1));
					putItemInSlot = true;
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			if (slotList.get(i).getItemName().equalsIgnoreCase("leer") && putItemInSlot == false) {
				slotList.set(i, tempSlot);
				putItemInSlot = true;
			}
		}
	**/	
		
		controller.addItemToInventory(tempArmor);
		
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
			slotBackgroundListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));		
			slotList.get(i).setUnmarked();
			if(i<5){
				slotBackgroundListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.slot_unmarked));		
				slotListEquip.get(i).setUnmarked();
			}
		}
	}

	public void onDestroy() {
		super.onDestroy();
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
			if (slotList.get(i).getItemName().equalsIgnoreCase("Pizza")){
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("1")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_one));
				}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("2")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_two));
				}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("3")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_three));
				}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("4")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_four));
				}
				if (slotList.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					slotItemImageList.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_five));
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
			
			if (slotListSell.get(i).getItemName().equalsIgnoreCase("Pizza")){
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("1")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_one));
				}
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("2")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_two));
				}
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("3")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_three));
				}
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("4")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_four));
				}
				if (slotListSell.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					slotItemImageListSell.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_five));
				}
			}
		}
		
	}
	
private void setItemsOnEquipSlots() {
		
		for(int i=0; i<5; i++){
			if (slotListEquip.get(i).getItemName().equalsIgnoreCase("leer")){
				slotItemImageListEquip.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
			}
			
			if (slotListEquip.get(i).getItemName().equalsIgnoreCase("Schwert")){
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("1")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_one));
				}
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("2")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_two));
				}
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("3")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_three));
				}
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("4")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_four));
				}
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.schwert_five));
				}
			}
			if (slotListEquip.get(i).getItemName().equalsIgnoreCase("Pizza")){
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("1")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_one));
				}
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("2")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_two));
				}
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("3")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_three));
				}
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("4")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_four));
				}
				if (slotListEquip.get(i).getNumberOfItems().equalsIgnoreCase("5")){
					slotItemImageListEquip.get(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.pizza_five));
				}
			}
		}
		
	}
	
	private void setupUI() {
		setContentView(R.layout.activity_inventar);
		
		intent = getIntent();
		
		controller = (Controller)intent.getExtras().getSerializable("controller");
		
		titleSellEquip = (TextView) findViewById(R.id.title_inventar_sell);

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
		
		playerEquip = (ImageButton) findViewById(R.id.player_equip);

		slotList = new ArrayList<Slot>();
		for(int i=0; i<9; i++){
			slotList.add(new Slot(0,"leer", "leer", "0"));
		}
		
		slotListSell = new ArrayList<Slot>();
		slotListEquip = new ArrayList<Slot>();
		for(int i=0; i<5; i++){
			slotListEquip.add(new Slot(0,"leer", "leer", "0"));
		}
		
		inventoryList = new ArrayList<Item>();
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
		
		
		
		testSlot = new Slot(0, "Schwert", EQUIP_HEAD, "1");
		testSlot2 = new Slot(0, "Pizza", EQUIP_HANDS, "1");
		testSlot3 = new Slot(0, "leer", "leer", "0");
	}
}

 

