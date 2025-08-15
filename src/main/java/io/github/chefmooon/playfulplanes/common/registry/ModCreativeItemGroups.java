package io.github.chefmooon.playfulplanes.common.registry;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.common.util.TextUtils;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

public class ModCreativeItemGroups {
	public static final RegistryKey<ItemGroup> PLAYFUL_PLANES = RegistryKey.of(Registries.ITEM_GROUP.getKey(), TextUtils.res(PlayfulPlanes.MOD_ID));

	public static void register() {
		Registry.register(Registries.ITEM_GROUP, PLAYFUL_PLANES,
			FabricItemGroup.builder()
				.icon(() -> new ItemStack(ModItems.PAPER_PLANE))
				.displayName(Text.translatable("itemGroup." + PlayfulPlanes.MOD_ID))
				.build());
	}
}
