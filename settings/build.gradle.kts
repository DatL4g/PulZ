plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
}

val artifact = VersionCatalog.artifactName("settings")

kotlin {
    jvm()
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    macosX64()
    macosArm64()

    js(IR) {
        nodejs()
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            api(libs.collections.immutable)
            api(libs.coroutines)
            implementation(libs.serialization.protobuf)
            implementation(libs.tooling)
        }

        val nonJsMain by creating {
            dependsOn(commonMain.get())

            jvmMain.get().dependsOn(this)
            androidMain.get().dependsOn(this)
            nativeMain.get().dependsOn(this)

            dependencies {
                api(libs.datastore)
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