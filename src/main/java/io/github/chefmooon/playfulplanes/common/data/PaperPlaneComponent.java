package io.github.chefmooon.playfulplanes.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.chefmooon.playfulplanes.common.data.types.PaperPlaneType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record PaperPlaneComponent(PaperPlaneType paperPlaneType) {
	public static final Codec<PaperPlaneComponent> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			PaperPlaneType.CODEC.optionalFieldOf("type", PaperPlaneType.BASIC).forGetter(PaperPlaneComponent::paperPlaneType)
		).apply(instance, PaperPlaneComponent::new)
	);

//	public static final PacketCodecs<ByteBuf, PaperPlaneComponent> PACKET_CODEC = PacketCodec.tuple(
//		PaperPlaneType.PACKET_CODEC, PaperPlaneComponent::getType,
//		PaperPlaneComponent::new
//	);

	public PaperPlaneType getType() {
		return paperPlaneType;
	}
}
