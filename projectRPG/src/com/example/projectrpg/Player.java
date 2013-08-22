package com.example.projectrpg;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Player extends FightingSprite {

//	private static final int ARMOR_HEAD = 0;
//	private static final int ARMOR_BODY = 1;
//	private static final int ARMOR_LEGS = 2;
//	private static final int ARMOR_HANDS = 3;
//	private static final int ARMOR_SHOES = 4;
	
	
	
	public Player(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
	}

}
