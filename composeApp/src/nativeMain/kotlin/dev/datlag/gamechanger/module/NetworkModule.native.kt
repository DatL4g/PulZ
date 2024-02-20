package dev.datlag.gamechanger.module

import coil3.ImageLoader

actual fun ImageLoader.Builder.extendImageLoader(): ImageLoader.Builder {
    return this
}