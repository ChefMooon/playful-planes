package io.github.chefmooon.playfulplanes.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.chefmooon.playfulplanes.common.registry.ModDataComponentTypes;
import io.github.chefmooon.playfulplanes.common.util.TextUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapedCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

  public class ModPotionShapedRecipe implements CraftingRecipe {
	final RawShapedRecipe raw;
	final ItemStack result;
	final String group;
	final CraftingRecipeCategory category;
	final boolean showNotification;
	@Nullable
	private IngredientPlacement ingredientPlacement;
	public ModPotionShapedRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
		this.group = group;
		this.category = category;
		this.raw = raw;
		this.result = result;
		this.showNotification = showNotification;
	}

	public RecipeSerializer<? extends ModPotionShapedRecipe> getSerializer() {
		return (RecipeSerializer<? extends ModPotionShapedRecipe>) Registries.RECIPE_SERIALIZER.get(TextUtils.res("potion_shaped"));
	}

	public String getGroup() {
		return this.group;
	}

	public CraftingRecipeCategory getCategory() {
		return this.category;
	}

	public IngredientPlacement getIngredientPlacement() {
		if (this.ingredientPlacement == null) {
			this.ingredientPlacement = IngredientPlacement.forMultipleSlots(this.raw.getIngredients());
		}

		return this.ingredientPlacement;
	}

	public boolean showNotification() {
		return this.showNotification;
	}

	public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
		Optional<PotionContentsComponent> resultPotionContents = Objects.requireNonNull(this.result.get(ModDataComponentTypes.PAPER_PLANE_COMPONENT)).potionContentsComponent();
		if (resultPotionContents.isPresent() && resultPotionContents.get().getEffects() != null) {

			for (int i = 0; i < craftingRecipeInput.getStackCount(); i++) {
				ItemStack ingredientStack = craftingRecipeInput.getStackInSlot(i);
				PotionContentsComponent ingredientPotionContents = ingredientStack.get(DataComponentTypes.POTION_CONTENTS);
				if (ingredientPotionContents != null && ingredientPotionContents.getEffects() != null) {
					if (!ingredientPotionContents.getEffects().equals(resultPotionContents.get().getEffects())) {
						return false;
					}
				}
			}
		}
		return this.raw.matches(craftingRecipeInput);
	}

	public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
		return this.result.copy();
	}

	public int getWidth() {
		return this.raw.getWidth();
	}

	public int getHeight() {
		return this.raw.getHeight();
	}

	public List<RecipeDisplay> getDisplays() {
		return List.of(new ShapedCraftingRecipeDisplay(this.raw.getWidth(), this.raw.getHeight(), this.raw.getIngredients().stream().map((ingredient) -> {
			return (SlotDisplay)ingredient.map(Ingredient::toDisplay).orElse(SlotDisplay.EmptySlotDisplay.INSTANCE);
		}).toList(), new SlotDisplay.StackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
	}

	public static class Serializer implements RecipeSerializer<ModPotionShapedRecipe> {
		public static final MapCodec<ModPotionShapedRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
			return instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter((recipe) -> {
				return recipe.group;
			}), CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter((recipe) -> {
				return recipe.category;
			}), RawShapedRecipe.CODEC.forGetter((recipe) -> {
				return recipe.raw;
			}), ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter((recipe) -> {
				return recipe.result;
			}), Codec.BOOL.optionalFieldOf("show_notification", true).forGetter((recipe) -> {
				return recipe.showNotification;
			})).apply(instance, (ModPotionShapedRecipe::new));
		});
		public static final PacketCodec<RegistryByteBuf, ModPotionShapedRecipe> PACKET_CODEC = PacketCodec.ofStatic(ModPotionShapedRecipe.Serializer::write, ModPotionShapedRecipe.Serializer::read);

		public MapCodec<ModPotionShapedRecipe> codec() {
			return CODEC;
		}

		public PacketCodec<RegistryByteBuf, ModPotionShapedRecipe> packetCodec() {
			return PACKET_CODEC;
		}

		private static ModPotionShapedRecipe read(RegistryByteBuf buf) {
			String string = buf.readString();
			CraftingRecipeCategory craftingRecipeCategory = (CraftingRecipeCategory)buf.readEnumConstant(CraftingRecipeCategory.class);
			RawShapedRecipe rawShapedRecipe = (RawShapedRecipe)RawShapedRecipe.PACKET_CODEC.decode(buf);
			ItemStack itemStack = (ItemStack)ItemStack.PACKET_CODEC.decode(buf);
			boolean bl = buf.readBoolean();
			return new ModPotionShapedRecipe(string, craftingRecipeCategory, rawShapedRecipe, itemStack, bl);
		}

		private static void write(RegistryByteBuf buf, ModPotionShapedRecipe recipe) {
			buf.writeString(recipe.group);
			buf.writeEnumConstant(recipe.category);
			RawShapedRecipe.PACKET_CODEC.encode(buf, recipe.raw);
			ItemStack.PACKET_CODEC.encode(buf, recipe.result);
			buf.writeBoolean(recipe.showNotification);
		}
	}
}
