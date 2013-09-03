package de.projectrpg.sprites;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Opponent extends FightingSprite {
	
	private boolean isEpic;

	public Opponent(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int level, boolean isEpic){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, level);
		this.isEpic = isEpic;
	}
	
	public boolean isEpic(){
		return isEpic;
	}
}
