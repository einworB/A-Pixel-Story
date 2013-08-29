package com.example.projectrpg.quest;

public class Quest {

	private String name;
	private String[] dialog;
	private int level;
	private int[] rewardSeed;

	public Quest(String name, String[] dialog, int level, int[] rewardSeed){
		this.name = name;
		this.dialog = dialog;
		this.level = level;
		this.rewardSeed = rewardSeed;
	}
	
	public String getName(){
		return name;
	}
	
	public String[] getDialog(){
		return dialog;
	}
	
	public int getLevel(){
		return level;
	}
	
	public int[] getRewardSeed(){
		return rewardSeed;
	}
	
}