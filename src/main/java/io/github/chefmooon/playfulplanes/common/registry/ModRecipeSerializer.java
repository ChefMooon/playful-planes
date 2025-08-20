package io.github.chefmooon.playfulplanes.common.registry;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.data.recipe.ModPotionShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ModRecipeSerializer {
	public static final Supplier<RecipeSerializer<ModPotionShapedRecipe>> POTION_SHAPED = registerRecipeSerializer("potion_shaped", ModPotionShapedRecipe.Serializer::new);

	public static <R, T extends R> Supplier<T> registerRecipeSerializer(String name, Supplier<T> supplier) {
		Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(PlayfulPlanes.MOD_ID, name),(RecipeSerializer<?>) supplier.get());
		return supplier;
	}

	public static void register() {
	}
}
