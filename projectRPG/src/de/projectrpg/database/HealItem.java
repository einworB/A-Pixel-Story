package de.projectrpg.database;

public class HealItem extends Item {

	public HealItem(String name, int levelNeeded, String itemType) {
		super(name, levelNeeded, itemType);
	}

	public int getHeal() {
		return 0;
	}

}
