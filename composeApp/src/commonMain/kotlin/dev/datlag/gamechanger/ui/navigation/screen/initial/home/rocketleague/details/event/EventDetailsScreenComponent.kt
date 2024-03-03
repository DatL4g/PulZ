package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.event

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.octane.Octane
import dev.datlag.gamechanger.octane.model.Event
import dev.datlag.gamechanger.octane.model.Match
import dev.datlag.gamechanger.octane.state.MatchesEventStateMachine
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.decompose.ioScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import org.kodein.di.DI
import org.kodein.di.instance

class EventDetailsScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val event: Event,
    private val onBack: () -> Unit,
    private val onMatch: (Match) -> Unit
) : EventDetailsComponent, ComponentContext by componentContext {

    private val octane by di.instance<Octane>()
    private val matchesEventStateMachine = MatchesEventStateMachine(
        octane = octane,
        eventId = event.id
    )
    override val matchesState: StateFlow<MatchesEventStateMachine.State> = matchesEventStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = matchesEventStateMachine.currentState
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
            EventDetailsScreen(this)
        }
    }

    override fun back() {
        onBack()
    }

    override fun retryLoadingMatches() {
        launchIO {
            matchesEventStateMachine.dispatch(MatchesEventStateMachine.Action.Retry)
        }
    }

    override fun showMatch(match: Match) {
        onMatch(match)
    }
}