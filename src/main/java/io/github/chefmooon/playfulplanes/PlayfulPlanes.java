package io.github.chefmooon.playfulplanes;

import io.github.chefmooon.playfulplanes.common.registry.ModDamageSources;
import io.github.chefmooon.playfulplanes.common.registry.ModEntityTypes;
import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import io.github.chefmooon.playfulplanes.common.registry.ModSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayfulPlanes implements ModInitializer {
	public static final String MOD_ID = "playful_planes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
//		LOGGER.info("[Mod ID] pretty pink princess ponies prancing perpendicular");
		ModItems.register();
		ModEntityTypes.register();
		ModSounds.register();
		ModDamageSources.register();
	}
}
