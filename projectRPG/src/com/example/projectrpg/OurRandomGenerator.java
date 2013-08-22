package com.example.projectrpg;

import java.util.Random;

@SuppressWarnings("serial")
public class OurRandomGenerator extends Random{
	

	public OurRandomGenerator(){
		super();
	}
	
	public boolean getBoolean(double chance){
		double randomDouble = nextDouble();
		if(randomDouble<=chance) return true;
		else return false;
	}

}
