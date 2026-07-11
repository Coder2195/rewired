package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class MaxGateBlock extends GateBlock {
	public static final MapCodec<MaxGateBlock> CODEC = simpleCodec(MaxGateBlock::new);

	public MaxGateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput) {
		return Math.max(leftInput.orElse(0), Math.max(centerInput.orElse(0), rightInput.orElse(0)));
	}

	@Override
	protected @NonNull MapCodec<? extends DiodeBlock> codec() {
		return CODEC;
	}
}
