package de.projectrpg.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class LootBag extends Sprite {
	
	private Opponent lootRoot;
	
	public LootBag(float x, float y, ITextureRegion textureRegion, final VertexBufferObjectManager vertexBufferObjectManager, Opponent opponent){
		super(x, y, textureRegion, vertexBufferObjectManager);
		
		lootRoot = opponent;
	}
	
	public int getLoot(){
		return lootRoot.getLevel();
	}
	
	public Opponent getLootRoot() {
		return lootRoot;
	}

}
