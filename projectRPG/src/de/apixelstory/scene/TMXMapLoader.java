package de.apixelstory.scene;

import java.io.InputStream;
import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.content.res.AssetManager;
import android.util.Log;

/**
 * This class loads the map form an input stream
 */
public class TMXMapLoader {
	
	/** The tmx map*/ 
	private TMXTiledMap tmxMap;
	/** A hashmap with all spawns in the game*/
	private HashMap<String, float[]> spawns;

	/**
	 * Load the tmx map. If there is a tile with property transition add it to the spawn hashmap.
	 * @param assetManager
	 * @param mEngine
	 * @param vertexBufferObjectManager
	 * @param inputStream the input stream where the map should be loaded from
	 * @return the tmx map
	 */
	public TMXTiledMap loadTMXMap(AssetManager assetManager, Engine mEngine, VertexBufferObjectManager vertexBufferObjectManager, InputStream inputStream){
		spawns = new HashMap<String, float[]>();
		/* load the tmx tiled map */
		try {
			final TMXLoader tmxLoader = new TMXLoader(assetManager, mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, vertexBufferObjectManager, new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(TMXTiledMap tmxTiledMap, TMXLayer tmxLayer, TMXTile tmxTile, TMXProperties<TMXTileProperty> tmxTileProperties) {
					for(int i=0; i<tmxTileProperties.size(); i++){
						if(tmxTileProperties.get(i).getName().contentEquals("TRANSITION")){
							float spawn[] = new float[2];
							spawn[0] = (float) tmxTile.getTileX();
							spawn[1] = (float) tmxTile.getTileY();
							Log.d("RPG", "x "+spawn[0]+", y "+spawn[1]);
							spawns.put(tmxTileProperties.get(i).getValue(), spawn);
						}
					}
				}
			});
			Log.d("RPG", "InputStream: "+inputStream.toString());
			this.tmxMap = tmxLoader.load(inputStream);
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}
		return tmxMap;
	}
	
	/**
	 * get all spawns of the game
	 * @return all spawns of the game
	 */
	public HashMap<String, float[]> getSpawn(){
		Log.d("RPG", "spawns: "+spawns);
		return spawns;
	}
}
