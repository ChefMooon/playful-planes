package io.github.chefmooon.playfulplanes.client.render.entity;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import io.github.chefmooon.playfulplanes.client.render.entity.model.ModEntityModelLayers;
import io.github.chefmooon.playfulplanes.client.render.entity.model.PaperPlaneEntityModel;
import io.github.chefmooon.playfulplanes.client.render.entity.state.PaperPlaneEntityRenderState;
import io.github.chefmooon.playfulplanes.common.entity.projectile.PaperPlaneEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class PaperPlaneEntityRenderer extends EntityRenderer<PaperPlaneEntity, PaperPlaneEntityRenderState> {
	public static final Identifier TEXTURE = Identifier.of(PlayfulPlanes.MOD_ID, "textures/entity/white_paper_plane_in_hand.png");
	private final PaperPlaneEntityModel model;

	public PaperPlaneEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.model = new PaperPlaneEntityModel(context.getPart(ModEntityModelLayers.PAPER_PLANE));
	}

	public void render(PaperPlaneEntityRenderState paperPlaneEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(paperPlaneEntityRenderState.yaw - 65.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(paperPlaneEntityRenderState.pitch + 180.0F));
		VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(TEXTURE), false, paperPlaneEntityRenderState.enchanted);
		this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
		matrixStack.pop();
		super.render(paperPlaneEntityRenderState, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public PaperPlaneEntityRenderState createRenderState() {
		return new PaperPlaneEntityRenderState();
	}

	public void updateRenderState(PaperPlaneEntity paperPlaneEntity, PaperPlaneEntityRenderState paperPlaneEntityRenderState, float f) {
		super.updateRenderState(paperPlaneEntity, paperPlaneEntityRenderState, f);
		paperPlaneEntityRenderState.yaw = paperPlaneEntity.getLerpedYaw(f);
		paperPlaneEntityRenderState.pitch = paperPlaneEntity.getLerpedPitch(f);
		paperPlaneEntityRenderState.enchanted = paperPlaneEntity.isEnchanted();
	}
}
