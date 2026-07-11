plugins {
	id("dev.kikugie.stonecutter")

	id("co.uzzu.dotenv.gradle") version "4.0.0"
}

stonecutter active "fabric"

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
	val loader = current.project
	val version = "26.2"

	// Makes version- and loader-specific properties apply from `stoncutter.properties.toml`
	properties {
		tags(version, loader)
	}

	// Adds constants to Stonecutter comments (i.e. for `//? if fabric {...`)
	constants {
		match(loader, "fabric", "neoforge")
	}

	swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
	swaps["mod_id"] = "\"${properties.get<String>("mod.id")}\";"
	swaps["minecraft"] = "\"${node.metadata.version}\";"
	constants["release"] = properties.get<String>("mod.id") != "template"
	dependencies["fapi"] = properties.getOrNull<String>("deps.fabric_api") ?: "0"
}
