package com.example.projectrpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.andengine.entity.IEntity;
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
	/** Arraylist with tmx object groups*/
	private ArrayList<TMXObjectGroup> tmxGroupObjects;

	/** the layer of the map*/
	private TMXLayer layer;
	/** the actual scene*/
	private Scene scene;
	
	// variables to get the path
	/** the a star path the sprite should go*/
	private org.andengine.util.algorithm.path.Path path;
	/** the heuristic to get the rest costs for the path*/
	private EuclideanHeuristic<TMXLayer> heuristic;
	/** the a star path finder*/
	private AStarPathFinder<TMXLayer> aStarPathFinder;
	/** the path finder map*/
	private IPathFinderMap<TMXLayer> pathFinderMap;
	/** the cost funktion*/
	private ICostFunction<TMXLayer> costCallback;
	
	/** the width and height of all tmxTiles*/
	private int tileWidth;
	
	/**
	 * The constructor of the algorithm. Set the instance variables to the given values.
	 * @param startPosition: the tile where the path should start (where the player stands)
	 * @param endPosition: the tile at which the player clicked.
	 * @param tmxTiledMap: the map with the tiles
	 * @param scene: the scene
	 */
	public Algorithm(TMXTile startPosition, TMXTile endPosition, TMXTiledMap tmxTiledMap, Scene scene) {
		this.endPosition = endPosition;
		this.startPosition = startPosition;
		this.tiledMap = tmxTiledMap;
		this.layer = tiledMap.getTMXLayers().get(0);
		this.scene = scene;
		this.tileWidth = startPosition.getTileWidth();
	}
	
	/**
	 * check if there are collision tiles or a sprite. Calculate the cost callback value.
	 */
	public Algorithm generatePathMap() {
		// Create the needed objects for the AStarPathFinder
        aStarPathFinder = new AStarPathFinder<TMXLayer>();
       
        // No special heuristic needed
        heuristic = new EuclideanHeuristic<TMXLayer>();

        // Define the block behavior
        pathFinderMap = new IPathFinderMap<TMXLayer>() {
        	private boolean collide;
        	
        	/**
        	 * check if the given tile is blocked. 
        	 * It is blocked if it has the property collision or if there is a sprite on it.
        	 * @param pX the x value of the tile to check
        	 * @param pY the x value of the tile to check
        	 * @param tmxLayer the actual layer
        	 * @return true if it is blocked. Else return false.
        	 */
        	@Override
            public boolean isBlocked(final int pX, final int pY, final TMXLayer tmxLayer) {
        		collide = false;
        		TMXTile endTile = tmxLayer.getTMXTile(pX, pY);
				//Null check. Used since not all tiles have properties
            	if(endTile.getTMXTileProperties(tiledMap) != null){
                    //Get tiles with collision property
            		if(endTile.getTMXTileProperties(tiledMap).containsTMXProperty("COLLISION", "true")) 
            			collide = true;
            	} else {
            		for(int i = 0; i < scene.getChildCount(); i++) {
            			IEntity entity = scene.getChildByIndex(i);
            			// check if there are stands an opponent or an npc
            			if(entity instanceof Opponent || entity instanceof NPC) {
            				float entityX = entity.getX();
            				float entityY = entity.getY();
            				TMXTile tile = tiledMap.getTMXLayers().get(0).getTMXTileAt(entityX+12, entityY+16); // TODO: 16 evtl durch PLAYER_WIDTH/HEIGHT ersetzen
            				if(endTile.equals(tile)) {
            					collide = true;
            					break;
            				}
            			}
            		}
            	}
            	if(getCollideTiles().contains(tmxLayer.getTMXTile(pX, pY))) {
            		collide = true;
            	}
        		return collide;
        	};
		};
		
		costCallback = new ICostFunction<TMXLayer>(){
			/**
			 * get the costs from one to another tile
			 * @param pathFinderMap the path finder map
			 * @param fromX the x coordinate of the start tile
			 * @param fromY the y coordinate of the start tile
			 * @param toX the x coordinate of the end tile
			 * @param toY the y coordinate of the end tile
			 * @param entity
			 * @return the costs from the start to the end tile
			 */
            @Override
            public float getCost(IPathFinderMap<TMXLayer> pathFinderMap,
            		 final int fromX, final int fromY, final int toX, final int toY, TMXLayer entity) {
            	final float dX = toX - fromX;
        		final float dY = toY - fromY;

        		return (float) Math.sqrt(dX * dX + dY * dY);
	        }
	    };
		return this;
	}
	
	/**
	 * update the path. Check if the endtile is reachable. If not return null for the path. 
	 * Then build the path with the a star path finder from the engine.
	 * @return the found path
	 */
	public Path updatePath() {
		// Sets the A* path from the player location to the touched location.
		if(pathFinderMap.isBlocked(endPosition.getTileColumn(), endPosition.getTileRow(), layer)) {
			if(isReachable(endPosition)) {
				endPosition = getNextTile(startPosition, endPosition);
			} else {
				return null;
			}
		}
		if(startPosition.equals(endPosition)) {
			return null;
		}
        // Determine the tile locations
        int FromCol = startPosition.getTileColumn();
        int FromRow = startPosition.getTileRow();
        int ToCol = endPosition.getTileColumn();
        int ToRow = endPosition.getTileRow();
        // Find the path. This needs to be refreshed
        path = aStarPathFinder.findPath(pathFinderMap, 0, 0, tiledMap.getTileColumns()-1, tiledMap.getTileRows()-1, layer, 
        		FromCol, FromRow, ToCol, ToRow, false, heuristic, costCallback);
        //Moves the sprite along the path
    	return loadPathFound();
	}

	/**
	 * check if the given tile is reachable or not. 
	 * It is not reachable if it is surrounded by blocked tiles.
	 * @param tile the tile to check
	 * @return true if it is reachable. False if not.
	 */
	private boolean isReachable(TMXTile tile) {
		TMXTile[] testTiles = new TMXTile[4];
		if(tile.getTileColumn() > 0 ) {
			testTiles[0] = layer.getTMXTile((tile.getTileColumn() - 1), (tile.getTileRow()));			
		}
		if(tile.getTileRow() > 0 ) {
			testTiles[1] = layer.getTMXTile((tile.getTileColumn()), (tile.getTileRow() - 1));
		}
		if(tile.getTileRow() < (layer.getTileRows() - 1) ) {
			testTiles[2] = layer.getTMXTile((tile.getTileColumn()), (tile.getTileRow() + 1));
		}
		if(tile.getTileColumn() < (layer.getTileColumns() - 1) ) {
			testTiles[3] = layer.getTMXTile((tile.getTileColumn() + 1), (tile.getTileRow()));			
		}
		
		for(int i = 0; i < testTiles.length; i++) {
			if(testTiles[i] != null) {
				if(!pathFinderMap.isBlocked(testTiles[i].getTileColumn(), testTiles[i].getTileRow(), layer)) {
					return true;
				}
				
			}
		}
		
		return false;
	}
	
	/**
	 * Load the path that was found. 
	 * The path finder from the engine gives back a path that cannot be used later.
	 * @return the found path in usable form
	 */
	private Path loadPathFound() {
		if (path != null) {
			Path currentPath = new Path(path.getLength());
			for (int i = 0; i < path.getLength(); i++) {
				currentPath.to((path.getX(i) * tileWidth) + 4, (path.getY(i) * tileWidth));
			}
			return currentPath;
		}
		
		return null;
	}
	
	/**
	 * Finds the next best tile if the final tile was blocked
	 * @param playerPosition the position the player stands
	 * @param finalPosition the position to check
	 * @return the new tile
	 */
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
			if(finalPosition.getTileColumn() + i < TMXMapLayer.getTileColumns())
				playerTiles.add(TMXMapLayer.getTMXTile(finalPosition.getTileColumn() + i, finalPosition.getTileRow()));
			//DOWN
			if(finalPosition.getTileRow() + i < TMXMapLayer.getTileRows())
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
	
	/**
	 * Get all tiles at the map that have the property Collision or the one where a sprite stands.
	 * @return a list of blocked tiles
	 */
	private ArrayList<TMXTile> getCollideTiles() {
		ArrayList<TMXTile> collideTiles;
		tmxGroupObjects = new ArrayList<TMXObjectGroup>();
		for (final TMXObjectGroup group : tiledMap.getTMXObjectGroups()) {
			tmxGroupObjects.add(group); 
		}
		collideTiles = this.getObjectGroupPropertyTiles("COLLISION",  tmxGroupObjects);
		for(int i = 0; i < scene.getChildCount(); i++) {
			IEntity entity = scene.getChildByIndex(i);
			// check if there are AnimatedSprites
			if(entity instanceof Opponent || entity instanceof NPC) {
				float entityX = entity.getX();
				float entityY = entity.getY();
				TMXTile tile = tiledMap.getTMXLayers().get(0).getTMXTileAt(entityX+12, entityY+16); // TODO: 16 evtl durch PLAYER_WIDTH/HEIGHT ersetzen
				collideTiles.add(tile);
			}
		}
		return collideTiles;
	}

	/**
	 * Get all tmxObjects that have the given property
	 * @param name the name of the property
	 * @param tmxObjectGroups a list with all tmxObjectGroups
	 * @return a list with all tiles with the given property
	 */
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
						int objectRows = objectTiles.getHeight() / tileWidth;
						int objectColumns = objectTiles.getWidth() / tileWidth;
						
						for (int tileRow = 0; tileRow < objectRows; tileRow++) {
							for (int tileColumn = 0; tileColumn < objectColumns; tileColumn++) {
								float objectTileX = objectX + tileColumn * tileWidth;
								float objectTileY = objectY + tileRow * tileWidth;
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
