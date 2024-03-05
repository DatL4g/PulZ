package dev.datlag.pulz.module

import coil3.ImageLoader
import coil3.request.allowHardware

actual fun ImageLoader.Builder.extendImageLoader(): ImageLoader.Builder {
    return this.allowHardware(false)
}