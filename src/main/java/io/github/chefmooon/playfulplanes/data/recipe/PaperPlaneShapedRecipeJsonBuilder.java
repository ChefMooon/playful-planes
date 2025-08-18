package io.github.chefmooon.playfulplanes.data.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.chefmooon.playfulplanes.common.data.PaperPlaneComponent;
import io.github.chefmooon.playfulplanes.common.data.types.PaperPlaneType;
import io.github.chefmooon.playfulplanes.common.registry.ModDataComponentTypes;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PaperPlaneShapedRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
	private final RegistryEntryLookup<Item> registryLookup;
	private final RecipeCategory category;
	private final Item output;
	private final int count;
	private final List<String> pattern = Lists.newArrayList();
	private final Map<Character, Ingredient> inputs = Maps.newLinkedHashMap();
	private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap();
	private PaperPlaneType paperPlaneType = PaperPlaneType.BASIC;
	private PotionContentsComponent potionContentsComponent = null;
	@Nullable
	private String group;
	private boolean showNotification = true;
	private PaperPlaneShapedRecipeJsonBuilder(RegistryEntryLookup<Item> registryLookup, RecipeCategory category, ItemConvertible output, int count) {
		this.registryLookup = registryLookup;
		this.category = category;
		this.output = output.asItem();
		this.count = count;
	}

	public static PaperPlaneShapedRecipeJsonBuilder create(RegistryEntryLookup<Item> registryLookup, RecipeCategory category, ItemConvertible output) {
		return create(registryLookup, category, output, 1);
	}

	public static PaperPlaneShapedRecipeJsonBuilder create(RegistryEntryLookup<Item> registryLookup, RecipeCategory category, ItemConvertible output, int count) {
		return new PaperPlaneShapedRecipeJsonBuilder(registryLookup, category, output, count);
	}

	public PaperPlaneShapedRecipeJsonBuilder input(Character c, TagKey<Item> tag) {
		return this.input(c, Ingredient.ofTag(this.registryLookup.getOrThrow(tag)));
	}

	public PaperPlaneShapedRecipeJsonBuilder input(Character c, ItemConvertible item) {
		return this.input(c, Ingredient.ofItem(item));
	}

	public PaperPlaneShapedRecipeJsonBuilder input(Character c, Ingredient ingredient) {
		if (this.inputs.containsKey(c)) {
			throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
		} else if (c == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.inputs.put(c, ingredient);
			return this;
		}
	}

	public PaperPlaneShapedRecipeJsonBuilder type(PaperPlaneType paperPlaneType) {
		this.paperPlaneType = paperPlaneType;
		return this;
	}

	public PaperPlaneShapedRecipeJsonBuilder potionContents(PotionContentsComponent potionContentsComponent) {
		this.potionContentsComponent = potionContentsComponent;
		return this;
	}

	public PaperPlaneShapedRecipeJsonBuilder pattern(String patternStr) {
		if (!this.pattern.isEmpty() && patternStr.length() != ((String)this.pattern.get(0)).length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.pattern.add(patternStr);
			return this;
		}
	}
	@Override
	public CraftingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}

	@Override
	public CraftingRecipeJsonBuilder group(@Nullable String group) {
		this.group = group;
		return this;
	}

	public CraftingRecipeJsonBuilder showNotification(boolean showNotification) {
		this.showNotification = showNotification;
		return this;
	}

	@Override
	public Item getOutputItem() {
		return this.output;
	}

	@Override
	public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
		RawShapedRecipe rawShapedRecipe = this.validate(recipeKey);
		Advancement.Builder builder = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey)).rewards(AdvancementRewards.Builder.recipe(recipeKey)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
		Objects.requireNonNull(builder);
		this.criteria.forEach(builder::criterion);
		ItemStack itemStack = new ItemStack(this.output, this.count);
		itemStack.set(ModDataComponentTypes.PAPER_PLANE_COMPONENT, new PaperPlaneComponent(paperPlaneType, potionContentsComponent != null ? Optional.of(potionContentsComponent) : Optional.empty()));
		ShapedRecipe shapedRecipe = new ShapedRecipe((String)Objects.requireNonNullElse(this.group, ""), CraftingRecipeJsonBuilder.toCraftingCategory(this.category), rawShapedRecipe, itemStack, this.showNotification);
		exporter.accept(recipeKey, shapedRecipe, builder.build(recipeKey.getValue().withPrefixedPath("recipes/" + this.category.getName() + "/")));
	}

	private RawShapedRecipe validate(RegistryKey<Recipe<?>> recipeKey) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(recipeKey.getValue()));
		} else {
			return RawShapedRecipe.create(this.inputs, this.pattern);
		}
	}
}
