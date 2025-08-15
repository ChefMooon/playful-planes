package io.github.chefmooon.playfulplanes.data;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends FabricRecipeProvider {
	public ModRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
		return new RecipeGenerator(wrapperLookup, recipeExporter) {
			@Override
			public void generate() {
				ShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), RecipeCategory.COMBAT, ModItems.PAPER_PLANE)
					.pattern(" A ")
					.pattern("AAA")
					.pattern(" A ")
					.input('A', Items.PAPER)
					.criterion(RecipeGenerator.hasItem(Items.PAPER), RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(wrapperLookup.getOrThrow(RegistryKeys.ITEM), Items.PAPER)))
					.offerTo(recipeExporter, RecipeGenerator.getRecipeName(ModItems.PAPER_PLANE));
			}
		};
	}

	@Override
	public String getName() {
		return PlayfulPlanes.MOD_ID.replaceAll("p", "P") + "RecipeProvider";
	}
}
