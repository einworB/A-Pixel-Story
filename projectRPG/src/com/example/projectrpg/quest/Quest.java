package com.example.projectrpg.quest;

import java.util.ArrayList;

import com.example.projectrpg.Item;

public class Quest {

	private String name;
	private ArrayList<String> startText;
	private int level;
	private Item specialLoot;
	private ArrayList<String> duringText;
	private ArrayList<String> endText;
	private int npcID;
	private boolean fulfilled;

	public Quest(String name, int npcID, ArrayList<String> startText, ArrayList<String> duringText, ArrayList<String> endText, int level, Item specialLoot){
		this.name = name;
		this.npcID = npcID;
		this.startText = startText;
		this.duringText = duringText;
		this.endText = endText;
		this.level = level;
		this.specialLoot = specialLoot;
		fulfilled = false;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<String> getStartText(){
		return startText;
	}
	
	public ArrayList<String> getDuringText(){
		return duringText;
	}
	
	public ArrayList<String> getEndText(){
		return endText;
	}
	
	public int getLevel(){
		return level;
	}
	
	public Item getReward(){
		return specialLoot;
	}

	public int getNpcID() {
		return npcID;
	}

	public void setFulfilled(){
		fulfilled = true;
	}
	
	public boolean isFulfilled(){
		return fulfilled;
	}
}