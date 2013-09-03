package de.projectrpg.algorithm;

import org.andengine.entity.modifier.PathModifier;

public class OurPathModifier extends PathModifier {
	
	public OurPathModifier(final float velocity, final Path path, final IEntityModifierListener entityModiferListener, final IPathModifierListener pathModifierListener) throws IllegalArgumentException {
		super(path.getLength() / velocity, path, entityModiferListener, pathModifierListener);
	}

}
