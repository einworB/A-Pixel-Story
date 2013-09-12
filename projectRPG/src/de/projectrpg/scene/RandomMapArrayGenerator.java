package de.projectrpg.scene;

import android.util.Log;
import de.projectrpg.util.OurRandomGenerator;


/**
 * make a random array with values of explicit tiles 
 * so a random tmxMap can build out of it.
 * 
 * @author Lena
 *
 */
public class RandomMapArrayGenerator {
	/** The array with values of the tileset tiles.*/
	private int[][] mapArray;
	/** RandomGenerator*/
	private OurRandomGenerator rGen = new OurRandomGenerator();
	
	/** The index of the last level.*/
	private int lastLevel;
	/** The side of the map where the transition tile to exit the level is.*/
	private int spawnExitSide;
	/** The side of the map where the transition tile was in the last level.*/
	private int lastTransitionExitSide;
	/** The index of the actual level.*/
	private int level;
	
	/** This index describes the top row of tiles in the map.*/
	private static final int TOP_SIDE = 0;
	/** This index describes the left row of tiles in the map.*/
	private static final int LEFT_SIDE = 1;
	/** This index describes the right row of tiles in the map.*/
	private static final int RIGHT_SIDE = 2;
	/** This index describes the bottom row of tiles in the map.*/
	private static final int BOTTOM_SIDE = 3;
	/**
	 * set the array
	 */
	public RandomMapArrayGenerator(int lastLevel) {
		mapArray = new int[30][30];
		this.lastLevel = lastLevel;
	}
	
	/**
	 * Set bushes at the sides of the map.
	 * Set the spawn tiles.
	 * Set all other objects that should be at the map, like bushes, trees, cactus..
	 * @param level the actual level
	 * @param lastTransitionExitSide 
	 * @return the array with the tile values
	 */
	public int[][] generateMapArray(int level, int lastTransitionExitSide) {
		
		this.level = level;
		this.lastTransitionExitSide = lastTransitionExitSide;
		
		for(int y = 0; y < 30; y++) {
			for(int x = 0; x < 30; x++) {
				if(x == 0 || y == 0 || x == 29 || y == 29) {
					mapArray[y][x] = 2;
				} else {
					mapArray[y][x] = -1;
				}
			}
		}
		
		setSpawnTiles();
		
		setHouse();
		setBigTree();
		setTreeStump();
		setBush();
		setGras();
		
		checkForUnreachableTiles();
		
		return mapArray;
	}

	/**
	 * Set the spawn tiles. 
	 * A spawn tile cannot be at a edge.
	 */
	private void setSpawnTiles() {

		//set spawn tiles
		boolean spawnSet = false;
		int sideSpawn = -1;
		int spawntile = -1;
		//only in the first level there is a spawntile
		if(level == 1 && !spawnSet) {
			//select on of the four sides to set the spawntile
			sideSpawn = rGen.nextInt(4);
			spawntile = (1 + rGen.nextInt(27)); //the edges cannot be set with spawntiles
			switch(sideSpawn) {
			case TOP_SIDE:
				mapArray[0][spawntile] = 23;
				mapArray[1][spawntile] = 1;
				break;
			case LEFT_SIDE:
				mapArray[spawntile][0] = 23;
				mapArray[spawntile][1] = 1;
				break;
			case RIGHT_SIDE:
				mapArray[29][spawntile] = 23;
				mapArray[28][spawntile] = 1;
				break;
			case BOTTOM_SIDE:
				mapArray[spawntile][29] = 23;
				mapArray[spawntile][28] = 1;
				break;
			}
			spawnSet = true;
		}
		
		if(level != lastLevel && level != 1) {
			while(true) {
				int sideTransition;
				
				if(lastTransitionExitSide == -1) {
					sideTransition = rGen.nextInt(4);				
				} else if(lastTransitionExitSide == LEFT_SIDE ) {
					sideTransition = BOTTOM_SIDE;
				} else if(lastTransitionExitSide == TOP_SIDE) {
					sideTransition = RIGHT_SIDE;
				} else if(lastTransitionExitSide == RIGHT_SIDE ) {
					sideTransition = TOP_SIDE;
				} else {
					sideTransition = LEFT_SIDE;
				}
				
				int transitionTile = (1 + rGen.nextInt(27)); //the edges cannot be set with transitiontiles
				
				if((sideTransition != sideSpawn) && (transitionTile != spawntile)) {
					switch(sideTransition) {
					case TOP_SIDE:
						mapArray[0][transitionTile] = 33;
						mapArray[1][transitionTile] = 1;
						break;
					case LEFT_SIDE:
						mapArray[transitionTile][0] = 33;
						mapArray[transitionTile][1] = 1;
						break;
					case RIGHT_SIDE:
						mapArray[29][transitionTile] = 33;
						mapArray[28][transitionTile] = 1;
						break;
					case BOTTOM_SIDE:
						mapArray[transitionTile][29] = 33;
						mapArray[transitionTile][28] = 1;
						break;
					}
					spawnExitSide = sideTransition;
					break;
				}
			}
		}
		//in every level there is a transitiontile, repeat till the transitiontile is not at the same position as the spawntile
		if(level == lastLevel) {
			while(true) {
				int sideTransition;
				
				if(lastTransitionExitSide == -1) {
					sideTransition = rGen.nextInt(4);				
				} else if(lastTransitionExitSide == LEFT_SIDE ) {
					sideTransition = BOTTOM_SIDE;
				} else if(lastTransitionExitSide == TOP_SIDE) {
					sideTransition = RIGHT_SIDE;
				} else if(lastTransitionExitSide == RIGHT_SIDE ) {
					sideTransition = TOP_SIDE;
				} else {
					sideTransition = LEFT_SIDE;
				}
				
				int transitionTile = (1 + rGen.nextInt(27)); //the edges cannot be set with transitiontiles
				
				if((sideTransition != sideSpawn) && (transitionTile != spawntile)) {
					switch(sideTransition) {
					case TOP_SIDE:
						mapArray[0][transitionTile] = 33;
						mapArray[1][transitionTile] = 1;
						break;
					case LEFT_SIDE:
						mapArray[transitionTile][0] = 33;
						mapArray[transitionTile][1] = 1;
						break;
					case RIGHT_SIDE:
						mapArray[29][transitionTile] = 33;
						mapArray[28][transitionTile] = 1;
						break;
					case BOTTOM_SIDE:
						mapArray[transitionTile][29] = 33;
						mapArray[transitionTile][28] = 1;
						break;
					}
					spawnExitSide = sideTransition;
					break;
				}
			}
		// if the actual level is not the last level there have to be a transition tile 
		} else if (level != lastLevel) {
			while(true) {
				int sideTransition = rGen.nextInt(4);
				
				int transitionTile = (1 + rGen.nextInt(27)); //the edges cannot be set with transitiontiles
				
				if((sideTransition != sideSpawn) && (transitionTile != spawntile)) {
					switch(sideTransition) {
					case TOP_SIDE:
						mapArray[0][transitionTile] = 13;
						mapArray[1][transitionTile] = 1;
						break;
					case LEFT_SIDE:
						mapArray[transitionTile][0] = 13;
						mapArray[transitionTile][1] = 1;
						break;
					case RIGHT_SIDE:
						mapArray[29][transitionTile] = 13;
						mapArray[28][transitionTile] = 1;
						break;
					case BOTTOM_SIDE:
						mapArray[transitionTile][29] = 13;
						mapArray[transitionTile][28] = 1;
						break;
					}
					spawnExitSide = sideTransition;
					break;
				}
			}
		}
	}

	/**
	 * Set a random amount of houses. In a map there can be 0, 1 or 2 houses. 
	 */
	private void setHouse() {
		int houseCount = rGen.nextInt(3);
		int houseX = 0;
		int houseY = 0;
		if(houseCount > 0) {
			while(true) {
				houseX = (1 + rGen.nextInt(26)); //numbers from 1-27 because the house has 3 tiles width
				houseY = (1 + rGen.nextInt(25)); //numbers from 1-26 because the house has 3 tiles height and the house should be reachable from bottom
				if(mapArray[houseX][houseY] == -1 && mapArray[houseX + 1][houseY] == -1 
						&& mapArray[houseX + 2][houseY] == -1 && mapArray[houseX][houseY + 1] == -1 
						&& mapArray[houseX + 1][houseY + 1] == -1 && mapArray[houseX + 2][houseY + 1] == -1) {
					if((mapArray[0][houseY] != 13) && (mapArray[0][houseY + 1] != 13) && (mapArray[0][houseY + 2] != 13) 
							&& (mapArray[0][houseY] != 23) && (mapArray[0][houseY + 1] != 23) && (mapArray[0][houseY + 2] != 23)) {
						if((mapArray[houseX][0] != 13) && (mapArray[houseX + 1][0] != 13) && (mapArray[houseX][0] != 23) && (mapArray[houseX + 1][0] != 23)) {
							if((mapArray[houseX][29] != 13) && (mapArray[houseX + 1][29] != 13) && (mapArray[houseX][29] != 23) && (mapArray[houseX + 1][29] != 23)) {
								break;
							}
						}
					}
				}
			}
			mapArray[houseX][houseY] = 4;
			mapArray[houseX + 1][houseY] = 5;
			mapArray[houseX + 2][houseY] = 6;
			mapArray[houseX][houseY + 1] = 14;
			mapArray[houseX + 1][houseY + 1] = 15;
			mapArray[houseX + 2][houseY + 1] = 16;

			mapArray[houseX + 1][houseY + 2] = 1; //the tile in front of the door have to be gras tile
			
		} 
		if(houseCount == 2) {
			while(true) {
				int house2X = (1 + rGen.nextInt(26)); //numbers from 1-27 because the house has 3 tiles width
				int house2Y = (1 + rGen.nextInt(25)); //numbers from 1-26 because the house has 3 tiles height and the house should be reachable from bottom
				//if all tiles where the house should stand are free go ahead
				if(mapArray[house2X][house2Y] == -1 && mapArray[house2X + 1][house2Y] == -1 
						&& mapArray[house2X + 2][house2Y] == -1 && mapArray[house2X][house2Y + 1] == -1 
						&& mapArray[house2X + 1][house2Y + 1] == -1 && mapArray[house2X + 2][house2Y + 1] == -1) {
					
					//if the house should stand on one side check if there is a spawn or transition tile
					if((mapArray[0][house2Y] != 13) && (mapArray[0][house2Y + 1] != 13) && (mapArray[0][house2Y + 2] != 13) 
							&& (mapArray[0][house2Y] != 23) && (mapArray[0][house2Y + 1] != 23) && (mapArray[0][house2Y + 2] != 23)) {
						if((mapArray[house2X][0] != 13) && (mapArray[house2X + 1][0] != 13) && (mapArray[house2X][0] != 23) && (mapArray[house2X + 1][0] != 23)) {
							if((mapArray[house2X][29] != 13) && (mapArray[house2X + 1][29] != 13) && (mapArray[house2X][29] != 23) && (mapArray[house2X + 1][29] != 23)) {
								
								//all tiles are clear so set the house tiles
								mapArray[house2X][house2Y] = 4;
								mapArray[house2X + 1][house2Y] = 5;
								mapArray[house2X + 2][house2Y] = 6;
								mapArray[house2X][house2Y + 1] = 14;
								mapArray[house2X + 1][house2Y + 1] = 15;
								mapArray[house2X + 2][house2Y + 1] = 16;
								
								mapArray[house2X + 1][house2Y + 2] = 1; 
								break;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Set the big trees at the map. There can be between 10 to 18 trees.
	 */
	private void setBigTree() {
		int treeCount = (10 + rGen.nextInt(9)); //How many trees are at the map
		
		for(int i = 0; i <= treeCount; i++) {
			while(true) {
				int treeX = (1 + rGen.nextInt(27)); //numbers from 1-27 because the tree is 2 tile wide
				int treeY = (1 + rGen.nextInt(27)); //numbers from 1-27 because the tree is 2 tiles high
				
				if(mapArray[treeX][treeY] == -1 && mapArray[treeX + 1][treeY] == -1 
						&& mapArray[treeX][treeY + 1] == -1 && mapArray[treeX + 1][treeY + 1] == -1) {
					mapArray[treeX][treeY] = 11;
					mapArray[treeX + 1][treeY] = 12;
					mapArray[treeX][treeY + 1] = 21;
					mapArray[treeX + 1][treeY + 1] = 22;
					break;
				}
			}
		}
	}

	/**
	 * Set the stumps. There can be 15 to 30 stumps in a map.
	 */
	private void setTreeStump() {
		int treeStumpCount = (15 + rGen.nextInt(16)); //How many stumps are at the map
		for(int i = 0; i <= treeStumpCount; i++) {
			while(true) {
				int treeStumpX = (1 + rGen.nextInt(28));
				int treeStumpY = (1 + rGen.nextInt(28));
				if(mapArray[treeStumpX][treeStumpY] == -1) {
					if(rGen.getBoolean(0.2)) {
						mapArray[treeStumpX][treeStumpY] = 26;
					} else {
						mapArray[treeStumpX][treeStumpY] = 3;						
					}
					break;
				}
			}
		}
	}
	
	/**
	 * Set the bushes. There can be between 15 to 30 bushes.
	 */
	private void setBush() {
		int bushCount = (15 + rGen.nextInt(16)); //How many bushes are at the map
		for(int i = 0; i <= bushCount; i++) {
			while(true) {
				int bushX = (1 + rGen.nextInt(28));
				int bushY = (1 + rGen.nextInt(28));
				
				if(mapArray[bushX][bushY] == -1) {
					mapArray[bushX][bushY] = 2;
					break;
				}
			}
		}
	}
	
	/**
	 * Fill all tiles that are not blocked with gras or sand.
	 */
	private void setGras() {
		for(int y = 0; y < 30; y++) {
			for(int x = 0; x < 30; x++) {
				if(mapArray[y][x] == -1) {
					if(rGen.getBoolean(0.03)) {
						mapArray[y][x] = 24;
					} else if(rGen.getBoolean(0.05)) {
						mapArray[y][x] = 25;
					} else {
						mapArray[y][x] = 1;
					}
				}
			}
		}
	}

	/**
	 * Check if there are tiles that are not reachable for the sprite and fill it with a bush.
	 * It only works if the gap is one or two tiles wide.
	 */
	private void checkForUnreachableTiles() {
		//TODO nur setzen wenns nich genau vorm eingang ist...
		int countX;
		int countY;

		for(int y = 1; y < 29; y++) {
			for(int x = 1; x < 29; x++) {
				countX = x;
				countY = y;
				// if the gap is one tile wide
				if((mapArray[countY][countX] == 1) || (mapArray[countY][countX] == 24) || (mapArray[countY][countX] == 25)) {
					
					if(((mapArray[countY - 1][countX] != 1) && (mapArray[countY - 1][countX] != 24) && (mapArray[countY - 1][countX] != 25)) 
							&& ((mapArray[countY + 1][countX] != 1) && (mapArray[countY + 1][countX] != 24) && (mapArray[countY + 1][countX] != 25)) 
							&& ((mapArray[countY][countX - 1] != 1) && (mapArray[countY][countX - 1] != 24) && (mapArray[countY][countX - 1] != 25)) 
							&& ((mapArray[countY][countX + 1] != 1) && (mapArray[countY][countX + 1] != 24) && (mapArray[countY][countX + 1] != 25))) {
						
						mapArray[countY][countX] = 2;
					//if the gap is two tiles wide the tiles can be beside or above each other.
					} else {
						if((mapArray[countY - 1][countX] == 1) || (mapArray[countY - 1][countX] == 24) || (mapArray[countY - 1][countX] == 25)) {
														
							if(((mapArray[countY - 2][countX] != 1) && (mapArray[countY - 2][countX] != 24) && (mapArray[countY - 2][countX] != 25)) 
									&& ((mapArray[countY + 1][countX] != 1) && (mapArray[countY + 1][countX] != 24) && (mapArray[countY + 1][countX] != 25))
									&& ((mapArray[countY][countX - 1] != 1) && (mapArray[countY][countX - 1] != 24) && (mapArray[countY][countX - 1] != 25))
									&& ((mapArray[countY][countX + 1] != 1) && (mapArray[countY][countX + 1] != 24) && (mapArray[countY][countX + 1] != 25))
									&& ((mapArray[countY - 1][countX - 1] != 1) && (mapArray[countY - 1][countX - 1] != 24) && (mapArray[countY - 1][countX - 1] != 25)) 
									&& ((mapArray[countY - 1][countX + 1] != 1) && (mapArray[countY - 1][countX + 1] != 24) && (mapArray[countY - 1][countX + 1] != 25))) {
							
								mapArray[countY][countX] = 2;
							}
						} 
						if((mapArray[countY + 1][countX] == 1) || (mapArray[countY + 1][countX] == 24) || (mapArray[countY + 1][countX] == 25))  {
							
							if(((mapArray[countY + 2][countX] != 1) && (mapArray[countY + 2][countX] != 24) && (mapArray[countY + 2][countX] != 25))
									&& ((mapArray[countY - 1][countX] != 1) && (mapArray[countY - 1][countX] != 24) && (mapArray[countY - 1][countX] != 25))
									
									&& ((mapArray[countY][countX - 1] != 1) && (mapArray[countY][countX - 1] != 24) && (mapArray[countY][countX - 1] != 25)) 
									&& ((mapArray[countY][countX + 1] != 1) && (mapArray[countY][countX + 1] != 24) && (mapArray[countY][countX + 1] != 25))
									
									&& ((mapArray[countY + 1][countX - 1] != 1) && (mapArray[countY + 1][countX - 1] != 24) && (mapArray[countY + 1][countX - 1] != 25)) 
									&& ((mapArray[countY + 1][countX + 1] != 1) && (mapArray[countY + 1][countX + 1] != 24) && (mapArray[countY + 1][countX + 1] != 25))) {
							
								mapArray[countY][countX] = 2;
							}
						}
						if((mapArray[countY][countX - 1] == 1) || (mapArray[countY][countX - 1] == 24) || (mapArray[countY][countX - 1] == 25)) {
							
							if(((mapArray[countY][countX - 2] != 1) && (mapArray[countY][countX - 2] != 24) && (mapArray[countY][countX - 2] != 25)) 
									&& ((mapArray[countY][countX + 1] != 1) && (mapArray[countY][countX + 1] != 24) && (mapArray[countY][countX + 1] != 25)) 
									
									&& ((mapArray[countY + 1][countX] != 1) && (mapArray[countY + 1][countX] != 24) && (mapArray[countY + 1][countX] != 25)) 
									&& ((mapArray[countY - 1][countX] != 1) && (mapArray[countY - 1][countX] != 24) && (mapArray[countY - 1][countX] != 25)) 
									
									&& ((mapArray[countY + 1][countX - 1] != 1) && (mapArray[countY + 1][countX - 1] != 24) && (mapArray[countY + 1][countX - 1] != 25)) 
									&& ((mapArray[countY - 1][countX - 1] != 1) && (mapArray[countY - 1][countX - 1] != 24) && (mapArray[countY - 1][countX - 1] != 25))) {
							
								mapArray[countY][countX] = 2;
							}
						}
						if((mapArray[countY][countX + 1] == 1) || (mapArray[countY][countX + 1] == 24) || (mapArray[countY][countX + 1] == 25)) {
							
							if(((mapArray[countY][countX + 2] != 1) && (mapArray[countY][countX + 2] != 24) && (mapArray[countY][countX + 2] != 25)) 
									&& ((mapArray[countY][countX - 1] != 1) && (mapArray[countY][countX - 1] != 24) && (mapArray[countY][countX - 1] != 25)) 

									&& ((mapArray[countY + 1][countX] != 1) && (mapArray[countY + 1][countX] != 24) && (mapArray[countY + 1][countX] != 25)) 
									&& ((mapArray[countY - 1][countX] != 1) && (mapArray[countY - 1][countX] != 24) && (mapArray[countY - 1][countX] != 25)) 
									
									&& ((mapArray[countY + 1][countX + 1] != 1) && (mapArray[countY + 1][countX + 1] != 24) && (mapArray[countY + 1][countX + 1] != 25)) 
									&& ((mapArray[countY - 1][countX + 1] != 1) && (mapArray[countY - 1][countX + 1] != 24) && (mapArray[countY - 1][countX + 1] != 25))) {
							
								mapArray[countY][countX] = 2;
							}
						}
					}
				}
			}
		}
	}

	public int getLastSpawnSide() {
		return spawnExitSide;
	}
}