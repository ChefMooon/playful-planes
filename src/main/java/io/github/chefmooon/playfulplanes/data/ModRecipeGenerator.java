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

				// TNT Paper Plane
				PaperPlaneShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), RecipeCategory.COMBAT, ModItems.PAPER_PLANE)
					.pattern(" A ")
					.pattern("ABA")
					.pattern(" A ")
					.input('A', Items.TNT)
					.input('B', ModItems.PAPER_PLANE)
					.type(PaperPlaneType.TNT)
					.criterion(RecipeGenerator.hasItem(Items.TNT), RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(wrapperLookup.getOrThrow(RegistryKeys.ITEM), Items.TNT, ModItems.PAPER_PLANE)))
					.offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, TextUtils.res("explosive_" + RecipeGenerator.getRecipeName(ModItems.PAPER_PLANE))));

				for (ModPotions potion : ModPotions.values()) {
					// Potion Paper Plane
					PotionContentsComponent potionContentsComponent = potion(List.of(potion.statusEffectInstance));
					ItemStack potionStack = new ItemStack(Items.POTION);
					potionStack.set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
					PaperPlaneShapedRecipeJsonBuilder.create(wrapperLookup.getOrThrow(RegistryKeys.ITEM), RecipeCategory.COMBAT, ModItems.PAPER_PLANE)
						.pattern(" A ")
						.pattern("ABA")
						.pattern(" A ")
						.input('A', potionStack.getItem())
						.input('B', ModItems.PAPER_PLANE)
						.type(PaperPlaneType.POTION)
						.potionContents(potionContentsComponent)
						.criterion("has_any", RecipeGenerator.conditionsFromPredicates(ItemPredicate.Builder.create().items(wrapperLookup.getOrThrow(RegistryKeys.ITEM), potionStack.getItem(), ModItems.PAPER_PLANE)))
						.offerTo(recipeExporter, RegistryKey.of(RegistryKeys.RECIPE, TextUtils.res(RecipeGenerator.getRecipeName(ModItems.PAPER_PLANE) + "_" + potion.name().toLowerCase())));

				}
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
	public enum PotionDuration {

		QUICK(400), // 20 seconds
		SHORT(1800), // 90 seconds
		MEDIUM(3600), // 3 minutes
		LONG(9600); // 8 minutes

		public final int duration;

		PotionDuration(int duration) {
			this.duration = duration;
		}
	}
	public enum ModPotions {
		NIGHT_VISION_MEDIUM(StatusEffects.NIGHT_VISION, PotionDuration.MEDIUM.duration, 0),
		NIGHT_VISION_LONG(StatusEffects.NIGHT_VISION, PotionDuration.LONG.duration, 0),

		INVISIBILITY_VISION_MEDIUM(StatusEffects.INVISIBILITY, PotionDuration.MEDIUM.duration, 0),
		INVISIBILITY_VISION_LONG(StatusEffects.INVISIBILITY, PotionDuration.LONG.duration, 0),

		JUMP_BOOST_MEDIUM(StatusEffects.JUMP_BOOST, PotionDuration.MEDIUM.duration, 0),
		JUMP_BOOST_LONG(StatusEffects.JUMP_BOOST, PotionDuration.LONG.duration, 0),
		JUMP_BOOST_II(StatusEffects.JUMP_BOOST, PotionDuration.SHORT.duration, 1),

		FIRE_RESISTANCE_MEDIUM(StatusEffects.FIRE_RESISTANCE, PotionDuration.MEDIUM.duration, 0),
		FIRE_RESISTANCE_LONG(StatusEffects.FIRE_RESISTANCE, PotionDuration.LONG.duration, 0),

		SPEED_MEDIUM(StatusEffects.SPEED, PotionDuration.MEDIUM.duration, 0),
		SPEED_LONG(StatusEffects.SPEED, PotionDuration.LONG.duration, 0),
		SPEED_II(StatusEffects.SPEED, PotionDuration.SHORT.duration, 1),

		SLOWNESS_MEDIUM(StatusEffects.SLOWNESS, PotionDuration.MEDIUM.duration, 0),
		SLOWNESS_LONG(StatusEffects.SLOWNESS, PotionDuration.LONG.duration, 0),
		SLOWNESS_IV(StatusEffects.SLOWNESS, PotionDuration.QUICK.duration, 3),

		WATER_BREATHING_MEDIUM(StatusEffects.WATER_BREATHING, PotionDuration.MEDIUM.duration, 0),
		WATER_BREATHING_LONG(StatusEffects.WATER_BREATHING, PotionDuration.LONG.duration, 0),

		INSTANT_HEALTH_I(StatusEffects.INSTANT_HEALTH, 1, 0),
		INSTANT_HEALTH_II(StatusEffects.INSTANT_HEALTH, 1, 1),

		INSTANT_DAMAGE_I(StatusEffects.INSTANT_DAMAGE, 1, 0),
		INSTANT_DAMAGE_II(StatusEffects.INSTANT_DAMAGE, 1, 1),

		POISION_MEDIUM(StatusEffects.POISON, 900, 0),
		POISION_LONG(StatusEffects.POISON, 1800, 0),
		POISION_II(StatusEffects.POISON, 420, 1),

		REGENERATION_MEDIUM(StatusEffects.REGENERATION, 900, 0),
		REGENERATION_LONG(StatusEffects.REGENERATION, 1800, 0),
		REGENERATION_II(StatusEffects.REGENERATION, 440, 1),

		STRENGTH_MEDIUM(StatusEffects.STRENGTH, PotionDuration.MEDIUM.duration, 0),
		STRENGTH_LONG(StatusEffects.STRENGTH, PotionDuration.LONG.duration, 0),
		STRENGTH_II(StatusEffects.STRENGTH, PotionDuration.SHORT.duration, 1),

		WEAKNESS_MEDIUM(StatusEffects.WEAKNESS, PotionDuration.SHORT.duration, 0),
		WEAKNESS_LONG(StatusEffects.WEAKNESS, 4800, 0),

		SLOW_FALLING_MEDIUM(StatusEffects.SLOW_FALLING, PotionDuration.SHORT.duration, 0),
		SLOW_FALLING_LONG(StatusEffects.SLOW_FALLING, 4800, 0),

		WEAVING(StatusEffects.WEAVING, PotionDuration.MEDIUM.duration, 0),

		OOZING(StatusEffects.OOZING, PotionDuration.MEDIUM.duration, 0),

		INFESTED(StatusEffects.INFESTED, PotionDuration.MEDIUM.duration, 0),
		;
		public final StatusEffectInstance statusEffectInstance;
		ModPotions(RegistryEntry<StatusEffect> statusEffect, int duration, int amplifier) {
			this.statusEffectInstance = new StatusEffectInstance(statusEffect, duration, amplifier);
		}
	}
}
