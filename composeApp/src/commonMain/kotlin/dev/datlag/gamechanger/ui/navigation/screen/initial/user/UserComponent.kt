package dev.datlag.gamechanger.ui.navigation.screen.initial.user

import dev.datlag.gamechanger.ui.navigation.Component

interface UserComponent : Component {
    val isSignedIn: Boolean

    fun login()
}