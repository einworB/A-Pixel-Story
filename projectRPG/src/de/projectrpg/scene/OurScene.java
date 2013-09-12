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

public class OurScene extends Scene {
	
	private HashMap<String, float[]> spawns;	
	private TMXTiledMap tmxTiledMap;
	private int id;
	private OurRandomGenerator rgen = new OurRandomGenerator();
	private ArrayList<NPC> npcInScene = new ArrayList<NPC>();
	
	public OurScene(int id, Context context, TMXTiledMap tmxTiledMap, HashMap<String, float[]> spawns){
		super();
		this.tmxTiledMap = tmxTiledMap;
		attachChild(tmxTiledMap.getTMXLayers().get(0));
		setOnSceneTouchListener((LevelActivity)context);
		this.spawns = spawns;
		this.id = id;
	}
	
	public float[] getSpawn(String previousID){
		if(previousID==null) return spawns.get("SPAWN");
		else return spawns.get(previousID);
	}
	
	public HashMap<String, float[]> getSpawns(){
		return spawns;
	}
	
	public TMXTiledMap getMap(){
		return tmxTiledMap;
	}
	
	public int getID(){
		return id;
	}
	
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
						if(entity instanceof Sprite && entity.getX()==tile.getTileX() && entity.getY()==tile.getTileY()){
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

	public ArrayList<NPC> getNPCsInScene() {
		return npcInScene;
	}
}
