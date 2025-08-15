package io.github.chefmooon.playfulplanes.data;

import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import io.github.chefmooon.playfulplanes.common.util.ModModels;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

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
//		Identifier inHandModel = ModModels.TEMPLATE_PAPER_PLANE_ITEM.upload(ModelIds.getItemSubModelId(item, "_in_hand"),
//			TextureMap.layer0(item), itemModelGenerator.modelCollector);
//		Identifier inHandThrowingModel = ModModels.TEMPLATE_PAPER_PLANE_THROWING_ITEM.upload(ModelIds.getItemSubModelId(item, "_throwing"),
//			TextureMap.layer0(item), itemModelGenerator.modelCollector);

		ItemModel.Unbaked unbaked = ItemModels.basic(itemModelGenerator.upload(item, Models.GENERATED));
		ItemModel.Unbaked inHandUnbakedModel = ItemModels.basic(
			ModModels.TEMPLATE_PAPER_PLANE_ITEM.upload(ModelIds.getItemSubModelId(item, "_in_hand"),
				TextureMap.layer0(ModelIds.getItemModelId(item).withSuffixedPath("_in_hand")), itemModelGenerator.modelCollector)
		);
		ItemModel.Unbaked inHandThrowingUnbakedModel = ItemModels.basic(
			ModModels.TEMPLATE_PAPER_PLANE_THROWING_ITEM.upload(ModelIds.getItemSubModelId(item, "_throwing"),
				TextureMap.layer0(ModelIds.getItemModelId(item).withSuffixedPath("_in_hand")), itemModelGenerator.modelCollector)
		);

		ItemModel.Unbaked condition = ItemModels.condition(ItemModels.usingItemProperty(), inHandThrowingUnbakedModel, inHandUnbakedModel);

		itemModelGenerator.output.accept(item, ItemModelGenerator.createModelWithInHandVariant(unbaked, condition));
	}
}
