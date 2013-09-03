package de.projectrpg.sprites;

import java.util.ArrayList;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import de.projectrpg.database.Armor;
import de.projectrpg.database.Item;
import de.projectrpg.database.Weapon;

public class Player extends FightingSprite {

	private Weapon equippedWeapon;
	/** Index 0 = Kopf, 1 = Oberk�rper, 2 = H�nde, 3 = Beine, 4 = F��e */
	private Armor[] equippedArmor;
	private ArrayList<Item> inventory;
	
	
	
	public Player(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int level){
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, level);
		equippedArmor = new Armor[5];
		inventory = new ArrayList<Item>();
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
		return equippedArmor;
	}

	@Override
	public int getAttackValue() {
		int attackValue = level*2;
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

	public void heal(int heal) {
		health += heal;
	}

	public void removeEquippedWeapon(Weapon weapon) {
		equippedWeapon = null;
	}

}
