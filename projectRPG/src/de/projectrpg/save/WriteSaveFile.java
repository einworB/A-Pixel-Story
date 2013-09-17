package de.projectrpg.save;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;
import de.projectrpg.database.Item;
import de.projectrpg.game.Controller;
import de.projectrpg.quest.GetItemQuest;
import de.projectrpg.quest.KillQuest;
import de.projectrpg.quest.Quest;
import de.projectrpg.quest.TalkToQuest;
import de.projectrpg.scene.OurScene;
import de.projectrpg.sprites.NPC;
import de.projectrpg.sprites.Opponent;
import de.projectrpg.sprites.Player;

/**
 * Write the xml file to save the actual game
 */
public class WriteSaveFile {
	
	/** the context */
	private Context context;
	/** the xml serializer */
	private XmlSerializer serializer;
	/** the actual quest that should be shown in the questscene*/
	private int questcount;
	/** the count of the level in the game*/
	private int levelcount;
	/** an array with all scenes of the game*/
	private OurScene[] sceneArray;
	/** the player*/
	private Player player;
	/** the controller*/
	private Controller controller;
	/** the save slot where the game should be saved */
	private int slot;
	
	/**
	 * The constructor
	 * @param context
	 */
	public WriteSaveFile(Context context) {
		this.context = context;
	}
	
	/** 
	 * create the save file if it doesn't exist. Then write the data into it.
	 * @param slot the save slot where the game should be saved
	 * @param levelcount the count of the level in the game
	 * @param questcount the actual quest that should be shown in the questscene
	 * @param scene an array with all scenes of the game
	 * @param player
	 * @param controller
	 */
	public void createFile(int slot, int levelcount, int questcount, OurScene[] scene, Player player, Controller controller){
		this.levelcount = levelcount;
		this.questcount = questcount;
		this.sceneArray = scene.clone();
		this.player = player;
		this.controller = controller;
		this.slot = slot;
		
		
		String filename = "slot" + slot +".xml";
		try {
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			OutputStreamWriter writer = new OutputStreamWriter(fos);
			writer.write(writeXml());
			writer.flush();
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setup the xml serializer and write the data..
	 * @return the file content as String
	 */
	private String writeXml() {
	    serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        
	        setupDoc();
           
	        serializer.endDocument();
        } catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return writer.toString();
	   
	}

	/**
	 * Set all variables that are used to reload the game.
	 * Write the level, quest and player data
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void setupDoc() throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startDocument("UTF-8", true);
		
		serializer.startTag("", "variables");
		serializer.attribute("", "questcount", "" + questcount);
		serializer.attribute("", "levelcount", "" + levelcount);
		serializer.endTag("", "variables");
			
		writeLevelData();
		writeQuestData();
		writePlayerData();
	}

	/**
	 * Save all data that are needed to load the quests.
	 * It is a list of open and closed quests. 
	 * For the open quests the type of quest, the npcID and the quest progress.
	 * For the closed only the npcID
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void writeQuestData() throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "openquests");
		serializer.endTag("", "openquests");

		ArrayList<Quest> openQuests = controller.getActiveQuests();
		for(int i = 0; i < openQuests.size(); i++) {
			
			serializer.startTag("", "quest" + i);
			
			if(openQuests.get(i) instanceof GetItemQuest) {
				GetItemQuest quest = (GetItemQuest)openQuests.get(i);
				serializer.attribute("", "type", quest.getType());
				serializer.attribute("", "npcID", "" + openQuests.get(i).getNpcID());
				serializer.attribute("", "progress", "" + quest.getAlreadyFound());
			} else if(openQuests.get(i) instanceof KillQuest) {
				KillQuest quest = (KillQuest)openQuests.get(i);
				serializer.attribute("", "type", quest.getType());
				serializer.attribute("", "npcID", "" + openQuests.get(i).getNpcID());
				serializer.attribute("", "progress", "" + quest.getAlreadyKilled());				
			} else if(openQuests.get(i) instanceof TalkToQuest) {
				TalkToQuest quest = (TalkToQuest)openQuests.get(i);
				serializer.attribute("", "type", quest.getType());
				serializer.attribute("", "npcID", "" + openQuests.get(i).getNpcID());
			}
			
			serializer.endTag("", "quest" + i);
		}
		serializer.startTag("", "openquests");
		serializer.endTag("", "openquests");

		ArrayList<Quest> closedQuests = controller.getClosedQuests();
		serializer.startTag("", "closedQuests");
		serializer.endTag("", "closedQuests");
		for(int i = 0; i < closedQuests.size(); i++) {
			serializer.startTag("", "quest" + i);
			serializer.attribute("", "npcID", "" + closedQuests.get(i).getNpcID());
			serializer.endTag("", "quest" + i);
		}
		serializer.startTag("", "closedQuests");
		serializer.endTag("", "closedQuests");
	}

	/**
	 * Write all Player data. The level in which the player stands. 
	 * The position, viewing direction and the level of the player. 
	 * The exp, health and gold.
	 * Save which armor and weapon the player is wearing and 
	 * the content of the inventory.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void writePlayerData() throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "player");
		serializer.attribute("", "level", "" + controller.getCurrentScene().getID());
		serializer.attribute("", "positionX", "" + player.getX());
		serializer.attribute("", "positionY", "" + player.getY());
		serializer.attribute("", "playerlevel", "" + player.getLevel());
		serializer.attribute("", "exp", "" + player.getEXP());
		serializer.attribute("", "health", "" + player.getHealth());
		serializer.attribute("", "direction", "" + player.getCurrentTileIndex());
		serializer.attribute("", "gold", "" + player.getGold());

		serializer.endTag("", "player");

		serializer.startTag("", "equipped");
		String str;
		if(player.getEquippedWeapon() != null) {
			str = player.getEquippedWeapon().getName();
		} else {
			str = "";
		}
		serializer.attribute("", "weapon", str);
		if(player.getArmor()[0] != null) {
			str = player.getArmor()[0].getName();
		} else {
			str = "";
		}
		serializer.attribute("", "armor0", str);
		if(player.getArmor()[1] != null) {
			str = player.getArmor()[1].getName();
		} else {
			str = "";
		}
		serializer.attribute("", "armor1", str);
		if(player.getArmor()[2] != null) {
			str = player.getArmor()[2].getName();
		} else {
			str = "";
		}
		serializer.attribute("", "armor2", str);
		if(player.getArmor()[3] != null) {
			str = player.getArmor()[3].getName();
		} else {
			str = "";
		}
		serializer.attribute("", "armor3", str);
		if(player.getArmor()[4] != null) {
			str = player.getArmor()[4].getName();
		} else {
			str = "";
		}
		serializer.attribute("", "armor4", str);
		serializer.endTag("", "equipped");
		
		serializer.startTag("", "inventory");
		ArrayList<Item> inventory = player.getInventory();
		
		for(int i = 0; i < inventory.size(); i++) {
			serializer.attribute("", "inventory" + i, "" + inventory.get(i).getName());
		}
		serializer.endTag("", "inventory");
		
		serializer.startTag("", "player");
		serializer.endTag("", "player");
	}

	/**
	 * Write all data that are used especially for each level. 
	 * Which opponents and npcs are at the level and how the map name is.
	 * For an opponent the position, health, viewing direction, level and the boolean if the opponent is epic is needed. 
	 * For a npc the position, viewing direction and id.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void writeLevelData() throws IllegalArgumentException, IllegalStateException, IOException {
		for(int i = 1; i <= levelcount; i++) {
			OurScene scene = sceneArray[i - 1];

			serializer.startTag("", "level" + i);
			serializer.endTag("", "level" + i);
			
			serializer.startTag("", "mapname");
			serializer.attribute("", "name", "slot" + slot + "level" + i + ".tmx");
			serializer.endTag("", "mapname");

			serializer.startTag("", "opponents");
			serializer.endTag("", "opponents");
			int opponentcount = 1;
			for(int k = 0; k < scene.getChildCount(); k++) {
				if(scene.getChildByIndex(k) instanceof Opponent) {
					Opponent opponent = (Opponent)scene.getChildByIndex(k);
					serializer.startTag("", "opponent" + opponentcount);
					serializer.attribute("", "positionX", "" + opponent.getX());
					serializer.attribute("", "positionY", "" + opponent.getY());
					serializer.attribute("", "level", "" + i);
					
					if(opponent.isEpic()) {
						serializer.attribute("", "isEpic", "true");
					} else {
						serializer.attribute("", "isEpic", "false");
					}
					serializer.attribute("", "direction", "" + opponent.getCurrentTileIndex());
					serializer.attribute("", "health", "" + opponent.getHealth());
					serializer.endTag("", "opponent" + opponentcount);
					opponentcount++;
				}
			}
			serializer.startTag("", "opponents");
			serializer.endTag("", "opponents");
			
			int npcCount = 1;
			serializer.startTag("", "npcs");
			serializer.endTag("", "npcs");
			for(int k = 0; k < scene.getChildCount(); k++) {
				if(scene.getChildByIndex(k) instanceof NPC) {
					NPC npc = (NPC)scene.getChildByIndex(k);
					serializer.startTag("", "npc" + npcCount);
					serializer.attribute("", "positionX", "" + npc.getX());
					serializer.attribute("", "positionY", "" + npc.getY());
					serializer.attribute("", "ID", "" + npc.getID());
					serializer.attribute("", "direction", "" + npc.getCurrentTileIndex());
					serializer.endTag("", "npc" + npcCount);
					npcCount++;
				}
			}
			serializer.startTag("", "npcs");
			serializer.endTag("", "npcs");

			serializer.startTag("", "level" + i);
			serializer.endTag("", "level" + i);
		}
	}
}
