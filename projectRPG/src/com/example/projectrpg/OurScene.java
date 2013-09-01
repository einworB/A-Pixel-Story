package com.example.projectrpg;

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

public class OurScene extends Scene {
	
	private HashMap<String, float[]> spawns;	
	private TMXTiledMap tmxTiledMap;
	private int id;
	private OurRandomGenerator rgen = new OurRandomGenerator();
	private ArrayList<int[]> opponentPositionList = new ArrayList<int[]>();

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
	
	public void generateOpponents(TiledTextureRegion opponentTextureRegion, VertexBufferObjectManager vertextBufferObjectManager, int level){
		int opponentCount = rgen.nextInt(10) + 10;
		for(int i=0; i<opponentCount; i++){
			while(true){
				boolean alreadySet = false;
				int[] positions = rgen.getInts(2, 29);
				TMXTile tile = tmxTiledMap.getTMXLayers().get(0).getTMXTile(positions[0]+1, positions[1]+1);
				int[] position = new int[]{tile.getTileX(), tile.getTileY()};
				for(int j=0; j<getChildCount(); j++){
					IEntity entity = getChildByIndex(j);
					if(entity instanceof Sprite && entity.getX()==tile.getTileX() && entity.getY()==tile.getTileY()){
						alreadySet = true;
						break;
					}
				}
				if(tile.getTMXTileProperties(tmxTiledMap)==null && !alreadySet){
					Opponent opponent = new Opponent(tile.getTileX()+4, tile.getTileY(), 24, 32, opponentTextureRegion, vertextBufferObjectManager, level, false);
					attachChild(opponent);
					opponent.setCurrentTileIndex(1+(rgen.nextInt(4)*4));
					opponentPositionList.add(position);
					Log.d("RPG", "Level "+level+": Opponent "+i+" created.");
					break;
				}
			}
		}
	}
}
