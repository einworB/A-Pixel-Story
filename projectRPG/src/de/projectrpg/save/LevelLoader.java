package de.projectrpg.save;

import java.util.ArrayList;

/**
 * this are methods and inner classes to get the loaded data for one special level.
 */
public class LevelLoader {
	
	/** the name of the map*/
	private String mapName;
	/** a list with all oppnent data*/
	private ArrayList<OpponentObjects> opponentList = new ArrayList<LevelLoader.OpponentObjects>();
	/** a list with all npc data*/
	private ArrayList<NpcObjects> npcList = new ArrayList<LevelLoader.NpcObjects>();
	/** the id of the actual level*/
	private int level;
	
	/**
	 * the constructor
	 * @param level the id of the actual level
	 */
	public LevelLoader(int level) {
		this.level = level;
	}

	/** 
	 * Set the map name
	 * @param mapName the name of the map
	 */
	public void setMapName(String mapName){
		this.mapName = mapName;
	}
	
	/**
	 * get the map name
	 * @return the map name
	 */
	public String getMapName(){
		return mapName;
	}
	
	/**
	 * Add an opponent to the list
	 * @param positionX the x position of the opponent
	 * @param positionY the y position of the opponent
	 * @param level the level of the opponent
	 * @param isEpic the boolean if the opponent is epic
	 * @param direction the viewing direction of the opponent
	 * @param health the health of the opponent
	 */
	public void addOpponent(float positionX, float positionY, int level, boolean isEpic, int direction, float health){
		opponentList.add(new OpponentObjects(positionX, positionY, level, isEpic, direction, health));
	}
	
	/**
	 * get the list with opponents
	 * @return the opponent list
	 */
	public ArrayList<OpponentObjects> getOpponentList(){
		return opponentList;
	}

	/**
	 * Add a npc to the npc list
	 * @param positionX the x position of the npc
	 * @param positionY the y position of the npc
	 * @param id the id of the npc
	 * @param level the level of the npc
	 * @param direction the viewing direction of the npc
	 */
	public void addNpc(float positionX, float positionY, int id, int level, int direction){
		npcList.add(new NpcObjects(positionX, positionY, id, level, direction));
	}
	
	/**
	 * get the list with npcs
	 * @return the npc list
	 */
	public ArrayList<NpcObjects> getNpcList(){
		return npcList;
	}

	/**
	 * get the actual level id
	 * @return the id of the level
	 */
	public int getLevel(){
		return level;
	}
	
	

	/**
	 * Inner class. A Npc object with all data to reload a npc on a map.
	 */
	public static class NpcObjects {
		/** the x position of the npc*/
		private float positionX;
		/** the y position of the npc*/
		private float positionY;
		/** the id of the npc*/
		private int id;
		/** the level of the npc*/
		private int level;
		/** the viewing direction of the npc*/
		private int direction;
		
		/**
		 * The constructor 
		 * @param positionX the x position of the npc
		 * @param positionY the y position of the npc
		 * @param id the id of the npc
		 * @param level the level of the npc
		 * @param direction the viewing direction of the npc
		 */
		public NpcObjects(float positionX, float positionY, int id, int level, int direction) {
			this.positionX = positionX;
			this.positionY = positionY;
			this.id = id;
			this.level = level;
			this.direction = direction;
		}
		
		/**
		 * get the x position
		 * @return the x position
		 */
		public float getPositionX() {
			return positionX;
		}
		
		/**
		 * get the y position
		 * @return the y position
		 */
		public float getPositionY() {
			return positionY;
		}
		
		/**
		 * get the id of the npc
		 * @return the id of the npc
		 */
		public int getID() {
			return id;
		}
		
		/**
		 * get the level of the npc
		 * @return the level of the npc
		 */
		public int getLevel() {
			return level;
		}
		
		/**
		 * get the viewing direction of the npc
		 * @return the viewing direction of the npc
		 */
		public int getDirection() {
			return direction;
		}
	}
	
	/**
	 * Inner class. A Opponent object with all data to reload a Opponent on a map.
	 */
	public static class OpponentObjects{
		/** the x position of the opponent*/
		private float positionX;
		/** the y position of the opponent*/
		private float positionY;
		/** the level of the opponent*/
		private int level;
		/** the viewing direction of the opponent*/
		private int direction;
		/** true if it is a epic opponent*/
		private boolean isEpic;
		/** the health of the opponent*/
		private float health;
		
		/**
		 * The constructor
		 * @param positionX the x position of the opponent
		 * @param positionY the y position of the opponent
		 * @param level the level of the opponent
		 * @param isEpic true if it is a epic opponent
		 * @param direction the viewing direction of the opponent
		 * @param health the health of the opponent
		 */
		public OpponentObjects(float positionX, float positionY, int level, boolean isEpic, int direction, float health){
			this.positionX = positionX;
			this.positionY = positionY;
			this.level = level;
			this.isEpic = isEpic;
			this.direction = direction;
			this.health = health;
		}
		
		/**
		 * get the x position of the opponent
		 * @return the x position of the opponent
		 */
		public float getPositionX(){
			return positionX;
		}
		
		/**
		 * get the y position of the opponent
		 * @return the y position of the opponent
		 */
		public float getPositionY(){
			return positionY;
		}
		
		/**
		 * get the level of the opponent
		 * @return the level of the opponent
		 */
		public int getLevel(){
			return level;
		}
		
		/**
		 * get the boolean if it is an epic opponent
		 * @return the boolean if it is an epic opponent
		 */
		public boolean getEpic(){
			return isEpic;
		}
		
		/**
		 * get the viewing direction of the opponent
		 * @return the viewing direction of the opponent
		 */
		public int getDirection() {
			return direction;
		}
		
		/**
		 * get the health of the opponent
		 * @return the health of the opponent
		 */
		public float getHealth() {
			return health;
		}
	}
}
