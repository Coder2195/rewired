package dev.coder2195.rewired.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DiodeBlock;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class XnorGateBlock extends NorGateBlock {
	public static final MapCodec<XnorGateBlock> CODEC = simpleCodec(XnorGateBlock::new);

	public XnorGateBlock(Properties properties) {
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
