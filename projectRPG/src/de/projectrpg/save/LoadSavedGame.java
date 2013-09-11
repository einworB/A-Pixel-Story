package de.projectrpg.save;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

public class LoadSavedGame {
	
	private Context context;
	private int questCount;
	private int lastLevel;
	private ArrayList<QuestObjects> openQuestList;
	private ArrayList<QuestObjects> closedQuestList;
	private PlayerObject player;
	private LevelLoader[] levelLoaders;
	
	
	public LoadSavedGame(Context context) {
		this.context = context;
		openQuestList = new ArrayList<QuestObjects>();
		closedQuestList = new ArrayList<QuestObjects>();
	}
	
	public void loadGame(int slot) {
		try {
			InputStream inputStream = context.openFileInput("slot" + slot + ".xml");
			Log.d("projekt", "inputStream: " + inputStream);

			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			Log.d("projekt", "parser");

			parser.setInput(inputStream, null);
			int eventType = parser.getEventType();
			boolean done = false;
			int level = 1;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
            	String startTagName = null;
            	
                switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
        			Log.d("projekt", "start document");
                	break;
                case XmlPullParser.START_TAG:

                	startTagName = parser.getName();
        			Log.d("projekt", "startTag: " + startTagName);
                	if(startTagName.equalsIgnoreCase("variables")) {
            			Log.d("projekt", "variables");
            			questCount = Integer.parseInt(parser.getAttributeValue(0));
                		lastLevel = Integer.parseInt(parser.getAttributeValue(1));
                		
                		levelLoaders = new LevelLoader[lastLevel];
                	} else if(startTagName.contains("level")) {
            			
                		LevelLoader levelLoader = new LevelLoader(level);
                		parser.next();
                		parser.next();
                		String startTagName2 = null;
                		Log.d("projekt", "level" + level);
                		Log.d("projekt", parser.getName());
                		
                		while(!parser.getName().contentEquals(startTagName)){
                			
                			startTagName2 = parser.getName();
                			if(startTagName2.equalsIgnoreCase("mapname")) {
                				
                        		levelLoader.setMapName(parser.getAttributeValue(0));
                			} else if(startTagName2.equalsIgnoreCase("opponents")) {
                    			Log.d("projekt", "opponents");
                				parser.next();
                				parser.next();
                        		String startTagName3 = null;
                        		while(!parser.getName().contentEquals(startTagName2)){
                        			startTagName3 = parser.getName();
                        			if(startTagName3.contains("opponent")) {

                        				float positionX = Float.parseFloat(parser.getAttributeValue(null, "positionX"));
                        				float positionY = Float.parseFloat(parser.getAttributeValue(null, "positionY"));
                        				int opponentLevel = Integer.parseInt(parser.getAttributeValue(null, "level"));
                        				boolean isEpic = Boolean.parseBoolean(parser.getAttributeValue(null, "isEpic"));
                        				levelLoader.addOpponent(positionX, positionY, opponentLevel, isEpic);
                        			}
                        			parser.next();
                        			parser.next();
                        			
                        		}
                			} else if(startTagName2.equalsIgnoreCase("npcs")) {
                    			Log.d("projekt", "npcs");

                    			parser.next();
                    			parser.next();
                        		String startTagName3 = null;
                        		while(!parser.getName().contentEquals(startTagName2)){
                        			startTagName3 = parser.getName();
                        			if(startTagName3.contains("npc")) {
                        				
                        				float positionX = Float.parseFloat(parser.getAttributeValue(null, "positionX"));
                        				float positionY = Float.parseFloat(parser.getAttributeValue(null, "positionY"));
                        				int id = Integer.parseInt(parser.getAttributeValue(null, "ID"));
                        				levelLoader.addNpc(positionX, positionY, id, level);
                        			}
                        			parser.next();
                        			parser.next();
                        			
                        		}
                			}
                			parser.next();
                			parser.next();
                		}
                		levelLoaders[level - 1] = levelLoader;
                		level++;
                	} else if(startTagName.equalsIgnoreCase("openquests")) {
            			Log.d("projekt", "openquests");

                		parser.next();
                		parser.next();
                		String startTagName2 = null;
                		while(!parser.getName().contentEquals(startTagName)){
                			startTagName2 = parser.getName();
                			if(startTagName2.contains("quest")) {
                				String type = parser.getAttributeValue(null, "type");
                    			int npcID = Integer.parseInt(parser.getAttributeValue(null, "npcID"));
                    			int progress = 0;
                    			if(!parser.getAttributeValue(null, "type").equals("TalkToQuest")) {
                    				progress = Integer.parseInt(parser.getAttributeValue(null, "progress"));                    				
                            	}
                				addOpenQuest(npcID, type, progress);
                			}
                			parser.next();
                			parser.next();
                		}
                	} else if(startTagName.equalsIgnoreCase("closedquests")) {
            			Log.d("projekt", "closedquests");

            			parser.next();
                		parser.next();
                		String startTagName2 = null;
                		while(!parser.getName().contentEquals(startTagName)){
                			startTagName2 = parser.getName();
                			if(startTagName2.contains("quest")) {
                    			int npcID = Integer.parseInt(parser.getAttributeValue(null, "npcID"));
                				addClosedQuest(npcID);
                			}
                			parser.next();
                			parser.next();
                		}
                	} else if(startTagName.equalsIgnoreCase("player")) {
            			Log.d("projekt", "player");

                		int inLevel = Integer.parseInt(parser.getAttributeValue(null, "level"));
                		float positionX = Float.parseFloat(parser.getAttributeValue(null, "positionX"));
                		float positionY = Float.parseFloat(parser.getAttributeValue(null, "positionY"));
                		int playerLevel = Integer.parseInt(parser.getAttributeValue(null, "playerlevel"));
                		float health = Float.parseFloat(parser.getAttributeValue(null, "health"));
                		int exp = Integer.parseInt(parser.getAttributeValue(null, "exp"));
                		player = new PlayerObject(inLevel, positionX, positionY, playerLevel, health, exp);
                		parser.next();
                		parser.next();
                		String startTagName2 = null;
                		while(!parser.getName().contentEquals(startTagName)){
                			startTagName2 = parser.getName();
                			if(startTagName2.equalsIgnoreCase("equipped")) {
                				String weapon = parser.getAttributeValue(null, "weapon");
                				String armor0 = parser.getAttributeValue(null, "armor0");
                				String armor1 = parser.getAttributeValue(null, "armor1");
                				String armor2 = parser.getAttributeValue(null, "armor2");
                				String armor3 = parser.getAttributeValue(null, "armor3");
                				String armor4 = parser.getAttributeValue(null, "armor4");
                				player.setWeapon(weapon);
                				player.setArmor(armor0, armor1, armor2, armor3, armor4);
                			} else if(startTagName2.equalsIgnoreCase("inventory")) {
                				for (int i = 0; i < parser.getAttributeCount(); i++) {
                					player.setInventory(parser.getAttributeValue(null, "inventory" + i));
                				}
                			}
                			parser.next();
                			parser.next();
                		}
                	}
                	break;
                case XmlPullParser.END_DOCUMENT:
                	break;
                }
                eventType = parser.next();
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getLastLevel() {
		return lastLevel;
	}
	
	public int getQuestCount() {
		return questCount;
	}
	public LevelLoader getLevelLoader(int level) {
		return levelLoaders[level - 1];
	}
	
	public void addOpenQuest(int npcID, String type, int progress){
		openQuestList.add(new QuestObjects(npcID));
	}
	
	public void addClosedQuest(int npcID){
		closedQuestList.add(new QuestObjects(npcID));
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
		
		public QuestObjects(int npcID) {
			this.npcID = npcID;
		}
		
		public int getNpcID() {
			return npcID;
		}
		
	}
	
	public class PlayerObject {
		private int level;
		private float positionX;
		private float positionY;
		private int playerLevel;
		private int exp;
		private float health;
		private String weapon;
		private String[] armor = new String[5];
		private ArrayList<String> inventory = new ArrayList<String>();
		
		public PlayerObject(int level, float positionX, float positionY, int playerLevel, float health, int exp) {
			this.level = level;
			this.positionX = positionX;
			this.positionY = positionY;
			this.playerLevel = playerLevel;
			this.health = health;
			this.exp = exp;
		}
		
		public void setWeapon(String weapon) {
			this.weapon = weapon;
		}

		public void setArmor(String armor0, String armor1, String armor2, String armor3, String armor4) {
			if(armor0 != null) {
				this.armor[0] = armor0;
			}

			if(armor1 != null) {
				this.armor[1] = armor1;
			}
			
			if(armor2 != null) {
				this.armor[2] = armor2;
			}
			
			if(armor3 != null) {
				this.armor[3] = armor3;
			}
			
			if(armor4 != null) {
				this.armor[4] = armor4;
			}
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

		public int getPlayerLevel() {
			return playerLevel;
		}

		public float getHealth() {
			return health;
		}
		
		public int getExp() {
			return exp;
		}
		public ArrayList<String> getInventory() {
			return inventory;
		}
	}
	
}
