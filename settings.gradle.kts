rootProject.name = "Gamechanger"

include(":composeApp", ":composeApp:sekret")
include(":settings")
include(":hltv")
include(":model")
include(":game")
include(":rawg")

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