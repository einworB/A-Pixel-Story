package de.projectrpg.database;

public class Armor extends Item{
	
	/** type of armor*/
	private int type;
	/** the defense value of this armor*/
	private int defenseValue;
	
	/**
	 * the constructor
	 * @param name the name of the armor
	 * @param levelNeeded the level the player needs to wear this armor
	 * @param defenseValue the defense value of this armor
	 * @param armorType the type of armor
	 */
	public Armor(String name, int levelNeeded, int defenseValue, int armorType){
		super(name, levelNeeded, "armor");
		this.defenseValue = defenseValue;
		this.type = armorType;
	}

	/**
	 * get the type of the armor
	 * @return the type of the armor
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * get the defense value of the armor
	 * @return the defense value
	 */
	public int getDefenseValue(){
		return defenseValue;
	}
}
