package de.projectrpg.database;

public class Weapon extends Item{
	
	/** the attack value of this weapon*/
	private int attackValue;
	/** the type of item*/
	private int type;

	/**
	 * the constructor
	 * @param name the name of the weapon
	 * @param levelNeeded the level the player needs to use this weapon
	 * @param attackValue the attack value of this weapon
	 * @param weaponType the typ of the item
	 */
	public Weapon(String name, int levelNeeded, int attackValue, int weaponType){
		super(name, levelNeeded, "weapon");
		this.attackValue = attackValue;
		this.type = weaponType;
	}
	
	/**
	 * get the attack value of this weapon
	 * @return the attack value
	 */
	public int getAttackValue(){
		return attackValue;
	}
	
	/**
	 * get the type of this item
	 * @return
	 */
	public int getType() {
		return type;
	}
}
