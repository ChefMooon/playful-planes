package io.github.chefmooon.playfulplanes.data;

import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import io.github.chefmooon.playfulplanes.common.util.ModModels;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.item.Item;

public class ModModelGenerator extends FabricModelProvider {
	public ModModelGenerator(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		registerPaperPlaneItemModel(ModItems.WHITE_PAPER_PLANE, itemModelGenerator);
	}

	public final void registerPaperPlaneItemModel(Item item, ItemModelGenerator itemModelGenerator) {
		ModModels.TEMPLATE_PAPER_PLANE_ITEM.upload(item,
			TextureMap.layer0(item), itemModelGenerator.modelCollector);
		ModModels.TEMPLATE_PAPER_PLANE_THROWING_ITEM.upload(ModelIds.getItemSubModelId(item, "_throwing"),
			TextureMap.layer0(item), itemModelGenerator.modelCollector);
	}
}
