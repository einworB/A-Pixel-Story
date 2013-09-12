package de.projectrpg.save;

import java.util.ArrayList;

public class LevelLoader {
	
	
	
	private String mapName;
	private ArrayList<OpponentObjects> opponentList = new ArrayList<LevelLoader.OpponentObjects>();
	private ArrayList<NpcObjects> npcList = new ArrayList<LevelLoader.NpcObjects>();
	private int level;
	
	public LevelLoader(int level) {
		this.level = level;
	}

	public void setMapName(String mapName){
		this.mapName = mapName;
	}
	
	public String getMapName(){
		return mapName;
	}
	
	public void addOpponent(float positionX, float positionY, int level, boolean isEpic, int direction, float health){
		opponentList.add(new OpponentObjects(positionX, positionY, level, isEpic, direction, health));
	}
	public ArrayList<OpponentObjects> getOpponentList(){
		return opponentList;
	}

	public void addNpc(float positionX, float positionY, int id, int level, int direction){
		npcList.add(new NpcObjects(positionX, positionY, id, level, direction));
	}
	
	public ArrayList<NpcObjects> getNpcList(){
		return npcList;
	}


	public int getLevel(){
		return level;
	}
	
	

	
	public class NpcObjects {
		private float positionX;
		private float positionY;
		private int id;
		private int level;
		private int direction;
		
		public NpcObjects(float positionX, float positionY, int id, int level, int direction) {
			this.positionX = positionX;
			this.positionY = positionY;
			this.id = id;
			this.level = level;
			this.direction = direction;
		}
		
		public float getPositionX() {
			return positionX;
		}
		public float getPositionY() {
			return positionY;
		}
		
		public int getID() {
			return id;
		}
		
		public int getLevel() {
			return level;
		}
		
		public int getDirection() {
			return direction;
		}
	}
	
	public class OpponentObjects{
		
		
		private float positionX;
		private float positionY;
		private int level;
		private boolean isEpic;
		private int direction;
		private float health;
		
		public OpponentObjects(float positionX, float positionY, int level, boolean isEpic, int direction, float health){
			this.positionX = positionX;
			this.positionY = positionY;
			this.level = level;
			this.isEpic = isEpic;
			this.direction = direction;
			this.health = health;
		}
		
		public float getPositionX(){
			return positionX;
		}
		
		public float getPositionY(){
			return positionY;
		}
		
		public int getLevel(){
			return level;
		}
		
		public boolean getEpic(){
			return isEpic;
		}
		
		public int getDirection() {
			return direction;
		}
		
		public float getHealth() {
			return health;
		}
	}
}
