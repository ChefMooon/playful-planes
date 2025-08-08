package io.github.chefmooon.playfulplanes.common.data.types;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.DyeColor;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.Locale;
import java.util.function.IntFunction;

public enum PaperPlaneType implements StringIdentifiable {
	BASIC(0),
	MESSAGE(1),
	POTION(2),
	TORCH(3),
	FIREWORK(4),
	TNT(5),
	;

	private static final IntFunction<PaperPlaneType> INDEX_MAPPER = ValueLists.createIndexToValueFunction(PaperPlaneType::getIndex, values(), (ValueLists.OutOfBoundsHandling)ValueLists.OutOfBoundsHandling.ZERO);
	public static final StringIdentifiable.EnumCodec<PaperPlaneType> CODEC = StringIdentifiable.createCodec(PaperPlaneType::values);
	public static final PacketCodec<ByteBuf, PaperPlaneType> PACKET_CODEC = PacketCodecs.indexed(INDEX_MAPPER, PaperPlaneType::getIndex);

	private final int index;

	PaperPlaneType(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String asString() {
		return name().toLowerCase();
	}
}
