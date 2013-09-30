package de.apixelstory.scene;

import java.util.ArrayList;

/**
 * This class manages the scenes of the game.
 */
public class SceneManager {
	
	/** a list with all scenes in the game*/
	private ArrayList<OurScene> sceneList;
	
	/**
	 * the constructor
	 */
	public SceneManager(){
		sceneList = new ArrayList<OurScene>();
	}
	
	/**
	 * add a scene to the scenemanager
	 * @param scene the scene to add
	 */
	public void addScene(OurScene scene){		
		sceneList.add(scene);
	}
	
	/**
	 * get a scene at a spezified indes in the list
	 * @param level the index of the level for which the scene should be returned
	 * @return the scene of the level that was requested
	 */
	public OurScene getScene(int level){
		return sceneList.get(level-1);
	}

}
