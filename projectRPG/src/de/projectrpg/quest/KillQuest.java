package de.projectrpg.quest;

import java.util.ArrayList;

import de.projectrpg.database.Item;


public class KillQuest extends Quest{
	
	private String enemyName;
	private int killCount;
	private int alreadyKilled;

	
	public KillQuest(String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, String enemyName, int killCount, int level, Item specialReward){
		super(name, npcID, startText, duringText, endText, level, specialReward);
		this.enemyName = enemyName;
		this.killCount = killCount;
		alreadyKilled = 0;
	}
	
	public String getEnemyName(){
		return enemyName;
	}
	
	public boolean gotOne(){
		alreadyKilled++;
		if(alreadyKilled==killCount) return true;
		else return false;
	}
	
	public int getKillCount() {
		return killCount;
	}

}
