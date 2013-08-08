package com.example.projectrpg;

import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.scene.Scene;

public class Algorithm {

	public Algorithm(int startPositionX, int startPositionY, int endPositionX, int endPositionY) {
		
	}
	
	public static Path calculatePath(float startX, float startY, float endX, float endY, Scene scene) {
		final Path path = new Path(2).to(startX, startY).to(endX, endY);
		return path;
	}
}
