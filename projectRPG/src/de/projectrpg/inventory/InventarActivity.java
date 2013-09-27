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
import de.projectrpg.R;
import de.projectrpg.database.Armor;
import de.projectrpg.database.HealItem;
import de.projectrpg.database.Item;
import de.projectrpg.database.Weapon;
import de.projectrpg.game.Controller;
import de.projectrpg.util.OurMusicManager;

/**
 * This is the Activity where the player can manage his whole 
 * inventory, including buy/sell items or add/remove/use items 
 */

public class InventarActivity extends Activity {

	
	//=======================================================================CONSTANTS========================================================================================	

	/** constants for the different body parts */
	private static final String EQUIP_HEAD = "0";
	private static final String EQUIP_UPPER_BODY = "1";
	private static final String EQUIP_HANDS = "2";
	private static final String EQUIP_LOWER_BODY = "3";
	private static final String EQUIP_FEET = "4";
	
	//=======================================================================FIELDS========================================================================================	
	
	/** the controller responsible for communication between 
	 *  this Activity and the other Activities and for getting 
	 *  and setting of other technical information */
	private Controller controller;
	
	/** shows the clicked Item */
	private TextView itemName;
	
	/** shows the title of the used interface ("Ausrüstung" or "Händler") */
	private TextView titleSellEquip;
	
	/** shows the amount of gold, the player has */
	private TextView moneyTextView;
	
	/** shows stats from clicked Item, like defenseValue/attackValue/healValue */
	private TextView itemDetails;
	
	/** shows the type of the equip slots (head, upper body, etc.) */
	private TextView equipSlotTypeText1;
	private TextView equipSlotTypeText2;
	private TextView equipSlotTypeText3;
	private TextView equipSlotTypeText4;
	private TextView equipSlotTypeText5;
	private TextView weaponSlotTypeText;

	/** removes the clicked Item (irreversible) */
	private Button removeItemButton;
	
	/** interacts with the clicked Item (use/sell/add/etc. Item) */
	private Button interactionButton;
	
	/**  background of the removeItemButton*/
	private ImageView removeItemBackground;

	/** background images of inventory slots */
	private ImageButton slot1Background;
	private ImageButton slot2Background;
	private ImageButton slot3Background;
	private ImageButton slot4Background;
	private ImageButton slot5Background;
	private ImageButton slot6Background;
	private ImageButton slot7Background;
	private ImageButton slot8Background;
	private ImageButton slot9Background;

	/** Item images of inventory slots */
	private ImageButton slot1ItemImage;
	private ImageButton slot2ItemImage;
	private ImageButton slot3ItemImage;
	private ImageButton slot4ItemImage;
	private ImageButton slot5ItemImage;
	private ImageButton slot6ItemImage;
	private ImageButton slot7ItemImage;
	private ImageButton slot8ItemImage;
	private ImageButton slot9ItemImage;
	
	/** background images of merchant slots */
	private ImageButton slot1BackgroundSell;
	private ImageButton slot2BackgroundSell;
	private ImageButton slot3BackgroundSell;
	private ImageButton slot4BackgroundSell;
	private ImageButton slot5BackgroundSell;
	private ImageButton slot6BackgroundSell;
	private ImageButton slot7BackgroundSell;
	private ImageButton slot8BackgroundSell;
	private ImageButton slot9BackgroundSell;

	/** Item images of merchant slots */
	private ImageButton slot1ItemImageSell;
	private ImageButton slot2ItemImageSell;
	private ImageButton slot3ItemImageSell;
	private ImageButton slot4ItemImageSell;
	private ImageButton slot5ItemImageSell;
	private ImageButton slot6ItemImageSell;
	private ImageButton slot7ItemImageSell;
	private ImageButton slot8ItemImageSell;
	private ImageButton slot9ItemImageSell;
	
	/** background images of equip slots */
	private ImageButton slot1BackgroundEquip;
	private ImageButton slot2BackgroundEquip;
	private ImageButton slot3BackgroundEquip;
	private ImageButton slot4BackgroundEquip;
	private ImageButton slot5BackgroundEquip;

	/** Item images of equip slots */
	private ImageButton slot1ItemImageEquip;
	private ImageButton slot2ItemImageEquip;
	private ImageButton slot3ItemImageEquip;
	private ImageButton slot4ItemImageEquip;
	private ImageButton slot5ItemImageEquip;

	/** background image of weapon slot */
	private ImageButton weaponSlotBackground;
	
	/** Item image of weapon slot */
	private ImageButton weaponSlotItemImage;
	
	/** shows picture of the player */
	private ImageButton playerEquip;

	/** alphabetical sorted list of all Item names */
	private ArrayList<String> sortedItemNames = null;
	
	/** alphabetical sorted list of all Item images */
	private ArrayList<Drawable> sortedItemImages = null;
	
	/** list of all inventory slots */
	private ArrayList<Slot> slotList = null;
	
	/** list of all merchant slots */
	private ArrayList<Slot> slotListSell = null;
	
	/** list of all equip slots */
	private ArrayList<Slot> slotListEquip = null;
	
	/** list of all background images of inventory slots */
	private ArrayList<ImageButton> slotBackgroundList = null;
	
	/** list of all Item images of inventory slots */
	private ArrayList<ImageButton> slotItemImageList = null;
	
	/** list of all background images of merchant slots */
	private ArrayList<ImageButton> slotBackgroundListSell = null;
	
	/** list of all Item images of merchant slots */
	private ArrayList<ImageButton> slotItemImageListSell = null;
	
	/** list of all background images of equip slots */
	private ArrayList<ImageButton> slotBackgroundListEquip = null;
	
	/** list of all Item images of equip slots */
	private ArrayList<ImageButton> slotItemImageListEquip = null;

	/** slot for used weapon */
	private Slot weaponSlot = null;
	
	/** saves the name of last clicked Item */
	private String clickedItem = "leer";
	
	/** saves if the last clicked Item is in inventory or not */
	private boolean isInInventory;
	
	/** saves the healValue of last clicked Item */
	private int healValue = 0;
	
	/** saves if the needed Interface is merchant interface or not.
	 * This information is put as extra on the intent in the LevelActivity */
	private boolean isSellInterface;

	/** saves the list of all Items in the inventory (unsorted),
	 *  which you can get from the controller */
	private ArrayList<Item> inventoryList = null;
	
	/** saves the list of all Items from a merchant (unsorted) */
	private ArrayList<Item> inventoryListMerchant = null;
	
	/** list of Items, which is used to store temporarily Items in it */
	private ArrayList<Item> tempInventoryList = null;
	
	/** Array of all equipped Armor */
	private Armor[] equipedArmorList = null;
	
	/** Weapon Item, which saves the equipped Weapon */
	private Weapon equippedWeapon;

	/** saves, if the Item, which is used/sold/etc., 
	 * is removed from the list, it was in before, or not */
	private boolean removedItem;
	
	/** saves, if the Item, which is equipped/removed/etc.,
	 * is put in the new list, in which it was moved, or not */
	private boolean putItemInSlot;

	/** saves temporarily slots */
	private Slot tempSlot;
	
	/** saves temporarily Armor Item */
	private Armor tempArmor;
	
	/** saves temporarily Item */
	private Item tempItem;
	
	/** saves temporarily Weapon Item */
	private Weapon tempWeapon;
	
	/** saves temporarily HealItem Item */
	private HealItem tempHealItem;

	/** counts used slots in inventory */
	private int inventoryUsedSlotCounter = 0;
	
	/** saves, if all inventory slots are full or not */
	private boolean slotsFull = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();
		getSavedItems();
		setItemsOnOwnSlots();
		
		if (checkNeededInterface()) {
			setItemsOnSellSlots();
			setMoneyTextView(controller.getGold());
		} else {
			setItemsOnEquipSlots();
			setMoneyTextView(controller.getGold());
		}
		
		setupAllClickListeners();
	}

	/** checks, if merchant interface or equip interface is needed */
	private boolean checkNeededInterface() {
		isSellInterface = getIntent().getExtras().getBoolean("isMerchant");
		if (isSellInterface) {
			Log.d("sell", "in if-abfrage");
			for (int i = 0; i < 9; i++) {
				slotBackgroundListSell.get(i).setVisibility(View.VISIBLE);
				slotItemImageListSell.get(i).setVisibility(View.VISIBLE);
				Log.d("sell", "sell visible gemacht beendet");
				if (i < 5) {
					slotBackgroundListEquip.get(i)
							.setVisibility(View.INVISIBLE);
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
			for (int i = 0; i < 9; i++) {
				slotBackgroundListSell.get(i).setVisibility(View.INVISIBLE);
				slotItemImageListSell.get(i).setVisibility(View.INVISIBLE);
				if (i < 5) {
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

	/** sets all OnClickListenres and OnLongClickListeners up */
	private void setupAllClickListeners() {
		for (int i = 0; i < 9; i++) {
			setupOnClickListener(slotBackgroundList.get(i),
					slotItemImageList.get(i), slotList.get(i));

			if (checkNeededInterface()) {
				setupOnLongClickListenerSell(slotBackgroundList.get(i),
						slotItemImageList.get(i), slotList.get(i));
				setupOnLongClickListenerBuy(slotBackgroundListSell.get(i),
						slotItemImageListSell.get(i), slotListSell.get(i));
				setupOnClickListenerSell(slotBackgroundListSell.get(i),
						slotItemImageListSell.get(i), slotListSell.get(i));
			} else {
				setupOnLongClickListenerAddEquip(slotBackgroundList.get(i),
						slotItemImageList.get(i), slotList.get(i));

				if (i < 5) {
					setupOnLongClickListenerRemoveEquip(
							slotBackgroundListEquip.get(i),
							slotItemImageListEquip.get(i), slotListEquip.get(i));
					setupOnClickListenerEquip(slotBackgroundListEquip.get(i),
							slotItemImageListEquip.get(i), slotListEquip.get(i));
				}
				setupOnLongClickListenerRemoveWeapon(weaponSlotBackground,
						weaponSlotItemImage, weaponSlot);
				setupOnClickListenerEquip(weaponSlotBackground,
						weaponSlotItemImage, weaponSlot);
				Log.d("remove", "clickListener ausgelöst");
				removeItemButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (clickedItem.equalsIgnoreCase("leer")) {

						} else {
							showRemoveItemNotification();
							Log.d("remove", clickedItem);
						}
					}
				});
			}
		}
	}

	/** shows Notification and asks if player is sure, he wants to use the clicked Item */
	private void showUseItemNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dich mit 1x " + clickedItem
				+ " heilen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, the item will be sold

				useItem();

			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/** shows Notification and asks if player is sure, he wants to remove/erase the clicked Item */
	private void showRemoveItemNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du 1x " + clickedItem
				+ " wirklich entfernen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, the item will be sold

				removeItem();

			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/** method, which handles the use of HealItems */
	private void useItem() {
		if(controller.getItemByName(clickedItem).getLevelNeeded() > controller.getPlayerLevel()){
			showLevelTooLowNotification();
		} else {
			removedItem = false;
			tempInventoryList = new ArrayList<Item>();
			for (int i = 0; i < inventoryList.size(); i++) {
				if (inventoryList.get(i).getName().equalsIgnoreCase(clickedItem)
						&& removedItem == false) {
					removedItem = true;
	
					tempHealItem = new HealItem(inventoryList.get(i).getName(),
							inventoryList.get(i).getLevelNeeded(), "healItem",
							healValue);
					controller.heal(tempHealItem);
				} else {
					tempInventoryList.add(inventoryList.get(i));
				}
			}
			setSlotsUnmarked();
			controller.setInventory(tempInventoryList);
		}

		slotList.clear();
		for (int i = 0; i < 9; i++) {
			slotList.add(new Slot(0, "leer", "leer", "0"));
		}

		resetItemNameTextView();
		resetInteractionButtonText();

		getSavedItems();
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		resetInteractionButtons();
		setupAllClickListeners();
	}

	/** resets the interactionButton */
	private void resetInteractionButtons() {
		interactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// deaktiviert den Button
			}
		});
		clickedItem = "leer";
	}

	/** method, which removes the clicked Item from the list, it is in */
	private void removeItem() {
		removedItem = false;

		if (isInInventory) {
			tempInventoryList = new ArrayList<Item>();
			for (int i = 0; i < inventoryList.size(); i++) {
				if (inventoryList.get(i).getName()
						.equalsIgnoreCase(clickedItem)
						&& removedItem == false) {
					removedItem = true;
				} else {
					tempInventoryList.add(inventoryList.get(i));
				}
			}
			setSlotsUnmarked();
			controller.setInventory(tempInventoryList);

			slotList.clear();
			for (int i = 0; i < 9; i++) {
				slotList.add(new Slot(0, "leer", "leer", "0"));
			}
		} else {
			Log.d("remove", "In else drinnen");

			for (int i = 0; i < 5; i++) {
				Log.d("remove", "In for-schleife drinnen"
						+ equipedArmorList.length + clickedItem);
				if (slotListEquip.get(i).getItemName()
						.equalsIgnoreCase(clickedItem)) {
					Log.d("remove", "In if-schleife drinnen");
					controller.removeEquippedArmor(i);
					Log.d("remove", "In if2 drinnen");
					removedItem = true;
				}
			}
			Log.d("remove", "item gelöscht");

			if (removedItem == false) {
				controller.removeEquippedWeapon(equippedWeapon);
				Log.d("remove", "weapon if-abfrage drinnen");
			}
			Log.d("remove", "weapon if-abfrage übersprungen");

			weaponSlot = new Slot(0, "leer", "leer", "0");

			slotList.clear();
			for (int i = 0; i < 9; i++) {
				slotList.add(new Slot(0, "leer", "leer", "0"));
			}

			slotListEquip.clear();
			for (int i = 0; i < 5; i++) {
				slotListEquip.add(new Slot(0, "leer", "leer", "0"));
				Log.d("remove", "slots geklärt");

			}
			setSlotsUnmarked();
		}

		resetItemNameTextView();
		resetInteractionButtonText();
		clickedItem = "leer";

		getSavedItems();
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		setupAllClickListeners();
		Log.d("remove", "fertig");

	}

	/** gets all saved Items from the controller like inventory, equip, merchant items, etc. */
	private void getSavedItems() {
		inventoryUsedSlotCounter = 0;

		putItemInSlot = false;
		tempSlot = new Slot(0, "leer", "leer", "0");

		inventoryList = new ArrayList<Item>();

		inventoryList = controller.getInventory();
		Log.d("testArmor", "" + inventoryList.size());

		if (checkNeededInterface()) {
			inventoryListMerchant = new ArrayList<Item>();

			if (controller.getLevel() == 1) {
				inventoryListMerchant.add(controller
						.getItemByName("Holzknüppel"));
				inventoryListMerchant.add(controller
						.getItemByName("Vertrocknetes Brot"));
				inventoryListMerchant.add(controller
						.getItemByName("Viermal geflickte Lederhose"));
			}
			if (controller.getLevel() == 2) {
				inventoryListMerchant.add(controller
						.getItemByName("Verstärkter Lederhelm"));
				inventoryListMerchant.add(controller
						.getItemByName("Stumpfe Kupferaxt"));
				inventoryListMerchant.add(controller
						.getItemByName("fast frischer Schinken"));
			}
			if (controller.getLevel() == 3) {
				inventoryListMerchant.add(controller
						.getItemByName("Gebratene Hähnchenkeule"));
				inventoryListMerchant.add(controller
						.getItemByName("bronzener Brustpanzer"));
				inventoryListMerchant.add(controller
						.getItemByName("Etwas zu schwerer Eisenhammer"));
			}
			if (controller.getLevel() >= 4) {
				inventoryListMerchant.add(controller
						.getItemByName("Schmiedeeiserner Streitkolben"));
				inventoryListMerchant.add(controller
						.getItemByName("Goldene Stiefel"));
				inventoryListMerchant.add(controller
						.getItemByName("überaus köstlicher Apfelkuchen"));
			}

			Log.d("kauf", inventoryListMerchant.get(0).getName());
			Log.d("kauf", inventoryListMerchant.get(1).getName());
			Log.d("kauf", inventoryListMerchant.get(2).getName());

		} else {
			equipedArmorList = new Armor[5];
			equipedArmorList = controller.getArmor();

			equippedWeapon = controller.getEquippedWeapon();
		}

		/** fills the inventory items in the inventory slots */
		for (int i = 0; i < inventoryList.size(); i++) {

			putItemInSlot = false;
			for (int j = 0; j < 9; j++) {
				if (inventoryList.get(i).getName()
						.equalsIgnoreCase(slotList.get(j).getItemName())) {
					if (slotList.get(j).getNumberOfItems()
							.equalsIgnoreCase("5")) {

					} else {
						slotList.set(
								j,
								new Slot(
										j,
										inventoryList.get(i).getName(),
										inventoryList.get(i).getItemType(),
										""
												+ (Integer.parseInt(slotList
														.get(j)
														.getNumberOfItems()) + 1)));
						slotList.get(j).setlevelNeeded(
								inventoryList.get(i).getLevelNeeded());
						if (inventoryList.get(i) instanceof Armor) {
							slotList.get(j).setDefenseValue(
									((Armor) inventoryList.get(i))
											.getDefenseValue());
							slotList.get(j).setItemType(
									""
											+ ((Armor) inventoryList.get(i))
													.getType());
						}
						if (inventoryList.get(i) instanceof Weapon) {
							slotList.get(j).setAttackValue(
									((Weapon) inventoryList.get(i))
											.getAttackValue());
							slotList.get(j).setItemType(
									""
											+ ((Weapon) inventoryList.get(i))
													.getType());
						}
						if (inventoryList.get(i) instanceof HealItem) {
							slotList.get(j).setHealValue(
									((HealItem) inventoryList.get(i))
											.getHeal());
							slotList.get(j).setItemType("6");
						}
						putItemInSlot = true;
					}
				}
				if (slotList.get(j).getItemName().equalsIgnoreCase("leer")
						&& putItemInSlot == false) {
					if (inventoryUsedSlotCounter == 9) {
						slotsFull = true;
					} else {

						if (inventoryList.get(i) instanceof Armor) {
							tempSlot = new Slot(j, inventoryList.get(i)
									.getName(), ""
									+ ((Armor) inventoryList.get(i)).getType(),
									"1");
						}
						if (inventoryList.get(i) instanceof Weapon) {
							tempSlot = new Slot(j, inventoryList.get(i)
									.getName(),
									""
											+ ((Weapon) inventoryList.get(i))
													.getType(), "1");
						}
						if (inventoryList.get(i) instanceof HealItem) {
							tempSlot = new Slot(j, inventoryList.get(i)
									.getName(), "5", "1");
						}
						slotList.set(j, tempSlot);
						slotList.get(j).setlevelNeeded(
								inventoryList.get(i).getLevelNeeded());
						if (inventoryList.get(i) instanceof Armor) {
							slotList.get(j).setDefenseValue(
									((Armor) inventoryList.get(i))
											.getDefenseValue());
							slotList.get(j).setItemType(
									""
											+ ((Armor) inventoryList.get(i))
													.getType());
						}
						if (inventoryList.get(i) instanceof Weapon) {
							slotList.get(j).setAttackValue(
									((Weapon) inventoryList.get(i))
											.getAttackValue());
							slotList.get(j).setItemType(
									""
											+ ((Weapon) inventoryList.get(i))
													.getType());
						}
						if (inventoryList.get(i) instanceof HealItem) {
							slotList.get(j)
									.setHealValue(
											((HealItem) inventoryList.get(i))
													.getHeal());
							slotList.get(j).setItemType("6");
						}
						putItemInSlot = true;
						inventoryUsedSlotCounter++;
					}
				}
			}
		}

		if (checkNeededInterface()) {
			/** fills the merchant items in the merchant slots */
			for (int i = 0; i < inventoryListMerchant.size(); i++) {

				putItemInSlot = false;
				for (int j = 0; j < 9; j++) {
					if (inventoryListMerchant
							.get(i)
							.getName()
							.equalsIgnoreCase(slotListSell.get(j).getItemName())) {
						if (slotListSell.get(j).getNumberOfItems()
								.equalsIgnoreCase("5")) {

						} else {
							slotListSell
									.set(j,
											new Slot(
													j,
													inventoryListMerchant
															.get(i).getName(),
													inventoryListMerchant
															.get(i)
															.getItemType(),
													""
															+ (Integer
																	.parseInt(slotListSell
																			.get(j)
																			.getNumberOfItems()) + 1)));
							slotListSell.get(j).setlevelNeeded(
									inventoryListMerchant.get(i)
											.getLevelNeeded());
							if (inventoryListMerchant.get(i) instanceof Armor) {
								slotListSell.get(j).setDefenseValue(
										((Armor) inventoryListMerchant.get(i))
												.getDefenseValue());
								slotListSell
										.get(j)
										.setItemType(
												""
														+ ((Armor) inventoryListMerchant
																.get(i))
																.getType());
							}
							if (inventoryListMerchant.get(i) instanceof Weapon) {
								slotListSell.get(j).setAttackValue(
										((Weapon) inventoryListMerchant.get(i))
												.getAttackValue());
								slotListSell
										.get(j)
										.setItemType(
												""
														+ ((Weapon) inventoryListMerchant
																.get(i))
																.getType());
							}
							if (inventoryListMerchant.get(i) instanceof HealItem) {
								slotListSell.get(j).setHealValue(
										((HealItem) inventoryListMerchant
												.get(i)).getHeal());
								slotListSell.get(j).setItemType("6");
							}
							putItemInSlot = true;
						}
					}
					if (slotListSell.get(j).getItemName()
							.equalsIgnoreCase("leer")
							&& putItemInSlot == false) {
						if (inventoryListMerchant.get(i) instanceof Armor) {
							tempSlot = new Slot(j, inventoryListMerchant.get(i)
									.getName(),
									""
											+ ((Armor) inventoryListMerchant
													.get(i)).getType(), "1");
						}
						if (inventoryListMerchant.get(i) instanceof Weapon) {
							tempSlot = new Slot(j, inventoryListMerchant.get(i)
									.getName(),
									""
											+ ((Weapon) inventoryListMerchant
													.get(i)).getType(), "1");
						}
						if (inventoryListMerchant.get(i) instanceof HealItem) {
							tempSlot = new Slot(j, inventoryListMerchant.get(i)
									.getName(), "5", "1");
						}
						slotListSell.set(j, tempSlot);
						slotListSell.get(j).setlevelNeeded(
								inventoryListMerchant.get(i).getLevelNeeded());
						if (inventoryListMerchant.get(i) instanceof Armor) {
							slotListSell.get(j).setDefenseValue(
									((Armor) inventoryListMerchant.get(i))
											.getDefenseValue());
							slotListSell.get(j).setItemType(
									""
											+ ((Armor) inventoryListMerchant
													.get(i)).getType());
						}
						if (inventoryListMerchant.get(i) instanceof Weapon) {
							slotListSell.get(j).setAttackValue(
									((Weapon) inventoryListMerchant.get(i))
											.getAttackValue());
							slotListSell.get(j).setItemType(
									""
											+ ((Weapon) inventoryListMerchant
													.get(i)).getType());
						}
						if (inventoryListMerchant.get(i) instanceof HealItem) {
							slotListSell.get(j).setHealValue(
									((HealItem) inventoryListMerchant.get(i))
											.getHeal());
							slotListSell.get(j).setItemType("6");
						}
						putItemInSlot = true;
					}
				}
			}
		} else {
			for (int i = 0; i < equipedArmorList.length; i++) {
				if (equipedArmorList[i] != null) {
					slotListEquip.get(equipedArmorList[i].getType())
							.setItemName(equipedArmorList[i].getName());
					if (equipedArmorList[i].getType() == 0) {
						if (equipedArmorList[i].getName().equalsIgnoreCase(
								"leer")) {
						} else {
							slotListEquip.set(equipedArmorList[i].getType(),
									new Slot(i, equipedArmorList[i].getName(),
											EQUIP_HEAD, "1"));
							slotListEquip.get(equipedArmorList[i].getType())
									.setDefenseValue(
											equipedArmorList[i]
													.getDefenseValue());
							slotListEquip.get(equipedArmorList[i].getType())
									.setlevelNeeded(
											equipedArmorList[i]
													.getLevelNeeded());
						}
					}
					if (equipedArmorList[i].getType() == 1) {
						if (equipedArmorList[i].getName().equalsIgnoreCase(
								"leer")) {
						} else {
							slotListEquip.set(equipedArmorList[i].getType(),
									new Slot(i, equipedArmorList[i].getName(),
											EQUIP_UPPER_BODY, "1"));
							slotListEquip.get(equipedArmorList[i].getType())
									.setDefenseValue(
											equipedArmorList[i]
													.getDefenseValue());
							slotListEquip.get(equipedArmorList[i].getType())
									.setlevelNeeded(
											equipedArmorList[i]
													.getLevelNeeded());
						}
					}
					if (equipedArmorList[i].getType() == 2) {
						if (equipedArmorList[i].getName().equalsIgnoreCase(
								"leer")) {
						} else {
							slotListEquip.set(equipedArmorList[i].getType(),
									new Slot(i, equipedArmorList[i].getName(),
											EQUIP_HANDS, "1"));
							slotListEquip.get(equipedArmorList[i].getType())
									.setDefenseValue(
											equipedArmorList[i]
													.getDefenseValue());
							slotListEquip.get(equipedArmorList[i].getType())
									.setlevelNeeded(
											equipedArmorList[i]
													.getLevelNeeded());
						}
					}
					if (equipedArmorList[i].getType() == 3) {
						if (equipedArmorList[i].getName().equalsIgnoreCase(
								"leer")) {
						} else {
							slotListEquip.set(equipedArmorList[i].getType(),
									new Slot(i, equipedArmorList[i].getName(),
											EQUIP_LOWER_BODY, "1"));
							slotListEquip.get(equipedArmorList[i].getType())
									.setDefenseValue(
											equipedArmorList[i]
													.getDefenseValue());
							slotListEquip.get(equipedArmorList[i].getType())
									.setlevelNeeded(
											equipedArmorList[i]
													.getLevelNeeded());
						}
					}
					if (equipedArmorList[i].getType() == 4) {
						if (equipedArmorList[i].getName().equalsIgnoreCase(
								"leer")) {
						} else {
							slotListEquip.set(equipedArmorList[i].getType(),
									new Slot(i, equipedArmorList[i].getName(),
											EQUIP_FEET, "1"));
							slotListEquip.get(equipedArmorList[i].getType())
									.setDefenseValue(
											equipedArmorList[i]
													.getDefenseValue());
							slotListEquip.get(equipedArmorList[i].getType())
									.setlevelNeeded(
											equipedArmorList[i]
													.getLevelNeeded());
						}
					}
				}
			}
			/** fills the weapon slot with equipped weapon */
			if (equippedWeapon != null) {
				weaponSlot = new Slot(0, equippedWeapon.getName(), "0", "1");
				weaponSlot.setAttackValue(equippedWeapon.getAttackValue());
				weaponSlot.setlevelNeeded(equippedWeapon.getLevelNeeded());
			}
		}
	}

	/** sets up the OnLongClickListener for the inventory slots when merchant interface is used */
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
					checkLongClickedItemSell();
				}
				return false;
			}
		});

	}

	/** sets up the OnLongClickListener for the merchant slots when merchant interface is used */
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
					if (slotsFull) {

					} else {
						if (controller.getGold() >= (10 * controller.getItemByName(clickedItem).getLevelNeeded())) {
							checkLongClickedItemBuy();
						} else {
							showNotEnoughMoneyNotification();
						}
					}
				}
				return false;
			}
		});

	}

	/** shows Notification, that the user has not enough money to buy the clicked item*/
	private void showNotEnoughMoneyNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Du hast nicht genug Geld, um dir 1x "
				+ clickedItem + " zu kaufen!");
		dialogBuilder.setPositiveButton("OK", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();

			}
		});
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/** sets up the OnLongClickListener for the inventory slots when equip interface is used */
	private void setupOnLongClickListenerAddEquip(
			final ImageButton slotBackground, final ImageButton slotItem,
			final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground, slot);
					slot.setMarked();
					clickedItem = slot.getItemName();
					healValue = slot.getHealValue();
					Log.d("heil","" + healValue);
					if (healValue != 0) {
						showUseItemNotification();
					} else {
						checkLongClickedItemAddEquip(slotBackground, slotItem,
								slot);
					}
				}
				return false;
			}
		});

	}

	/** sets up OnLongClickListener for the equip slots when equip interface is used */
	private void setupOnLongClickListenerRemoveEquip(
			final ImageButton slotBackground, final ImageButton slotItem,
			final Slot slot) {
		slotItem.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (slot.getNumberOfItems().equals("0")) {
				} else {
					markThisSlot(slotBackground, slot);
					slot.setMarked();
					clickedItem = slot.getItemName();
					checkLongClickedItemRemoveEquip();
				}
				return false;
			}
		});

	}

	/** sets up OnLongClickListener for the weapon slot when equip interface is used */
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
					clickedItem = slot.getItemName();
					checkLongClickedItemRemoveWeapon();
				}
				return false;
			}
		});
	}

	/** checks longclicked Item in inventory slots when merchant interface is used */
	private void checkLongClickedItemSell() {
		showSellNotification();
	}

	/** checks longclicked Item in merchant slots when merchant interface is used */
	private void checkLongClickedItemBuy() {
		showBuyNotification();
	}

	/** checks longclicked Item in inventory slots when equip interface is used */
	private void checkLongClickedItemAddEquip(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		Log.d("addEquip", "drinnen in checkLongClickedItemAddEquip");
		if (slot.getItemType().equalsIgnoreCase(EQUIP_HEAD)
				|| slot.getItemType().equalsIgnoreCase(EQUIP_UPPER_BODY)
				|| slot.getItemType().equalsIgnoreCase(EQUIP_HANDS)
				|| slot.getItemType().equalsIgnoreCase(EQUIP_LOWER_BODY)
				|| slot.getItemType().equalsIgnoreCase(EQUIP_FEET)) {
			showAddEquipNotification(slotBackground, slotItem, slot);
		} else {
			showNotEquipableNotification();
		}
	}

	/** checks longclicked Item in equip slots when equip interface is used */
	private void checkLongClickedItemRemoveEquip() {
		showRemoveEquipNotification();
	}

	/** checks longclicked Item in weapon slot when equip interface is used */
	private void checkLongClickedItemRemoveWeapon() {
		showRemoveWeaponNotification();
	}

	/** shows Notification, which checks, if the player really wants to sell the clicked item */
	private void showSellNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du 1x " + clickedItem
				+ " unwiderruflich verkaufen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				sellItem();

			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/** shows Notification, which checks, if the player really wants to add this Item to his equip */
	private void showAddEquipNotification(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich anlegen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, the item will be sold
				if (slot.getDefenseValue() != 0) {
					addEquipItem();
				}
				if (slot.getAttackValue() != 0) {
					addWeaponItem();
				}

			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	/** shows Notification, which checks, if the player really wants to add this Weapon to his equip */
	private void showAddWeaponNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich anlegen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, the item will be sold
					addWeaponItem();
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	/** shows Notification, which checks, if the player really wants to add this Armor to his equip */
	private void showAddArmorNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich anlegen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, the item will be sold
					addEquipItem();
			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	/** shows Notification, which checks, if the player really wants to remove this Item from his equip */
	private void showRemoveEquipNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich ablegen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, the item will be sold

				removeEquipItem();

			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/** shows Notification, which checks, if the player really wants to buy the clicked Item */
	private void showBuyNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du 1x " + clickedItem
				+ " wirklich kaufen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, the item will be sold

				buyItem();

			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	

	/** shows Notification, which checks, if the player really wants to remove this Weapon from his equip */
	private void showRemoveWeaponNotification() {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Willst du dieses Item wirklich ablegen?");
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton("Ja", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, the item will be sold

				removeWeaponItem();

			}
		});
		dialogBuilder.setNegativeButton("Nein", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();
				dialog.cancel();
			}
		});

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	/** method, which handles the selling of the clicked Item */
	private void sellItem() {
		removedItem = false;
		tempInventoryList = new ArrayList<Item>();
		for (int i = 0; i < inventoryList.size(); i++) {
			Log.d("remove", "clickedItem ist " + clickedItem);
			if (inventoryList.get(i).getName().equalsIgnoreCase(clickedItem)
					&& removedItem == false) {
				removedItem = true;
				Log.d("remove", "item gelöscht");
			} else {
				tempInventoryList.add(inventoryList.get(i));
			}
		}
		setSlotsUnmarked();
		controller.setInventory(tempInventoryList);

		slotList.clear();
		for (int i = 0; i < 9; i++) {
			slotList.add(new Slot(0, "leer", "leer", "0"));
		}
		slotListSell.clear();
		for (int i = 0; i < 9; i++) {
			slotListSell.add(new Slot(0, "leer", "leer", "0"));
		}

		
		addmoney(controller.getItemByName(clickedItem).getLevelNeeded());
		clickedItem = "leer";
		setMoneyTextView(controller.getGold());
		resetItemNameTextView();
		resetInteractionButtonText();
		getSavedItems();
		setItemsOnOwnSlots();
		setItemsOnSellSlots();
		resetInteractionButtons();
		setupAllClickListeners();
		Log.d("remove", "fertig");
	}

	/** resets the text of the interactionButton */
	private void resetInteractionButtonText() {
		interactionButton.setText("");
	}

	/** adds gold to the players own gold */
	private void addmoney(int i) {
		controller.changeGold(5 * i);
	}

	/** method, which handles the buying of the clicked Item */
	private void buyItem() {
		removedItem = false;
		tempInventoryList = new ArrayList<Item>();
		for (int i = 0; i < inventoryListMerchant.size(); i++) {
			Log.d("remove", "clickedItem ist " + clickedItem);
			if (inventoryListMerchant.get(i).getName()
					.equalsIgnoreCase(clickedItem)
					&& removedItem == false) {
				removedItem = true;
				controller.addItemToInventory(inventoryListMerchant.get(i));
				Log.d("remove", "item geaddet");
			} else {
				tempInventoryList.add(inventoryListMerchant.get(i));
			}
		}
		setSlotsUnmarked();
		inventoryListMerchant = tempInventoryList;

		slotList.clear();
		for (int i = 0; i < 9; i++) {
			slotList.add(new Slot(0, "leer", "leer", "0"));
		}

		slotListSell.clear();
		for (int i = 0; i < 9; i++) {
			slotListSell.add(new Slot(0, "leer", "leer", "0"));
		}

		pay(controller.getItemByName(clickedItem).getLevelNeeded());
		clickedItem = "leer";
		setMoneyTextView(controller.getGold());
		resetItemNameTextView();
		resetInteractionButtonText();
		getSavedItems();
		setItemsOnOwnSlots();
		setItemsOnSellSlots();
		resetInteractionButtons();
		setupAllClickListeners();
		Log.d("remove", "fertig");
	}

	/** removes gold from the players own gold */
	private void pay(int i) {
		controller.changeGold(-10 * i);
	}

	/** method, which handles the adding of the clicked Item to the equip */
	private void addEquipItem() {
		if(controller.getItemByName(clickedItem).getLevelNeeded() > controller.getPlayerLevel()){
			showLevelTooLowNotification();
		} else {

			removedItem = false;
			tempArmor = (Armor) controller.getItemByName(clickedItem);
			Log.d("neu",
					"" + tempArmor.getName() + tempArmor.getLevelNeeded()
							+ tempArmor.getDefenseValue() + tempArmor.getType());
			tempItem = (Item) tempArmor;
			Log.d("neu",
					"" + tempItem.getName() + tempItem.getLevelNeeded()
							+ ((Armor) tempItem).getDefenseValue()
							+ ((Armor) tempItem).getType());
	
			tempItem.setItemType("armor");
			tempArmor = (Armor) tempItem;
			Log.d("neu",
					"" + tempArmor.getName() + tempArmor.getLevelNeeded()
							+ tempArmor.getDefenseValue() + tempArmor.getType()
							+ tempArmor.getItemType());
	
			if (slotListEquip.get(tempArmor.getType()).getNumberOfItems()
					.equalsIgnoreCase("0")) {
				controller.addArmor(tempArmor);
				tempInventoryList = new ArrayList<Item>();				
				for (int i = 0; i < inventoryList.size(); i++) {
	
					if (inventoryList.get(i).getName()
							.equalsIgnoreCase(tempArmor.getName())
							&& removedItem == false) {
						removedItem = true;
					} else {
						tempInventoryList.add(inventoryList.get(i));
					}
				}
	
				controller.setInventory(tempInventoryList);
			} else {
	
				showUsedSlotNotification();
			}
		}

		slotList.clear();
		for (int i = 0; i < 9; i++) {
			slotList.add(new Slot(0, "leer", "leer", "0"));
		}

		resetItemNameTextView();
		resetInteractionButtonText();
		setSlotsUnmarked();
		getSavedItems();
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		resetInteractionButtons();
		setupAllClickListeners();
		Log.d("testArmor", "" + inventoryList.size());
	}

	/** shows Notification, the level is too low to use the clicked Item */
	private void showLevelTooLowNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.achtung);
		dialogBuilder.setMessage("Du musst mindestens Level " + controller.getItemByName(clickedItem).getLevelNeeded() + " haben um dieses Item nutzen zu können!");
		dialogBuilder.setPositiveButton("OK", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();

			}
		});
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}
	
	/** method, which handles the adding of the clicked Weapon to the equip */
	private void addWeaponItem() {
		if(controller.getItemByName(clickedItem).getLevelNeeded() > controller.getPlayerLevel()){
			showLevelTooLowNotification();
		} else {
			removedItem = false;
	
			tempWeapon = (Weapon) controller.getItemByName(clickedItem);
			tempItem = (Item) tempWeapon;
			tempItem.setItemType("weapon");
			tempWeapon = (Weapon) tempItem;
			setSlotsUnmarked();
	
			if (weaponSlot.getNumberOfItems().equalsIgnoreCase("0")) {
	
				controller.setWeapon(tempWeapon);
				tempInventoryList = new ArrayList<Item>();
				for (int i = 0; i < inventoryList.size(); i++) {
					if (inventoryList.get(i).getName()
							.equalsIgnoreCase(tempWeapon.getName())
							&& removedItem == false) {
						removedItem = true;
					} else {
						tempInventoryList.add(inventoryList.get(i));
					}
				}
				controller.setInventory(tempInventoryList);
			} else {
				showUsedSlotNotification();
			}
		}

		slotList.clear();
		for (int i = 0; i < 9; i++) {
			slotList.add(new Slot(0, "leer", "leer", "0"));
		}

		resetItemNameTextView();
		resetInteractionButtonText();

		getSavedItems();
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		resetInteractionButtons();
		setupAllClickListeners();

	}

	/** resets the itemNameTextView */
	private void resetItemNameTextView() {
		itemName.setText("Item:");
		itemDetails.setText("");
	}

	/** method, which handles the removing of the clicked Armor from the equip */
	private void removeEquipItem() {
		
		if(controller.getItemByName(clickedItem).getLevelNeeded() > controller.getPlayerLevel()){
			showLevelTooLowNotification();
		} else {
			inventoryList.add(((Item)controller.getItemByName(clickedItem)));
	
			controller.setInventory(inventoryList);
			setSlotsUnmarked();
	
			controller.removeEquippedArmor((Armor) controller.getItemByName(clickedItem));
		}

		slotList.clear();
		for (int i = 0; i < 9; i++) {
			slotList.add(new Slot(0, "leer", "leer", "0"));
		}

		slotListEquip.clear();
		for (int i = 0; i < 5; i++) {
			slotListEquip.add(new Slot(0, "leer", "leer", "0"));
		}

		resetItemNameTextView();
		resetInteractionButtonText();

		getSavedItems();
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		resetInteractionButtons();
		setupAllClickListeners();
		Log.d("testArmor", "" + inventoryList.size());
	}

	/** method, which handles the removing of the clicked Weapon from the equip */
	private void removeWeaponItem() {
		if(controller.getItemByName(clickedItem).getLevelNeeded() > controller.getPlayerLevel()){
			showLevelTooLowNotification();
		} else {
			inventoryList.add((Item) controller.getItemByName(clickedItem));
	
			controller.setInventory(inventoryList);
			setSlotsUnmarked();
	
			controller.removeEquippedWeapon(equippedWeapon);
			
		}

		slotList.clear();
		for (int i = 0; i < 9; i++) {
			slotList.add(new Slot(0, "leer", "leer", "0"));
		}

		weaponSlot = new Slot(0, "leer", "leer", "0");
		

		resetItemNameTextView();
		resetInteractionButtonText();

		getSavedItems();
		setItemsOnOwnSlots();
		setItemsOnEquipSlots();
		resetInteractionButtons();
		setupAllClickListeners();
	}

	/** shows Notification, that the slot is already used by a Armor of the same type */
	private void showUsedSlotNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder
				.setMessage("Du bist schon mit einem Item des selben Typs ausgerüstet! Du musst Zuerst dieses Item ablegen!");
		dialogBuilder.setPositiveButton("OK", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();

			}
		});
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/** shows Notification, that the clicked Item is not allowed to be used as equip */
	private void showNotEquipableNotification() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Achtung");
		dialogBuilder.setMessage("Du kannst dieses Item nicht anlegen!");
		dialogBuilder.setPositiveButton("OK", new Dialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				resetInteractionButtons();
				resetInteractionButtonText();
				resetItemNameTextView();
				setSlotsUnmarked();

			}
		});
		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/** sets up the OnClickListener for the inventory slots */
	private void setupOnClickListener(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
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
					if (checkNeededInterface()) {
						if (slot.getAttackValue() != 0) {
							itemDetails.setText("Angriff +"
									+ slot.getAttackValue() + "\nWert: "
									+ (5 * (controller.getItemByName(clickedItem).getLevelNeeded())) + " Gold");
							interactionButton.setText("Item\nverkaufen");
						}
						if (slot.getDefenseValue() != 0) {
							itemDetails.setText("Verteidigung +"
									+ slot.getDefenseValue() + "\nWert: "
									+ (5 * (controller.getItemByName(clickedItem).getLevelNeeded())) + " Gold");
							interactionButton.setText("Item\nverkaufen");
						}
						if (slot.getHealValue() != 0) {
							itemDetails.setText("Leben +" + slot.getHealValue()
									+ "\nWert: "
									+ (5 * (controller.getItemByName(clickedItem).getLevelNeeded())) + " Gold");
							interactionButton.setText("Item\nverkaufen");
						}
						setInteractionButtonSell();
					} else {
						if (slot.getAttackValue() != 0) {
							itemDetails.setText("Angriff +"
									+ slot.getAttackValue());
							interactionButton.setText("Waffe\nanlegen");
							setInteractionButtonAddWeapon();
							
						}
						if (slot.getDefenseValue() != 0) {
							itemDetails.setText("Verteidigung +"
									+ slot.getDefenseValue());
							interactionButton.setText("Rüstung\nanlegen");
							setInteractionButtonAddArmor();
						}
						if (slot.getHealValue() != 0) {
							itemDetails.setText("Leben +" + slot.getHealValue());
							interactionButton.setText("Nahrung\nessen");
							setInteractionButtonUseItem();
						}
					}
				}
			}
		});
	}

	/** sets interactionButtons function as useItem */
	private void setInteractionButtonUseItem() {
		interactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showUseItemNotification();
			}
		});
		
	}
	
	/** sets interactionButtons function as addArmor */
	private void setInteractionButtonAddArmor() {
		interactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAddArmorNotification();
			}
		});
		
	}

	/** sets interactionButtons function as addWeapon */
	private void setInteractionButtonAddWeapon() {
		interactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAddWeaponNotification();
			}
		});
	}

	/** sets interactionButtons function as sellItem */
	private void setInteractionButtonSell() {
		interactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSellNotification();
			}
		});
	}

	/** sets up the OnClickListener for the equip slots */
	private void setupOnClickListenerEquip(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
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
					
					if (slot.getAttackValue() != 0) {
						itemDetails.setText("Angriff +"
								+ slot.getAttackValue());
						interactionButton.setText("Waffe\nablegen");
						setInteractionButtonRemoveWeapon();
					}
					if (slot.getDefenseValue() != 0) {
						itemDetails.setText("Verteidigung +"
								+ slot.getDefenseValue());
						interactionButton.setText("Rüstung\nablegen");
						setInteractionButtonRemoveArmor();
					}
				}
			}

		});
	}

	/** sets interactionButtons function as removeArmor */
	private void setInteractionButtonRemoveArmor() {
		interactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showRemoveEquipNotification();
				
			}
		});
	}
	
	/** sets interactionButtons function as removeWeapon */
	private void setInteractionButtonRemoveWeapon() {
		interactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showRemoveWeaponNotification();
				
			}
		});
	}

	/** sets interactionButtons function as buyItem */
	private void setInteractionButtonBuy() {
		interactionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (controller.getGold() >= (10 * controller.getItemByName(clickedItem).getLevelNeeded())) {
					checkLongClickedItemBuy();
				} else {
					showNotEnoughMoneyNotification();
				}
			}
		});
	}

	/** sets up the OnClickListener for the inventory slots when merchant interface is used */
	private void setupOnClickListenerSell(final ImageButton slotBackground,
			final ImageButton slotItem, final Slot slot) {
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
					if (slot.getAttackValue() != 0) {
						itemDetails.setText("Angriff +" + slot.getAttackValue()
								+ "\nPreis: " + (10 * (controller.getItemByName(clickedItem).getLevelNeeded()))
								+ " Gold");
						interactionButton.setText("Waffe\nkaufen");
					}
					if (slot.getDefenseValue() != 0) {
						itemDetails.setText("Verteidigung +"
								+ slot.getDefenseValue() + "\nPreis: "
								+ (10 * (controller.getItemByName(clickedItem).getLevelNeeded())) + " Gold");
						interactionButton.setText("Rüstung\nkaufen");
					}
					if (slot.getHealValue() != 0) {
						itemDetails.setText("Leben +" + slot.getHealValue()
								+ "\nPreis: " + (10 * (controller.getItemByName(clickedItem).getLevelNeeded()))
								+ " Gold");
						interactionButton.setText("Nahrung\nkaufen");
					}
					setInteractionButtonBuy();
				}
			}

		});
	}

	/** sets text of itemName to the name of the clicked Item */
	private void showItemName(Slot slot) {
		itemName.setText("Item: " + slot.getNumberOfItems() + "x "
				+ slot.getItemName());
	}

	@SuppressWarnings("deprecation")
	/** marks clicked slot */
	private void markThisSlot(ImageButton slotBackground, Slot slot) {
		setSlotsUnmarked();
		slotBackground.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.slot_marked));
	}

	@SuppressWarnings("deprecation")
	/** sets all slots unmarked */
	private void setSlotsUnmarked() {

		for (int i = 0; i < 9; i++) {
			slotBackgroundList.get(i).setBackgroundDrawable(
					getResources().getDrawable(R.drawable.slot_unmarked));
			slotList.get(i).setUnmarked();
			if (checkNeededInterface()) {
				slotBackgroundListSell.get(i).setBackgroundDrawable(
						getResources().getDrawable(R.drawable.slot_unmarked));
				slotListSell.get(i).setUnmarked();
			} else {
				if (i < 5) {
					slotBackgroundListEquip.get(i).setBackgroundDrawable(
							getResources()
									.getDrawable(R.drawable.slot_unmarked));
					slotListEquip.get(i).setUnmarked();

					weaponSlotBackground.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.slot_unmarked));
					weaponSlot.setUnmarked();
				}
			}
		}
		healValue = 0;
	}

	public void onDestroy() {
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	/** sets item images on inventory slots */
	private void setItemsOnOwnSlots() {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < sortedItemNames.size(); j++) {
				if (slotList.get(i).getItemName().equalsIgnoreCase("leer")) {
					slotItemImageList.get(i).setBackgroundColor(
							getResources().getColor(R.color.transparent));
				}
				if (slotList.get(i).getItemName()
						.equalsIgnoreCase(sortedItemNames.get(j))) {
					slotItemImageList.get(i).setBackgroundDrawable(
							sortedItemImages.get(j));
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	/** sets item images on merchant slots */
	private void setItemsOnSellSlots() {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < sortedItemNames.size(); j++) {
				if (slotListSell.get(i).getItemName().equalsIgnoreCase("leer")) {
					slotItemImageListSell.get(i).setBackgroundColor(
							getResources().getColor(R.color.transparent));
				}
				if (slotListSell.get(i).getItemName()
						.equalsIgnoreCase(sortedItemNames.get(j))) {
					slotItemImageListSell.get(i).setBackgroundDrawable(
							sortedItemImages.get(j));
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	/** sets item images on equip slots */
	private void setItemsOnEquipSlots() {

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < sortedItemNames.size(); j++) {
				if (slotListEquip.get(i).getItemName().equalsIgnoreCase("leer")) {
					slotItemImageListEquip.get(i).setBackgroundColor(
							getResources().getColor(R.color.transparent));
				}
				if (slotListEquip.get(i).getItemName()
						.equalsIgnoreCase(sortedItemNames.get(j))) {
					slotItemImageListEquip.get(i).setBackgroundDrawable(
							sortedItemImages.get(j));
				}
				if (weaponSlot.getItemName().equalsIgnoreCase("leer")) {
					weaponSlotItemImage.setBackgroundColor(getResources()
							.getColor(R.color.transparent));
				}
				if (weaponSlot.getItemName().equalsIgnoreCase(
						sortedItemNames.get(j))) {
					weaponSlotItemImage.setBackgroundDrawable(sortedItemImages
							.get(j));
				}
			}
		}
	}
	
	/** sets the amount of gold in the moneyTextView to the given i */
	private void setMoneyTextView(int i) {
		moneyTextView.setText("Gold: " + i);
	}

	/** sets up the whole user interface */
	private void setupUI() {
		setContentView(R.layout.activity_inventar);

		sortedItemImages = new ArrayList<Drawable>();

		sortedItemImages.add(getResources().getDrawable(
				R.drawable.abgewetztelederhandschuhe));
		sortedItemImages
				.add(getResources().getDrawable(R.drawable.axtdestodes));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.beinkleiderausrostigemeisen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.bronzehelm));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.bronzenebeinkleider));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.bronzenelanze));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.bronzenerbrustpanzer));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.bronzenestiefel));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.bronzenestulpen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.brustpanzerausverstaerktemleder));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.bunterledernerumhang));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.diamantbeinkleider));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.diamantbrustpanzer));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.diamanthelm));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.diamantstiefel));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.diamantstulpen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.eisernehellebarde));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.eisernerdolch));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.eisernermorgenstern));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.eisernerschlagring));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.eivoneinemgluecklichenhuhn));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.etwaszuschwerereisenhammer));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.fastfrischerschinken));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.fastreifetomate));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.faulerapfel));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.frischerkopfsalat));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.ganzeskaeserad));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.gebratenehaehnchenkeule));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.geraeuchertewuerstchen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.geschaerfterglasdolch));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.goldenebeinkleider));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.goldenerbrustpanzer));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.goldenerhelm));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.goldenestiefel));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.goldenestulpen));
		sortedItemImages.add(getResources()
				.getDrawable(R.drawable.holzknueppel));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.kaumueberreiferkease));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.kupferstreitkolben));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.ledernerkopfschutz));
		sortedItemImages.add(getResources().getDrawable(R.drawable.leinenhemd));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.loechrigeledersandalen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.nagelneueeisenstiefel));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.nagelneueeisenstulpen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.nagelneueeisernebeinkleider));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.nagelneuereisenhelm));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.nagelneuereisernerbrustpanzer));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.nagelneueseisenschwert));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.nochwarmeschweinerippchen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.raeucherlachs));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.rohekartoffel));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.rostigeeisenstiefel));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.rostigeeisenstulpen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.rostigereisenbrustpanzer));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.rostigereisenhelm));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.rostigeseisenschwert));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.schmiedeeisernerstreitkolben));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.schmiedeeiserneslangschwert));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.steinhartehaferkekse));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.stoffhandschuhe));
		sortedItemImages.add(getResources().getDrawable(R.drawable.stoffhose));
		sortedItemImages
				.add(getResources().getDrawable(R.drawable.stoffschuhe));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.stoffstirnband));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.stumpfekupferaxt));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.stumpferkupferdolch));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.ueberauskoestlicherapfelkuchen));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.versalzenerstockfisch));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.verstaerktelederhandschuhe));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.verstaerktelederhose));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.verstaerktelederschuhe));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.verstaerkterlederhelm));
		sortedItemImages
		.add(getResources().getDrawable(R.drawable.vertrocknetesbrot));
		sortedItemImages.add(getResources().getDrawable(
				R.drawable.viermalgeflicktelederhose));

		sortedItemNames = new ArrayList<String>();

		sortedItemNames.add("abgewetzte Lederhandschuhe");
		sortedItemNames.add("Axt des Todes");
		sortedItemNames.add("Beinkleider aus rostigem Eisen");
		sortedItemNames.add("Bronzehelm");
		sortedItemNames.add("Bronzene Beinkleider");
		sortedItemNames.add("Bronzene Lanze");
		sortedItemNames.add("Bronzener Brustpanzer");
		sortedItemNames.add("Bronzene Stiefel");
		sortedItemNames.add("Bronzene Stulpen");
		sortedItemNames.add("Brustpanzer aus verstärktem Leder");
		sortedItemNames.add("Bunter lederner Umhang");
		sortedItemNames.add("Beinkleider aus Diamant");
		sortedItemNames.add("Brustpanzer aus Diamant");
		sortedItemNames.add("Diamanthelm");
		sortedItemNames.add("Diamantstiefel");
		sortedItemNames.add("Diamantstulpen");
		sortedItemNames.add("Eiserne Hellebarde");
		sortedItemNames.add("Eiserner Dolch");
		sortedItemNames.add("Eiserner Morgenstern");
		sortedItemNames.add("Eiserner Schlagring");
		sortedItemNames.add("Ei von einem glücklichen Huhn");
		sortedItemNames.add("Etwas zu schwerer Eisenhammer");
		sortedItemNames.add("fast frischer Schinken");
		sortedItemNames.add("Fast reife Tomate");
		sortedItemNames.add("Fauler Apfel");
		sortedItemNames.add("Frischer Kopfsalat");
		sortedItemNames.add("Ganzes Käserad");
		sortedItemNames.add("Gebratene Hähnchenkeule");
		sortedItemNames.add("Geräucherte Würstchen");
		sortedItemNames.add("Geschärfter Glasdolch");
		sortedItemNames.add("Goldener Beinkleider");
		sortedItemNames.add("Goldener Brustpanzer");
		sortedItemNames.add("Goldener Helm");
		sortedItemNames.add("Goldene Stiefel");
		sortedItemNames.add("Goldene Stulpen");
		sortedItemNames.add("Holzknüppel");
		sortedItemNames.add("kaum überreifer Käse");
		sortedItemNames.add("Kupferstreitkolben");
		sortedItemNames.add("Lederner Kopfschutz");
		sortedItemNames.add("Leinenhemd");
		sortedItemNames.add("löchrige Ledersandalen");
		sortedItemNames.add("Nagelneue Eisenstiefel");
		sortedItemNames.add("Nagelneue Eisenstulpen");
		sortedItemNames.add("Nagelneue eiserne Beinkleider");
		sortedItemNames.add("Nagelneuer Eisenhelm");
		sortedItemNames.add("Nagelneuer eiserner Brustpanzer");
		sortedItemNames.add("Nagelneues Eisenschwert");
		sortedItemNames.add("noch warme Schweinerippchen");
		sortedItemNames.add("Räucherlachs");
		sortedItemNames.add("Rohe Kartoffel");
		sortedItemNames.add("Rostige Eisenstiefel");
		sortedItemNames.add("Rostige Eisenstulpen");
		sortedItemNames.add("Rostiger Eisenbrustpanzer");
		sortedItemNames.add("Rostiger Eisenhelm");
		sortedItemNames.add("Rostiges Eisenschwert");
		sortedItemNames.add("Schmiedeeiserner Streitkolben");
		sortedItemNames.add("Schmiedeeisernes Langschwert");
		sortedItemNames.add("Steinharte Haferkekse");
		sortedItemNames.add("Stoffhandschuhe");
		sortedItemNames.add("Stoffhose");
		sortedItemNames.add("Stoffschuhe");
		sortedItemNames.add("Stoffstirnband");
		sortedItemNames.add("stumpfe Kupferaxt");
		sortedItemNames.add("stumpfer Kupferdolch");
		sortedItemNames.add("überaus köstlicher Apfelkuchen");
		sortedItemNames.add("Versalzener Stockfisch");
		sortedItemNames.add("Verstärkte Lederhandschuhe");
		sortedItemNames.add("Verstärkte Lederhose");
		sortedItemNames.add("Verstärkte Lederschuhe");
		sortedItemNames.add("Verstärkter Lederhelm");
		sortedItemNames.add("Vertrocknetes Brot");
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
		
		interactionButton = (Button) findViewById(R.id.interaction_button);
		resetInteractionButtonText();
		
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
		for (int i = 0; i < 9; i++) {
			slotList.add(new Slot(0, "leer", "leer", "0"));
		}

		slotListSell = new ArrayList<Slot>();
		for (int i = 0; i < 9; i++) {
			slotListSell.add(new Slot(0, "leer", "leer", "0"));
		}

		slotListEquip = new ArrayList<Slot>();
		for (int i = 0; i < 5; i++) {
			slotListEquip.add(new Slot(0, "leer", "leer", "0"));
		}
		weaponSlot = (new Slot(0, "leer", "leer", "0"));

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

	/** returns the boolean slotsFull, which is true, if the inventory slots all are full */
	public boolean inventoryIsFull() {
		return slotsFull;
	}
	
	/**
	 * called when the activity is brought to the front
	 */
	@Override
	protected synchronized void onResume() {
		super.onResume();
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
}