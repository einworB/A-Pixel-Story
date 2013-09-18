package de.projectrpg.scene;

import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.util.Log;
import de.projectrpg.game.LevelActivity;
import de.projectrpg.save.LevelLoader;
import de.projectrpg.save.LevelLoader.NpcObjects;
import de.projectrpg.save.LevelLoader.OpponentObjects;
import de.projectrpg.save.LoadSavedGame;
import de.projectrpg.sprites.NPC;
import de.projectrpg.sprites.Opponent;
import de.projectrpg.util.OurRandomGenerator;

/**
 * This Class is to set all Opponents and npcs on a scene 
 * and set the map in each level of the game.
 * Each level has a scene.
 */
public class OurScene extends Scene {
	
	/** all spawn fields in the game*/
	private HashMap<String, float[]> spawns;	
	/** the map*/
	private TMXTiledMap tmxTiledMap;
	/** the id of the scene*/
	private int id;
	/** the random generator*/
	private OurRandomGenerator rgen = new OurRandomGenerator();
	/** all npc in this scene*/
	private ArrayList<NPC> npcInScene = new ArrayList<NPC>();
	
	/**
	 * the constructor
	 * @param id the id of the scene
	 * @param context the calling activity
	 * @param tmxTiledMap the map
	 * @param spawns all spawns in this game
	 */
	public OurScene(int id, Context context, TMXTiledMap tmxTiledMap, HashMap<String, float[]> spawns){
		super();
		this.tmxTiledMap = tmxTiledMap;
		attachChild(tmxTiledMap.getTMXLayers().get(0));
		if(context instanceof LevelActivity) {
			setOnSceneTouchListener((LevelActivity)context);
		}
		this.spawns = spawns;
		this.id = id;
	}
	
	/**
	 * get a special spawn by the previous id. 
	 * If the previous id is null it is the first level so the spawn should be the first spawn tile in game. 
	 * If it is not null it is a arbitrary level. Here it is the spawn tile to the previous level
	 * @param previousID This is a string that contains "LEVEL" and the id of the scene.
	 * @return
	 */
	public float[] getSpawn(String previousID){
		if(previousID==null) return spawns.get("SPAWN");
		else return spawns.get(previousID);
	}
	
	/**
	 * get all spawns in a hashmap
	 * @return all spawns in a hashmap
	 */
	public HashMap<String, float[]> getSpawns(){
		return spawns;
	}
	
	/**
	 * get the tmx map of this scene
	 * @return the tmx map
	 */
	public TMXTiledMap getMap(){
		return tmxTiledMap;
	}
	
	/**
	 * get the id of this scene
	 * @return the id
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Generate all sprites which should stand in this scene/level. 
	 * First get a random number between 10 and 19. This is the sprite count for this level.
	 * Check if a tile is blocked with an already set Sprite.
	 * @param opponentTextureRegion
	 * @param npcTextureRegion
	 * @param vertextBufferObjectManager
	 * @param level the actual level 
	 */
	public void generateAnimatedSprites(TiledTextureRegion opponentTextureRegion, TiledTextureRegion npcTextureRegion, VertexBufferObjectManager vertextBufferObjectManager, int level){
		int spriteCount = rgen.nextInt(10) + 10;
		for(int i=0; i<=spriteCount; i++){
			while(true){
				boolean alreadySet = false;
				int[] positions = rgen.getInts(2, 29);
				TMXTile tile = tmxTiledMap.getTMXLayers().get(0).getTMXTile(positions[0]+1, positions[1]+1);
				if(tileIsntBlockingSpawn(tile)){
					for(int j=0; j<getChildCount(); j++){
						IEntity entity = getChildByIndex(j);
						if(entity instanceof Sprite && (Math.abs(entity.getX() - tile.getTileX() - 4) < .0000001) && (Math.abs(entity.getY() - tile.getTileY()) < .0000001)){
							alreadySet = true;
							break;
						}
					}
					if(tile.getTMXTileProperties(tmxTiledMap)==null && !alreadySet){
						if(i==spriteCount){
							NPC npc = new NPC(tile.getTileX()+4, tile.getTileY(), 24, 32, npcTextureRegion, vertextBufferObjectManager, id); // TODO: bei mehr(weniger) als einem NPC pro Level id anpassen!!!
							npcInScene.add(npc);
							attachChild(npc);
							npc.setCurrentTileIndex(1+(rgen.nextInt(4)*4));
							Log.d("RPG", "Level "+level+": NPC created.");
						}
						else{
							Opponent opponent = new Opponent(tile.getTileX()+4, tile.getTileY(), 24, 32, opponentTextureRegion, vertextBufferObjectManager, level, false);
							attachChild(opponent);
							opponent.setCurrentTileIndex(1+(rgen.nextInt(4)*4));
							Log.d("RPG", "Level "+level+": Opponent "+i+" created.");
						}					
						break;
					}
				}
			}
		}
	}
	
	/**
	 * If the game is loaded set the sprites again. 
	 * The position and direction of the sprites is saved.
	 * @param opponentTextureRegion
	 * @param npcTextureRegion
	 * @param vertextBufferObjectManager
	 * @param level the actual level
	 * @param loadSavedGame the game loader
	 */
	public void loadAnimatedSprites(TiledTextureRegion opponentTextureRegion, TiledTextureRegion npcTextureRegion, VertexBufferObjectManager vertextBufferObjectManager, int level, LoadSavedGame loadSavedGame) {
		LevelLoader loader = loadSavedGame.getLevelLoader(level);
		ArrayList<OpponentObjects> opponents = loader.getOpponentList();
		ArrayList<NpcObjects> npcs = loader.getNpcList();
		
		for(int i = 0; i < opponents.size(); i++) {
			if (opponents.get(i).getLevel() == level) {
				Opponent opponent = new Opponent((opponents.get(i).getPositionX()), (opponents.get(i).getPositionY()), 24, 32, opponentTextureRegion, vertextBufferObjectManager, level, opponents.get(i).getEpic());
				opponent.setCurrentTileIndex(opponents.get(i).getDirection());
				opponent.changeHealth(-(opponent.getHealth() - opponents.get(i).getHealth()));
				attachChild(opponent);
			}
		}
		
		for(int i = 0; i < npcs.size(); i++) {
			if (npcs.get(i).getLevel() == level) {
				NPC npc = new NPC(npcs.get(i).getPositionX(), npcs.get(i).getPositionY(), 24, 32, npcTextureRegion, vertextBufferObjectManager, npcs.get(i).getID());
				npc.setCurrentTileIndex(npcs.get(i).getDirection());
				npcInScene.add(npc);
				attachChild(npc);
			}
		}
	}
	
	/**
	 * check if a given tile is in front of a the spawn tile
	 * @param tile the tile to check
	 * @return true if the tile is in front of a spawn tile
	 */
	private boolean tileIsntBlockingSpawn(TMXTile tile) {
		int tileColumn = tile.getTileColumn();
		int tileRow = tile.getTileRow();
		TMXTile adjacentTile = null;
		if(tileRow>0){
			adjacentTile = tmxTiledMap.getTMXLayers().get(0).getTMXTile(tileColumn, tileRow-1);
			if(isSpawnTile(adjacentTile)) return false;
		}
		if(tileRow<29){
			adjacentTile = tmxTiledMap.getTMXLayers().get(0).getTMXTile(tileColumn, tileRow+1);
			if(isSpawnTile(adjacentTile)) return false;
		}
		if(tileColumn>0){
			adjacentTile = tmxTiledMap.getTMXLayers().get(0).getTMXTile(tileColumn-1, tileRow);
			if(isSpawnTile(adjacentTile)) return false;
		}
		if(tileColumn<29){
			adjacentTile = tmxTiledMap.getTMXLayers().get(0).getTMXTile(tileColumn+1, tileRow);
			if(isSpawnTile(adjacentTile)) return false;
		}
		return true;
	}

	/**
	 * Check if the given tile is the spawn tile.
	 * @param tile the tile to check
	 * @return true if it is the spawn tile
	 */
	private boolean isSpawnTile(TMXTile tile) {
		if(tile!=null){
			if(tile.getTMXTileProperties(tmxTiledMap)!=null){
				TMXProperties<TMXTileProperty> properties = tile.getTMXTileProperties(tmxTiledMap);
				for(int i=0; i<properties.size(); i++) {
					TMXTileProperty property = properties.get(i);
					if(property.getName().contentEquals("TRANSITION") && property.getValue().contentEquals("SPAWN")) return true;
				}
			}
		}		
		return false;
	}

	/**
	 * get all npcs in this scene
	 * @return all npcs in this scene
	 */
	public ArrayList<NPC> getNPCsInScene() {
		return npcInScene;
	}
}
