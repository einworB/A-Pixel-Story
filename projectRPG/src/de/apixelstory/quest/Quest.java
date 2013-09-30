package de.apixelstory.quest;

import java.util.ArrayList;

import de.apixelstory.database.Item;

/**
 * This is the parent of KillQuest, TalkToQuest, KillQuest
 * All methods and variables that all of these have in common are here.
 */
public class Quest {

	/** the id of the quest*/
	private int id;
	/** the name of the quest*/
	private String name;
	/** the start text of the quest*/
	private ArrayList<String> startText;
	/** the during text of the quest*/
	private ArrayList<String> duringText;
	/** the end text of the quest*/
	private ArrayList<String> endText;
	/** the short text of the quest*/
	private String shortText;
	/** the level of the quest*/
	private int level;
	/** the special item that can be looted when this quest is finished*/
	private Item specialLoot;
	/** the id of the npc that gives this quest*/
	private int npcID;
	/** is true if the quest is fulfilled*/
	private boolean fulfilled;

	/**
	 * the constructor
	 * @param id the id of the quest
	 * @param name the name of the quest
	 * @param npcID the id of the npc that gives this quest
	 * @param startText the start text of the quest
	 * @param duringText the during text of the quest
	 * @param endText the end text of the quest
	 * @param level the level of the quest
	 * @param specialLoot the special loot of the quest
	 * @param shortText the short text of the quest
	 */
	public Quest(int id, String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, int level, Item specialLoot, String shortText){
		this.id = id;
		this.name = name;
		this.npcID = npcID;
		this.startText = startText;
		this.duringText = duringText;
		this.endText = endText;
		this.level = level;
		this.specialLoot = specialLoot;
		this.shortText = shortText;
		fulfilled = false;
	}
	
	/**
	 * get the id of the quest
	 * @return the id of the quest
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * get the name of the quest
	 * @return the name of the quest
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * get the start text of the quest
	 * @return the start text of the quest
	 */
	public ArrayList<String> getStartText(){
		return startText;
	}
	
	/**
	 * get the during text of the quest. This is the text that is shown if the quest 
	 * was not fulfilled but the player talks to the npc that gives the quest.
	 * @return the during text of the quest
	 */	
	public ArrayList<String> getDuringText(){
		return duringText;
	}
	
	/**
	 * get the end text of the quest
	 * @return the end text of the quest
	 */
	public ArrayList<String> getEndText(){
		return endText;
	}
	
	/**
	 * get the level of the quest 
	 * @return the level of the quest
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * get the reward of the quest. 
	 * This can be null if this quest has no reward.
	 * @return the reward of the quest
	 */
	public Item getReward(){
		return specialLoot;
	}

	/**
	 * get the id of the npc that gives that quest
	 * @return the id of the npc
	 */
	public int getNpcID() {
		return npcID;
	}

	/**
	 * set the boolean fulfilled to true is this method is called.
	 */
	public void setFulfilled(){
		fulfilled = true;
	}
	
	/**
	 * get the boolean if this quest is fulfilled
	 * @return true if this quest is fulfilled.
	 */
	public boolean isFulfilled(){
		return fulfilled;
	}
	
	/**
	 * get the short text of the quest
	 * @return the short text of the quest
	 */
	public String getShortText(){
		return shortText;
	}
}