package de.projectrpg.quest;

import java.util.ArrayList;

import de.projectrpg.database.Item;

/**
 * A Killquest is a quest where the task is to kill an amount of opponents
 */
public class KillQuest extends Quest{
	
	/** the name of the enemy*/
	private String enemyName;
	/** the counter how many opponents the player have to kill*/
	private int killCount;
	/** the counter how many opponents where killed for this quest*/
	private int alreadyKilled;

	/**
	 * the constructor
	 * @param id the id of the quest
	 * @param name the name of the quest
	 * @param npcID the id of the npc that gives that quest
	 * @param startText the start text of the quest
	 * @param duringText the text to show when the quest wasn't finished and the player speaks to the npc
	 * @param endText the end text of the quest
	 * @param enemyName the name of the opponent
	 * @param killCount the counter how many opponents the player have to kill
	 * @param level the level of the quest
	 * @param specialReward the special reward the quest gives
	 * @param shortText the short description of the quest
	 */
	public KillQuest(int id, String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, String enemyName, int killCount, int level, Item specialReward, String shortText){
		super(id, name, npcID, startText, duringText, endText, level, specialReward, shortText);
		this.enemyName = enemyName;
		this.killCount = killCount;
		alreadyKilled = 0;
	}
	
	/**
	 * get the name of the enemy
	 * @return the name of the enemy
	 */
	public String getEnemyName(){
		return enemyName;
	}
	
	/**
	 * If a opponent was killed this method is called. 
	 * @return if the killcount equals the alreadyKilled count this method returns true
	 */
	public boolean gotOne(){
		if(alreadyKilled < killCount) {
			alreadyKilled++;			
		}
		if(alreadyKilled==killCount) return true;
		else return false;
	}
	
	/**
	 * get the kill count. The kill count if the counter how many opponents the player have to kill
	 * @return the kill count
	 */
	public int getKillCount() {
		return killCount;
	}

	/**
	 * Get the count how many opponents where killed already
	 * @return the count
	 */
	public int getAlreadyKilled() {
		return alreadyKilled;
	}
	
	/**
	 * set the count how many opponents where killed already
	 * @param alreadyKilled the count
	 */
	public void setAlreadyKilled(int alreadyKilled) {
		if(alreadyKilled < killCount) {
			this.alreadyKilled = alreadyKilled;			
		} else {
			this.alreadyKilled = killCount;
		}
	}
	
	/**
	 * get the type of the quest
	 * @return the type of the quest
	 */
	public String getType() {
		return "KillQuest";
	}
}
