package dev.datlag.gamechanger.ui.navigation.screen.login

import dev.datlag.gamechanger.ui.navigation.Component
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent : Component {
    val emailRegex: Regex
    val passwordReset: StateFlow<Boolean>
    val loggingIn: StateFlow<Boolean>
    fun login(email: String, pass: String)
    fun skip()
    fun resetPassword(email: String)
}