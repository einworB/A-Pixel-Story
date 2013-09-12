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
import de.projectrpg.quest.GetItemQuest;
import de.projectrpg.quest.KillQuest;
import de.projectrpg.quest.Quest;

public class QuestScene extends Scene {
	private int id;
	TMXTiledMap tmxMap;
	
	public QuestScene(int id, Controller controller, AssetManager assetManager, Engine engine, VertexBufferObjectManager vertexBufferObjectManager) {
		super();
		this.id = id;
		TMXLoader loader = new TMXLoader(assetManager, engine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, vertexBufferObjectManager);
		try {
			tmxMap = loader.loadFromAsset("tmx/gras.tmx");
		} catch (TMXLoadException e) {
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
	
	public String getTask(Quest quest) {
		return quest.getShortText();
	}

	public String getProgress(Quest quest) {
		if((quest instanceof KillQuest)) {
			return ((KillQuest)quest).getAlreadyKilled() + " von " + ((KillQuest)quest).getKillCount();
		}
		if((quest instanceof GetItemQuest)) {
			return ((GetItemQuest)quest).getAlreadyFound() + " von " + ((GetItemQuest)quest).getItemCount();
		}
		return "";
	}
}
