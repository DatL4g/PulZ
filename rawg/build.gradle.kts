plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.library)
}

val artifact = VersionCatalog.artifactName("rawg")

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
            implementation(libs.coroutines)
            implementation(libs.tooling)
            api(libs.ktorfit)
            implementation(libs.serialization)
            implementation(libs.datetime)
            api(libs.flowredux)
            api(project(":model"))
            api(project(":firebase"))
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.ktorfit.ksp)
    add("kspAndroid", libs.ktorfit.ksp)
    add("kspJvm", libs.ktorfit.ksp)
    add("kspIosX64", libs.ktorfit.ksp)
    add("kspIosArm64", libs.ktorfit.ksp)
    add("kspIosSimulatorArm64", libs.ktorfit.ksp)
    add("kspMacosX64", libs.ktorfit.ksp)
    add("kspMacosArm64", libs.ktorfit.ksp)
    add("kspLinuxX64", libs.ktorfit.ksp)
    add("kspLinuxArm64", libs.ktorfit.ksp)
    add("kspMingwX64", libs.ktorfit.ksp)
    add("kspJs", libs.ktorfit.ksp)
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
