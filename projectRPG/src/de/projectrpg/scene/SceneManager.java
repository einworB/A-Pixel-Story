package de.projectrpg.scene;

import java.util.ArrayList;

public class SceneManager {
	
	private ArrayList<OurScene> sceneList;
	
	public SceneManager(){
		sceneList = new ArrayList<OurScene>();
	}
	
	public void addScene(OurScene scene){		
		sceneList.add(scene);
	}
	
	
	public OurScene getScene(int level){
		return sceneList.get(level-1);
	}

}
