package de.projectrpg.database;

public class Armor extends Item{
	
	
	private int type;
	private int defenseValue;
	
	public Armor(String name, int levelNeeded, int defenseValue, int armorType){
		super(name, levelNeeded, "armor");
		this.defenseValue = defenseValue;
		this.type = armorType;
	}

	

	public int getType() {
		return type;
	}
	
	public int getDefenseValue(){
		return defenseValue;
	}

}
