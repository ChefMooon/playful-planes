package io.github.chefmooon.playfulplanes.client.render.entity.model;

import com.google.common.collect.Sets;
import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class ModEntityModelLayers {
	private static final Set<EntityModelLayer> LAYERS = Sets.newHashSet();

	public static final EntityModelLayer PAPER_PLANE = registerMain("paper_plane");

	private static EntityModelLayer registerMain(String id) {
		return register(id, "main");
	}

	private static EntityModelLayer register(String id, String layer) {
		EntityModelLayer entityModelLayer = create(id, layer);
		if (!LAYERS.add(entityModelLayer)) {
			throw new IllegalStateException("Duplicate registration for " + String.valueOf(entityModelLayer));
		} else {
			return entityModelLayer;
		}
	}

	private static EntityModelLayer create(String id, String layer) {
		return new EntityModelLayer(Identifier.of(PlayfulPlanes.MOD_ID, id), layer);
	}
}
