package de.apixelstory.save;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

/**
 * Load the saved game from xml file.
 */
public class LoadSavedGame {
	/** the context*/
	private Context context;
	/** the quest count to show the right quest in questscene*/
	private int questCount;
	/** the index of the last level*/
	private int lastLevel;
	/** the list with open quests*/
	private ArrayList<QuestObjects> openQuestList;
	/** the list with closed quests*/
	private ArrayList<QuestObjects> closedQuestList;
	/** the player*/
	private PlayerObject player;
	/** the array with all level data*/
	private LevelLoader[] levelLoaders;
	/** the xml pull parser to read the xml file*/
	private XmlPullParser parser;
	
	/**
	 * the constructor
	 * @param context
	 */
	public LoadSavedGame(Context context) {
		this.context = context;
		openQuestList = new ArrayList<QuestObjects>();
		closedQuestList = new ArrayList<QuestObjects>();
		levelLoaders = new LevelLoader[1];
	}
	
	/**
	 * Load the saved game.
	 * Switch the tags of the xml file. If it is a starttag do something.
	 * If the starttag is "variables" read it the attributes from it. 
	 * If it contains level, load all data that have to do with the level. 
	 * These are the opponents and npcs and the mapname. 
	 * If it is openquests or closedquests load all data that have to do with quests. 
	 * And if it is player load all data that are important to set all values for the player.
	 * @param slot the slot in which the game was saved
	 */
	public void loadGame(int slot) {
		try {
			InputStream inputStream = context.openFileInput("slot" + slot + ".xml");
//			Log.d("projekt", "inputStream: " + inputStream);

			parser = XmlPullParserFactory.newInstance().newPullParser();
//			Log.d("projekt", "parser");

			parser.setInput(inputStream, null);
			int eventType = parser.getEventType();
			boolean done = false;
			int level = 1;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
            	String startTagName = null;
            	
                switch (eventType) {
                case XmlPullParser.START_TAG:

                	startTagName = parser.getName();
//        			Log.d("projekt", "startTag: " + startTagName);
                	if(startTagName.equalsIgnoreCase("variables")) {
//            			Log.d("projekt", "variables");
            			
            			questCount = Integer.parseInt(parser.getAttributeValue(0));
                		lastLevel = Integer.parseInt(parser.getAttributeValue(1));
                		
                		levelLoaders = new LevelLoader[lastLevel];
                	} else if(startTagName.contains("level")) {
            			
                		levelData(level, startTagName);
            			level++;
            			
                	} else if(startTagName.equalsIgnoreCase("openquests")) {
            			
                		loadOpenQuests(startTagName);
                		
                	} else if(startTagName.equalsIgnoreCase("closedquests")) {
            			
                		loadClosedQuests(startTagName);
                		
                	} else if(startTagName.equalsIgnoreCase("player")) {
                		
                		loadPlayer(startTagName);
                		
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

	/**
	 * Load all open quests from xml file
	 * @param startTagName the name of the actual start tag
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void loadOpenQuests(String startTagName) throws XmlPullParserException, IOException {
//		Log.d("projekt", "openquests");

		parser.next();
		parser.next();
		String startTagName2 = null;
		
		// if the same tag as the actual starttag appears it is the endtag of this block
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
	}

	/**
	 * Load all closed quests from xml file
	 * @param startTagName the name of the actual start tag
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void loadClosedQuests(String startTagName) throws XmlPullParserException, IOException {
//		Log.d("projekt", "closedquests");

		parser.next();
		parser.next();
		String startTagName2 = null;
		
		// if the same tag as the actual starttag appears it is the endtag of this block
		while(!parser.getName().contentEquals(startTagName)){
			startTagName2 = parser.getName();
			if(startTagName2.contains("quest")) {
    			
				int npcID = Integer.parseInt(parser.getAttributeValue(null, "npcID"));
				
				addClosedQuest(npcID);
			}
			parser.next();
			parser.next();
		}
	}

	/**
	 * Load all player data from xml file. 
	 * Get the inventory and the equipped armor and weapon.
	 * @param startTagName the name of the actual start tag
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void loadPlayer(String startTagName) throws XmlPullParserException, IOException {
//		Log.d("projekt", "player");

		int inLevel = Integer.parseInt(parser.getAttributeValue(null, "level"));
		float positionX = Float.parseFloat(parser.getAttributeValue(null, "positionX"));
		float positionY = Float.parseFloat(parser.getAttributeValue(null, "positionY"));
		int playerLevel = Integer.parseInt(parser.getAttributeValue(null, "playerlevel"));
		float health = Float.parseFloat(parser.getAttributeValue(null, "health"));
		int exp = Integer.parseInt(parser.getAttributeValue(null, "exp"));
		int direction = Integer.parseInt(parser.getAttributeValue(null, "direction"));
		int gold = Integer.parseInt(parser.getAttributeValue(null, "gold"));
		
		player = new PlayerObject(inLevel, positionX, positionY, playerLevel, health, exp, direction, gold);
		parser.next();
		parser.next();
		String startTagName2 = null;
		
		// if the same tag as the actual starttag appears it is the endtag of this block
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

	/**
	 * Load all level data. Load all opponents and npcs that are in a level
	 * @param level the level to load
	 * @param startTagName the name of the actual start tag 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void levelData(int level, String startTagName) throws XmlPullParserException, IOException {
		LevelLoader levelLoader = new LevelLoader(level);
		parser.next();
		parser.next();
		String startTagName2 = null;
//		Log.d("projekt", "level" + level);
//		Log.d("projekt", parser.getName());
		
		// if the same tag as the actual starttag appears it is the endtag of this block
		while(!parser.getName().contentEquals(startTagName)){
			
			startTagName2 = parser.getName();
			if(startTagName2.equalsIgnoreCase("mapname")) {
				
        		levelLoader.setMapName(parser.getAttributeValue(0));
			
			} else if(startTagName2.equalsIgnoreCase("opponents")) {
//    			Log.d("projekt", "opponents");
    			
				parser.next();
				parser.next();
        		String startTagName3 = null;
        		// if the same tag as the actual starttag appears it is the endtag of this block
        		while(!parser.getName().contentEquals(startTagName2)){
        			startTagName3 = parser.getName();
        			if(startTagName3.contains("opponent")) {

        				float positionX = Float.parseFloat(parser.getAttributeValue(null, "positionX"));
        				float positionY = Float.parseFloat(parser.getAttributeValue(null, "positionY"));
        				int opponentLevel = Integer.parseInt(parser.getAttributeValue(null, "level"));
        				boolean isEpic = Boolean.parseBoolean(parser.getAttributeValue(null, "isEpic"));
        				int direction = Integer.parseInt(parser.getAttributeValue(null, "direction"));
        				float health = Float.parseFloat(parser.getAttributeValue(null, "health"));
        				
        				levelLoader.addOpponent(positionX, positionY, opponentLevel, isEpic, direction, health);
        			}
        			parser.next();
        			parser.next();
        			
        		}
			} else if(startTagName2.equalsIgnoreCase("npcs")) {
//    			Log.d("projekt", "npcs");

    			parser.next();
    			parser.next();
        		String startTagName3 = null;
        		// if the same tag as the actual starttag appears it is the endtag of this block
        		while(!parser.getName().contentEquals(startTagName2)){
        			startTagName3 = parser.getName();
        			if(startTagName3.contains("npc")) {
        				
        				float positionX = Float.parseFloat(parser.getAttributeValue(null, "positionX"));
        				float positionY = Float.parseFloat(parser.getAttributeValue(null, "positionY"));
        				int id = Integer.parseInt(parser.getAttributeValue(null, "ID"));
        				int direction = Integer.parseInt(parser.getAttributeValue(null, "direction"));
        				
        				levelLoader.addNpc(positionX, positionY, id, level, direction);
        			}
        			parser.next();
        			parser.next();
        			
        		}
			}
			parser.next();
			parser.next();
		}
		levelLoaders[level - 1] = levelLoader;
	}

	/**
	 * get the index of the last level of the game
	 * @return the index of the last level of the game
	 */
	public int getLastLevel() {
		return lastLevel;
	}
	
	/**
	 * get the quest count to decide which quest should be shown in the questscene
	 * @return the quest count
	 */
	public int getQuestCount() {
		return questCount;
	}
	
	/**
	 * get the levelLoader at a specific index
	 * @param level the index of the level
	 * @return the level loader at the specific index
	 */
	public LevelLoader getLevelLoader(int level) {
		if(levelLoaders.length >= level - 1) {
			return levelLoaders[level - 1];			
		}
		return null;
	}
	
	/**
	 * Add an open quest to the open quest list
	 * @param npcID the npc id
	 * @param type the type of quest
	 * @param progress the progress the user has made
	 */
	public void addOpenQuest(int npcID, String type, int progress){
		openQuestList.add(new QuestObjects(npcID, type, progress));
	}

	/**
	 * Add an closed quest to the closed quest list
	 * @param npcID the npc id
	 */
	public void addClosedQuest(int npcID){
		closedQuestList.add(new QuestObjects(npcID, null, 0));
	}

	/**
	 * get the open quest list
	 * @return the open quest list
	 */
	public ArrayList<QuestObjects> getOpenQuestList(){
		return openQuestList;
	}

	/**
	 * get the closed quest list
	 * @return the closed quest list
	 */
	public ArrayList<QuestObjects> getClosedQuestList(){
		return closedQuestList;
	}
	
	/**
	 * get the player data
	 * @return the player data
	 */
	public PlayerObject getPlayerData() {
		return player;
	}

	/**
	 * Inner class.  A quest Object to load a quest
	 */
	public static class QuestObjects {
		/** the npc id*/
		private int npcID;
		/** the progress of the quest*/
		private int progress;
		
		/**
		 * the constructor
		 * @param npcID the npc id
		 * @param type the type of quest
		 * @param progress the progress that where made
		 */
		public QuestObjects(int npcID, String type, int progress) {
			this.npcID = npcID;
			this.progress = progress;
		}
		
		/**
		 * get the npc id
		 * @return the npc id
		 */
		public int getNpcID() {
			return npcID;
		}
		
		/**
		 * get the progress of the quest
		 * @return the progress of the quest
		 */
		public int getProgress() {
			return progress;
		}
	}
	
	/**
	 * Inner class. A player object with all data to relaod the player.
	 */
	public static class PlayerObject {
		/** the level in which the player stands*/
		private int level;
		/** the x position of the player*/
		private float positionX;
		/** the y position of the player*/
		private float positionY;
		/** the level of the player*/ 
		private int playerLevel;
		/** the experience of the player*/
		private int exp;
		/** the health of the player*/
		private float health;
		/** the weapon the player is wearing*/
		private String weapon;
		/** the armor the player is wearing*/
		private String[] armor = new String[5];
		/** the inventory of the player*/
		private ArrayList<String> inventory = new ArrayList<String>();
		/** the viewing direction of the player*/
		private int direction;
		/** the gold of the player*/
		private int gold;
		
		/**
		 * the constructor
		 * @param level the level in which the player stands
		 * @param positionX the x position of the player
		 * @param positionY the y position of the player
		 * @param playerLevel the level of the player
		 * @param health the health of the player
		 * @param exp the experience of the player
		 * @param direction the viewing direction of the player
		 * @param gold the gold of the player
		 */
		public PlayerObject(int level, float positionX, float positionY, int playerLevel, float health, int exp, int direction, int gold) {
			this.level = level;
			this.positionX = positionX;
			this.positionY = positionY;
			this.playerLevel = playerLevel;
			this.health = health;
			this.exp = exp;
			this.direction = direction;
			this.gold = gold;
		}
		
		/**
		 * set the weapon
		 * @param weapon
		 */
		public void setWeapon(String weapon) {
			this.weapon = weapon;
		}

		/**
		 * set the armor. If it is null do nothing
		 * @param armor0 head
		 * @param armor1 body
		 * @param armor2 hands
		 * @param armor3 legs
		 * @param armor4 foot
		 */
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
		
		/**
		 * add an item to the inventory
		 * @param item the item to add
		 */
		public void setInventory(String item) {
			inventory.add(item);
		}
		
		/**
		 * get the level in which the player stands
		 * @return the level
		 */
		public int getSavedLevel() {
			return level;
		}
		
		/**
		 * get the x position of the player
		 * @return the x position of the player
		 */
		public float getPositionX() {
			return positionX;
		}
		
		/**
		 * get the y position of the player
		 * @return the y position of the player
		 */
		public float getPositionY() {
			return positionY;
		}
		
		/**
		 * get the armor
		 * @return the armor
		 */
		public String[] getArmor() {
			return armor.clone();
		}
		
		/**
		 * get the weapon
		 * @return the weapon
		 */
		public String getWeapon() {
			return weapon;
		}

		/**
		 * get the player level
		 * @return the player level
		 */
		public int getPlayerLevel() {
			return playerLevel;
		}

		/**
		 * get the health of the player
		 * @return the health of the player
		 */
		public float getHealth() {
			return health;
		}
		
		/**
		 * get the experience of the player
		 * @return the experience of the player
		 */
		public int getSavedExp() {
			return exp;
		}
		
		/**
		 * get the inventory of the player
		 * @return the inventory of the player
		 */
		public ArrayList<String> getInventory() {
			return inventory;
		}
		
		/**
		 * get the viewing direction of the player
		 * @return the viewing direction of the player
		 */
		public int getDirection() {
			return direction;
		}

		/**
		 * get the gold of the player
		 * @return the gold of the player
		 */
		public int getGold() {
			return gold;
		}
	}
	
}
