package com.example.projectrpg.quest;


public class GetItemQuest extends Quest{
	
	private int itemID;
	private int count;


	/** getItemQuest without specific reward */
	public GetItemQuest(String name, String[] dialog, int itemID, int count, int level){
		this(name, dialog, itemID, count, level, null);
	}
	
	
	/** getItemQuest with specific reward */
	public GetItemQuest(String name, String[] dialog, int itemID, int count, int level, int[] rewardSeed){
		super(name, dialog, level, rewardSeed);
		this.itemID = itemID;
		this.count = count;
	}

}
