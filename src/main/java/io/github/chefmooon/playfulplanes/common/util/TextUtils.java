package io.github.chefmooon.playfulplanes.common.util;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import net.minecraft.util.Identifier;

public class TextUtils {
	public static Identifier res(String path) {
		return Identifier.of(PlayfulPlanes.MOD_ID, path);
	}
}
