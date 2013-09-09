package de.projectrpg.save;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;
import de.projectrpg.sprites.Opponent;

public class LoadSavedGame {
	
	private Context context;
	private HashMap<String, String> loadValues = new HashMap<String, String>();
	private ArrayList<Opponent> opponents;
	private ArrayList<QuestObjects> openQuestList;
	private ArrayList<QuestObjects> closedQuestList;
	private PlayerObject player;
	
	public LoadSavedGame(Context context) {
		this.context = context;
		opponents = new ArrayList<Opponent>();
		openQuestList = new ArrayList<QuestObjects>();
		closedQuestList = new ArrayList<QuestObjects>();
	}
	
	public void loadGame(int slot) {
		try {
			InputStream inputStream = context.openFileInput("slot" + slot + ".xml");

			XmlPullParser parser = Xml.newPullParser();
			
			parser.setInput(inputStream, null);
			int eventType = parser.getEventType();
			boolean done = false;
			int level = 1;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
            	String startTagName = null;
            	String attributeName = null;
            	String attributeValue = null;
            	
                switch (eventType) {
                case XmlPullParser.START_TAG:
                	startTagName = parser.getName();
                	if(startTagName.equalsIgnoreCase("variables")) {
                		attributeName = parser.getAttributeName(0);
                		attributeValue = parser.getAttributeValue(0);
                		loadValues.put(attributeName, attributeValue);
                		
                		attributeName = parser.getAttributeName(1);
                		attributeValue = parser.getAttributeValue(1);
                		loadValues.put(attributeName, attributeValue);
                	} else if(startTagName.contains("level")) {
                		LevelLoader levelLoader = new LevelLoader(level);
                		parser.next();
                		String startTagName2 = null;
                		while(!parser.getName().contentEquals(startTagName)){
                			startTagName2 = parser.getName();
                			if(startTagName2.equalsIgnoreCase("mapName")) {
                        		levelLoader.setMapName(parser.getAttributeValue(0));
                			} else if(startTagName2.equalsIgnoreCase("opponents")) {
                				parser.next();
                        		String startTagName3 = null;
                        		while(!parser.getName().contentEquals(startTagName2)){
                        			startTagName3 = parser.getName();
                        			if(startTagName3.contains("opponent")) {
                        				float positionX = Float.parseFloat(parser.getAttributeValue(0));
                        				float positionY = Float.parseFloat(parser.getAttributeValue(1));
                        				int opponentLevel = Integer.parseInt(parser.getAttributeValue(2));
                        				boolean isEpic = Boolean.parseBoolean(parser.getAttributeValue(3));
                        				levelLoader.addOpponent(positionX, positionY, opponentLevel, isEpic);
                        			}
                        		}
                			} else if(startTagName2.equalsIgnoreCase("npcs")) {
                				parser.next();
                        		String startTagName3 = null;
                        		while(!parser.getName().contentEquals(startTagName2)){
                        			startTagName3 = parser.getName();
                        			if(startTagName3.contains("opponent")) {
                        				float positionX = Float.parseFloat(parser.getAttributeValue(0));
                        				float positionY = Float.parseFloat(parser.getAttributeValue(1));
                        				int id = Integer.parseInt(parser.getAttributeValue(2));
                        				levelLoader.addNpc(positionX, positionY, id);
                        			}
                        		}
                			}
                		}
                		level++;
                	} else if(startTagName.equalsIgnoreCase("openquests")) {
                		parser.next();
                		String startTagName2 = null;
                		while(!parser.getName().contentEquals(startTagName)){
                			startTagName2 = parser.getName();
                			if(startTagName2.contains("quest")) {
                				String type = parser.getAttributeValue(0);
                    			int npcID = Integer.parseInt(parser.getAttributeValue(1));
                    			int progress = 0;
                    			if(parser.getAttributeValue(0).equals("TalkToQuest")) {
                    				progress = Integer.parseInt(parser.getAttributeValue(2));                    				
                            	}
                				addOpenQuest(npcID, type, progress);
                			}
                		}
                	} else if(startTagName.equalsIgnoreCase("closedquests")) {
                		parser.next();
                		String startTagName2 = null;
                		while(!parser.getName().contentEquals(startTagName)){
                			startTagName2 = parser.getName();
                			if(startTagName2.contains("quest")) {
                				String type = parser.getAttributeValue(0);
                    			int npcID = Integer.parseInt(parser.getAttributeValue(1));
                    			int progress = 0;
                    			if(parser.getAttributeValue(0).equals("TalkToQuest")) {
                    				progress = Integer.parseInt(parser.getAttributeValue(2));                    				
                            	}
                				addClosedQuest(npcID, type, progress);
                			}
                		}
                	} else if(startTagName.equalsIgnoreCase("player")) {
                		int inLevel = Integer.parseInt(parser.getAttributeName(0));
                		float positionX = Float.parseFloat(parser.getAttributeName(1));
                		float positionY = Float.parseFloat(parser.getAttributeName(2));
                		int playerLevel = Integer.parseInt(parser.getAttributeName(3));
                		float health = Float.parseFloat(parser.getAttributeName(4));
                		player = new PlayerObject(inLevel, positionX, positionY, playerLevel, health);
                		parser.next();
                		String startTagName2 = null;
                		while(!parser.getName().contentEquals(startTagName)){
                			startTagName2 = parser.getName();
                			if(startTagName2.equalsIgnoreCase("equipped")) {
                				String weapon = parser.getAttributeValue(1);
                				String armor0 = parser.getAttributeValue(1);
                				String armor1 = parser.getAttributeValue(2);
                				String armor2 = parser.getAttributeValue(3);
                				String armor3 = parser.getAttributeValue(4);
                				String armor4 = parser.getAttributeValue(5);
                				player.setWeapon(weapon);
                				player.setArmor(armor0, armor1, armor2, armor3, armor4);
                			} else if(startTagName2.equalsIgnoreCase("inventory")) {
                				for (int i = 0; i < parser.getAttributeCount(); i++) {
                					player.setInventory(parser.getAttributeValue(i));
                				}
                			}
                		}
                	}
                	break;
                }
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addOpenQuest(int npcID, String type, int progress){
		openQuestList.add(new QuestObjects(npcID, type, progress));
	}
	
	public void addClosedQuest(int npcID, String type, int progress){
		closedQuestList.add(new QuestObjects(npcID, type, progress));
	}

	public ArrayList<QuestObjects> getOpenQuestList(){
		return openQuestList;
	}

	public ArrayList<QuestObjects> getClosedQuestList(){
		return closedQuestList;
	}
	
	public PlayerObject getPlayerData() {
		return player;
	}

	public class QuestObjects {
		private int npcID;
		private String type;
		private int progress;
		
		public QuestObjects(int npcID, String type, int progress) {
			this.npcID = npcID;
			this.type = type;
			this.progress = progress;
		}
		
		public int getNpcID() {
			return npcID;
		}
		
		public String getType() {
			return type;
		}
		
		public int getProgress() {
			return progress;
		}
	}
	
	public class PlayerObject {
		private int level;
		private float positionX;
		private float positionY;
		private int playerLevel;
		private float health;
		private String weapon;
		private String[] armor;
		private ArrayList<String> inventory = new ArrayList<String>();
		
		public PlayerObject(int level, float positionX, float positionY, int playerLevel, float health) {
			this.level = level;
			this.positionX = positionX;
			this.positionY = positionY;
			this.playerLevel = playerLevel;
			this.health = health;
		}
		
		public void setWeapon(String weapon) {
			this.weapon = weapon;
		}

		public void setArmor(String armor0, String armor1, String armor2, String armor3, String armor4) {
			this.armor[0] = armor0;
			this.armor[1] = armor1;
			this.armor[2] = armor2;
			this.armor[3] = armor3;
			this.armor[4] = armor4;
		}
		
		public void setInventory(String item) {
			inventory.add(item);
		}
		
		public int getlevel() {
			return level;
		}
		
		public float getPositionX() {
			return positionX;
		}
		
		public float getPositionY() {
			return positionY;
		}
		
		public String[] getArmor() {
			return armor;
		}
		
		public String getWeapon() {
			return weapon;
		}

		public float getPlayerLevel() {
			return playerLevel;
		}

		public float getHealth() {
			return health;
		}
		
		public ArrayList<String> getInventory() {
			return inventory;
		}
	}
	
}
