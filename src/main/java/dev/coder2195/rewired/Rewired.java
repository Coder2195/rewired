package dev.coder2195.rewired;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Rewired {
	String MOD_ID = /*$ mod_id*/ "rewired";
	Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	String VERSION = /*$ mod_version*/ "0.1.0";
	String MINECRAFT = /*$ minecraft*/ "26.2";
	LoaderAccess INSTANCE =
		/*? if fabric{*/new FabricLoaderAccess();
	 /*?} elif neoforge *///new NeoforgeLoaderAccess();


	static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	static Identifier id(String namespace, String path) {
		return Identifier.fromNamespaceAndPath(namespace, path);
	}

	static Identifier mcId(String path) {
		return Identifier.withDefaultNamespace(path);
	}

	static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String path) {
		return ResourceKey.create(registry, id(path));
	}

	static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String namespace, String path) {
		return ResourceKey.create(registry, id(namespace, path));
	}

	static <T> ResourceKey<T> mcKey(ResourceKey<? extends Registry<T>> registry, String path) {
		return ResourceKey.create(registry, mcId(path));
	}

	interface LoaderAccess {
		boolean isClient();
		boolean isServer();
		boolean isModLoaded(String id);
	}

	//? if fabric {
	final class FabricLoaderAccess implements LoaderAccess {
		private final net.fabricmc.loader.api.FabricLoader loader = net.fabricmc.loader.api.FabricLoader.getInstance();

		@Override
		public boolean isClient() {
			return loader.getEnvironmentType().equals(net.fabricmc.api.EnvType.CLIENT);
		}

		@Override
		public boolean isServer() {
			return loader.getEnvironmentType().equals(net.fabricmc.api.EnvType.SERVER);
		}

		@Override
		public boolean isModLoaded(String id) {
			return loader.isModLoaded(id);
		}
	}
	//?} elif neoforge {
	/*static final class NeoforgeLoaderAccess implements LoaderAccess {
		private final net.neoforged.api.distmarker.Dist dist =
			/^? if >=1.21.9 {^/net.neoforged.fml.loading.FMLEnvironment.getDist();
		/^?} else^///net.neoforged.fml.loading.FMLEnvironment.dist;
		private final net.neoforged.fml.loading.LoadingModList mods =
			/^? if >=1.21.9 {^/net.neoforged.fml.loading.FMLLoader.getCurrent().getLoadingModList();
		/^?} else^///net.neoforged.fml.loading.FMLLoader.getLoadingModList();

		@Override
		public boolean isClient() {
			return dist.isClient();
		}

		@Override
		public boolean isServer() {
			return dist.isDedicatedServer();
		}

		@Override
		public boolean isModLoaded(String id) {
			return mods.getModFileById(id) != null;
		}
	}
	*///?}
}
