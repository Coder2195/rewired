package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class MinGateBlock extends GateBlock {
	public static final MapCodec<MinGateBlock> CODEC = simpleCodec(MinGateBlock::new);

	public MinGateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput) {
		int value = -1;
		if (leftInput.isPresent()) value = leftInput.get();
		if (centerInput.isPresent() && value < 0) value = centerInput.get();
		if (rightInput.isPresent() && value < 0) value = rightInput.get();

		return Math.max(0, value);
	}

	@Override
	protected @NonNull MapCodec<? extends DiodeBlock> codec() {
		return CODEC;
	}
}
