package io.github.chefmooon.playfulplanes.common.registry;

import io.github.chefmooon.playfulplanes.common.data.PaperPlaneComponent;
import io.github.chefmooon.playfulplanes.common.util.TextUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModDataComponentTypes {
	public static final ComponentType<PaperPlaneComponent> PAPER_PLANE_COMPONENT = register("paper_plane",
		ComponentType.<PaperPlaneComponent>builder().codec(PaperPlaneComponent.CODEC).build());

	private static <T> ComponentType<T> register(String name, ComponentType<T> componentType) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, TextUtils.res(name), componentType);
	}

	public static void register() {
	}
}
