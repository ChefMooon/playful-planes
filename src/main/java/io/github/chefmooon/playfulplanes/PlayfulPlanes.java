package io.github.chefmooon.playfulplanes;

import io.github.chefmooon.playfulplanes.common.CommonSetup;
import io.github.chefmooon.playfulplanes.common.registry.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayfulPlanes implements ModInitializer {
	public static final String MOD_ID = "playful_planes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModDataComponentTypes.register();
		ModCreativeItemGroups.register();
		ModItems.register();
		ModTrackedData.register();
		ModEntityTypes.register();
		ModSounds.register();
		ModDamageSources.register();

		CommonSetup.init();
	}
}
