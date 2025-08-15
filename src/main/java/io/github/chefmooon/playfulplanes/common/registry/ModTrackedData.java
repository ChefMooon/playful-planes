package io.github.chefmooon.playfulplanes.common.registry;

import io.github.chefmooon.playfulplanes.common.data.PaperPlaneComponent;
import io.github.chefmooon.playfulplanes.common.util.TextUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;

public class ModTrackedData {
	public static final TrackedDataHandler<PaperPlaneComponent> PAPER_PLANE = TrackedDataHandler.create(PaperPlaneComponent.PACKET_CODEC);

	public static void register() {
		FabricTrackedDataRegistry.register(TextUtils.res("paper_plane"), PAPER_PLANE);
	}
}
