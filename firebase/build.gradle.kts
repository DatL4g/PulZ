plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
}

val artifact = VersionCatalog.artifactName("firebase")

kotlin {
    jvm()
    androidTarget()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.firebase.auth)
            implementation(libs.tooling)
        }

        androidMain.dependencies {
            implementation(libs.firebase.android)
            implementation(libs.firebase.android.analytics)
            implementation(libs.firebase.android.auth)
            implementation(libs.firebase.android.crashlytics)

            implementation(libs.firebase.crashlytics)
        }

        nativeMain.dependencies {
            implementation(libs.firebase.crashlytics)
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
