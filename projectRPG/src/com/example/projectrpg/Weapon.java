package com.example.projectrpg;

public class Weapon extends Item{
	
	private int attackValue;
	private int type;

	public Weapon(String name, int levelNeeded, int attackValue, int weaponType){
		super(name, levelNeeded, "weapon");
		this.attackValue = attackValue;
		this.type = weaponType;
	}
	
	public int getAttackValue(){
		return attackValue;
	}
	
	public int getType() {
		return type;
	}
	
	
	

}
