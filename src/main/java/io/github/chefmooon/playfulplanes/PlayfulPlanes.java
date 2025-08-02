package io.github.chefmooon.playfulplanes;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayfulPlanes implements ModInitializer {
	public static final String ID = "playful_planes";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[Mod ID] pretty pink princess ponies prancing perpendicular");
	}
}
