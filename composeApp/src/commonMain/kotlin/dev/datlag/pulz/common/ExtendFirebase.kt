package dev.datlag.pulz.common

import dev.datlag.pulz.firebase.FirebaseFactory
import dev.datlag.pulz.model.common.name
import dev.datlag.pulz.ui.navigation.Component

fun FirebaseFactory.Initialized.screen(value: Component) {
    this.crashlyticsCustomKey("Screen", value::class.name)
}