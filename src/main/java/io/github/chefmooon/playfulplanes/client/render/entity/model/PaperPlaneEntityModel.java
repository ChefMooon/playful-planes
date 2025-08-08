package io.github.chefmooon.playfulplanes.client.render.entity.model;

import io.github.chefmooon.playfulplanes.PlayfulPlanes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PaperPlaneEntityModel extends Model {
	public PaperPlaneEntityModel(ModelPart root) {
		super(root, RenderLayer::getEntitySolid);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, -16).cuboid(0.0F, -3.0F, -8.0F, 0.0F, 3.0F, 16.0F, new Dilation(0.0F)), ModelTransform.rotation(0.0F, 24.0F, 0.0F));
		bb_main.addChild("east_wing_r1", ModelPartBuilder.create().uv(-10, 16).cuboid(-3.0F, 0.0F, -8.0F, 3.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.3491F));
		bb_main.addChild("west_wing_r1", ModelPartBuilder.create().uv(-16, 16).cuboid(0.0F, 0.0F, -8.0F, 3.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.3491F));
		return TexturedModelData.of(modelData, 32, 32);
	}
}
