package io.github.chefmooon.playfulplanes.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.chefmooon.playfulplanes.common.data.types.PaperPlaneType;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.Optional;

public record PaperPlaneComponent(PaperPlaneType paperPlaneType, Optional<PotionContentsComponent> potionContentsComponent) {
	public static final Codec<PaperPlaneComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			PaperPlaneType.CODEC.fieldOf("type").forGetter(PaperPlaneComponent::paperPlaneType),
			PotionContentsComponent.CODEC.optionalFieldOf("potion_contents").forGetter(PaperPlaneComponent::potionContentsComponent)
		).apply(instance, PaperPlaneComponent::new)
	);

	public static final PacketCodec<RegistryByteBuf, PaperPlaneComponent> PACKET_CODEC = PacketCodec.tuple(
		PaperPlaneType.PACKET_CODEC, PaperPlaneComponent::getType,
		PotionContentsComponent.PACKET_CODEC.collect(PacketCodecs::optional), PaperPlaneComponent::potionContentsComponent,
		PaperPlaneComponent::new
	);

	public PaperPlaneType getType() {
		return paperPlaneType;
	}

	public Optional<PotionContentsComponent> potionContentsComponent() {
		return potionContentsComponent;
	}

	public static PaperPlaneComponent getDefault() {
		return new PaperPlaneComponent(PaperPlaneType.BASIC, Optional.empty());
	}
}
