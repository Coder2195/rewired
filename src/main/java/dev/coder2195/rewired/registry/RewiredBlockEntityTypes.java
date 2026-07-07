package dev.coder2195.rewired.registry;


import dev.coder2195.rewired.Rewired;
import dev.coder2195.rewired.block.entity.GateBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

//? neoforge {
import net.neoforged.neoforge.registries.DeferredRegister;

//? } else {
/*import net.minecraft.core.Registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
*///? }

public interface RewiredBlockEntityTypes {
	//? neoforge
	DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Rewired.MOD_ID);

	Holder<BlockEntityType<?>> GATE = register("gate", GateBlockEntity::new, RewiredBlocks.GATES);

	@SafeVarargs
	static <T extends BlockEntity> Holder<BlockEntityType<?>> register(String id,
	                                                                   /*? fabric {*//*FabricBlockEntityTypeBuilder.Factory<T>*//*? } else { */BlockEntityType.BlockEntitySupplier<T>/*? } */ entityFactory,
	                                                                   Holder<Block>... blockHolders) {
		//? fabric
		//return Registry.registerForHolder(BuiltInRegistries.BLOCK_ENTITY_TYPE, Rewired.id(id), FabricBlockEntityTypeBuilder.create(entityFactory, Arrays.stream(blockHolders).map(Holder::value).toArray(Block[]::new)).build());
		//? neoforge
		return BLOCK_ENTITY_TYPES.register(id, () -> new BlockEntityType<>(entityFactory, Arrays.stream(blockHolders).map(Holder::value).collect(Collectors.toSet())));
	}

	static void init() {
		Rewired.LOGGER.info("Registering Rewired Block Entity Types");
	}
}
