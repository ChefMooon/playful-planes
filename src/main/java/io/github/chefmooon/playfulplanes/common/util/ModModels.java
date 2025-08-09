package io.github.chefmooon.playfulplanes.common.util;

import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;

import java.util.Optional;

public class ModModels {

	public static final Model TEMPLATE_PAPER_PLANE_ITEM = item("template_paper_plane", TextureKey.LAYER0);
	public static final Model TEMPLATE_PAPER_PLANE_THROWING_ITEM = item("template_paper_plane_throwing", TextureKey.LAYER0);

	private static Model item(String parent, TextureKey... textureKeys) {
		return new Model(Optional.of(TextUtils.res("item/" + parent)), Optional.empty(), textureKeys);
	}
}
