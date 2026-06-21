//? fabric {
package dev.coder2195.rearmament;

import net.fabricmc.api.ModInitializer;
import static dev.coder2195.rearmament.Rearmament.LOGGER;

public class RearmamentFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		LOGGER.info("Fabric!");
	}
}
//? }

