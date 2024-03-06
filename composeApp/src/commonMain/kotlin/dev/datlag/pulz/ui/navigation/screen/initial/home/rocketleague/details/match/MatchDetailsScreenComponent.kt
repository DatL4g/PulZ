package dev.datlag.pulz.ui.navigation.screen.initial.home.rocketleague.details.match

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.pulz.common.nullableFirebaseInstance
import dev.datlag.pulz.common.onRender
import dev.datlag.pulz.octane.Octane
import dev.datlag.pulz.octane.model.Match
import dev.datlag.pulz.octane.state.GamesMatchStateMachine
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.decompose.ioScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import org.kodein.di.DI
import org.kodein.di.instance

class MatchDetailsScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val match: Match,
    private val onBack: () -> Unit
) : MatchDetailsComponent, ComponentContext by componentContext {

    private val octane by di.instance<Octane>()
    private val gamesMatchStateMachine = GamesMatchStateMachine(
        octane = octane,
        crashlytics = di.nullableFirebaseInstance()?.crashlytics,
        matchId = match.id,
    )
    override val gamesState: StateFlow<GamesMatchStateMachine.State> = gamesMatchStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = gamesMatchStateMachine.currentState
    )

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)
    }

    @Composable
    override fun render() {
        onRender {
            MatchDetailsScreen(this)
        }
    }

    override fun back() {
        onBack()
    }

    override fun retryLoadingGames() {
        launchIO {
            gamesMatchStateMachine.dispatch(GamesMatchStateMachine.Action.Retry)
        }
    }
}