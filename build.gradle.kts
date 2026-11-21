plugins {
    id("java-library")
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://libraries.minecraft.net/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases/")
    maven("https://repo.jeff-media.com/public/")
    maven("https://repo.citizensnpcs.co/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.1-SNAPSHOT")
    // Local jars that are not on Maven (e.g. NativePackets API). Drop them in libs/.
    compileOnly(fileTree("libs") { include("*.jar") })
    compileOnly("com.mojang:datafixerupper:9.0.19")
    compileOnly("com.mojang:authlib:7.0.61")
    compileOnly("com.mojang:logging:1.6.11")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("io.netty:netty-transport:4.2.7.Final")
    compileOnly("io.netty:netty-buffer:4.2.7.Final")
    compileOnly("io.netty:netty-codec-http:4.2.7.Final")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.4.2")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.17")
    compileOnly("io.lumine:Mythic-Dist:5.14.0")
    compileOnly("com.github.SkriptLang:Skript:2.17.1")
    compileOnly("com.jeff_media:ArmorEquipEvent:1.0.1")
    compileOnly("net.citizensnpcs:citizensapi:2.0.44-SNAPSHOT")
    compileOnly("com.jayway.jsonpath:json-path:3.0.0")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.16.2")
    compileOnly("me.clip:placeholderapi:2.12.3")
    compileOnly("org.apache.httpcomponents:httpclient:4.5.14")
    compileOnly("org.lwjgl:lwjgl-shaderc:3.4.3")
    compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("org.jctools:jctools-core:4.0.7")
    compileOnly("org.apache.logging.log4j:log4j-api:2.25.2")
    compileOnly("org.apache.logging.log4j:log4j-core:2.25.2")
    compileOnly("net.kyori:adventure-api:4.17.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.17.0")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.66")
    implementation("it.unimi.dsi:fastutil:8.5.6")
    implementation("commons-lang:commons-lang:2.6")
    implementation("commons-io:commons-io:2.20.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("beer.devs:FastNbt-core:1.4.8")
    implementation("com.github.stephengold:jolt-jni-Windows64:7.0.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

// spigot-api and paper-api provide the same capability; Paper's API is the superset, so it wins.
configurations.all {
    resolutionStrategy.capabilitiesResolution.withCapability("org.spigotmc:spigot-api") {
        select("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    }
}

sourceSets {
    main {
        java {
            // Target: Minecraft 1.21.11 (CraftBukkit/NMS v1_21_R7).
            // Adapters for other server versions (v1_20_R4, v1_21_R1..R6) are not compiled.
            exclude { element ->
                val f = element.file
                f.isFile && (
                    f.name.matches(Regex(".*V1_(20_R4|21_R[1-6]).*\\.java")) ||
                        f.readText().contains(Regex("""craftbukkit\.v1_(20_R4|21_R[1-6])\b""")))
            }

            // Simplified build: sources that do not compile against the 1.21.11 server yet.
            // Files are kept in src/, only removed from compilation. One path per line, relative to src/main/java.
            val listed = file("compile-exclusions.txt")
            if (listed.exists()) {
                val excludedPaths = listed.readLines().map { it.trim() }.filter { it.isNotEmpty() }.toSet()
                exclude { element -> excludedPaths.contains(element.relativePath.pathString) }
            }
        }
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    withType<Javadoc>().configureEach {
        (options as StandardJavadocDocletOptions).encoding = "UTF-8"
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
