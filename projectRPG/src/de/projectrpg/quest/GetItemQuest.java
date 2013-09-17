package de.projectrpg.quest;

import java.util.ArrayList;

import de.projectrpg.database.Item;

/**
 * A GetItemQuest is a quest where the task is to get special items from someone or something.
 */
public class GetItemQuest extends Quest{
	/** the name of the item to get*/
	private String itemName;
	/** the count how many items the user should collect*/
	private int count;
	/** the count how many items the user got already*/
	private int alreadyFound;

	/**
	 * 
	 * @param id the id of the quest
	 * @param name the name of the quest
	 * @param npcID the id of the npc that gives that quest
	 * @param startText the start text of this quest
	 * @param duringText the text that appears when the user want to speak to the npc when the quest is not finished
	 * @param endText the text when the quest was finished
	 * @param itemName the name of the item to collect
	 * @param count the count how many items the user should collect
	 * @param level 
	 * @param specialReward the name of an item if this quest gives a special reward
	 * @param shortText the text that is shown in the questscene
	 */
	public GetItemQuest(int id, String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, String itemName, int count, int level, Item specialReward, String shortText){
		super(id, name, npcID, startText, duringText, endText, level, specialReward, shortText);
		this.itemName = itemName;
		this.count = count;
		alreadyFound = 0;
	}
	
	/**
	 * get the name of the item
	 * @return the name of the item
	 */
	public String getItemName(){
		return itemName;
	}

	/**
	 * This method is called if a item is collected.
	 * @return If the already found count equals the count of items that should be collected
	 * this mtehod returns true.
	 */
	public boolean gotOne(){
		if(alreadyFound < count) {
			alreadyFound++;
		}
		if(alreadyFound==count) return true;
		else return false;
	}
	
	/**
	 * get the count how many items where collected already
	 * @return the count how many items where collected already
	 */
	public int getAlreadyFound() {
		return alreadyFound;
	}
	
	/**
	 * set the count to a given value
	 * @param alreadyFound the maximal value or the actual count
	 */
	public void setAlreadyFound(int alreadyFound) {
		if(alreadyFound < count) {
			this.alreadyFound = alreadyFound;
		} else {
			this.alreadyFound = count;
		}
	}
	
	/**
	 * get the item count
	 * @return the item count
	 */
	public int getItemCount(){
		return count;
	}
	
	/**
	 * get the type of the quest as String
	 * @return the type of the quest
	 */
	public String getType() {
		return "GetItemQuest";
	}
}
