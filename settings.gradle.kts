rootProject.name = "Gamechanger"

include(":composeApp", ":composeApp:sekret")
include(":settings")
include(":hltv")
include(":model")
include(":game")
include(":rawg")
include(":firebase")
include(":octane")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://gitlab.com/api/v4/projects/38224197/packages/maven")
        maven("https://jitpack.io")
        maven("https://jogamp.org/deployment/maven")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}