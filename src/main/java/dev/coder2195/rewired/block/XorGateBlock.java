package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.stream.Stream;

public class XorGateBlock extends GateBlock {
	public static MapCodec<XorGateBlock> CODEC = simpleCodec(XorGateBlock::new);

	public XorGateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput) {
		return Stream.of(leftInput, centerInput, rightInput).filter(Optional::isPresent).filter(i -> i.get() > 0).count() % 2 == 1 ? 15 : 0;
	}

	@Override
	protected @NonNull MapCodec<? extends DiodeBlock> codec() {
		return CODEC;
	}
}
