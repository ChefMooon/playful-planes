package io.github.chefmooon.playfulplanes.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class DataGeneration implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(TranslationGenerator::new);
		FabricTagProvider.BlockTagProvider blockTags = pack.addProvider(BlockTagGenerator::new);
		pack.addProvider((output, registriesFuture) -> new ItemTagGenerator(output, registriesFuture, blockTags));
	}
}
