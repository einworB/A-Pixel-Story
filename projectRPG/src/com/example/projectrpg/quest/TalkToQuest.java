package com.example.projectrpg.quest;


public class TalkToQuest extends Quest{
	
	private String npcName;

	/** talkToQuest without specific reward */
	public TalkToQuest(String name, String[] dialog, String npcName, int level){
		this(name, dialog, npcName, level, null);
	}

	/** talkToQuest with specific reward */
	public TalkToQuest(String name, String[] dialog, String npcName, int level, int[] rewardSeed){
		super(name, dialog, level, rewardSeed);
		this.npcName = npcName;
	}
}
