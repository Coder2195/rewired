package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class AverageGateBlock extends LogicGateBlock {
	public static final MapCodec<AverageGateBlock> CODEC = simpleCodec(AverageGateBlock::new);

	public AverageGateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput) {
		int total = 0, count = 0;
		if (leftInput.isPresent()) {
			total += leftInput.get();
			count++;
		}
		if (centerInput.isPresent()) {
			total += centerInput.get();
			count++;
		}
		if (rightInput.isPresent()) {
			total += rightInput.get();
			count++;
		}
		return count > 0 ? total / count : 0;
	}

	@Override
	protected @NonNull MapCodec<? extends DiodeBlock> codec() {
		return CODEC;
	}
}
