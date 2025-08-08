package io.github.chefmooon.playfulplanes.common.registry;

import io.github.chefmooon.playfulplanes.common.util.TextUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ModDamageSources {
	public static final RegistryKey<DamageType> PAPER_PLANE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, TextUtils.res("paper_plane"));

	public static DamageSource paperPlane(World world, @Nullable Entity source, @Nullable Entity attacker) {
		return new DamageSource(world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).getEntry(PAPER_PLANE.getValue()).get(), source, attacker);
	}
	public static DamageSource simpleDamageSource(World world, RegistryKey<DamageType> type) {
		return new DamageSource(world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).getEntry(type.getValue()).get());
	}

	public static void register() {
	}
}
