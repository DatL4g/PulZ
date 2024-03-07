package dev.datlag.pulz.ui.navigation.screen.login

import dev.datlag.pulz.firebase.GoogleUser
import dev.datlag.pulz.ui.navigation.Component
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent : Component {
    val emailRegex: Regex
    val passwordReset: StateFlow<Boolean>
    val loggingIn: StateFlow<Boolean>
    val loginError: StateFlow<Boolean>

    val googleLoginSupported: Boolean

    fun login(email: String, pass: String)
    fun skip()
    fun resetPassword(email: String)

    fun googleLogin(user: GoogleUser?)
}