package com.example.projectrpg;

public class Armor extends Item{
	
	
	private int type;
	private String name;
	private int levelNeeded;
	private int defenseValue;
	
	public Armor(String name, int levelNeeded, int defenseValue, int armorType){
		super("armor");
		this.name = name;
		this.levelNeeded = levelNeeded;
		this.defenseValue = defenseValue;
		this.type = armorType;
	}

	public String getName(){
		return name;
	}
	
	public int getLevelNeeded() {
		return levelNeeded;
	}

	public int getType() {
		return type;
	}
	
	public int getDefenseValue(){
		return defenseValue;
	}

}
