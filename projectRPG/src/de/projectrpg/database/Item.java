package de.projectrpg.database;

/**
 * parent class for all item types
 * holds values like type(heal/armor/weapon), the needed level and the name of the item
 */
public class Item {
	
	/** the type of the item*/
	private String itemType;
	/** the level the player needs to use this item*/
	private int levelNeeded;
	/** the name of the item*/
	private String name;
	
	/**
	 * the constructor
	 * @param name the name of the item
	 * @param levelNeeded the level the player needs to use this item
	 * @param itemType the type of the item
	 */
	public Item(String name, int levelNeeded, String itemType){
		this.name = name;
		this.levelNeeded = levelNeeded;
		this.itemType = itemType;
	}

	/**
	 * get the type of the item
	 * @return the type of the item
	 */
	public String getItemType(){
		return itemType;
	}
	
	/**
	 * get the name of the item
	 * @return the name of the item
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * get the level the player needs to use this item
	 * @return the level the player needs
	 */
	public int getLevelNeeded() {
		return levelNeeded;
	}

	/**
	 * get the type of the item
	 * @param itemType the type of the item
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
}
