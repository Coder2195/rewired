
package dev.coder2195.rewired;

import dev.coder2195.rewired.registry.RewiredBlocks;
import dev.coder2195.rewired.registry.RewiredItems;
import net.fabricmc.api.ModInitializer;
import static dev.coder2195.rewired.Rewired.LOGGER;

public class RewiredFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		LOGGER.info("Fabric!");

		RewiredBlocks.init();
		RewiredItems.init();
	}
}

