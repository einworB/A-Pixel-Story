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
	
	public void addOpponent(float positionX, float positionY, int level, boolean isEpic){
		opponentList.add(new OpponentObjects(positionX, positionY, level, isEpic));
	}
	public ArrayList<OpponentObjects> getOpponentList(){
		return opponentList;
	}

	public void addNpc(float positionX, float positionY, int id){
		npcList.add(new NpcObjects(positionX, positionY, id));
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
		
		public NpcObjects(float positionX, float positionY, int id) {
			this.positionX = positionX;
			this.positionY = positionY;
			this.id = id;
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
	}
	
	public class OpponentObjects{
		
		
		private float positionX;
		private float positionY;
		private int level;
		private boolean isEpic;
		
		public OpponentObjects(float positionX, float positionY, int level, boolean isEpic){
			this.positionX = positionX;
			this.positionY = positionY;
			this.level = level;
			this.isEpic = isEpic;
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
	}
}
