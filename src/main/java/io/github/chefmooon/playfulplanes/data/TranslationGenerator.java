package io.github.chefmooon.playfulplanes.data;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import io.github.chefmooon.playfulplanes.common.tag.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TranslationGenerator extends FabricLanguageProvider {
	protected TranslationGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, "en_us", registryLookup);
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
		String MOD_ID = PlayfulPlanes.MOD_ID;

		translationBuilder.add(ModItems.WHITE_PAPER_PLANE, "White Paper Plane");

		translationBuilder.add("death.attack.playful_planes.paper_plane", "%1$s died from a Paper Plane");

		translationBuilder.add(ModTags.PAPER_PLANE, "Paper Planes");
	}
}
