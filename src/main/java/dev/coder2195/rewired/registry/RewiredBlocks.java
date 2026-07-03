package dev.coder2195.rewired.registry;

import dev.coder2195.rewired.Rewired;
import dev.coder2195.rewired.block.AndGateBlock;
import dev.coder2195.rewired.block.GateBlock;
import net.minecraft.core.Holder;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
//? fabric {
/*import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
*///? } else {
import static dev.coder2195.rewired.Rewired.MOD_ID;
import net.neoforged.neoforge.registries.DeferredRegister;
//? }

import java.util.function.Function;

public interface RewiredBlocks {
	//? neoforge
	DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Rewired.MOD_ID);

	BlockItemId AND_GATE_ID = blockItem("and_gate");

	static BlockItemId blockItem(String id) {
		return BlockItemId.create(Rewired.id(id), Rewired.id(id));
	}

	Holder<Block> AND_GATE = register(AND_GATE_ID, AndGateBlock::new, BlockBehaviour.Properties.of());

	static Holder<Block> register(BlockItemId id, Function<BlockBehaviour.Properties, Block> block, BlockBehaviour.Properties properties) {
		var blockKey = id.block();
		properties.setId(blockKey);

		//? fabric
		//return Registry.registerForHolder(BuiltInRegistries.BLOCK, blockKey, block.apply(properties));
		//? neoforge
		return BLOCKS.registerBlock(blockKey.identifier().getPath(), block, () -> properties);
	}

  static void init() {
		Rewired.LOGGER.info("Registering Rewired blocks");
  }
}
