package de.projectrpg.quest;

import java.util.ArrayList;

import de.projectrpg.database.Item;


public class TalkToQuest extends Quest{
	
	private int targetNpcID;


	public TalkToQuest(int id, String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, int targetNpcID, int level, Item specialReward, String shortText){
		super(id, name, npcID, startText, duringText, endText, level, specialReward, shortText);
		this.targetNpcID = targetNpcID;
	}
	
	public int getTargetID(){
		return targetNpcID;
	}
	
	public String getType() {
		return "TalkToQuest";
	}
}
