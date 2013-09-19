package de.projectrpg.sprites;

import java.util.ArrayList;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.projectrpg.database.Armor;
import de.projectrpg.database.Item;
import de.projectrpg.database.Weapon;

public class Player extends FightingSprite {

	/** the equipped weapon*/
	private Weapon equippedWeapon;
	/** the equipped armor of the player
	 * Index 0 = Head, 1 = Body, 2 = Hands, 3 = Legs, 4 = Feet */
	private Armor[] equippedArmor;
	/** the inventory of the player*/
	private ArrayList<Item> inventory;
	/** the gold value of the player*/
	private int gold;
	/** the experience of the player*/
	private int EXP = 0;
	/** the maximal exp value the player can have in this level*/
	private int maxEXP = 0;
	
	/**
	 * the constructor
	 * @param pX the x position of the player
	 * @param pY the y position of the player
	 * @param pWidth the width of the player
	 * @param pHeight the height of the player
	 * @param pTiledTextureRegion
	 * @param pVertexBufferObjectManager
	 * @param level the level of the player
	 */
	public Player(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int level){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, level);
		equippedArmor = new Armor[5];
		inventory = new ArrayList<Item>();
		gold = 20;
	}
	
	/**
	 * Add an item to the armor of the player
	 * @param armor the armor
	 * @return true if it could be added else return false.
	 */
	public boolean addArmor(Armor armor){
		if(armor.getLevelNeeded()<=level){
			equippedArmor[armor.getType()] = armor;
			return true;
		} else return false;
	}
	
	/**
	 * Set the weapon to the given one.
	 * @param weapon the new weapon
	 */
	public void setWeapon(Weapon weapon){
		equippedWeapon = weapon;
	}
	
	/**
	 * get the equipped weapon
	 * @return the weapon
	 */
	public Weapon getEquippedWeapon(){
		return equippedWeapon;
	}
	
	/**
	 * get the equipped armor
	 * @return the armor
	 */
	public Armor[] getArmor(){
		return equippedArmor.clone();
	}

	/**
	 * get the attack value
	 */
	@Override
	public int getAttackValue() {
		int attackValue = level*20;
		if(equippedWeapon != null) attackValue += equippedWeapon.getAttackValue();
		return attackValue;
	}
	
	/**
	 * get the inventory of the player
	 * @return the inventory
	 */
	public ArrayList<Item> getInventory(){
		return inventory;
	}
	
	/**
	 * add an item to the inventory
	 * @param item the item to add
	 */
	public void addItemToInventory(Item item){
		inventory.add(item);
	}

	/**
	 * remove an item from the inventory
	 * @param item the item to remove
	 */
	public void removeItemFromInventory(Item item) {
		inventory.remove(item);
	}

	/**
	 * remove the equipped armor
	 * @param armor the armor to remove
	 */
	public void removeEquippedArmor(Armor armor) {
		equippedArmor[armor.getType()] = null;
	}
	
	/**
	 * remove the equipped armor by index
	 * @param i the armor to remove
	 */
	public void removeEquippedArmor(int i) {
		equippedArmor[i] = null;
	}

	/**
	 * Heal the player. If the health plus the given value is higher than the maximal health value of the player, 
	 * set the actual health value to the maximal value. 
	 * @param heal the value to add to the actual health value
	 */
	public void heal(int heal) {
		if((health + heal) <= maxHealth) {
			health += heal;
		} else health = maxHealth;
	}

	/**
	 * remove the equipped weapon.
	 */
	public void removeEquippedWeapon(Weapon weapon) {
		equippedWeapon = null; //TODO muss keine waffe übergeben werden. muss aber auch im inventar geändert werden.
	}

	/**
	 * add the given experience points to the actual value.
	 * If the maximal value is lower than the actual value plus the given one level up and 
	 * add the points that where to much to the new value.
	 * @param points the points to add
	 */
	public void addEXP(int points) {
		EXP += points;
		maxEXP = level * 500;
		if(EXP >= maxEXP) {
			levelUp();
			EXP = EXP - maxEXP;
		}
	}
	
	/**
	 * get the percentage value of the exp value
	 * @return the percentage value
	 */
	public int getEXPPercentage() {
		maxEXP = level * 500;
		int percent = (EXP * 100) / maxEXP;
		return percent;
	}
	
	/**
	 * get the exp value
	 * @return the exp value
	 */
	public int getEXP() {
		return EXP;
	}

	/**
	 * set the inventory to the given list
	 * @param tempInventoryList the saved inventory list
	 */
	public void setInventory(ArrayList<Item> tempInventoryList) {
		inventory = tempInventoryList;
	}
	
	/**
	 * get the gold value
	 * @return the gold value
	 */
	public int getGold(){
		return gold;
	}
	
	/**
	 * change the gold value of  the player. This can be a positive or negative change
	 * @param value the value to change.
	 */
	public void changeGold(int value){
		gold += value;
	}
	
	/**
	 * Get the defense value. This value can be calculated by adding the defense values of the equipped armors.
	 * @return the defense value
	 */
	public double getDefenseValue(){
		double defenseValue = 0;
		for(int i=0; i<5; i++){
			if(equippedArmor[i]!=null) defenseValue += equippedArmor[i].getDefenseValue(); 
		}
		return defenseValue;
	}
}
