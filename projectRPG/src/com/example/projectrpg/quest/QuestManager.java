package com.example.projectrpg.quest;

import java.util.ArrayList;


public class QuestManager {
	
	private ArrayList<Quest> activeQuests;
	private ArrayList<Quest> closedQuests;
	
	public QuestManager(){
		activeQuests = new ArrayList<Quest>();
		closedQuests = new ArrayList<Quest>();
	}

	public void startQuest(Quest quest){
		activeQuests.add(quest);
	}
	
	public void endQuest(int i){
		closedQuests.add(activeQuests.get(i));
		activeQuests.remove(i);
	}
	
	public ArrayList<Quest> getActiveQuests(){
		return activeQuests;
	}
}
