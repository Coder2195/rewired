plugins {
	id("net.neoforged.moddev") version "2.0.140"
	id("neoforge-mutex")
	id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22"
	id("me.modmuss50.mod-publish-plugin") version "2.1.1"
}

val modId = property("mod.id") as String
version = "${property("mod.version")}"
base.archivesName = "${modId}-neoforge"

val compatibleVersions: List<String> = sc.properties.rawOrNull("mod", "mc_releases")
	?.asList().orEmpty().map { it.toString() }

val requiredJava = JavaVersion.VERSION_25

repositories {
	/**
	 * Restricts dependency search of the given [groups] to the [maven URL][url],
	 * improving the setup speed.
	 */
	fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
		forRepository { maven(url) { name = alias } }
		filter { groups.forEach(::includeGroup) }
	}
	strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
	strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
}

dependencies {

}

neoForge {
	version = property("deps.neo_loader") as String

	runs {
		register("client") {
			client()

			systemProperty("neoforge.enabledGameTestNamespaces", modId)
		}

		register("server") {
			gameDirectory = file("./serverrun/")
			server()

			systemProperty("neoforge.enabledGameTestNamespaces", modId)
		}
	}

	mods {
		// define mod <-> source bindings
		// these are used to tell the game which sources are for which mod
		// multi mod projects should define one per mod
		sourceSets.register("client")
		register(modId) {
			sourceSet(sourceSets["main"])
			sourceSet(sourceSets["test"])
		}
	}
}

java {
	withSourcesJar()
	targetCompatibility = requiredJava
	sourceCompatibility = requiredJava
}

tasks {
	processResources {
		fun MutableMap<String, String>.register(key: String, property: String) {
			val value: String = sc.properties[property]
			inputs.property(key, value)
			set(key, value)
		}
		sourceSets["main"].resources {
			srcDir(rootProject.file("src/generated/resources"))
		}

		val props = buildMap {
			register("id", "mod.id")
			register("name", "mod.name")
			register("version", "mod.version")
			register("minecraft", "mod.mc_compat")
			register("description", "mod.description")
		}

		filesMatching("META-INF/neoforge.mods.toml") { expand(props) }

		val mixinJava = "JAVA_${requiredJava.majorVersion}"
		filesMatching("*.mixins.json") { expand("java" to mixinJava) }

		exclude("fabric.mod.json", "*.ct", "*.classtweaker")


	}

	named("createMinecraftArtifacts") {
		dependsOn("stonecutterGenerate")
	}

	// Builds the version into a shared folder in `build/libs/${mod version}/`
	register<Copy>("buildAndCollect") {
		group = "build"
		description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

		inputs.property("version", project.property("mod.version"))
		from(jar.flatMap { it.archiveFile }, named<Jar>("sourcesJar").flatMap { it.archiveFile })
		into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
	}
}

fletchingTable {
	neoforge /* or neoforge { } */ {
		applyMixinConfig = false
	}
}

publishMods {
	file = tasks.jar.map { it.archiveFile.get() }
	displayName = "${property("mod.name")} ${property("mod.version")} for Neoforge"
	
	version = property("mod.version") as String
	changelog.set(rootProject.file("CHANGELOG.md").readText())
	type = STABLE
	modLoaders.add("neoforge")

	dryRun = !env.isPresent("MODRINTH_TOKEN")
		|| !env.isPresent("CURSEFORGE_TOKEN")

	modrinth {
		projectId = property("publish.modrinth") as String
		accessToken = env.fetch("MODRINTH_TOKEN", "")
		minecraftVersions.addAll(compatibleVersions)
		type = ALPHA
		environment = CLIENT_AND_SERVER
	}

	curseforge {
		projectId = property("publish.curseforge") as String
		accessToken = env.fetch("CURSEFORGE_TOKEN", "")
		minecraftVersions.addAll(compatibleVersions)

		client = true
		server = true
		changelogType = "markdown"
		javaVersions.add(JavaVersion.VERSION_25)
	}
}