package de.apixelstory.quest;

import java.util.ArrayList;



import de.apixelstory.database.Item;
import de.apixelstory.game.Controller;
import de.apixelstory.sprites.NPC;

/**
 * Contains all quests and check if a quest is fulfilled or not.
 */
public class QuestManager {
	
	/** a list with all active quests*/
	private ArrayList<Quest> activeQuests;
	/** a list with all closed quests*/
	private ArrayList<Quest> closedQuests;
	/** the controller*/
	private Controller controller;
	
	/**
	 * The constructor. Initialize the active and closed quests.
	 * @param controller
	 */
	public QuestManager(Controller controller){
		activeQuests = new ArrayList<Quest>();
		closedQuests = new ArrayList<Quest>();
		this.controller = controller;
	}

	/**
	 * Start a quest.
	 * @param quest the quest to start.
	 */
	public void startQuest(Quest quest){
		activeQuests.add(quest);
	}
	
	/**
	 * end a quest.
	 * @param quest the quest to end
	 */
	public void endQuest(Quest quest) {
		closedQuests.add(quest);
	}
	
	/**
	 * end a quest by index
	 * @param i the index of the quest to end
	 */
	public void endQuestByIndex(int i){
		controller.addExp(activeQuests.get(i).getLevel()*150);
		closedQuests.add(activeQuests.remove(i));
	}
	
	/**
	 * get all active quests
	 * @return all active quests
	 */
	public ArrayList<Quest> getActiveQuests(){
		return activeQuests;
	}

	/**
	 * get all closed quests
	 * @return all closed quests
	 */
	public ArrayList<Quest> getClosedQuests() {
		return closedQuests;
	}

	/**
	 * check if a quest is a GetItemQuest and if this quest is fulfilled.
	 * @param item the item that was collected
	 */
	public void checkQuests(Item item) {
//		Log.d("checkQuests", "checking Getitemquests; Active Quests: "+activeQuests.toString());
		if(!activeQuests.isEmpty()){
			for(int i=0; i<activeQuests.size(); i++){
				Quest quest = activeQuests.get(i);
				if(quest instanceof GetItemQuest){
					if(((GetItemQuest) quest).getItemName().contentEquals(item.getName())){
						if(((GetItemQuest) quest).gotOne()){
//							Log.d("checkQuests", "Quest fulfilled!");
							quest.setFulfilled();
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * check if a quest is a TalkToQuest and if this quest is fulfilled.
	 * @param npc the npc that the user talks to
	 */
	public void checkQuests(NPC npc) {
//		Log.d("checkQuests", "checking Talktoquests; Active Quests: "+activeQuests.toString());
		if(!activeQuests.isEmpty()){
			for(int i=0; i<activeQuests.size(); i++){
				Quest quest = activeQuests.get(i);
				if(quest instanceof TalkToQuest){
					if(((TalkToQuest) quest).getTargetID()==npc.getID()){
//						Log.d("checkQuests", "Quest fulfilled!");
						quest.setFulfilled();					
						break;
					}
				}
			}
		}
	}

	/**
	 * check if a quest is a GetKillQuest and if this quest is fulfilled.
	 * @param enemyName the name of the enemy that was killed
	 */
	public void checkQuests(String enemyName) {
//		Log.d("checkQuests", "checking Killquests; Active Quests: "+activeQuests.toString());
		if(!activeQuests.isEmpty()){
			for(int i=0; i<activeQuests.size(); i++){
				Quest quest = activeQuests.get(i);
				if(quest instanceof KillQuest){
					if(((KillQuest) quest).getEnemyName().contentEquals(enemyName)){
						if(((KillQuest) quest).gotOne()){
//							Log.d("checkQuests", "Quest fulfilled!");
							quest.setFulfilled();
							break;
						}
					}
				}
			}
		}
	}
}
