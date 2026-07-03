package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class AndGateBlock extends GateBlock{
	public static MapCodec<AndGateBlock> CODEC = simpleCodec(AndGateBlock::new);
	public AndGateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput) {
		if (leftInput.isEmpty() && centerInput.isEmpty() && rightInput.isEmpty())
			return 0;
		if (leftInput.map(i -> i==0).orElse(false)) return 0;
		if (centerInput.map(i -> i==0).orElse(false)) return 0;
		if (rightInput.map(i -> i==0).orElse(false)) return 0;
		return 15;
	}

	@Override
	protected @NonNull MapCodec<? extends DiodeBlock> codec() {
		return CODEC;
	}
}
