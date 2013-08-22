package com.example.projectrpg;

import android.util.Log;

public class FightHelper {
	
	public static int fight(Player player, Opponent opponent){
		OurRandomGenerator rand = new OurRandomGenerator();
		double playerAgility = player.getAgility();
		double opponentAgility = opponent.getAgility();
		double playerAttackValue = player.getAttackValue();
		double opponentAttackValue = opponent.getAttackValue();
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
		return 0;
	}

}
