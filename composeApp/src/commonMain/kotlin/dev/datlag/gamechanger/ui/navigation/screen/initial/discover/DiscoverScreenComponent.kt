package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.rawg.state.GamesStateMachine
import dev.datlag.tooling.compose.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.kodein.di.DI
import org.kodein.di.instance

class DiscoverScreenComponent(
    componentContext: ComponentContext,
    override val di: DI
) : DiscoverComponent, ComponentContext by componentContext {

    private val gamesStateMachine by di.instance<GamesStateMachine>()
    override val gamesState: Flow<GamesStateMachine.State> = gamesStateMachine.state.flowOn(ioDispatcher())

    @Composable
    override fun render() {
        onRender {
            DiscoverScreen(this)
        }
    }
}