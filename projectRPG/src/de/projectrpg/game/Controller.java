package de.projectrpg.game;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import de.projectrpg.algorithm.Algorithm;
import de.projectrpg.database.Armor;
import de.projectrpg.database.HealItem;
import de.projectrpg.database.Item;
import de.projectrpg.database.OurDatabase;
import de.projectrpg.database.Weapon;
import de.projectrpg.quest.GetItemQuest;
import de.projectrpg.quest.KillQuest;
import de.projectrpg.quest.Quest;
import de.projectrpg.quest.QuestManager;
import de.projectrpg.scene.OurScene;
import de.projectrpg.scene.RandomMapGenerator;
import de.projectrpg.scene.SceneManager;
import de.projectrpg.scene.TMXMapLoader;
import de.projectrpg.sprites.NPC;
import de.projectrpg.sprites.Opponent;
import de.projectrpg.sprites.Player;
import de.projectrpg.util.FightHelper;
import de.projectrpg.util.InteractionDecider;

/**
 * Controller class responsible for communication 
 * between Activity and other classes
 * 
 * has to be a static instances in order to be accessed from all activities
 *
 */
public class Controller {
	
	/** the static instance of the controller */
	private static Controller controller;

	/** boolean if the player is moving */
	private boolean isMoving;
	
	/** the level the player is walking/fighting/etc in currently */
	private int level;

	/** an instance of the TMXMapLoader responsible for loading the maps for the scenes */
	private TMXMapLoader mapLoader;

	/** the scene manager containing all scenes */
	private SceneManager sceneManager;
	
	/** the database for items, texts, quests, etc */
	private OurDatabase db;

	/** the context of the calling activity */
	private Context context;

	/** the maximum level */
	private int lastLevel = 4;
	
	/** the side on which the last scene's exit tile was located */
	private int lastTransitionExitSide;

	/** the player */
	private Player player;

	/** the questmanager holding list containing all active and all closed quests */
	private QuestManager questManager;

	/** the bar showing the experience progress to the next level of the player */
	private Sprite expBar;
	
	
	/**
	 * initializes the static instance of the controller
	 * needed in order to have the same controller in each activity
	 * @param context the context of the calling activity
	 * @param expBar the expbar of the player
	 */
	public static void initInstance(Context context, Sprite expBar){
		controller = new Controller(context, expBar);
	}
	
	/**
	 * returns the static instance of the controller
	 * needed in order to have the same controller in each activity
	 * @return
	 */
	public static Controller getInstance(){
		return controller;
	}
	
	/**
	 * constructor
	 * @param context the context of the calling activity
	 * @param expBar the expbar of the player
	 */
	public Controller(Context context, Sprite expBar){
		mapLoader = new TMXMapLoader();
		sceneManager = new SceneManager();
		isMoving = false;
		level = 1;
		this.context = context;
		db = new OurDatabase(context);
		questManager = new QuestManager(this);
		this.expBar = expBar;
	}
	
	/**
	 * gets an Item from the database with the given name
	 * @param itemName the name of the item
	 * @return a new created item with values from the database 
	 */
	public Item getItemByName(String itemName) {
		db.open();
		Item item = (Item) db.getItem(itemName);
//		Log.d("DBTEST", item.getName());
		db.close();
		return item;
	}

	/**
	 * Issues the Algorithm class to calculate the shortest path between start and destination tile and returns it 
	 *  
	 * @param startTile - The tile the player is at the moment 
	 * @param destinationTile - The Tile the player wants to go to 
	 * @param tmxTiledMap - The whole map containing above Tiles 
	 * @returns the calculated shortest Path 
	 */
	public Path getPath(TMXTile startTile, TMXTile destinationTile,
			TMXTiledMap tmxTiledMap) {
		Algorithm algo = new Algorithm(startTile, destinationTile, tmxTiledMap, getCurrentScene());
		algo.generatePathMap();
		return algo.updatePath();
	}
	
	/**
	 * creates an Interaction decider to determine if the player should move to or interact
	 * 
	 * @param startTile - the tile the player is located
	 * @param destinationTile - the tile the user has clicked
	 * @param tmxTiledMap - the map containing those tiles
	 * @param scene - the scene containing the map
	 * @returns true if the player shall move to the tile, false if he shall interac
	 */
	public int doAction(TMXTile startTile, TMXTile destinationTile, TMXTiledMap tmxTiledMap, Scene scene) {
		InteractionDecider decider = new InteractionDecider();
		return decider.decide(startTile, destinationTile, tmxTiledMap, scene);
	}

	/**
	 * calculates and returns the correct type of animation
	 *  by comparing the coordinates of the current and the next waypoint 
	 * 
	 * @param path - the according path 
	 * @param waypointIndex - the index of the Waypoint 
	 * @return the index of the animation type
	 */
	public int getAnimationType(Path path, int waypointIndex) {
		float startX = path.getCoordinatesX()[waypointIndex];
		float startY = path.getCoordinatesY()[waypointIndex];
		float endX = path.getCoordinatesX()[waypointIndex+1];
		float endY = path.getCoordinatesY()[waypointIndex+1];
		float divX = Math.abs(startX - endX);
		float divY = Math.abs(startY - endY);
		
		if(divX > divY){
			/* move left */
			if(startX > endX){
				return 1;
			}
			/* move right */
			else {
				return 2;
			}
		}else {
			/* move up */
			if(startY > endY){
				return 3;
			}
			/* move down */
			else {
				return 4;
			}
		}
	}

	/**
	 * Called when the player starts to move 
	 * sets the moving boolean true
	 */
	public void animationStarted() {
		isMoving = true;
	}

	/**
	 * Called when the player finishes his movement 
	 * sets the moving boolean false
	 */
	public void animationFinished() {
		isMoving = false;
	}

	/**
	 * @returns if the player is moving at the moment
	 */
	public boolean isMoving() {
		return isMoving;
	}

	/**
	 * gets the string to show in the dialog from the database in future versions
	 * @param npc 
	 * @returns the String to show in the dialog
	 */
	public ArrayList<String> getInteractionText(NPC npc) {
		db.open();
		ArrayList<String> text =  db.getText(npc, questManager);
		db.close();
		return text;
	}
	
	/**
	 * @return a boolean if the merchant screen(InventarActivity) should be shown
	 */
	public boolean startMerchant(){
		return db.startMerchant();
	}

	/** 
	 * @returns the path to the tmx file according to the current level
	 */
	public InputStream getLevelPath(int index, int slot) {
		RandomMapGenerator gen = new RandomMapGenerator(context, lastLevel, slot);
		if(index < 1 || index > lastLevel) return null;
		else  {
			InputStream input = gen.createMap(index, lastTransitionExitSide);
			lastTransitionExitSide = gen.getLastSpawnSide();
			return input;
		}
	}

	/** increments the level counter */
	public void nextLevel() {
		level++;
	}
	
	/** decrements the level counter */
	public void previousLevel() {
		level--;
	}

	/** 
	 * @return the layer containing the TMXTiles the map of the level is made of
	 */
	public TMXLayer getTMXLayer() {
		return getCurrentScene().getMap().getTMXLayers().get(0);
	}

	/**
	 * adds a scene to the scenemanager
	 * @param scene the scene to be added
	 */
	public void addSceneToManager(OurScene scene) {
		sceneManager.addScene(scene);
	}

	/**
	 * load and returns the TMXMap
	 * @param assets the assetManager of the calling activity
	 * @param engine the engine
	 * @param vertexBufferObjectManager the vertexbufferobjectmanager of the calling activity
	 * @param index the index of the level
	 * @param input the inputstream of the tmx file containing the data of the map
	 * @return the loaded TMXMap
	 */
	public TMXTiledMap loadTMXMap(AssetManager assets, Engine engine,
			VertexBufferObjectManager vertexBufferObjectManager, int index, InputStream input, int slot) {
		Log.d("projekt", "input: " + input);
		if(input == null) {
			input = getLevelPath(index, slot);
		}
		return mapLoader.loadTMXMap(assets, engine, vertexBufferObjectManager, input);
	}

	/**
	 * @return a hashmap with float arrays of the spawnlocations of the levels
	 */
	public HashMap<String, float[]> getSpawn() {
		return mapLoader.getSpawn();
	}
	
	/**
	 * @return the current Scene
	 */
	public OurScene getCurrentScene(){
		Log.d("RPG", "level: "+level);
		return sceneManager.getScene(level);
	}

	/**
	 * called each time the player attacks an opponent
	 * uses the FightHelper class for calculating and returning a result
	 * @param player the player
	 * @param opponent the opponent the player fights
	 * @param redBar (the red part of)the healthbar of the player
	 * @param redBarEnemy (the red part of)the healthbar of the opponent
	 * @return the fight result as int
	 */
	public int fight(Player player, Opponent opponent, Sprite redBar, Sprite redBarEnemy) {
		return FightHelper.fight(player, opponent, redBar, redBarEnemy);
	}

	/**
	 * called when the player defeated an opponent
	 * gets an item from the database which is chosen randomly from all items with the given level
	 * @param loot the level
	 * @return an randomly chosen item of the given level
	 */
	public Item getLoot(int loot) {
		db.open();
		Item items = db.getLoot(loot);
		db.close();
		return items;
	}

	/**
	 * sets the current level to the given value
	 * @param level the future level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * @return the current level
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * @return the maximum level
	 */
	public int getLastLevel() {
		return lastLevel;
	}
	
	/**
	 * reference the player field to the given object
	 * @param player the player
	 */
	public void setPlayer(Player player){
		this.player = player;
	}
	
	/**
	 * add an armor object to the player's armor
	 * if the according armor slot is empty and the player's level is high enough
	 * otherwise nothing is added and false is returned
	 * @param armor the armor to add
	 * @return true if successfull, false if not
	 */
	public boolean addArmor(Armor armor){
		return player.addArmor(armor);
	}
	
	/**
	 * set the equipped weapon of the player to the given weapon object
	 * @param weapon the weapon to equip
	 */
	public void setWeapon(Weapon weapon){
		player.setWeapon(weapon);
	}
	
	/**
	 * @return the equipped weapon of the player
	 */
	public Weapon getEquippedWeapon(){
		return player.getEquippedWeapon();
	}
	
	/**
	 * @return the armor array of the player
	 */
	public Armor[] getArmor(){
		return player.getArmor();
	}
	
	/**
	 * @return return the arraylist containing the inventory items of the player
	 */
	public ArrayList<Item> getInventory(){
		return player.getInventory();
	}
	
	/**
	 * adds a given item to the players inventory
	 * @param item the item to add to the inventory
	 */
	public void addItemToInventory(Item item){
		player.addItemToInventory(item);
	}

	/**
	 * called after killing an enemy
	 * check if a killquest is fulfilled
	 * @param enemyName the name of the killed enemy
	 */
	public void checkQuests(String enemyName) {
		questManager.checkQuests(enemyName);
	}

	/**
	 * called when the player talks to a npc
	 * check if a talktoquest
	 * @param npc the npc the player talks to
	 */
	public void checkQuests(NPC npc) {
		questManager.checkQuests(npc);
	}

	/**
	 * called when the player picks up an item
	 * checks if an getitemquest is fulfilled
	 * @param item the item the player has picked up
	 */
	public void checkQuests(Item item) {
		questManager.checkQuests(item);
	}

	/**
	 * used when a game is loaded
	 * starts a quest from the given npc with the given progress
	 * @param npcID the npc who gives this quest
	 * @param progress the progress the player already has made with this quest
	 */
	public void startQuest(int npcID, int progress) {
		db.open();
		Quest quest = db.getQuest(npcID);
		db.close();
		questManager.startQuest(quest);
		if(quest instanceof GetItemQuest) {
			((GetItemQuest)quest).setAlreadyFound(progress);
			if(((GetItemQuest) quest).getItemCount() == progress) {
				quest.setFulfilled();
			}
		} else if(quest instanceof KillQuest) {
			((KillQuest)quest).setAlreadyKilled(progress);
			if(((KillQuest) quest).getKillCount() == progress) {
				quest.setFulfilled();
			}
		}
	}
	
	/**
	 * ends a quest from the given npc
	 * @param npcID the id of the npc who gave that quest
	 */
	public void endQuest(int npcID) {
		db.open();
		questManager.endQuest(db.getQuest(npcID));
		db.close();
	}
	
	/**
	 * removes a given item from the players inventory
	 * @param item the item to be removed
	 */
	public void removeItemFromInventory(Item item) {
		player.removeItemFromInventory(item);
	}
	
	/**
	 * removes the given piece of armor from the equip
	 * @param armor the armor to be unequipped
	 */
	public void removeEquippedArmor(Armor armor) {
		player.removeEquippedArmor(armor);
	}
	
	/**
	 * removes the given weapon from the equip
	 * @param weapon the weapon to be unequipped
	 */
	public void removeEquippedWeapon(Weapon weapon){
		player.removeEquippedWeapon(weapon);
	}
	
	/**
	 * called when the player consumes an healitem
	 * heals the player according to the items healvalue
	 * @param item the healitem the player has consumed
	 */
	public void heal(HealItem item){
		player.heal(item.getHeal());
	}

	/**
	 * called when the player gets experience
	 * adds the given amount to the layers experience and sets the expbar accordingly
	 * @param points the amount of exp points the player got
	 */
	public void addExp(int points) {
		player.addEXP(points);
		float percentage = player.getEXPPercentage()/100.0f;
		expBar.setWidth(558*percentage);
	}

	/**
	 * @return an ArrayList containing all active Quests
	 */
	public ArrayList<Quest> getActiveQuests() {
		return questManager.getActiveQuests();
	}

	/**
	 * @return an ArrayList containing all closed Quests
	 */
	public ArrayList<Quest> getClosedQuests() {
		return questManager.getClosedQuests();
	}
	
	/**
	 * checks if the quest at the given index is a killquest and returns the amount of enemies to be still killed
	 * @param index the index of the quest in the active quest list
	 * @return the amount of enemies to be still killed, 0 if no killquest
	 */
	public int getKillCount(int index) {
		if(questManager.getActiveQuests().get(index) instanceof KillQuest) {
		return ((KillQuest) questManager.getActiveQuests().get(index)).getKillCount();
		}
		return 0; 
	}
	
	/**
	 * gets a certain npc from the given scene
	 * @param scene the scene containing the npc
	 * @param index the index of the npc
	 * @return the npc with the given index in the given scene
	 */
	public NPC getNPCWithID(OurScene scene, int index) {
		ArrayList<NPC> npcList = scene.getNPCsInScene();
		if(npcList.size() > index) {
			return npcList.get(index);
		}
		return null;
	}

	/**
	 * gets the name of a npc from the database
	 * @param id the id of the npc
	 * @return the npc's name
	 */
	public String getNPCName(int id) {
		db.open();
		String name = db.getNPCName(id);
		db.close();
		return name;
	}

	/**
	 * set the player's inventory to the given ArrayList
	 * @param tempInventoryList the future inventory
	 */
	public void setInventory(ArrayList<Item> tempInventoryList) {
		player.setInventory(tempInventoryList);
	}

	/**
	 * removes the piece of armor with the given index
	 * @param i the index of the piece of armor to be removed
	 */
	public void removeEquippedArmor(int i) {
		player.removeEquippedArmor(i);
	}
	
	/**
	 * @param i the index of the scene to be returned
	 * @return the scene with index i from the scenemanager
	 */
	public OurScene getScene(int i) {
		return sceneManager.getScene(i);
	}
	
	/**
	 * @return the amount of gold the player currently has
	 */
	public int getGold(){
		return player.getGold();
	}
	
	/**
	 * adds the given value to the player's gold amount
	 * if gold should be subtracted call with negative value
	 * @param value the amount of gold to be added
	 */
	public void changeGold(int value){
		player.changeGold(value);
	}
	
	/**
	 * @return the level of the player
	 */
	public int getPlayerLevel(){
		return player.getLevel();
	}
}
