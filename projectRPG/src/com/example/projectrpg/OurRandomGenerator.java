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
	
	public int[] getInts(int count, int end){
		if(count>end) return null;
		int[] result = new int[count];
		if(count==end){
			for(int i=0; i<end; i++){
				result[i] = i;
			}
			return result;
		}
		for(int i=0; i<count; i++) result[i] = -1;
		int counter = 0;
		while(counter<count){
			int randomInt = nextInt(end);
			boolean alreadyFound = false;
			for(int i=0; i<counter; i++){
				if(result[i]==randomInt){
					alreadyFound = true;
					break;
				}
			}
			if(!alreadyFound){
				result[counter] = randomInt;
				counter++;
			}
		}
		return result;
	}

}
