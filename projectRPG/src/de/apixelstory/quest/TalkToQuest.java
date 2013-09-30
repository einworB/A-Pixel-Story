package de.apixelstory.quest;

import java.util.ArrayList;

import de.apixelstory.database.Item;

/**
 * A TalkToQuest is a quest where the user has to talk to a npc.
 */
public class TalkToQuest extends Quest{
	
	/** the id of the target npc */
	private int targetNpcID;

	/**
	 * The constructor
	 * @param id the id of the quest
	 * @param name the name of the quest
	 * @param npcID the id to the npc that gives the quest
	 * @param startText the start text of the quest
	 * @param duringText the text that appears when the user talks to the npc when the quest is not closed
	 * @param endText the end text of the quest
	 * @param targetNpcID the id of the target npc
	 * @param level the level of the quest
	 * @param specialReward the name of the item if this quest gives a special reward
	 * @param shortText the short text of the quest
	 */
	public TalkToQuest(int id, String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, int targetNpcID, int level, Item specialReward, String shortText){
		super(id, name, npcID, startText, duringText, endText, level, specialReward, shortText);
		this.targetNpcID = targetNpcID;
	}
	
	/**
	 * get the target npc id
	 * @return the target npc id 
	 */
	public int getTargetID(){
		return targetNpcID;
	}
	
	/**
	 * get the type of the quest
	 * @return the type of the quest
	 */
	public String getType() {
		return "TalkToQuest";
	}
}
