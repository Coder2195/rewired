package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class NorGateBlock extends OrGateBlock {
	public static final MapCodec<NorGateBlock> CODEC = simpleCodec(NorGateBlock::new);

	public NorGateBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput) {
		return super.calcSignal(leftInput, centerInput, rightInput) > 0 ? 0 : 15;
	}

	@Override
	protected @NonNull MapCodec<? extends DiodeBlock> codec() {
		return CODEC;
	}
}
