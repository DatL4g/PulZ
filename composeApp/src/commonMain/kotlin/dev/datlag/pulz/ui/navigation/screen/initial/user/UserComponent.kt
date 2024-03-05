package dev.datlag.pulz.ui.navigation.screen.initial.user

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import dev.datlag.pulz.ui.navigation.Component
import dev.datlag.pulz.ui.navigation.DialogComponent

interface UserComponent : Component {
    val isSignedIn: Boolean
    val dialog: Value<ChildSlot<DialogConfig, DialogComponent>>
    val isInstantApp: Boolean

    fun login()
    fun libraryDetails(library: Library)
    fun licenseDetails(license: License)
}