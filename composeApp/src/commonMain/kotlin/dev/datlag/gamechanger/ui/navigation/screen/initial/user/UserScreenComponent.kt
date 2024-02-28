package dev.datlag.gamechanger.ui.navigation.screen.initial.user

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.gamechanger.common.nullableFirebaseInstance
import dev.datlag.gamechanger.common.onRender
import org.kodein.di.DI

class UserScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onLogin: () -> Unit
) : UserComponent, ComponentContext by componentContext {

    private val firebaseApp by lazy {
        di.nullableFirebaseInstance()
    }
    override val isSignedIn: Boolean
        get() = firebaseApp?.isSignedIn ?: false

    @Composable
    override fun render() {
        onRender {
            UserScreen(this)
        }
    }

    override fun login() {
        onLogin()
    }
}