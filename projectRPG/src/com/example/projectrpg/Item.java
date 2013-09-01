package com.example.projectrpg;

public class Item {
	
	private String itemType;
	private int levelNeeded;
	private String name;
	
	public Item(String name, int levelNeeded, String itemType){
		this.name = name;
		this.levelNeeded = levelNeeded;
		this.itemType = itemType;
	}

	public String getItemType(){
		return itemType;
	}
	
	public String getName(){
		return name;
	}
	
	public int getLevelNeeded() {
		return levelNeeded;
	}
}
