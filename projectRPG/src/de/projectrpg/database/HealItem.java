package de.projectrpg.database;

public class HealItem extends Item {
	
	/** the heal value of this item*/
	private int healValue;

	/**
	 * the constructor
	 * @param name the name of this item
	 * @param levelNeeded the level the player needs to eat this item
	 * @param itemType the typ of item
	 * @param healValue the heal value of this item
	 */
	public HealItem(String name, int levelNeeded, String itemType, int healValue) {
		super(name, levelNeeded, itemType);
		this.healValue = healValue;
	}

	/**
	 * get the heal value
	 * @return the heal value
	 */
	public int getHeal() {
		return healValue;
	}

}
