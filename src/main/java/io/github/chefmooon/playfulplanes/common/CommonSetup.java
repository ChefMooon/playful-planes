package io.github.chefmooon.playfulplanes.common;

import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import net.minecraft.block.DispenserBlock;

public class CommonSetup {

	public static void init() {
		DispenserBlock.registerProjectileBehavior(ModItems.PAPER_PLANE);
	}
}
