package io.github.chefmooon.playfulplanes.client;

import io.github.chefmooon.playfulplanes.client.render.entity.PaperPlaneEntityRenderer;
import io.github.chefmooon.playfulplanes.client.render.entity.model.ModEntityModelLayers;
import io.github.chefmooon.playfulplanes.client.render.entity.model.PaperPlaneEntityModel;
import io.github.chefmooon.playfulplanes.common.registry.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class PlayfulPlanesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.PAPER_PLANE, PaperPlaneEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntityTypes.PAPER_PLANE, PaperPlaneEntityRenderer::new);
	}
}
