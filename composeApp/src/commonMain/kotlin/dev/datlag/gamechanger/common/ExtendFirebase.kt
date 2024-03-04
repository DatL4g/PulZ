package dev.datlag.gamechanger.common

import dev.datlag.gamechanger.firebase.FirebaseFactory
import dev.datlag.gamechanger.model.common.name
import dev.datlag.gamechanger.ui.navigation.Component

fun FirebaseFactory.Initialized.screen(value: Component) {
    this.crashlyticsCustomKey("Screen", value::class.name)
}