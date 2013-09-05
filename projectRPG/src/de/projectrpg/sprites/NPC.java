package de.projectrpg.sprites;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.projectrpg.game.Controller;

public class NPC extends AnimatedSprite {
	
	private int ID;
	private Controller controller;
	private String name;

	
	public NPC(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int ID){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		this.ID = ID;
		this.controller = Controller.getInstance();
		this.name = controller.getNPCName(ID);
	}

	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
}
