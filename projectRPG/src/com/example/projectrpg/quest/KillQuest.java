package com.example.projectrpg.quest;


public class KillQuest extends Quest{
	
	private String enemyName;
	private int killCount;

	/** killQuest without specific reward */
	public KillQuest(String name, String[] dialog, String enemyName, int killCount, int level){
		this(name, dialog, enemyName, killCount, level, null);
	}
	
	/** killQuest with specific loot reward */
	public KillQuest(String name, String[] dialog, String enemyName, int killCount, int level, int[] rewardSeed){
		super(name, dialog, level, rewardSeed);
		this.enemyName = enemyName;
		this.killCount = killCount;

	}

}
