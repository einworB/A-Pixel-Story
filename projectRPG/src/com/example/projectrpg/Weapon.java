package com.example.projectrpg;

public class Weapon {
	
	private String name;
	private int levelNeeded;
	private int attackValue;

	public Weapon(String id){
		getValuesFromDatabase(id);
	}
	
	private void getValuesFromDatabase(String id) {
		
	}

	public String getName(){
		return name;
	}
	
	public int getAttackValue(){
		return attackValue;
	}
	
	public int getLevelNeeded(){
		return levelNeeded;
	}
	

}
