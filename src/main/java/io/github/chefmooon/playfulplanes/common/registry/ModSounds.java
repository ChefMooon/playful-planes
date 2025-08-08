package io.github.chefmooon.playfulplanes.common.registry;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

	public static final SoundEvent ENTITY_PAPER_PLANE_THROW = registerSound("entity.paper_plane.throw");
	public static final SoundEvent ENTITY_PAPER_PLANE_HIT_GROUND = registerSound("entity.paper_plane.hit_ground");
	public static final SoundEvent ENTITY_PAPER_PLANE_HIT = registerSound("entity.paper_plane.hit");
	public static final SoundEvent ENTITY_PAPER_PLANE_RETURN = registerSound("entity.paper_plane.return");

	private static SoundEvent registerSound(String id) {
		Identifier identifier = Identifier.of(PlayfulPlanes.MOD_ID, id);
		return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
	}

	public static void register() {
	}
}
