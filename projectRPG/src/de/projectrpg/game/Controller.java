package de.projectrpg.game;

import java.io.InputStream;
import java.io.Serializable;
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
 * between Activity and Algorithm 
 * and other technical information like animation type 
 * or if the player is moving 
 * 
 * @author Philip
 *
 */
public class Controller {
	
	private static Controller controller;

	/** boolean if the player is moving */
	private boolean isMoving;
	
	private int level;

	private TMXMapLoader mapLoader;

	private SceneManager sceneManager;
	
	private OurDatabase db;

	private Context context;

	private int lastLevel = 4;
	private int lastTransitionExitSide;

	private Player player;

	private QuestManager questManager;

	private Sprite expBar;
	
	/** constructor 
	 * @param expBar */
	
	public static void initInstance(Context context, Sprite expBar){
		controller = new Controller(context, expBar);
	}
	
	public static Controller getInstance(){
		return controller;
	}
	
	public Controller(Context context, Sprite expBar){
		mapLoader = new TMXMapLoader(this);
		sceneManager = new SceneManager();
		isMoving = false;
		level = 1;
		this.context = context;
		db = new OurDatabase(context);
		questManager = new QuestManager(this);
		this.expBar = expBar;
	}

	public void testDatabase() {
		db.open();
		Armor weapon = (Armor) db.getItem("Dreckiger Regenmantel");
		Log.d("DBTEST", weapon.getName());
		db.close();
	}
	
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
	
	public boolean startMerchant(){
		return db.startMerchant();
	}

	/** 
	 * @returns the path to the tmx file according to the current level
	 */
	public InputStream getLevelPath(int index) {
		RandomMapGenerator gen = new RandomMapGenerator(context, lastLevel, 1);
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

	public void previousLevel() {
		level--;
	}

	public TMXLayer getTMXLayer() {
		return getCurrentScene().getMap().getTMXLayers().get(0);
	}

	public void addSceneToManager(OurScene scene) {
		sceneManager.addScene(scene);
	}

	public TMXTiledMap loadTMXMap(AssetManager assets, Engine engine,
			VertexBufferObjectManager vertexBufferObjectManager, int index, InputStream input) {
		Log.d("projekt", "input: " + input);
		if(input == null) {
			input = getLevelPath(index);
		}
		return mapLoader.loadTMXMap(assets, engine, vertexBufferObjectManager, input);
	}


	public HashMap<String, float[]> getSpawn() {
		return mapLoader.getSpawn();
	}
	
	public OurScene getCurrentScene(){
		Log.d("RPG", "level: "+level);
		return sceneManager.getScene(level);
	}

	public int fight(Player player, Opponent opponent, Sprite redBar, Sprite redBarEnemy) {
		return FightHelper.fight(player, opponent, redBar, redBarEnemy);
	}

	public Item getLoot(int loot) {
		db.open();
		Item items = db.getLoot(loot);
		db.close();
		return items;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLevel(){
		return level;
	}

	public int getLastLevel() {
		return lastLevel;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public boolean addArmor(Armor armor){
		return player.addArmor(armor);
	}
	
	public void setWeapon(Weapon weapon){
		player.setWeapon(weapon);
	}
	
	public Weapon getEquippedWeapon(){
		return player.getEquippedWeapon();
	}
	
	public Armor[] getArmor(){
		return player.getArmor();
	}
	
	public ArrayList<Item> getInventory(){
		return player.getInventory();
	}
	
	public void addItemToInventory(Item item){
		player.addItemToInventory(item);
	}

	public void checkQuests(String enemyName) {
		questManager.checkQuests(enemyName);
	}

	public void checkQuests(NPC npc) {
		questManager.checkQuests(npc);
	}

	public void checkQuests(Item item) {
		questManager.checkQuests(item);
	}

	public void removeItemFromInventory(Item item) {
		player.removeItemFromInventory(item);
	}
	
	public void removeEquippedArmor(Armor armor) {
		player.removeEquippedArmor(armor);
	}
	
	public void removeEquippedWeapon(Weapon weapon){
		player.removeEquippedWeapon(weapon);
	}
	
	public void heal(HealItem item){
		player.heal(item.getHeal());
	}

	public void addExp(int points) {
		player.addEXP(points);
		float percentage = player.getEXPPercentage()/100.0f;
		expBar.setWidth(558*percentage);
	}

	public ArrayList<Quest> getActiveQuests() {
		return questManager.getActiveQuests();
	}
	
	public ArrayList<Quest> getClosedQuests() {
		return questManager.getClosedQuests();
	}
	
	public int getKillCount(int index) {
		if(questManager.getActiveQuests().get(index) instanceof KillQuest) {
		return ((KillQuest) questManager.getActiveQuests().get(index)).getKillCount();
		}
		return 0; 
	}
	
	public NPC getNPCWithID(OurScene scene, int index) {
		ArrayList<NPC> npcList = scene.getNPCsInScene();
		if(npcList.size() > index) {
			return npcList.get(index);
		}
		return null;
	}

	public String getNPCName(int id) {
		db.open();
		String name = db.getNPCName(id);
		db.close();
		return name;
	}

	public void setInventory(ArrayList<Item> tempInventoryList) {
		player.setInventory(tempInventoryList);
	}

	public void removeEquippedArmor(int i) {
		player.removeEquippedArmor(i);
	}
	
	public OurScene getScene(int i) {
		return sceneManager.getScene(i);
	}
	
	public int getGold(){
		return player.getGold();
	}
	
	public void changeGold(int value){
		player.changeGold(value);
	}
}
