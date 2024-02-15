package dev.datlag.gamechanger.ui.navigation.screen.initial.user

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.gamechanger.common.onRender
import org.kodein.di.DI

class UserScreenComponent(
    componentContext: ComponentContext,
    override val di: DI
) : UserComponent, ComponentContext by componentContext {

    @Composable
    override fun render() {
        onRender {
            UserScreen(this)
        }
    }
}