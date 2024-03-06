package dev.datlag.pulz.common

import dev.datlag.pulz.firebase.FirebaseFactory
import dev.datlag.pulz.model.common.name
import dev.datlag.pulz.ui.navigation.Component

fun FirebaseFactory.Crashlytics.screen(value: Component) {
    this.customKey("Screen", value::class.name)
}