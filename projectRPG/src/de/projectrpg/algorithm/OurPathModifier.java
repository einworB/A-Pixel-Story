package de.projectrpg.algorithm;

import org.andengine.entity.modifier.PathModifier;

/**
 * extends the path modifier of the engine
 */
public class OurPathModifier extends PathModifier {

	/**
	 * The contructor of the engine needs the duration the player has to walk. 
	 * But we want the same velocity no matter how long the path is. 
	 * So we calculate the duration for the path and give this to the constructor of the engine path modifier
	 * @param velocity the speed the player should move
	 * @param path the path to move
	 * @param entityModiferListener
	 * @param pathModifierListener
	 * @throws IllegalArgumentException
	 */
	public OurPathModifier(final float velocity, final Path path, final IEntityModifierListener entityModiferListener, final IPathModifierListener pathModifierListener) throws IllegalArgumentException {
		super(path.getLength() / velocity, path, entityModiferListener, pathModifierListener);
	}

}
