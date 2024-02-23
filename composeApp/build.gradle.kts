import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.osdetector)
    alias(libs.plugins.moko.resources)
    alias(libs.plugins.sekret)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ktorfit)
}

val artifact = VersionCatalog.artifactName()

group = artifact
version = appVersion

multiplatformResources {
    resourcesPackage.set(artifact)
    resourcesClassName.set("SharedRes")
}

sekret {
    properties {
        enabled.set(true)
        packageName.set(artifact)

        androidJNIFolder.set(project.layout.projectDirectory.dir("src/androidMain/jniLibs"))
        desktopComposeResourcesFolder.set(project.layout.projectDirectory.dir("src/desktopMain/jniLibs"))
    }
}

kotlin {
    /** Use as soon as wasm is available in every dependency
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
    */
    
    androidTarget()
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    applyDefaultHierarchyTemplate()
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)

            api(libs.decompose)
            implementation(libs.decompose.compose)
            implementation(libs.coroutines)
            implementation(libs.kodein)
            implementation(libs.kodein.compose)

            implementation(libs.tooling.decompose)
            implementation(libs.napier)
            implementation(libs.moko.resources.compose)

            implementation(libs.windowsize)

            implementation(libs.coil)
            implementation(libs.coil.network)
            implementation(libs.coil.svg)
            implementation(libs.coil.compose)

            implementation(libs.compottie)

            implementation(libs.kmpalette)
            implementation(libs.kolor)

            implementation(libs.haze)
            implementation(libs.haze.materials)

            implementation(libs.datetime)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.serialization.json)

            implementation(project(":settings"))
            implementation(project(":hltv"))
            implementation(project(":game"))
            implementation(project(":rawg"))
        }

        nativeMain.dependencies {
            implementation(libs.ktor.darwin)
        }

        androidMain.dependencies {
            implementation(libs.android)
            implementation(libs.activity)
            implementation(libs.activity.compose)
            implementation(libs.ads)
            implementation(libs.appcompat)
            implementation(libs.multidex)

            implementation(libs.ktor.jvm)
        }
        androidMain.configure {
            apply(plugin = "kotlin-parcelize")
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.context.menu)

                implementation(libs.ktor.jvm)
            }
        }
    }
}

android {
    sourceSets["main"].setRoot("src/androidMain/")
    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    sourceSets["main"].assets.srcDirs("src/androidMain/assets", "src/commonMain/assets")
    compileSdk = Configuration.compileSdk
    namespace = artifact

    defaultConfig {
        applicationId = artifact
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = appVersionCode
        versionName = appVersion

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = CompileOptions.sourceCompatibility
        targetCompatibility = CompileOptions.targetCompatibility
    }
    buildFeatures {
        buildConfig = true
    }
}

compose.desktop {
    application {
        mainClass = "$artifact.MainKt"

        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/sun.java2d=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED")
        when (getHost()) {
            Host.Linux -> {
                jvmArgs("--add-opens", "java.desktop/sun.awt.X11=ALL-UNNAMED")
                jvmArgs("--add-opens", "java.desktop/sun.awt.wl=ALL-UNNAMED")
            }
            Host.MAC -> {
                jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
                jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
            }
            else -> { }
        }

        nativeDistributions {
            packageName = "Gamechanger"
            packageVersion = appVersion
            outputBaseDir.set(rootProject.layout.buildDirectory.asFile.get().resolve("release"))
            appResourcesRootDir.set(project.layout.projectDirectory.dir("src/desktopMain/jniLibs").also {
                println(it.asFile.canonicalPath)
            })

            when (getHost()) {
                Host.Linux -> targetFormats(
                    TargetFormat.AppImage, TargetFormat.Deb, TargetFormat.Rpm
                )
                Host.MAC -> targetFormats(
                    TargetFormat.Dmg
                )
                Host.Windows -> targetFormats(
                    TargetFormat.Exe, TargetFormat.Msi
                )
            }

            includeAllModules = true
        }
    }
}

compose.experimental {
    web.application { }
}

fun getHost(): Host {
    return when (osdetector.os) {
        "linux" -> Host.Linux
        "osx" -> Host.MAC
        "windows" -> Host.Windows
        else -> {
            val hostOs = System.getProperty("os.name")
            val isMingwX64 = hostOs.startsWith("Windows")

            when {
                hostOs == "Linux" -> Host.Linux
                hostOs == "Mac OS X" -> Host.MAC
                isMingwX64 -> Host.Windows
                else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
            }
        }
    }
}

enum class Host(val label: String) {
    Linux("linux"),
    Windows("win"),
    MAC("mac");
}