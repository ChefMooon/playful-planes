package io.github.chefmooon.playfulplanes.common.tag;

import io.github.chefmooon.playfulplanes.common.util.TextUtils;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModTags {

	public static final TagKey<Item> PAPER_PLANE = item("paper_plane");

	private static TagKey<Item> item(String string) {
		return TagKey.of(Registries.ITEM.getKey(), TextUtils.res(string));
	}
}
