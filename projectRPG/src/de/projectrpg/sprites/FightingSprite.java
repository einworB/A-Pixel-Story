package de.projectrpg.sprites;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Parent class for the player and all opponents.
 * Holds all values that are used generally for Sprites that are able to fight
 */
public class FightingSprite extends AnimatedSprite {
	
	/** the level of the sprite*/
	protected int level;
	/** the health of the sprite*/
	protected double health;
	/** the maximal health value pf this sprite*/
	protected double maxHealth;
	/** the agility of the sprite*/
	private int agility;
	/** the luck of the sprite*/
	private int luck;
	/** the concentration of the sprite*/
	private double concentration;
	
	/**
	 * the constructor
	 * @param pX the x position of this sprite
	 * @param pY the y position of this sprite
	 * @param pWidth the width of the sprite
	 * @param pHeight the height of the sprite
	 * @param pTiledTextureRegion
	 * @param pVertexBufferObjectManager
	 * @param level the level of the sprite
	 */
	public FightingSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int level){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		this.level = level;
		health = 100;
		maxHealth = health;
		agility = level;
		luck = level;
		concentration = 0.5 + ((double)level)/10;
	}
	
	/**
	 * get the level of the sprite
	 * @return the level
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * level up the sprite. 
	 * To level up a sprite all values that have to do with the level of the sprite have to be reseted.
	 */
	public void levelUp(){
		concentration += 0.1;
		agility++;
		luck++;
		level++;
	}
	
	/**
	 * get the health value of the sprite
	 * @return the health value
	 */
	public double getHealth(){
		return health;
	}
	
	/**
	 * change the health value by the given value. This can be a negative or positive value.
	 * @param d the value to change
	 */
	public void changeHealth(double d){
		health += d;
	}
	
	/**
	 * get the attack value
	 * @return the attack value
	 */
	public int getAttackValue(){
		int attackValue = level*2;
		return attackValue;		
	}
	
	/**
	 * Get the agility.
	 * @return the agility
	 */
	public int getAgility(){
		return agility;
	}
	
	/**
	 * Get the luck value.
	 * @return the luck value
	 */
	public int getLuck(){
		return luck;
	}
	
	/**
	 * Get the concentration value.
	 * @return the concentration value
	 */
	public double getConcentration(){
		return concentration;
	}
}
