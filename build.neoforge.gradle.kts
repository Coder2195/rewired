plugins {
	id("net.neoforged.moddev") version "2.0.140"
	id("neoforge-mutex")
	id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22"
}

val modId = property("mod.id") as String
version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${modId}-neoforge"

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
