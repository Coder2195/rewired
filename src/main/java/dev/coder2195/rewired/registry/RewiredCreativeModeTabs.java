package dev.coder2195.rewired.registry;

import dev.coder2195.rewired.Rewired;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
//? fabric {
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
//? } else {
/*import net.neoforged.neoforge.registries.DeferredRegister;
*///? }

import java.util.List;
import java.util.function.Supplier;

public interface RewiredCreativeModeTabs {
	//? neoforge
	//DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Rewired.MOD_ID);

	Holder<CreativeModeTab> GATES = register("gates", () -> new ItemStack(RewiredItems.OR_GATE.value()), (itemDisplayParameters, output) -> {
		for (var item: List.of(RewiredItems.AND_GATE, RewiredItems.OR_GATE, RewiredItems.XOR_GATE, RewiredItems.NAND_GATE, RewiredItems.NOR_GATE, RewiredItems.XNOR_GATE, RewiredItems.AVERAGE_GATE)) {
			output.accept(item.value());
		}
	});

	static Holder<CreativeModeTab> register(String id, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator generator) {
		var builder = /*? fabric {*/FabricCreativeModeTab.builder()/*? } else {*/ /*CreativeModeTab.builder()*//*? }*/;
		var built = builder.title(Component.translatable("itemGroup." + Rewired.MOD_ID + "." + id)).icon(icon).displayItems(generator).build();
		//? fabric
		return Registry.registerForHolder(BuiltInRegistries.CREATIVE_MODE_TAB, Rewired.id(id), built);
		//? neoforge
		//return CREATIVE_MODE_TABS.register(id, () -> built);
	}

	static void init() {
		Rewired.LOGGER.info("Registering Rewired Creative Mode tabs");
	}
}
