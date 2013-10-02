package de.apixelstory.util;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;

import de.apixelstory.sprites.NPC;
import de.apixelstory.sprites.Opponent;

/**
 * Created to get a decision if the player shall interact with a tile or not
 */
public class InteractionDecider {
	
	private static final int MOVE = 1;
	private static final int TALK = 2;
	private static final int FIGHT = 3;
	private static final int DOOR = 4;

	/**
	 * determine if the player should move to or interact
	 * 
	 * @param startTile - the tile the player is located
	 * @param destinationTile - the tile the user has clicked
	 * @param tiledMap - the map containing those tiles
	 * @param scene - the scene containing the map
	 * @returns true if the player shall move to the tile, false if he shall interact
	 */
	public int decide(TMXTile startTile, TMXTile destinationTile, TMXTiledMap tiledMap, Scene scene) {
		boolean collide = false;
		IEntity entity = null;
		// cycle through all childs of the scene 
		for(int i = 0; i<scene.getChildCount(); i++){
			entity = scene.getChildByIndex(i);
			// check if they are AnimatedSprites
			if(entity instanceof NPC || entity instanceof Opponent){
				float entityX = entity.getX();
				float entityY = entity.getY();
				TMXTile tile = tiledMap.getTMXLayers().get(0).getTMXTileAt(entityX+12, entityY+16);
				if(destinationTile.equals(tile)){
					collide = true;
					break;
				}
			}
		}
		// no collision and destination tile is not a door tile -> nothing on destination tile -> no interaction, move to tile
    	if(!(collide || destinationTile.getGlobalTileID() == 15)) return MOVE;
    	// check if the player is standing in front of the tile
    	int divColumns = Math.abs(startTile.getTileColumn()-destinationTile.getTileColumn());
    	int divRows = Math.abs(startTile.getTileRow()-destinationTile.getTileRow());
    	if(startTile.getTileRow()==destinationTile.getTileRow() && divColumns==1){
    		if(entity instanceof NPC) return TALK;
    		else if(destinationTile.getGlobalTileID() == 15) {
        		return DOOR;
        	}
    		// only NPC or opponent possible
    		else return FIGHT;
    	}
    	else if(startTile.getTileColumn()==destinationTile.getTileColumn() && divRows==1){
    		if(entity instanceof NPC) return TALK;
    		else if(destinationTile.getGlobalTileID() == 15) {
        		return DOOR;
        	}
    		// only NPC or opponent possible
    		else return FIGHT;
    	}
    	// tile containing an Animated Sprite but too far away for interaction
    	else return MOVE;
	}
}
