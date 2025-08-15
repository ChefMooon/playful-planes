package io.github.chefmooon.playfulplanes.common.registry;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.common.data.PaperPlaneComponent;
import io.github.chefmooon.playfulplanes.common.item.PaperPlaneItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {

	public static final Item PAPER_PLANE = register("paper_plane", PaperPlaneItem::new,
		new Item.Settings().maxDamage(32).attributeModifiers(PaperPlaneItem.createAttributeModifiers())
			.component(ModDataComponentTypes.PAPER_PLANE_COMPONENT, PaperPlaneComponent.getDefault())
			.component(DataComponentTypes.TOOL, PaperPlaneItem.createToolComponent()).enchantable(1)
			.component(DataComponentTypes.WEAPON, new WeaponComponent(1)));

	public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(PlayfulPlanes.MOD_ID, name));
		Item item = itemFactory.apply(settings.registryKey(itemKey));
		Registry.register(Registries.ITEM, itemKey, item);
		ItemGroupEvents.modifyEntriesEvent(ModCreativeItemGroups.PLAYFUL_PLANES).register(itemGroup -> {
			itemGroup.add(item);
		});

		return item;
	}

	public static void register() {
	}

}
