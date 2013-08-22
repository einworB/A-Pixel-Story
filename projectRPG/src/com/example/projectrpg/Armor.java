package com.example.projectrpg;

public class Armor {
	
	
	private int type;
	private String name;
	private int levelNeeded;
	private int defenseValue;
	
	public Armor(int id){
		getValuesFromDatabase(id);
	}

	private void getValuesFromDatabase(int id) {
		
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
