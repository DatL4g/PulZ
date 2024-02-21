plugins {
  alias(libs.plugins.multiplatform)
  alias(libs.plugins.android.library)
}

val artifact = VersionCatalog.artifactName("sekret")

kotlin {
  androidTarget()
  iosArm64 {
    binaries {
      sharedLib()
    }
  }
  iosSimulatorArm64 {
    binaries {
      sharedLib()
    }
  }
  iosX64 {
    binaries {
      sharedLib()
    }
  }
  androidNativeX86 {
    binaries {
      sharedLib()
    }
  }
  androidNativeX64 {
    binaries {
      sharedLib()
    }
  }
  androidNativeArm32 {
    binaries {
      sharedLib()
    }
  }
  androidNativeArm64 {
    binaries {
      sharedLib()
    }
  }

  jvm()
  linuxX64 {
    binaries {
      sharedLib()
    }
  }
  linuxArm64 {
    binaries {
      sharedLib()
    }
  }
  macosX64 {
    binaries {
      sharedLib()
    }
  }
  macosArm64 {
    binaries {
      sharedLib()
    }
  }
  mingwX64 {
    binaries {
      sharedLib()
    }
  }

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonMain.dependencies {
      api("dev.datlag.sekret:sekret:2.0.0-alpha-01")
    }

    val jniNativeMain by creating {
      nativeMain.orNull?.let { dependsOn(it) } ?: dependsOn(commonMain.get())
      androidNativeMain.orNull?.dependsOn(this)
      linuxMain.orNull?.dependsOn(this)
      mingwMain.orNull?.dependsOn(this)
      macosMain.orNull?.dependsOn(this)
    }

    val jniMain by creating {
      dependsOn(commonMain.get())
      androidMain.orNull?.dependsOn(this)
      jvmMain.orNull?.dependsOn(this)
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
