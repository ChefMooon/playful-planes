package io.github.chefmooon.playfulplanes.common.registry;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.common.entity.projectile.PaperPlaneEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntityTypes {
//	public static final EntityType<PaperPlaneEntity> PAPER_PLANE = registerEntityType(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(PlayfulPlanes.MOD_ID, "white_paper_plane")),
//		EntityType.Builder.<PaperPlaneEntity>create(PaperPlaneEntity::new, SpawnGroup.MISC)
//			.dimensions(0.5F, 0.5F)
//			.maxTrackingRange(4)
//			.trackingTickInterval(10)
//	);

	public static final EntityType<PaperPlaneEntity> PAPER_PLANE = registerEntityType(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(PlayfulPlanes.MOD_ID, "white_paper_plane")),
		EntityType.Builder.<PaperPlaneEntity>create(PaperPlaneEntity::new, SpawnGroup.MISC)
			.dimensions(0.6F, 0.6F)
			.maxTrackingRange(4)
			.trackingTickInterval(10)
	);

	private static <T extends Entity> EntityType<T> registerEntityType(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
		return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
	}

	public static void register() {
	}
}
