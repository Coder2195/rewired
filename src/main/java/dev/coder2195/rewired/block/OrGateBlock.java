package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class OrGateBlock extends LogicGateBlock {
	public static final MapCodec<OrGateBlock> CODEC = simpleCodec(OrGateBlock::new);

	public OrGateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput) {
		if (leftInput.map(i -> i > 0).orElse(false) || centerInput.map(i -> i > 0).orElse(false) || rightInput.map(i -> i > 0).orElse(false))
			return 15;
		return 0;
	}

	@Override
	protected @NonNull MapCodec<? extends DiodeBlock> codec() {
		return CODEC;
	}
}
