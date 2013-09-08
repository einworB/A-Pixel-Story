package de.projectrpg.database;

public class HealItem extends Item {
	
	private int healValue;

	public HealItem(String name, int levelNeeded, String itemType, int healValue) {
		super(name, levelNeeded, itemType);
		this.healValue = healValue;
	}

	public int getHeal() {
		return healValue;
	}

}
