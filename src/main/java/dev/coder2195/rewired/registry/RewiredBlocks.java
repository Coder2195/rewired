package dev.coder2195.rewired.registry;

import dev.coder2195.rewired.Rewired;
import dev.coder2195.rewired.block.*;
import net.minecraft.core.Holder;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
//? fabric {
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
//? } else {
/*import static dev.coder2195.rewired.Rewired.MOD_ID;
import net.neoforged.neoforge.registries.DeferredRegister;
*///? }

import java.util.function.Function;

public interface RewiredBlocks {
	//? neoforge
	//DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Rewired.MOD_ID);

	BlockItemId AND_GATE_ID = blockItem("and_gate");
	BlockItemId OR_GATE_ID = blockItem("or_gate");
	BlockItemId XOR_GATE_ID = blockItem("xor_gate");
	BlockItemId NAND_GATE_ID = blockItem("nand_gate");
	BlockItemId NOR_GATE_ID = blockItem("nor_gate");
	BlockItemId XNOR_GATE_ID = blockItem("xnor_gate");
	BlockItemId AVERAGE_GATE_ID = blockItem("average_gate");
	static BlockItemId blockItem(String id) {
		return BlockItemId.create(Rewired.id(id), Rewired.id(id));
	}

	Holder<Block> AND_GATE = register(AND_GATE_ID, AndGateBlock::new, BlockBehaviour.Properties.of());
	Holder<Block> OR_GATE = register(OR_GATE_ID, OrGateBlock::new, BlockBehaviour.Properties.of());
	Holder<Block> XOR_GATE = register(XOR_GATE_ID, XorGateBlock::new, BlockBehaviour.Properties.of());
	Holder<Block> NAND_GATE = register(NAND_GATE_ID, NandGateBlock::new, BlockBehaviour.Properties.of());
	Holder<Block> NOR_GATE = register(NOR_GATE_ID, NorGateBlock::new, BlockBehaviour.Properties.of());
	Holder<Block> XNOR_GATE = register(XNOR_GATE_ID, XnorGateBlock::new, BlockBehaviour.Properties.of());
	Holder<Block> AVERAGE_GATE = register(AVERAGE_GATE_ID, AverageGateBlock::new, BlockBehaviour.Properties.of());

	Holder<Block>[] GATES = new Holder[]{
		AND_GATE,
		OR_GATE,
		XOR_GATE,
		NAND_GATE,
		NOR_GATE,
		XNOR_GATE,
		AVERAGE_GATE
	};

	static Holder<Block> register(BlockItemId id, Function<BlockBehaviour.Properties, Block> block, BlockBehaviour.Properties properties) {
		var blockKey = id.block();
		properties.setId(blockKey);

		//? fabric
		return Registry.registerForHolder(BuiltInRegistries.BLOCK, blockKey, block.apply(properties));
		//? neoforge
		//return BLOCKS.registerBlock(blockKey.identifier().getPath(), block, () -> properties);
	}

  static void init() {
		Rewired.LOGGER.info("Registering Rewired blocks");
  }
}
