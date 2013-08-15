package com.example.projectrpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.andengine.entity.modifier.PathModifier.Path;
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

import android.util.Log;


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
	private TMXLayer layer;
	
	
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
	
	
	public Algorithm(TMXTile startPosition, TMXTile endPosition, TMXTiledMap tmxTiledMap) {
		this.endPosition = endPosition;
		this.startPosition = startPosition;
		this.tiledMap = tmxTiledMap;
		this.layer = tiledMap.getTMXLayers().get(0);
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
            		 final int pFromX, final int pFromY, final int pToX, final int pToY, TMXLayer pEntity) {
            	final float dX = pToX - pFromX;
        		final float dY = pToY - pFromY;

        		return (float) Math.sqrt(dX * dX + dY * dY);
	        }
	    };
		return this;
	}
	
	/*
	 * Updates the path
	 */
	public Path updatePath() {	
		// Sets the A* path from the player location to the touched location.
		if(pathFinderMap.isBlocked(endPosition.getTileColumn(), endPosition.getTileRow(), layer)) {
			//TODO abfangen ob außenrum alle blockiert sind
			Log.d("projekt", ""+ isReachable(endPosition));
			if(isReachable(endPosition)) {
				endPosition = getNextTile(startPosition, endPosition);
			} else {
				return null;
			}
		}
		
        // Determine the tile locations
        int FromCol = startPosition.getTileColumn();
        int FromRow = startPosition.getTileRow();
        int ToCol = endPosition.getTileColumn();
        int ToRow = endPosition.getTileRow();
        // Find the path. This needs to be refreshed
        path = aStarPathFinder.findPath(pathFinderMap, 0, 0, tiledMap.getTileColumns()-1, tiledMap.getTileRows()-1, layer, 
        		FromCol, FromRow, ToCol, ToRow, false, heuristic, costCallback);
        Log.d("projekt", "" + path.toString());
    	//Moves the sprite along the path
    	return loadPathFound();
	}

	private boolean isReachable(TMXTile tile) {
		TMXTile[] testTiles = new TMXTile[4];

		testTiles[0] = layer.getTMXTile((tile.getTileColumn()), (tile.getTileRow() - 1));
		testTiles[1] = layer.getTMXTile((tile.getTileColumn()), (tile.getTileRow() + 1));
		testTiles[2] = layer.getTMXTile((tile.getTileColumn() - 1), (tile.getTileRow()));
//		testTiles[3] = layer.getTMXTile((tile.getTileColumn() - 1), (tile.getTileRow() - 1));
//		testTiles[4] = layer.getTMXTile((tile.getTileColumn() - 1), (tile.getTileRow() + 1));
		testTiles[3] = layer.getTMXTile((tile.getTileColumn() + 1), (tile.getTileRow()));
//		testTiles[6] = layer.getTMXTile((tile.getTileColumn() + 1), (tile.getTileRow() - 1));
//		testTiles[7] = layer.getTMXTile((tile.getTileColumn() + 1), (tile.getTileRow() + 1));
		
		for(int i = 0; i < testTiles.length; i++) {
			if(!pathFinderMap.isBlocked(testTiles[i].getTileColumn(), testTiles[i].getTileRow(), layer)) {
				return true;
			}
		}
		
		return false;
	}
	private Path loadPathFound() {
		if (path != null) {
			Path currentPath = new Path(path.getLength());
			for (int i = 0; i < path.getLength(); i++) {
				currentPath.to(path.getX(i) * 32, (path.getY(i) * 32) - 32); //TODO feste Größe angegeben..
			}
			return currentPath;
		}
		
		return null;
	}
	
	/** Finds the next best tile if the final tile was blocked*/
	private TMXTile getNextTile(TMXTile playerPosition, TMXTile finalPosition){
		List<TMXTile> playerTiles = new ArrayList<TMXTile>();
		List<TMXTile> removeTiles = new ArrayList<TMXTile>(); //This is necessary to avoid concurrent errors
		List<Float> distanceTiles = new ArrayList<Float>();
		TMXTile playerTile = null;
		final int MAX_DISTANCE = 1; //This is the range it will search for the new tile
		
		
		//if the tile was blocked then get the next best one
		//finds the next walkable tile if blocked
		for (int i = 1; i <= MAX_DISTANCE; i++) {
			TMXLayer TMXMapLayer = tiledMap.getTMXLayers().get(0);
			//Create a buffer on your map the same thickness as max distance, or reduce max distance
			//LEFT
			if(finalPosition.getTileColumn() - i >= 0)
				playerTiles.add(TMXMapLayer.getTMXTile(finalPosition.getTileColumn() - i, finalPosition.getTileRow()));
			//UP
			if(finalPosition.getTileRow() - i >= 0)
				playerTiles.add(TMXMapLayer.getTMXTile(finalPosition.getTileColumn(), finalPosition.getTileRow() - i));
			//RIGHT
			if(finalPosition.getTileColumn() + i <= TMXMapLayer.getTileColumns())
				playerTiles.add(TMXMapLayer.getTMXTile(finalPosition.getTileColumn() + i, finalPosition.getTileRow()));
			//DOWN
			if(finalPosition.getTileRow() + i <= TMXMapLayer.getTileRows())
				playerTiles.add(TMXMapLayer.getTMXTile(finalPosition.getTileColumn(), finalPosition.getTileRow() + i));

			for (TMXTile tmxTile : playerTiles) {
				//If tile is over a blocked tile or out of bounds then remove
				if (pathFinderMap.isBlocked(tmxTile.getTileColumn(), tmxTile.getTileRow(), TMXMapLayer) 
						|| tmxTile.getTileX() >= TMXMapLayer.getWidth()
						|| tmxTile.getTileY() >= TMXMapLayer.getHeight() 
						|| tmxTile.getTileX() < 0
						|| tmxTile.getTileY() < 0 ) {	
					removeTiles.add(tmxTile);
				}									
			}
			//If any of the above tiles went outside of the blocked tile then stop looping
			if(playerTiles.size() >= 1){
				break;
			}
		}

		//Remove all offending tiles
		playerTiles.removeAll(removeTiles);
		
		for (TMXTile tmxTile2 : playerTiles) {
			//This is the distance formula for each tile from the player to the alternate tile
			distanceTiles.add((float)Math.sqrt(((Math.pow(tmxTile2.getTileX() - playerPosition.getTileX() , 2) 
					+ Math.pow(tmxTile2.getTileY() - playerPosition.getTileY() , 2)))));						
		}

		//Gets the index of the smallest distance
		int tempIndex = distanceTiles.indexOf(Collections.min(distanceTiles));

		//The tile that was outside is the tile we move to
		playerTile = playerTiles.get(tempIndex);
		

		return playerTile;
	}
	
	
	private ArrayList<TMXTile> getCollideTiles() {
		tmxGroupObjects = new ArrayList<TMXObjectGroup>();
		for (final TMXObjectGroup group : tiledMap.getTMXObjectGroups()) {
			tmxGroupObjects.add(group); 
		}
		return this.getObjectGroupPropertyTiles("COLLISION",  tmxGroupObjects);
	}

	public ArrayList<TMXTile> getObjectGroupPropertyTiles(String name, ArrayList<TMXObjectGroup> tmxObjectGroups){
		ArrayList<TMXTile> objectTile = new ArrayList<TMXTile>();
		for (final TMXObjectGroup objectGroups : tmxObjectGroups) {
			// Iterates through the properties and assigns them to the new variable
			for (final TMXObjectGroupProperty groupProperties : objectGroups.getTMXObjectGroupProperties()) {
				//Sees if any of the elements have this condition
				if (groupProperties.getName().contains(name)) {
					for (final TMXObject objectTiles : objectGroups.getTMXObjects()) {
						int objectX = objectTiles.getX();
						int objectY = objectTiles.getY();
						// Gets the number of rows and columns in the object
						int objectRows = objectTiles.getHeight() / 32; //TODO feste Größe angegeben..
						int objectColumns = objectTiles.getWidth() / 32; //TODO feste Größe angegeben..
						
						for (int tileRow = 0; tileRow < objectRows; tileRow++) {
							for (int tileColumn = 0; tileColumn < objectColumns; tileColumn++) {
								float objectTileX = objectX + tileColumn * 32; //TODO feste Größe angegeben..
								float objectTileY = objectY + tileRow * 32; //TODO feste Größe angegeben..
								objectTile.add(tiledMap.getTMXLayers().get(0).getTMXTileAt(objectTileX, objectTileY));						
							}							 
						}
					}
				}			
			}
		}
		return objectTile;
	}
}
