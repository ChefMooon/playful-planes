package io.github.chefmooon.playfulplanes.data;

import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import io.github.chefmooon.playfulplanes.common.tag.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
	public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, @Nullable BlockTagProvider blockTagProvider) {
		super(output, registriesFuture, blockTagProvider);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		valueLookupBuilder(ModTags.PAPER_PLANE).add(ModItems.WHITE_PAPER_PLANE);

		valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE).addTag(ModTags.PAPER_PLANE);
		valueLookupBuilder(ItemTags.TRIDENT_ENCHANTABLE).addTag(ModTags.PAPER_PLANE);

	}
}
