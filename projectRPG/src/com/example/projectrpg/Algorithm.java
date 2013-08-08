package com.example.projectrpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXObjectGroupProperty;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.util.algorithm.path.ICostFunction;
import org.andengine.util.algorithm.path.IPathFinderMap;
import org.andengine.util.algorithm.path.astar.AStarPathFinder;
import org.andengine.util.algorithm.path.astar.EuclideanHeuristic;

import android.content.Context;

/**
 * Get the path the sprite should move.
 * Calculated with the a star algorithm.
 * 
 * @author Lena
 *
 */
public class Algorithm {

	// TMX variables
	/** the tiled map that was generated before*/
	private TMXTiledMap tiledMap;
	/** the position where the sprite should go to*/
	private TMXTile endPosition;
	/** the position where the sprite stands actually*/
	private TMXTile startPosition;
	private ArrayList<TMXObjectGroup> tmxGroupObjects;
	
	// variables to get the path
	/** the a star path the sprite should go*/
	private org.andengine.util.algorithm.path.Path path;
	/** the heuristic to get the rest costs for the path*/
	private EuclideanHeuristic<TMXLayer> heuristic;
	/** */
	private AStarPathFinder<TMXLayer> aStarPathFinder;
	/** */
	private IPathFinderMap<TMXLayer> pathFinderMap;
	/** */
	private ICostFunction<TMXLayer> costCallback;
	
	// context
	/** the context of the activity that has called this class*/
	private Context context;
	
	
	public Algorithm(TMXTile startPosition, TMXTile endPosition, TMXTiledMap tmxTiledMap, Context context) {
		this.endPosition = endPosition;
		this.startPosition = startPosition;
		this.tiledMap = tmxTiledMap;
		this.context = context;
	}
	
	public Algorithm generatePathMap() {
		// Create the needed objects for the AStarPathFinder
        aStarPathFinder = new AStarPathFinder<TMXLayer>();
       
        // No special heuristic needed
        heuristic = new EuclideanHeuristic<TMXLayer>();

        // Define the block behavior
        pathFinderMap = new IPathFinderMap<TMXLayer>() {
        	private boolean collide;
        	
        	@Override
            public boolean isBlocked(final int pX, final int pY, final TMXLayer pTMXLayer) {
        		collide = false;
				//Null check. Used since not all tiles have properties
            	if(pTMXLayer.getTMXTile(pX, pY).getTMXTileProperties(tiledMap) != null){
                    //Get tiles with collision property
            		if(pTMXLayer.getTMXTile(pX, pY).getTMXTileProperties(tiledMap).containsTMXProperty("COLLISION", "true")) 
            			collide = true;    					           		
            	}
            	if(getCollideTiles().contains(pTMXLayer.getTMXTile(pX, pY))) {
            		collide = true;
            	}
            	
        		return collide;
        	};
		};
		
		costCallback = new ICostFunction<TMXLayer>(){
            @Override
            public float getCost(IPathFinderMap<TMXLayer> pPathFinderMap,
            		int pFromX, int pFromY, int pToX, int pToY, TMXLayer pEntity) {
				return 0;
        }
    };
	return this;
	}
	
	/*
	 * Updates the path
	 */
	public Path updatePath() {	
		// Get the tile the feet of the player are currently waking on. 
		TMXTile playerPosition = startPosition;
		
		// Sets the A* path from the player location to the touched location.
		if(pathFinderMap.isBlocked(endPosition.getTileColumn(), endPosition.getTileRow(), tiledMap.getTMXLayers().get(0))){	
			endPosition = getNextTile(playerPosition, endPosition);
		}
		
        // Determine the tile locations
        int FromCol = playerPosition.getTileColumn();
        int FromRow = playerPosition.getTileRow();
        int ToCol = endPosition.getTileColumn();
        int ToRow = endPosition.getTileRow();
        // Find the path. This needs to be refreshed
        path = aStarPathFinder.findPath(pathFinderMap, 0, 0, tiledMap.getTileColumns()-1, tiledMap.getTileRows()-1, tiledMap.getTMXLayers().get(0), 
        		FromCol, FromRow, ToCol, ToRow, false, heuristic, costCallback);

    	//Moves the sprite along the path
    	return loadPathFound();
	}
	
	private Path loadPathFound() {
		if (path != null) {
			Path lCurrentPath = new Path(path.getLength());
			for (int i = 0; i < path.getLength(); i++) {
				lCurrentPath.to(path.getX(i) * 32, path.getY(i) * 32); //TODO feste Größe angegeben..
			}
			return lCurrentPath;
		}
		
		return null;
	}
	
	/*
	 * Finds the next best tile if the final tile was blocked
	 */
	private TMXTile getNextTile(TMXTile pPlayerPosition, TMXTile pFinalPosition){
		List<TMXTile> playerAltTiles = new ArrayList<TMXTile>();
		List<TMXTile> removeAltTiles = new ArrayList<TMXTile>();//This is necessary to avoid concurrent errors
		List<Float> distanceAltTiles = new ArrayList<Float>();
		TMXTile playerAltTile = null;
		final int MAX_DISTANCE = 1;//This is the range it will search for the new tile
		
		//if the tile was blocked then get the next best one
		//finds the next walkable tile if blocked
		OUTERMOST: for (int i = 1; i <= MAX_DISTANCE; i++) {
			TMXLayer TMXMapLayer = tiledMap.getTMXLayers().get(0);
			//Create a buffer on your map the same thickness as max distance, or reduce max distance
			//LEFT
			if(pFinalPosition.getTileColumn() - i >= 0)
				playerAltTiles.add(TMXMapLayer.getTMXTile(pFinalPosition.getTileColumn() - i, pFinalPosition.getTileRow()));
			//UP
			if(pFinalPosition.getTileRow() - i >= 0)
				playerAltTiles.add(TMXMapLayer.getTMXTile(pFinalPosition.getTileColumn(), pFinalPosition.getTileRow() - i));
			//RIGHT
			if(pFinalPosition.getTileColumn() + i <= TMXMapLayer.getTileColumns())
				playerAltTiles.add(TMXMapLayer.getTMXTile(pFinalPosition.getTileColumn() + i, pFinalPosition.getTileRow()));
			//DOWN
			if(pFinalPosition.getTileRow() + i <= TMXMapLayer.getTileRows())
				playerAltTiles.add(TMXMapLayer.getTMXTile(pFinalPosition.getTileColumn(), pFinalPosition.getTileRow() + i));

			for (TMXTile tmxTile : playerAltTiles) {
				//If tile is over a blocked tile or out of bounds then remove
				if (pathFinderMap.isBlocked(tmxTile.getTileColumn(), tmxTile.getTileRow(), TMXMapLayer) 
						|| tmxTile.getTileX() >= TMXMapLayer.getWidth()
						|| tmxTile.getTileY() >= TMXMapLayer.getHeight() 
						|| tmxTile.getTileX() < 0
						|| tmxTile.getTileY() < 0 ) {	
					removeAltTiles.add(tmxTile);
				}									
			}
			//If any of the above tiles went outside of the blocked tile then stop looping
			if(playerAltTiles.size() >= 1){
				break OUTERMOST;
			}
		}

		//Remove all offending tiles
		playerAltTiles.removeAll(removeAltTiles);
		
		for (TMXTile tmxTile2 : playerAltTiles) {
			//This is the distance formula for each tile from the player to the alternate tile
			distanceAltTiles.add((float)Math.sqrt(((Math.pow(tmxTile2.getTileX() - pPlayerPosition.getTileX() , 2) 
					+ Math.pow(tmxTile2.getTileY() - pPlayerPosition.getTileY() , 2)))));						
		}

		//Gets the index of the smallest distance
		int tempIndex = distanceAltTiles.indexOf(Collections.min(distanceAltTiles));

		//The tile that was outside is the tile we move to
		playerAltTile = playerAltTiles.get(tempIndex);
		
		return playerAltTile;
	}
	
	
	private ArrayList<TMXTile> getCollideTiles() {
		tmxGroupObjects = new ArrayList<TMXObjectGroup>();
		for (final TMXObjectGroup pGroup : tiledMap.getTMXObjectGroups()) {
			tmxGroupObjects.add(pGroup); 
		}
		return this.getObjectGroupPropertyTiles("COLLIDE",  tmxGroupObjects);
	}

	public ArrayList<TMXTile> getObjectGroupPropertyTiles(String pName, ArrayList<TMXObjectGroup> tmxObjectGroups){
		ArrayList<TMXTile> ObjectTile = new ArrayList<TMXTile>();
		for (final TMXObjectGroup pObjectGroups : tmxObjectGroups) {
			// Iterates through the properties and assigns them to the new variable
			for (final TMXObjectGroupProperty pGroupProperties : pObjectGroups.getTMXObjectGroupProperties()) {
				//Sees if any of the elements have this condition
				if (pGroupProperties.getName().contains(pName)) {
					for (final TMXObject pObjectTiles : pObjectGroups.getTMXObjects()) {
						int ObjectX = pObjectTiles.getX();
						int ObjectY = pObjectTiles.getY();
						// Gets the number of rows and columns in the object
						int ObjectRows = pObjectTiles.getHeight() / 32; //TODO feste Größe angegeben..
						int ObjectColumns = pObjectTiles.getWidth() / 32; //TODO feste Größe angegeben..
						
						for (int TileRow = 0; TileRow < ObjectRows; TileRow++) {
							for (int TileColumn = 0; TileColumn < ObjectColumns; TileColumn++) {
								float lObjectTileX = ObjectX + TileColumn * 32; //TODO feste Größe angegeben..
								float lObjectTileY = ObjectY + TileRow * 32; //TODO feste Größe angegeben..
								ObjectTile.add(tiledMap.getTMXLayers().get(0).getTMXTileAt(lObjectTileX, lObjectTileY));						
							}							 
						}
					}
				}			
			}
		}
		return ObjectTile;
	}
	
	public static Path calculatePath(float startX, float startY, float endX, float endY, Scene scene) {
		final Path path = new Path(2).to(startX, startY).to(endX, endY);
		return path;
	}
}
