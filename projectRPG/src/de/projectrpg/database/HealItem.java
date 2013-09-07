package de.projectrpg.database;

public class HealItem extends Item {
	
	private int healValue = 10;

	public HealItem(String name, int levelNeeded, String itemType) {
		super(name, levelNeeded, itemType);
	}

	public int getHeal() {
		return healValue;
	}

	public void setHeal(int healValue) {
		this.healValue = healValue;
	}

}
