package de.apixelstory.sprites;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.apixelstory.game.Controller;

/**
 * A NPC is a person in the game that is peaceful and cannot be fight.
 */
public class NPC extends AnimatedSprite {
	
	/** the id of the NPC*/
	private int ID;
	/** the controller*/
	private Controller controller;
	/** the name of the npc*/
	private String name;

	/**
	 * the constructor
	 * @param pX the x position of the npc
	 * @param pY the y position of the npc
	 * @param pWidth the width of the npc
	 * @param pHeight the height of the npc
	 * @param pTiledTextureRegion
	 * @param pVertexBufferObjectManager
	 * @param ID the id of the npc
	 */
	public NPC(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int ID){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		this.ID = ID;
		this.controller = Controller.getInstance();
		this.name = controller.getNPCName(ID);
	}

	/**
	 * get the id of the npc
	 * @return the id
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * get the name of the npc
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
