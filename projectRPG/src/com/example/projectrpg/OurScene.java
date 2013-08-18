package com.example.projectrpg;

import java.util.HashMap;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXTiledMap;

import android.content.Context;
import android.util.Log;

public class OurScene extends Scene {
	
	private HashMap<String, float[]> spawns;	
	private TMXTiledMap tmxTiledMap;
	private int id;

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
}
