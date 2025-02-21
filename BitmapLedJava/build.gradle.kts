/*
 * Copyright (c) 2025 Lolosia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.daemon.common.toHexString
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.security.MessageDigest


plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
    id("org.jetbrains.kotlin.kapt") version "2.1.10"
    // id("org.jetbrains.compose")
    // id("org.jetbrains.kotlin.plugin.compose")
    id("io.ebean") version "15.1.0"
    id("org.springframework.boot") version "3.2.5"
    // id("io.spring.dependency-management") version "1.1.6" apply false
}

group = "top.lolosia"
version = "0.0.1-SNAPSHOT"

dependencies {

    /* Kotlin */
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    /* Spring Boot */
    implementation("org.springframework.boot:spring-boot-starter:3.3.5")
    implementation("org.springframework.boot:spring-boot-starter-integration:3.3.5")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.3.5")
    developmentOnly("org.springframework.boot:spring-boot-starter-actuator:3.3.5")
    // developmentOnly("org.springframework.boot:spring-boot-devtools:3.2.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.5")
    implementation("org.springframework.security:spring-security-crypto:6.2.4")

    /* Other */
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.4")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("net.java.dev.jna:jna:5.16.0")
    implementation("net.java.dev.jna:jna-platform:5.16.0")
    implementation("org.jboss.errai.reflections:reflections:4.15.0.Final")
    implementation("org.spongepowered:mixin:0.8.7")
    annotationProcessor("org.spongepowered:mixin:0.8.7:processor")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.ow2.asm:asm:9.7")
    implementation("org.ow2.asm:asm-tree:9.7")
    implementation("org.ow2.asm:asm-util:9.7")
    implementation("org.ow2.asm:asm-commons:9.7")


    /* Junit Test */
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    /* Data Source */
    implementation(project(":orm"))
    implementation("io.ebean:ebean:15.1.0") {
        exclude("io.ebean", "ebean-platform-all")
        exclude("io.ebean", "ebean-querybean")
    }
    implementation("io.ebean:ebean-platform-sqlite:15.1.0")
    implementation("org.xerial:sqlite-jdbc:3.43.2.1")
    kapt("io.ebean:kotlin-querybean-generator:15.1.0")
    testImplementation("io.ebean:ebean-test:15.1.0")

    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.jmdns:jmdns:3.5.12")
    implementation("com.illposed.osc:javaosc-core:0.9")

    // Kotlin Compose
    // implementation(compose.desktop.currentOs)
    // implementation("io.github.vinceglb:filekit-compose:0.8.7")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

sourceSets {
    main {
        java {
            srcDir(projectDir.resolve("src/main/kotlin"))
        }
    }
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf("--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED")
}

ebean {
    debugLevel = 1
}

springBoot {
    mainClass = "top.lolosia.vrc.led.Launcher"
}

tasks.jar {
    dependsOn(":generateDependencyUrls")
    archiveFileName = "${rootProject.name}-${rootProject.version}.jar"
    manifest {
        val at = attributes
        at["Main-Class"] = "top.lolosia.vrc.led.Launcher"
        at["Implementation-Title"] = rootProject.name
        at["Implementation-Version"] = rootProject.version
    }
}

tasks.bootJar {
    dependsOn(tasks.jar)
    dependsOn(":static:jar")
    archiveClassifier = "fat"

    val staticJar = project(":static").tasks.jar.get().outputs.files.singleFile
    from(zipTree(staticJar)) {
        exclude("META-INF/MANIFEST.MF")
        into("BOOT-INF/classes")
    }
}

tasks.register<Jar>("installerJar") {
    group = "build"
    dependsOn(tasks.jar)
    dependsOn(":static:jar")
    archiveClassifier = "installer"
    fun mergeJar(task: Jar, conf: CopySpec.() -> Unit = {}) {
        val staticJar = task.outputs.files.singleFile
        from(zipTree(staticJar)) {
            exclude("META-INF/MANIFEST.MF")
            into("")
            conf()
        }
    }
    mergeJar(project(":static").tasks.jar.get())
    mergeJar(project(":orm").tasks.jar.get())
    mergeJar(tasks.jar.get())
}

tasks.register("generateDependencyUrls") {
    doLast {
        val outputFile = file("$projectDir/build/resources/main/dependencies-urls.json")
        outputFile.delete()

        // 怎么获得 settings.dependencyResolutionManagement.repositories?
        val repos = repositories.filterIsInstance<UrlArtifactRepository>().map { it.url.toString() }.toMutableList()
        var lines = File("${projectDir}/settings.gradle.kts").readLines()
        lines = lines.dropWhile { !it.contains("dependencyResolutionManagement") }
        lines.forEach {
            if (it.contains("https://")) {
                val url = "https://" + it.replace(".+https://".toRegex(), "")
                    .replace("\".+$".toRegex(), "")
                repos.add(url)
            } else if (it.contains("mavenCentral()")) {
                repos.add("https://repo.maven.apache.org/maven2")
            } else if (it.contains("google()")) {
                repos.add("https://dl.google.com/dl/android/maven2")
            }
        }
        val deps = runBlocking {
            configurations.productionRuntimeClasspath.get()
                .resolvedConfiguration.resolvedArtifacts.map { artifact ->
                    val group = artifact.moduleVersion.id.group
                    val name = artifact.moduleVersion.id.name
                    val version = artifact.moduleVersion.id.version
                    val extension = artifact.extension
                    val classifier = artifact.classifier ?: ""

                    val fileName = artifact.file.name

                    val groupUrl = group.replace(".", "/")
                    async(Dispatchers.IO) {
                        mapOf(
                            "group" to group,
                            "name" to name,
                            "version" to version,
                            "extension" to extension,
                            "classifier" to classifier,
                            "sha256" to artifact.file.sha256,
                            "url" to "/${groupUrl}/$name/$version/$fileName",
                        )
                    }
                }.awaitAll()
        }.filter { it["url"]?.contains("ebean-querybean") != true }

        val rs = mapOf("repositories" to repos, "dependencies" to deps)
        val jsonMapper = ObjectMapper()
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        val rsJson = jsonMapper.writeValueAsString(rs)
        outputFile.writeText(rsJson)
    }
}


val File.sha256: String
    get() {
        val digest = MessageDigest.getInstance("SHA-256")

        inputStream().use { fis ->
            val bytes = ByteArray(8192) // 8 KB buffer
            while (fis.read(bytes) != -1) {
                digest.update(bytes)
            }
            return digest.digest().toHexString()
        }
    }