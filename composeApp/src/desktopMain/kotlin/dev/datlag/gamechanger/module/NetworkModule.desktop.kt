package dev.datlag.gamechanger.module

import coil3.ImageLoader
import coil3.util.DebugLogger

actual fun ImageLoader.Builder.extendImageLoader(): ImageLoader.Builder {
    return this.logger(DebugLogger())
}