plugins {
	base
	java
	idea
	`maven-publish`
	alias(libs.plugins.fabric.loom)
}

group = libs.versions.maven.group.get()
version = "${libs.versions.minecraft.get()}-${libs.versions.mod.get()}"

base {
	archivesName.set(libs.versions.archives.name)
}

repositories {
	mavenCentral()
}

dependencies {
	minecraft(libs.minecraft)
	mappings(libs.yarn)
	modImplementation(libs.bundles.fabric)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17

	withSourcesJar()
}

tasks {
	processResources {
		inputs.property("version", libs.versions.mod.get())

		filesMatching("fabric.mod.json") {
			expand(mapOf("version" to libs.versions.mod.get()))
		}
	}

	jar {
		from("LICENSE")
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	repositories {
	}
}
