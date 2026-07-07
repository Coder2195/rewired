package dev.coder2195.rewired.block.entity;

import dev.coder2195.rewired.registry.RewiredBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

public class GateBlockEntity extends BlockEntity {
	private int output = 0;

	public GateBlockEntity(BlockPos worldPosition, BlockState blockState) {
		super(RewiredBlockEntityTypes.GATE.value(), worldPosition, blockState);
	}

	protected void saveAdditional(@NonNull ValueOutput output) {
		super.saveAdditional(output);
		output.putInt("OutputSignal", this.output);
	}

	protected void loadAdditional(@NonNull ValueInput input) {
		super.loadAdditional(input);
		this.output = input.getIntOr("OutputSignal", 0);
	}

	public int getOutputSignal() {
		return this.output;
	}

	public void setOutputSignal(int value) {
		this.output = value;
	}
}
