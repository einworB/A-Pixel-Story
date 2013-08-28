package com.example.projectrpg;


public class RandomMapArrayGenerator {
	/** the array with values of the tileset tiles*/
	private int[][] mapArray;
	/** RandomGenerator*/
	private OurRandomGenerator rGen = new OurRandomGenerator();
	
	/**
	 * set the array
	 */
	public RandomMapArrayGenerator() {
		mapArray = new int[30][30];
	}
	
	/**
	 * Set bushes at the sides of the map.
	 * Set the spawn tiles. A spawn tile cannot be at a edge.
	 * Set all other objects that should be at the map, like bushes, trees, cactus..
	 * @param level the actual level
	 * @return the array with the tile values
	 */
	public int[][] generateMapArray(int level) {
		for(int y = 0; y < 30; y++) {
			for(int x = 0; x < 30; x++) {
				if(x == 0 || y == 0 || x == 29 || y == 29) {
					mapArray[y][x] = 2;
				} else {
					mapArray[y][x] = -1;
				}
			}
		}
		//set spawn tiles
		boolean spawnSet = false;
		int sideSpawn = -1;
		int spawntile = -1;
		//only in the first level there is a spawntile
		if(level == 1 && !spawnSet) {
			//select on of the four sides to set the spawntile
			// 0 = top; 1 = left; 2 = right; 3 = bottom;
			sideSpawn = rGen.nextInt(4);
			spawntile = (1 + rGen.nextInt(27)); //the edges cannot be set with spawntiles
			switch(sideSpawn) {
			case 0:
				mapArray[0][spawntile] = 23;
				mapArray[1][spawntile] = 1;
				break;
			case 1:
				mapArray[spawntile][0] = 23;
				mapArray[spawntile][1] = 1;
				break;
			case 2:
				mapArray[29][spawntile] = 23;
				mapArray[28][spawntile] = 1;
				break;
			case 3:
				mapArray[spawntile][29] = 23;
				mapArray[spawntile][28] = 1;
				break;
			}
			spawnSet = true;
		}
		//in every level there is a transitiontile, repeat till the transitiontile is not at the same position as the spawntile
		while(true) {
			int sideTransition = rGen.nextInt(4);
			int transitionTile = (1 + rGen.nextInt(27)); //the edges cannot be set with transitiontiles
			
			if((sideTransition != sideSpawn) && (transitionTile != spawntile)) {
				switch(sideTransition) {
				case 0:
					mapArray[0][transitionTile] = 13;
					mapArray[1][transitionTile] = 1;
					break;
				case 1:
					mapArray[transitionTile][0] = 13;
					mapArray[transitionTile][1] = 1;
					break;
				case 2:
					mapArray[29][transitionTile] = 13;
					mapArray[28][transitionTile] = 1;
					break;
				case 3:
					mapArray[transitionTile][29] = 13;
					mapArray[transitionTile][28] = 1;
					break;
				}
				break;
			}
		}
		
		setHouse();
		setBigTree();
		setTreeStump();
		setBush();
		setGras();
		
		checkForUnreachableTiles();
		
		return mapArray;
	}

	/**
	 * Set a random amount of houses. In a map there can be 0, 1 or 2 houses. 
	 */
	private void setHouse() {
		int houseCount = rGen.nextInt(3);
		int houseX = 0;
		int houseY = 0;
		if(houseCount > 0) {
			houseX = (1 + rGen.nextInt(26)); //numbers from 1-27 because the house has 3 tiles width
			houseY = (1 + rGen.nextInt(25)); //numbers from 1-26 because the house has 3 tiles height and the house should be reachable from bottom
			while(true) {
				if((mapArray[0][houseY] != 13) && (mapArray[0][houseY + 1] != 13) && (mapArray[0][houseY + 2] != 13) 
						&& (mapArray[0][houseY] != 23) && (mapArray[0][houseY + 1] != 23) && (mapArray[0][houseY + 2] != 23)) {
					if((mapArray[houseX][0] != 13) && (mapArray[houseX + 1][0] != 13) && (mapArray[houseX][0] != 23) && (mapArray[houseX + 1][0] != 23)) {
						if((mapArray[houseX][29] != 13) && (mapArray[houseX + 1][29] != 13) && (mapArray[houseX][29] != 23) && (mapArray[houseX + 1][29] != 23)) {

							houseX = (1 + rGen.nextInt(26)); //numbers from 1-27 because the house has 3 tiles width
							houseY = (1 + rGen.nextInt(25)); //numbers from 1-26 because the house has 3 tiles height and the house should be reachable from bottom
							break;
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
				if(mapArray[house2X][house2Y] == -1 && mapArray[house2X + 1][house2Y] == -1 
						&& mapArray[house2X + 2][house2Y] == -1 && mapArray[house2X][house2Y + 1] == -1 
						&& mapArray[house2X + 1][house2Y + 1] == -1 && mapArray[house2X + 2][house2Y + 1] == -1) {
					if((mapArray[0][houseY] != 13) && (mapArray[0][houseY + 1] != 13) && (mapArray[0][houseY + 2] != 13) 
							&& (mapArray[0][houseY] != 23) && (mapArray[0][houseY + 1] != 23) && (mapArray[0][houseY + 2] != 23)) {
						if((mapArray[houseX][0] != 13) && (mapArray[houseX + 1][0] != 13) && (mapArray[houseX][0] != 23) && (mapArray[houseX + 1][0] != 23)) {
							if((mapArray[houseX][29] != 13) && (mapArray[houseX + 1][29] != 13) && (mapArray[houseX][29] != 23) && (mapArray[houseX + 1][29] != 23)) {
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
					mapArray[treeStumpX][treeStumpY] = 3;
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

	private void checkForUnreachableTiles() {

		int countX;
		int countY;

		for(int y = 1; y < 29; y++) {
			for(int x = 1; x < 29; x++) {
				countX = x;
				countY = y;
				if((mapArray[countY][countX] == 1) || (mapArray[countY][countX] == 24) || (mapArray[countY][countX] == 25)) {
					
					if(((mapArray[countY - 1][countX] != 1) && (mapArray[countY - 1][countX] != 24) && (mapArray[countY - 1][countX] != 25)) 
							&& ((mapArray[countY + 1][countX] != 1) && (mapArray[countY + 1][countX] != 24) && (mapArray[countY + 1][countX] != 25)) 
							&& ((mapArray[countY][countX - 1] != 1) && (mapArray[countY][countX - 1] != 24) && (mapArray[countY][countX - 1] != 25)) 
							&& ((mapArray[countY][countX + 1] != 1) && (mapArray[countY][countX + 1] != 24) && (mapArray[countY][countX + 1] != 25))) {
						
						mapArray[countY][countX] = 2;
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
}
