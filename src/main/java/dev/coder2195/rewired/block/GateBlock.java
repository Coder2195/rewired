package dev.coder2195.rewired.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.TickPriority;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public abstract class GateBlock extends DiodeBlock {
	public static final BooleanProperty LEFT_INPUT = BooleanProperty.create("left_input");
	public static final BooleanProperty RIGHT_INPUT = BooleanProperty.create("right_input");
	public static final BooleanProperty CENTER_INPUT = BooleanProperty.create("center_input");

	public GateBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LEFT_INPUT, true).setValue(RIGHT_INPUT, true).setValue(CENTER_INPUT, true).setValue(POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LEFT_INPUT, RIGHT_INPUT, CENTER_INPUT, POWERED, FACING);
	}

	@Override
	protected int getDelay(@NonNull BlockState state) {
		return 0;
	}


	@Override
	protected int getInputSignal(Level level, BlockPos pos, BlockState state) {

		Direction direction = state.getValue(FACING);

		var leftOn = state.getValue(LEFT_INPUT);
		var centerOn = state.getValue(CENTER_INPUT);
		var rightOn = state.getValue(RIGHT_INPUT);

		BlockPos centerPos = pos.relative(direction);
		BlockState centerBlockState = level.getBlockState(centerPos);


		int centerSignal = Math.max(level.getSignal(centerPos, direction), centerBlockState.is(Blocks.REDSTONE_WIRE) ? centerBlockState.getValue(RedStoneWireBlock.POWER) : 0);

		BlockPos rightPos = pos.relative(direction.getCounterClockWise());
		BlockState rightBlockState = level.getBlockState(rightPos);
		int rightSignal = Math.max(level.getSignal(rightPos, direction), rightBlockState.is(Blocks.REDSTONE_WIRE) ? rightBlockState.getValue(RedStoneWireBlock.POWER) : 0);

		BlockPos leftPos = pos.relative(direction.getClockWise());
		BlockState leftBlockState = level.getBlockState(leftPos);
		int leftSignal = Math.max(level.getSignal(leftPos, direction), leftBlockState.is(Blocks.REDSTONE_WIRE) ? leftBlockState.getValue(RedStoneWireBlock.POWER) : 0);

		return calcSignal(
			leftOn ? Optional.of(leftSignal) : Optional.empty(),
			centerOn ? Optional.of(centerSignal) : Optional.empty(),
			rightOn ? Optional.of(rightSignal) : Optional.empty()
		);
	}

	public abstract int calcSignal(Optional<Integer> leftInput, Optional<Integer> centerInput, Optional<Integer> rightInput);


	@Override
	protected void tick(final @NonNull BlockState state, final @NonNull ServerLevel level, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
		boolean on = state.getValue(POWERED);
		boolean shouldTurnOn = this.shouldTurnOn(level, pos, state);
		if (on == shouldTurnOn) return;
		level.setBlock(pos, state.setValue(POWERED, shouldTurnOn), 2);
		level.scheduleTick(pos, this, this.getDelay(state), TickPriority.VERY_HIGH);
	}

	@Override
	protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
		if (!player.getAbilities().mayBuild) {
			return InteractionResult.PASS;
		}

		var facing = state.getValue(FACING);
		var hitPosition = hitResult.getLocation();
		var xOffset = hitPosition.x - pos.getX();
		var zOffset = hitPosition.z - pos.getZ();


		BooleanProperty toToggle = null;
		// directions are hella weird, facing actually points opposite, but i'm gonna use the direction where the signal faces as north when the gate is north
		boolean northClick = 0.33 < xOffset && xOffset < 0.67 && 0.67 < zOffset;
		boolean westClick = 0.67 < xOffset && 0.33 < zOffset && zOffset < 0.67;
		boolean eastClick = xOffset < 0.33 && 0.33 < zOffset && zOffset < 0.67;
		boolean southClick = 0.33 < xOffset && xOffset < 0.67 && zOffset < 0.33;

		if (facing.equals(Direction.NORTH)) {
			toToggle = westClick ? LEFT_INPUT : southClick ? CENTER_INPUT : eastClick ? RIGHT_INPUT : null;
		} else if (facing.equals(Direction.SOUTH)) {
			toToggle = eastClick ? LEFT_INPUT : northClick ? CENTER_INPUT : westClick ? RIGHT_INPUT : null;
		} else if (facing.equals(Direction.EAST)) {
			toToggle = northClick ? LEFT_INPUT : westClick ? CENTER_INPUT : southClick ? RIGHT_INPUT : null;
		} else if (facing.equals(Direction.WEST)) {
			toToggle = southClick ? LEFT_INPUT : eastClick ? CENTER_INPUT : northClick ? RIGHT_INPUT : null;
		}


		if (toToggle != null) {
			var newState = state.setValue(toToggle, !state.getValue(toToggle));
			level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
			level.scheduleTick(pos, this, this.getDelay(newState));
			return InteractionResult.SUCCESS;
		}

		return super.useWithoutItem(state, level, pos, player, hitResult);
	}
}
