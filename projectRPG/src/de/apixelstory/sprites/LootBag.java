package de.apixelstory.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * A lootbag contains one or more items that the player can collect and use.
 */
public class LootBag extends Sprite {
	
	/** This is the opponent that drops the lootbag*/
	private Opponent lootRoot;
	
	/**
	 * the construtor
	 * @param x the x position of the lootbag
	 * @param y the y position of the lootbag
	 * @param textureRegion
	 * @param vertexBufferObjectManager
	 * @param opponent the opponent that drops the lootbag
	 */
	public LootBag(float x, float y, ITextureRegion textureRegion, final VertexBufferObjectManager vertexBufferObjectManager, Opponent opponent){
		super(x, y, textureRegion, vertexBufferObjectManager);
		
		lootRoot = opponent;
	}
	
	/**
	 * get the index of the loot that the opponent drops
	 * @return the index
	 */
	public int getLoot(){
		return lootRoot.getLevel();
	}
	
	/**
	 * get the opponent that drops the lootbag
	 * @return the opponent
	 */
	public Opponent getLootRoot() {
		return lootRoot;
	}

}
