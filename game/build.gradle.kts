plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
}

val artifact = VersionCatalog.artifactName("game")

kotlin {
    jvm()
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
    macosArm64()

    linuxX64()
    linuxArm64()

    mingwX64()

    js(IR) {
        nodejs()
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            api(libs.coroutines)
            implementation(libs.tooling)
            implementation(libs.serialization)
            implementation(libs.serialization.json)
            implementation(libs.okio)
            implementation(libs.codepoints)

            implementation(project(":model"))
        }

        val nonJsMain by creating {
            dependsOn(commonMain.get())

            jvmMain.get().dependsOn(this)
            androidMain.get().dependsOn(this)
            nativeMain.get().dependsOn(this)
        }

        val javaMain by creating {
            dependsOn(nonJsMain)

            jvmMain.get().dependsOn(this)
            androidMain.get().dependsOn(this)

            dependencies {
                implementation(libs.apache.commons.io)
            }
        }

        val desktopMain by creating {
            dependsOn(nonJsMain)

            jvmMain.get().dependsOn(this)
            linuxMain.get().dependsOn(this)
            mingwMain.get().dependsOn(this)
            macosMain.get().dependsOn(this)

            dependencies {
                implementation(libs.kommand)
            }
        }
    }
}

android {
    compileSdk = Configuration.compileSdk
    namespace = artifact

    defaultConfig {
        minSdk = Configuration.minSdk
    }
    compileOptions {
        sourceCompatibility = CompileOptions.sourceCompatibility
        targetCompatibility = CompileOptions.targetCompatibility
    }
}