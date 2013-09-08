package de.projectrpg.util;

import org.andengine.entity.sprite.Sprite;

import de.projectrpg.sprites.Opponent;
import de.projectrpg.sprites.Player;

import android.util.Log;

public class FightHelper {
	
	public static int fight(Player player, Opponent opponent, Sprite redBar, Sprite redBarEnemy){
		OurRandomGenerator rand = new OurRandomGenerator();
		double playerAgility = player.getAgility();
		double opponentAgility = opponent.getAgility();
		double playerAttackValue = player.getAttackValue();
		double playerDefenseValue = player.getDefenseValue();
		double opponentAttackValue = opponent.getAttackValue()-(playerDefenseValue/10.0);
		// crit
		if(rand.getBoolean(player.getLuck()/10.0)){
			playerAttackValue *= 1.5;
			Log.d("FIGHT", "PLAYER CRIT");
		}
		if(rand.getBoolean(opponent.getLuck()/10.0)){
			opponentAttackValue *= 1.5;
			Log.d("FIGHT", "OPPONENT CRIT");
		}
		// miss
		if(!rand.getBoolean(player.getConcentration())){
			playerAttackValue = 0;
			Log.d("FIGHT", "PLAYER MISS");
		}
		if(!rand.getBoolean(opponent.getConcentration())){
			opponentAttackValue = 0;
			Log.d("FIGHT", "OPPONENT MISS");
		}
		// who's faster?
		if(rand.getBoolean(playerAgility / (playerAgility+opponentAgility))){
			opponent.changeHealth(playerAttackValue*(-1));
			Log.d("FIGHT", "NEW OPPONENT HEALTH: "+opponent.getHealth());
			// opponent dead, player wins
			if(opponent.getHealth()<=0) return 1;
			player.changeHealth(opponentAttackValue*(-1));
			Log.d("FIGHT", "NEW PLAYER HEALTH: "+player.getHealth());
			// player dead, opponent wins
			if(player.getHealth()<=0) return 2;
		} else{
			player.changeHealth(opponentAttackValue*(-1));
			Log.d("FIGHT", "NEW PLAYER HEALTH: "+player.getHealth());
			// player dead, opponent wins
			if(player.getHealth()<=0) return 2;
			opponent.changeHealth(playerAttackValue*(-1));
			Log.d("FIGHT", "NEW OPPONENT HEALTH: "+opponent.getHealth());
			// opponent dead, player wins
			if(opponent.getHealth()<=0) return 1;
		}
		// noone dies
		redBar.setWidth((float)(100-player.getHealth())/3);
		redBar.setX(44-redBar.getWidth());
		redBarEnemy.setWidth((float)(100-opponent.getHealth())/3);
		redBarEnemy.setX(710-redBarEnemy.getWidth());
		return 0;
	}

}
