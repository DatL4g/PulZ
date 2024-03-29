plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.serialization)
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
            api(libs.flowredux)
            implementation(libs.serialization.json)
            api(project(":model"))
            api(project(":firebase"))
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