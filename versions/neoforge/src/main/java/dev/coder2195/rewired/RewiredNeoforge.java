package dev.coder2195.rewired;

import dev.coder2195.rewired.registry.RewiredBlocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import static dev.coder2195.rewired.Rewired.LOGGER;
import static dev.coder2195.rewired.Rewired.MOD_ID;

@Mod(MOD_ID)
public class RewiredNeoforge {
	public RewiredNeoforge(IEventBus modEventBus, ModContainer modContainer) {
		LOGGER.info("Neoforge!");

		RewiredBlocks.BLOCKS.register(modEventBus);
	}
}