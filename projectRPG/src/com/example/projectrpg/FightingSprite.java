package com.example.projectrpg;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class FightingSprite extends AnimatedSprite {
	
	private int level;
	private Weapon equippedWeapon;
	private Armor[] equippedArmor;
	private double health;
	private int agility;
	private int luck;
	private double concentration;
	
	public FightingSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);
		level = 1;
		equippedArmor = new Armor[5];
		health = 100;
		agility = level;
		luck = level;
		concentration = 0.5 + level/10;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void levelUp(){
		level++;
	}
	
	public void addArmor(Armor armor){
		if(armor.getLevelNeeded()<=level) equippedArmor[armor.getType()] = armor;
	}
	
	public void setWeapon(Weapon weapon){
		equippedWeapon = weapon;
	}
	
	public double getHealth(){
		return health;
	}
	
	public void changeHealth(double d){
		health += d;
	}
	
	public int getAttackValue(){
		int attackValue = level*2;
		if(equippedWeapon != null) attackValue += equippedWeapon.getAttackValue();
		return attackValue;
		
	}
	
	public int getAgility(){
		return agility;
	}
	
	public int getLuck(){
		return luck;
	}
	
	public double getConcentration(){
		return concentration;
	}
}
