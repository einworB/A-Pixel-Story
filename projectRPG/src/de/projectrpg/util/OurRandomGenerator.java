package de.projectrpg.util;

import java.util.Random;

/**
 * Extends the normal Random Generator in Java
 */
@SuppressWarnings("serial")
public class OurRandomGenerator extends Random{
	
	/**
	 * the constructor
	 */
	public OurRandomGenerator(){
		super();
	}
	
	/**
	 * get a boolean with a given chance. 
	 * @param chance a double value as a chance
	 * @return true if the given chance value is bigger of equal to a random double between 0 to 1
	 */
	public boolean getBoolean(double chance){
		double randomDouble = nextDouble();
		if(randomDouble<=chance) return true;
		else return false;
	}
	
	/**
	 * gives back a number of ints in a give range between 0 and the given end number
	 * @param count the count of ints that should be given back
	 * @param end the upper border of the range 
	 * @return an array with ints
	 */
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
