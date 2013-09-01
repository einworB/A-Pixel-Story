package com.example.projectrpg;

public class Slot {
	
	int slotID;
	String itemName;
	String itemType;
	boolean marked;
	String numberOfItems;
	int defenseValue = 0;
	int attackValue = 0;
	int levelNeeded = 0;
	
	public Slot(){
		slotID = 0;
		itemName = "";
		itemType = "";
		marked = false;
		numberOfItems = "0";
	}
	
	public Slot(int slotID, String itemName, String itemType, String numberOfItems) {
		this.slotID = slotID;
		this.itemName = itemName;
		this.itemType = itemType;
		this.numberOfItems = numberOfItems;
	}
	
	public void setSlotID(int slotID){
		this.slotID = slotID;
	}
	
	public int getSlotID(){
		return slotID;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}
	
	public String getItemName (){
		return itemName;
		
	}
	
	public void setItemType(String itemType){
		this.itemType = itemType;
	}
	
	public String getItemType(){
		return itemType;
	}
	
	public void setMarked(){
		marked = true;
	}
	
	public void setUnmarked(){
		marked = false;
	}
	
	public boolean getIfMarked(){
		return marked;
	}
	
	public void setNumberOfItems(String numberOfItems){
		this.numberOfItems = numberOfItems;
	}
	
	public String getNumberOfItems(){
		return numberOfItems;
	}
	
	public void eraseSlot(){
		itemName = "leer";
		itemType = "leer";
		numberOfItems = "0";
		marked = false;
		defenseValue = 0;
	}
	
	public void setDefenseValue(int defenseValue){
		this.defenseValue = defenseValue;
	}
	
	public int getDefenseValue(){
		return defenseValue;
	}
	
	public void setAttackValue(int attackValue){
		this.attackValue = attackValue;
	}
	
	public int getAttackValue(){
		return attackValue;
	}
	
	public void setlevelNeeded(int levelNeeded){
		this.levelNeeded = levelNeeded;
	}
	
	public int getLevelNeeded(){
		return levelNeeded;
	}

}
