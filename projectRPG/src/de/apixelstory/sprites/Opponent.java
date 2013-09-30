package de.apixelstory.sprites;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * An Opponent is a creature in the game that isn't peaceful.
 */
public class Opponent extends FightingSprite {
	
	/** if this is true this opponent is an epic opponent*/ 
	private boolean isEpic;

	/**
	 * the constructor
	 * @param pX the x position of the opponent
	 * @param pY the y position of the opponent
	 * @param pWidth the width of the opponent
	 * @param pHeight the height of the opponent
	 * @param pTiledTextureRegion
	 * @param pVertexBufferObjectManager
	 * @param level the level of the opponent
	 * @param isEpic boolean if this is an epic enemy
	 */
	public Opponent(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int level, boolean isEpic){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, level);
		this.isEpic = isEpic;
	}
	
	/**
	 * get the boolean that shows if this opponent is an epic one
	 * @return true is this opponent is an epic opponent
	 */
	public boolean isEpic(){
		return isEpic;
	}
}
