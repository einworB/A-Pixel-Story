package de.projectrpg.sprites;

import java.util.ArrayList;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.projectrpg.database.Armor;
import de.projectrpg.database.Item;
import de.projectrpg.database.Weapon;

public class Player extends FightingSprite {

	private Weapon equippedWeapon;
	/** Index 0 = Kopf, 1 = Oberkörper, 2 = Hände, 3 = Beine, 4 = Füße */
	private Armor[] equippedArmor;
	private ArrayList<Item> inventory;
	private int gold;
	private int EXP = 0;
	private int maxEXP = 0;
	
	
	public Player(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int level){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, level);
		equippedArmor = new Armor[5];
		inventory = new ArrayList<Item>();
		gold = 20;
	}
	
	public boolean addArmor(Armor armor){
		if(armor.getLevelNeeded()<=level){
			equippedArmor[armor.getType()] = armor;
			return true;
		} else return false;
	}
	
	public void setWeapon(Weapon weapon){
		equippedWeapon = weapon;
	}
	
	public Weapon getEquippedWeapon(){
		return equippedWeapon;
	}
	
	public Armor[] getArmor(){
		return equippedArmor.clone();
	}

	@Override
	public int getAttackValue() {
		int attackValue = level*20;
		if(equippedWeapon != null) attackValue += equippedWeapon.getAttackValue();
		return attackValue;
	}
	
	public ArrayList<Item> getInventory(){
		return inventory;
	}
	
	public void addItemToInventory(Item item){
		inventory.add(item);
	}

	public void removeItemFromInventory(Item item) {
		inventory.remove(item);
	}

	public void removeEquippedArmor(Armor armor) {
		equippedArmor[armor.getType()] = null;
	}
	
	public void removeEquippedArmor(int i) {
		equippedArmor[i] = null;
	}

	public void heal(int heal) {
		if((health + heal) <= maxHealth) {
			health += heal;
		} else health = maxHealth;
	}

	public void removeEquippedWeapon(Weapon weapon) {
		equippedWeapon = null;
	}

	public void addEXP(int points) {
		EXP += points;
		maxEXP = level * 500;
		if(EXP >= maxEXP) {
			levelUp();
			EXP = EXP - maxEXP;
		}

	}
	
	public int getEXPPercentage() {
		maxEXP = level * 500;
		int percent = (EXP * 100) / maxEXP;
		return percent;
	}
	
	public int getEXP() {
		return EXP;
	}

	public void setInventory(ArrayList<Item> tempInventoryList) {
		inventory = tempInventoryList;
	}
	
	public int getGold(){
		return gold;
	}
	
	public void changeGold(int value){
		gold += value;
	}
	
	public double getDefenseValue(){
		double defenseValue = 0;
		for(int i=0; i<5; i++){
			if(equippedArmor[i]!=null) defenseValue += equippedArmor[i].getDefenseValue(); 
		}
		return defenseValue;
	}
}
