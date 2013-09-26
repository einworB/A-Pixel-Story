package de.projectrpg.inventory;

/**
 * This is the class for the slots, which are used in the InventoryActivity
 */
public class Slot {
	
	
	//=======================================================================FIELDS========================================================================================	
	
	/** saves the ID of the slot */
	int slotID;
	
	/** saves the name of the Item, which is put in the slot */
	String itemName;
	
	/** saves the type of the Item, which is put in the slot */
	String itemType;
	
	/** if the slot gets clicked by the user, this boolean gets true*/
	boolean marked = false;
	
	/** saves the number(amount) of the Item, which is put in the slot*/
	String numberOfItems;
	
	/** saves the defense value of the Item, which is put in the slot*/
	int defenseValue = 0;
	
	/** saves the attack value of the Item, which is put in the slot*/
	int attackValue = 0;
	
	/** saves the playerlevel, which is needed to use the Item, which is put in the slot*/
	int levelNeeded = 0;
	
	/** saves the heal value of the Item, which is put in the slot*/
	int healValue = 0;
	
	/**
	 * the empty constructor
	 */
	public Slot(){
		slotID = 0;
		itemName = "";
		itemType = "";
		marked = false;
		numberOfItems = "0";
	}
	
	/**
	 * the constructor
	 * @param slotID the ID of the slot
	 * @param itemName the name of the Item, which is put in the slot
	 * @param itemType the type of the Item, which is put in the slot
	 * @param numberOfItems the amount of the Item, which is put in the slot
	 */
	public Slot(int slotID, String itemName, String itemType, String numberOfItems) {
		this.slotID = slotID;
		this.itemName = itemName;
		this.itemType = itemType;
		this.numberOfItems = numberOfItems;
	}
	
	/**
	 * set the ID of the slot
	 * @param slotID the ID of the slot
	 */
	public void setSlotID(int slotID){
		this.slotID = slotID;
	}
	/**
	 * get the ID of the slot
	 * @return slotID
	 */
	public int getSlotID(){
		return slotID;
	}

	/**
	 * set the name of the Item, which is in the slot
	 * @param itemName the name of the Item, which is put in the slot
	 */
	public void setItemName(String itemName){
		this.itemName = itemName;
	}
	
	/**
	 * get the name of the Item, which is put in the slot
	 * @return itemName
	 */
	public String getItemName (){
		return itemName;
		
	}
	
	/**
	 * set the type of the Item, which is in the slot
	 * @param itemType the type of the Item, which is put in the slot
	 */
	public void setItemType(String itemType){
		this.itemType = itemType;
	}
	
	/**
	 * get the type of the Item, which is put in the slot
	 * @return itemType
	 */
	public String getItemType(){
		return itemType;
	}
	
	/**
	 * set this slot marked
	 */
	public void setMarked(){
		marked = true;
	}
	
	/**
	 * set this slot unmarked
	 */
	public void setUnmarked(){
		marked = false;
	}
	
	/**
	 * get, if this slot is marked
	 * @return marked
	 */
	public boolean getIfMarked(){
		return marked;
	}
	
	/**
	 * set the number of the Item, which is in the slot
	 * @param numberOfItems the number(amount) of the Item, which is put in the slot
	 */
	public void setNumberOfItems(String numberOfItems){
		this.numberOfItems = numberOfItems;
	}
	
	/**
	 * get the number(amount) of the Item, which is put in the slot
	 * @return numberOfItems
	 */
	public String getNumberOfItems(){
		return numberOfItems;
	}
	/**
	 * reset the slot and make the slot empty
	 */
	public void eraseSlot(){
		itemName = "leer";
		itemType = "leer";
		numberOfItems = "0";
		marked = false;
		defenseValue = 0;
	}
	
	/**
	 * set the defense value of the Item, which is put in the slot
	 * @param defenseValue the defense value of the Item, which is put in the slot
	 */
	public void setDefenseValue(int defenseValue){
		this.defenseValue = defenseValue;
	}
	
	/**
	 * get the defense value of the Item, which is put in the slot
	 * @return defenseValue
	 */
	public int getDefenseValue(){
		return defenseValue;
	}
	
	/**
	 * set the attack value of the Item, which is put in the slot
	 * @param attackValue the attack value of the Item, which is put in the slot
	 */
	public void setAttackValue(int attackValue){
		this.attackValue = attackValue;
	}
	
	/**
	 * get the attack value of the Item, which is put in the slot
	 * @return attackValue
	 */
	public int getAttackValue(){
		return attackValue;
	}
	
	/**
	 * set the level, which is needed to use the Item, which is put in the slot
	 * @param levelNeeded the level, which is needed to use the Item, which is put in the slot
	 */
	public void setlevelNeeded(int levelNeeded){
		this.levelNeeded = levelNeeded;
	}
	
	/**
	 * get the level, which is needed to use the Item, which is put in the slot
	 * @return levelNeeded
	 */
	public int getLevelNeeded(){
		return levelNeeded;
	}
	
	/**
	 * set the heal value of the Item, which is put in the slot
	 * @param healValue the heal value of the Item, which is put in the slot
	 */
	public void setHealValue(int healValue){
		this.healValue = healValue;
	}
	
	/**
	 * get the heal value of the Item, which is put in the slot
	 * @return healValue
	 */
	public int getHealValue() {
		return healValue;
	}
}
