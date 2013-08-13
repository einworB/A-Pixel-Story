package com.example.projectrpg;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;

public class InteractionDecider {

	public boolean decide(TMXTile startTile, TMXTile destinationTile, TMXTiledMap tiledMap, Scene scene) {
		boolean collide = false;
		//Null check. Used since not all tiles have properties
//    	if(destinationTile.getTMXTileProperties(tiledMap) != null){
//            //Get tiles with collision property
//    		if(destinationTile.getTMXTileProperties(tiledMap).containsTMXProperty("COLLISION", "true")) 
//    			collide = true;    					           		
//    	}
		for(int i = 0; i<scene.getChildCount(); i++){
			IEntity entity = scene.getChildByIndex(i);
			if(!(entity instanceof AnimatedSprite)) continue;
			float entityX = entity.getX();
			float entityY = entity.getY();
			TMXTile tile = tiledMap.getTMXLayers().get(0).getTMXTileAt(entityX, entityY);
			if(destinationTile.equals(tile)){
				collide = true;
				break;
			}
		}
    	if(!collide) return true;
    	int divColumns = Math.abs(startTile.getTileColumn()-destinationTile.getTileColumn());
    	int divRows = Math.abs(startTile.getTileRow()-destinationTile.getTileRow());
    	if(startTile.getTileRow()==destinationTile.getTileRow() && divColumns==1) return false;
    	else if(startTile.getTileColumn()==destinationTile.getTileColumn() && divRows==1) return false;
    	else return true;
	}
	
	

}
