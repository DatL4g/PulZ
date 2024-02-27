package dev.datlag.gamechanger.ui.navigation.screen.login

import dev.datlag.gamechanger.ui.navigation.Component

interface LoginComponent : Component {
    val emailRegex: Regex
    fun login(email: String, pass: String)
    fun skip()
}