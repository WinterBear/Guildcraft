/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven("https://repo.papermc.io/repository/maven-public/")

    maven {
        url = uri("https://mammoth.snowcave.dev")
    }

    maven {
        url = uri("https://libraries.minecraft.net/")
    }

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-core:2.15.2")
    api("io.github.winterbear.wintercore:plugin-annotations:1.0.5")
    api("io.github.winterbear.wintercore:WinterCoreUtils:1.0.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.12.7.1")
    shadow("com.sk89q.worldguard:worldguard-bukkit:7.0.5-SNAPSHOT")
    shadow("net.kyori:adventure-text-minimessage:4.14.0")
    shadow("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
    shadow("com.github.MilkBowl:VaultAPI:1.7")
    shadow("xyz.jpenilla:squaremap-api:1.1.0-SNAPSHOT")
    shadow("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.28.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

shadow {
    dependencies {
        shadow("com.sk89q.worldguard:worldguard-bukkit:7.0.5-SNAPSHOT")
        shadow("net.kyori:adventure-text-minimessage:4.14.0")
        shadow("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
        shadow("com.github.MilkBowl:VaultAPI:1.7")
        shadow("xyz.jpenilla:squaremap-api:1.1.0-SNAPSHOT")
        shadow("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    }
}



java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

group = "dev.snowcave.winterbear"
version = "1.3.0-SNAPSHOT"
description = "Guilds"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
