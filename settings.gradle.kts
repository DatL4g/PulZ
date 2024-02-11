rootProject.name = "Gamechanger"

include(":composeApp")
include(":settings")
include(":hltv")
include(":model")
include(":game")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://jogamp.org/deployment/maven")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}