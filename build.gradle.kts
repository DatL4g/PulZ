import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.yarn
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.file.Files

plugins {
    alias(libs.plugins.aboutlibraries) apply false
    alias(libs.plugins.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.cocoapods) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.moko.resources) apply false
    alias(libs.plugins.osdetector) apply false
    alias(libs.plugins.sekret) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.complete.kotlin)
    alias(libs.plugins.versions)
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://jogamp.org/deployment/maven")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
        classpath(libs.moko.resources.generator)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://jogamp.org/deployment/maven")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = CompileOptions.jvmTarget
    }
    configurations.configureEach {
        exclude("androidx.palette", "palette")
    }
    plugins.withType<YarnPlugin> {
        yarn.yarnLockAutoReplace = true
    }
}

plugins.withType<YarnPlugin> {
    yarn.yarnLockAutoReplace = true
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

fun File.existsSafely() = runCatching {
    Files.exists(this.toPath())
}.getOrNull() ?: runCatching {
    this.exists()
}.getOrNull() ?: false

fun File.create() = runCatching {
    Files.createFile(this.toPath()).toFile()
}.getOrNull() ?: runCatching {
    this.createNewFile()
    this
}.getOrNull() ?: this