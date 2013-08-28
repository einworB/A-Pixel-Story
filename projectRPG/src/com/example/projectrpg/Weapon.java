package com.example.projectrpg;

public class Weapon extends Item{
	
	private String name;
	private int levelNeeded;
	private int attackValue;
	private int type;

	public Weapon(String name, int levelNeeded, int attackValue, int weaponType){
		super("weapon");
		this.name = name;
		this.levelNeeded = levelNeeded;
		this.attackValue = attackValue;
		this.type = weaponType;
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
	
	public int getType() {
		return type;
	}
	
	
	

}
