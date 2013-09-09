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

public class WriteSaveFile {
	
	private Context context;
	private XmlSerializer serializer;
	private int questcount;
	private int levelcount;
	private OurScene[] sceneArray;
	private Player player;
	private Controller controller;
	private int slot;
	
	public WriteSaveFile(Context context) {
		this.context = context;
	}
	
	public void createFile(int slot, int levelcount, int questcount, OurScene[] scene, Player player, Controller controller){
		this.levelcount = levelcount;
		this.questcount = questcount;
		this.sceneArray = scene;
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

	private void writeQuestData() throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "openquests");
		ArrayList<Quest> openQuests = controller.getActiveQuests();
		for(int i = 0; i < openQuests.size(); i++) {
			serializer.startTag("", "quest" + i);
			if(openQuests.get(i) instanceof GetItemQuest) {
				GetItemQuest quest = (GetItemQuest)openQuests.get(i);
				serializer.attribute("", "type", quest.getType());
				serializer.attribute("", "npcID", "" + openQuests.get(i).getNpcID());
				serializer.attribute("", "alreadyFound", "" + quest.getAlreadyFound());
			} else if(openQuests.get(i) instanceof KillQuest) {
				KillQuest quest = (KillQuest)openQuests.get(i);
				serializer.attribute("", "type", quest.getType());
				serializer.attribute("", "npcID", "" + openQuests.get(i).getNpcID());
				serializer.attribute("", "alreadyKilled", "" + quest.getAlreadyKilled());				
			} else if(openQuests.get(i) instanceof TalkToQuest) {
					TalkToQuest quest = (TalkToQuest)openQuests.get(i);
					serializer.attribute("", "type", quest.getType());
					serializer.attribute("", "npcID", "" + openQuests.get(i).getNpcID());
			}
			serializer.endTag("", "quest" + i);
		}
		serializer.endTag("", "openquests");

		ArrayList<Quest> closedQuests = controller.getClosedQuests();
		serializer.startTag("", "closedQuests");
		for(int i = 0; i < closedQuests.size(); i++) {
			serializer.startTag("", "quest" + i);
			serializer.attribute("", "npcID", "" + closedQuests.get(i).getNpcID());
			serializer.endTag("", "quest" + i);
		}
		serializer.endTag("", "closedQuests");
	}

	private void writePlayerData() throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "player");
		serializer.attribute("", "level", "" + controller.getCurrentScene().getID());
		serializer.attribute("", "positionX", "" + player.getX());
		serializer.attribute("", "positionY", "" + player.getY());
		serializer.attribute("", "playerlevel", "" + player.getLevel());
		serializer.attribute("", "exp", "" + player.getEXP());
		serializer.attribute("", "health", "" + player.getHealth());
		
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
		
		serializer.endTag("", "player");
	}

	private void writeLevelData() throws IllegalArgumentException, IllegalStateException, IOException {
		for(int i = 1; i <= levelcount; i++) {
			OurScene scene = sceneArray[i - 1];
			
			serializer.startTag("", "level" + i);
			
			serializer.startTag("", "mapName");
			serializer.attribute("", "name", "slot" + slot + "level" + i + ".tmx");
			serializer.endTag("", "mapName");
			
//			serializer.startTag("", "map");
//			serializer.attribute("", "tileset", map.getTMXTileSets().get(0).getName());
//			serializer.startTag("", "tiles");
//			
//			for(int x = 0; x < map.getTileColumns(); x++) {
//				for(int y = 0; y < map.getTileRows(); y++) {
//					serializer.startTag("", "tile");
//					serializer.attribute("", "gid", "" + layer.getTMXTile(x, y).getGlobalTileID());
//					serializer.endTag("", "tile");
//				}
//			}
//			serializer.endTag("", "tiles");
//			serializer.endTag("", "map");

			serializer.startTag("", "opponents");
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
					serializer.endTag("", "opponent" + opponentcount);
					opponentcount++;
				}
			}
			serializer.endTag("", "opponents");
			
			int npcCount = 1;
			serializer.startTag("", "npcs");
			for(int k = 0; k < scene.getChildCount(); k++) {
				if(scene.getChildByIndex(k) instanceof NPC) {
					NPC npc = (NPC)scene.getChildByIndex(k);
					serializer.startTag("", "npc" + npcCount);
					serializer.attribute("", "positionX", "" + npc.getX());
					serializer.attribute("", "positionY", "" + npc.getY());
					serializer.attribute("", "ID", "" + npc.getID());
					serializer.endTag("", "npc" + npcCount);
					npcCount++;
				}
			}
			serializer.endTag("", "npcs");
			
/*			int lootbagCount = 1;
			serializer.startTag("", "lootbags");
			for(int k = 0; k < scene.getChildCount(); k++) {
				if(scene.getChildByIndex(k) instanceof LootBag) {
					LootBag lootbag = (LootBag)layer.getChildByIndex(k);
					serializer.startTag("", "lootbag" + lootbagCount);
					serializer.attribute("", "positionX", "" + lootbag.getX());
					serializer.attribute("", "positionY", "" + lootbag.getY());
					serializer.attribute("", "opponent", "" + lootbag.getLootRoot());
					
					serializer.endTag("", "lootbag" + lootbagCount);
				}
			}
			serializer.endTag("", "lootbags");
*/		
			serializer.endTag("", "level" + i);
		}
	}
}
