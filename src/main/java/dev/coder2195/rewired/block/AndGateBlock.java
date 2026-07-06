package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class AndGateBlock extends LogicGateBlock {
	public static MapCodec<AndGateBlock> CODEC = simpleCodec(AndGateBlock::new);

	public AndGateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput) {
		if (leftInput.isEmpty() && centerInput.isEmpty() && rightInput.isEmpty())
			return 0;

		return (
			leftInput.map(i -> i == 0).orElse(false)
				|| centerInput.map(i -> i == 0).orElse(false)
				|| rightInput.map(i -> i == 0).orElse(false)) ? 0 : 15;
	}

	@Override
	protected @NonNull MapCodec<? extends DiodeBlock> codec() {
		return CODEC;
	}
}
