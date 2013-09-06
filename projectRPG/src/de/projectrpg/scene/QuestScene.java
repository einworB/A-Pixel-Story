package de.projectrpg.scene;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.res.AssetManager;
import de.projectrpg.game.Controller;
import de.projectrpg.quest.Quest;
import de.projectrpg.quest.QuestManager;

public class QuestScene extends Scene {
	private int id;
	TMXMapLoader loader;
	TMXTiledMap tmxMap;
	
	public QuestScene(int id, Controller controller, AssetManager assetManager, Engine engine, VertexBufferObjectManager vertexBufferObjectManager) {
		super();
		this.id = id;
		loader = new TMXMapLoader(controller);
		TMXLoader loader = new TMXLoader(assetManager, engine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, vertexBufferObjectManager);
		try {
			tmxMap = loader.loadFromAsset("tmx/gras.tmx");
		} catch (TMXLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public TMXTiledMap getMap() {
		return tmxMap;
	}
	
	public int getID() {
		return id;
	}

	public String getQuestTitel(ArrayList<Quest> activeQuests, int index) {
		return activeQuests.get(index).getName();
	}
	
	public String getTalkToTask(String npcName) {
		return "Suche jemanden der mit dir spricht!";
		//.        ..Sprich mit " + npcName + 
	}
	
	public String getKillTask(int count) {
		return "Bringe " + count + " von diesen Kreaturen um!";
	}
	
	public String getItemTask(String itemType, int count) {
		return "Bringe mir " + count + " von diesen " + itemType + "!";
	}
}
