package de.projectrpg.quest;

import java.util.ArrayList;

import de.projectrpg.database.Item;


public class GetItemQuest extends Quest{
	
	private String itemName;
	private int count;
	private int alreadyFound;

	public GetItemQuest(String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, String itemName, int count, int level, Item specialReward){
		super(name, npcID, startText, duringText, endText, level, specialReward);
		this.itemName = itemName;
		this.count = count;
		alreadyFound = 0;
	}
	
	public String getItemName(){
		return itemName;
	}

	public boolean gotOne(){
		alreadyFound++;
		if(alreadyFound==count) return true;
		else return false;
	}
}
