package dev.datlag.gamechanger.module

import org.kodein.di.DI

actual object PlatformModule {

    private const val NAME = "AndroidPlatformModule"

    actual val di = DI.Module(NAME) {

    }

}