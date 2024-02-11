plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
}

val artifact = VersionCatalog.artifactName("hltv")

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
            api(libs.coroutines)
            implementation(libs.tooling)
            implementation(libs.ktsoup)
            implementation(libs.ktsoup.ktor)
            api(libs.ktor)
            api(libs.datetime)
            api(libs.tooling.country)
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