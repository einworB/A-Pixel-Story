package com.example.projectrpg;

public class Slot {
	
	int slotID;
	String itemName;
	String itemType;
	boolean marked;
	int numberOfItems;
	
	public Slot(){
		slotID = 0;
		itemName = "";
		itemType = "";
		marked = false;
		numberOfItems = 0;
	}
	
	public Slot(int slotID, String itemName, String itemType, int numberOfItems) {
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
	
	public void setNumberOfItems(int numberOfItems){
		this.numberOfItems = numberOfItems;
	}
	
	public int getNumberOfItems(){
		return numberOfItems;
	}

}
