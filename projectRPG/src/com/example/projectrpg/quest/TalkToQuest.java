package com.example.projectrpg.quest;

import java.util.ArrayList;

import com.example.projectrpg.Item;


public class TalkToQuest extends Quest{
	
	private int targetNpcID;


	public TalkToQuest(String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, int targetNpcID, int level, Item specialReward){
		super(name, npcID, startText, duringText, endText, level, specialReward);
		this.targetNpcID = targetNpcID;
	}
	
	public int getTargetID(){
		return targetNpcID;
	}
}
