package de.projectrpg.database;

public class HealItem extends Item {
	
	private int healValue = 10;

	public HealItem(String name, int levelNeeded, String itemType, int healValue) {
		super(name, levelNeeded, itemType);
		this.healValue = healValue;
	}

	public int getHeal() {
		return healValue;
	}

}
