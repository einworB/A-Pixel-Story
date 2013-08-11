package com.example.projectrpg;

import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;

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
	
	/** boolean if the player is moving */
	private boolean isMoving;
	
	/** constructor */
	public Controller(){
		isMoving = false;
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
		Algorithm algo = new Algorithm(startTile, destinationTile, tmxTiledMap);
		algo.generatePathMap();
		return algo.updatePath();
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

}
