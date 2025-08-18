package io.github.chefmooon.playfulplanes.data;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.common.data.types.PaperPlaneType;
import io.github.chefmooon.playfulplanes.common.registry.ModItems;
import io.github.chefmooon.playfulplanes.common.util.TextUtils;
import io.github.chefmooon.playfulplanes.data.recipe.PaperPlaneShapedRecipeJsonBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

				// Flammable Paper Plane
				PaperPlaneShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), RecipeCategory.COMBAT, ModItems.PAPER_PLANE)
					.pattern(" A ")
					.pattern("ABA")
					.pattern(" A ")
					.input('A', Items.FIRE_CHARGE)
					.input('B', ModItems.PAPER_PLANE)
					.type(PaperPlaneType.FIRE)
					.criterion(RecipeGenerator.hasItem(Items.FIRE_CHARGE), RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(wrapperLookup.getOrThrow(RegistryKeys.ITEM), Items.FIRE_CHARGE, ModItems.PAPER_PLANE)))
					.offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, TextUtils.res("flammable_" + RecipeGenerator.getRecipeName(ModItems.PAPER_PLANE))));

//				for (ModPotions potion : ModPotions.values()) {
//					// Potion Paper Plane
//					PotionContentsComponent potionContentsComponent = potion(List.of(potion.statusEffectInstance));
//					ItemStack potionStack = new ItemStack(Items.POTION);
//					potionStack.set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
//					PaperPlaneShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), RecipeCategory.COMBAT, ModItems.PAPER_PLANE)
//						.pattern(" A ")
//						.pattern("ABA")
//						.pattern(" A ")
//						.input('A', potionStack.getItem())
//						.input('B', ModItems.PAPER_PLANE)
//						.type(PaperPlaneType.POTION)
//						.potionContents(potionContentsComponent)
//						.criterion(RecipeGenerator.hasItem(potionStack.getItem()), RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(wrapperLookup.getOrThrow(RegistryKeys.ITEM), potionStack.getItem(), ModItems.PAPER_PLANE)))
//						.offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, TextUtils.res("potion_" + potion.name().toLowerCase())));
//				}
			}
		};
	}

	private static PotionContentsComponent potion(List<StatusEffectInstance> effects) {
		return new PotionContentsComponent(Optional.empty(), Optional.empty(), effects, Optional.empty());
	}

	@Override
	public String getName() {
		return PlayfulPlanes.MOD_ID.replaceAll("p", "P") + "RecipeProvider";
	}

	// Potion Recipe Prep
	public enum ModPotions {
		NIGHT_VISION_SHORT(StatusEffects.NIGHT_VISION, 3600, 0),
		;
		public final StatusEffectInstance statusEffectInstance;
		ModPotions(RegistryEntry<StatusEffect> statusEffect, int duration, int amplifier) {
			this.statusEffectInstance = new StatusEffectInstance(statusEffect, duration, amplifier);
		}
	}
}
