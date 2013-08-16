package com.example.projectrpg;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;

/**
 * Created to get a decision if the player shall interact with a tile or not
 * 
 * @author Philip
 *
 */
public class InteractionDecider {

	/**
	 * determine if the player should move to or interact
	 * 
	 * @param startTile - the tile the player is located
	 * @param destinationTile - the tile the user has clicked
	 * @param tiledMap - the map containing those tiles
	 * @param scene - the scene containing the map
	 * @returns true if the player shall move to the tile, false if he shall interact
	 */
	public boolean decide(TMXTile startTile, TMXTile destinationTile, TMXTiledMap tiledMap, Scene scene) {
		boolean collide = false;

		// cycle through all childs of the scene 
		for(int i = 0; i<scene.getChildCount(); i++){
			IEntity entity = scene.getChildByIndex(i);
			// check if they are AnimatedSprites
			if(entity instanceof AnimatedSprite){
				float entityX = entity.getX();
				float entityY = entity.getY();
				TMXTile tile = tiledMap.getTMXLayers().get(0).getTMXTileAt(entityX+12, entityY+16); // TODO: 16 evtl durch PLAYER_WIDTH/HEUGHT ersetzen
				if(destinationTile.equals(tile)){
					collide = true;
					break;
				}
			}
		}
		// no collision -> nothing on destination tile -> no interaction, move to tile
    	if(!collide) return true;
    	// check if the player is standing in front of the tile
    	int divColumns = Math.abs(startTile.getTileColumn()-destinationTile.getTileColumn());
    	int divRows = Math.abs(startTile.getTileRow()-destinationTile.getTileRow());
    	if(startTile.getTileRow()==destinationTile.getTileRow() && divColumns==1) return false;
    	else if(startTile.getTileColumn()==destinationTile.getTileColumn() && divRows==1) return false;
    	// tile containing an Animated Sprite but too far away for interaction
    	else return true;
	}
	
	

}
