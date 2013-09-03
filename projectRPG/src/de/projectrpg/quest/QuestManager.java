package de.projectrpg.quest;

import java.util.ArrayList;

import android.util.Log;


import de.projectrpg.database.Item;
import de.projectrpg.game.Controller;
import de.projectrpg.sprites.NPC;


public class QuestManager {
	
	private ArrayList<Quest> activeQuests;
	private ArrayList<Quest> closedQuests;
	private Controller controller;
	
	public QuestManager(Controller controller){
		activeQuests = new ArrayList<Quest>();
		closedQuests = new ArrayList<Quest>();
		this.controller = controller;
	}

	public void startQuest(Quest quest){
		activeQuests.add(quest);
	}
	
	public void endQuest(int i){
		controller.addExp(activeQuests.get(i).getLevel()*150);
		closedQuests.add(activeQuests.remove(i));
	}
	
	
	public ArrayList<Quest> getActiveQuests(){
		return activeQuests;
	}

	public ArrayList<Quest> getClosedQuests() {
		return closedQuests;
	}

	public void checkQuests(Item item) {
		Log.d("checkQuests", "checking Getitemquests; Active Quests: "+activeQuests.toString());
		if(!activeQuests.isEmpty()){
			for(int i=0; i<activeQuests.size(); i++){
				Quest quest = activeQuests.get(i);
				if(quest instanceof GetItemQuest){
					if(((GetItemQuest) quest).getItemName().contentEquals(item.getName())){
						if(((GetItemQuest) quest).gotOne()){
							Log.d("checkQuests", "Quest fulfilled!");
							quest.setFulfilled();
							break;
						}
					}
				}
			}
		}
	}

	public void checkQuests(NPC npc) {
		Log.d("checkQuests", "checking Talktoquests; Active Quests: "+activeQuests.toString());
		if(!activeQuests.isEmpty()){
			for(int i=0; i<activeQuests.size(); i++){
				Quest quest = activeQuests.get(i);
				if(quest instanceof TalkToQuest){
					if(((TalkToQuest) quest).getTargetID()==npc.getID()){
						Log.d("checkQuests", "Quest fulfilled!");
						quest.setFulfilled();					
						break;
					}
				}
			}
		}
	}

	public void checkQuests(String enemyName) {
		Log.d("checkQuests", "checking Killquests; Active Quests: "+activeQuests.toString());
		if(!activeQuests.isEmpty()){
			for(int i=0; i<activeQuests.size(); i++){
				Quest quest = activeQuests.get(i);
				if(quest instanceof KillQuest){
					if(((KillQuest) quest).getEnemyName().contentEquals(enemyName)){
						if(((KillQuest) quest).gotOne()){
							Log.d("checkQuests", "Quest fulfilled!");
							quest.setFulfilled();
							break;
						}
					}
				}
			}
		}
	}
}
