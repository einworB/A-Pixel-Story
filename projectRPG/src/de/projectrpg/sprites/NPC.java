package de.projectrpg.sprites;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class NPC extends AnimatedSprite {
	
	private int ID;

	
	public NPC(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int ID){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}
	
	public String getName() {
		return "Hans"; //TODO richtigen Namen holen!!!
	}
}
