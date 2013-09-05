package de.projectrpg.scene;

import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.util.Log;
import de.projectrpg.game.LevelActivity;
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
	
	public ArrayList<NPC> getNPCsInScene() {
		return npcInScene;
	}
}
