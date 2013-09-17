package de.projectrpg.quest;

import java.util.ArrayList;

import de.projectrpg.database.Item;

/**
 * A Killquest is a quest where the task is to kill a amount of opponents
 */
public class KillQuest extends Quest{
	
	private String enemyName;
	private int killCount;
	private int alreadyKilled;

	
	public KillQuest(int id, String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, String enemyName, int killCount, int level, Item specialReward, String shortText){
		super(id, name, npcID, startText, duringText, endText, level, specialReward, shortText);
		this.enemyName = enemyName;
		this.killCount = killCount;
		alreadyKilled = 0;
	}
	
	public String getEnemyName(){
		return enemyName;
	}
	
	public boolean gotOne(){
		if(alreadyKilled < killCount) {
			alreadyKilled++;			
		}
		if(alreadyKilled==killCount) return true;
		else return false;
	}
	
	public int getKillCount() {
		return killCount;
	}

	public int getAlreadyKilled() {
		return alreadyKilled;
	}
	
	public void setAlreadyKilled(int alreadyKilled) {
		if(alreadyKilled < killCount) {
			this.alreadyKilled = alreadyKilled;			
		} else {
			this.alreadyKilled = killCount;
		}
	}
	
	public String getType() {
		return "KillQuest";
	}
}
