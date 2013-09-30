package de.apixelstory.scene;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.res.AssetManager;
import de.apixelstory.game.Controller;
import de.apixelstory.quest.GetItemQuest;
import de.apixelstory.quest.KillQuest;
import de.apixelstory.quest.Quest;

/**
 * The scene to show the quests in the game
 */
public class QuestScene extends Scene {
	/** the id of the scene*/
	private int id;
	/** the map for the background of the map*/
	private TMXTiledMap tmxMap;
	
	/**
	 * the constructor
	 * @param id the id of the scene
	 * @param controller 
	 * @param assetManager
	 * @param engine
	 * @param vertexBufferObjectManager
	 */
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
	
	/**
	 * get the tmx map
	 * @return the tmx map
	 */
	public TMXTiledMap getMap() {
		return tmxMap;
	}
	
	/**
	 * get the id of the scene
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	/**
	 * get the quest titel
	 * @param activeQuests all active quests
	 * @param index the index of the actual quest
	 * @return the name of the actual quest
	 */
	public String getQuestTitel(ArrayList<Quest> activeQuests, int index) {
		return activeQuests.get(index).getName();
	}
	
	/**
	 * get the task text of the quest
	 * @param quest the actual quest 
	 * @return the task text
	 */
	public String getTask(Quest quest) {
		return quest.getShortText();
	}

	/**
	 * get the text for the progress that where made in a quest
	 * @param quest the actual quest 
	 * @return the progress text if there is no progress in a quest return an empty string.
	 */
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
