package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.pulz.common.nullableFirebaseInstance
import dev.datlag.pulz.common.onRenderApplyCommonScheme
import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.hltv.state.TeamStateMachine
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.decompose.ioScope
import io.ktor.client.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.instance

class TeamDetailsScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val initialTeam: Home.Team,
    private val onBack: () -> Unit
) : TeamDetailsComponent, ComponentContext by componentContext {

    private val client by di.instance<HttpClient>()
    private val json by di.instance<Json>()
    private val teamStateMachine = TeamStateMachine(
        client = client,
        json = json,
        crashlytics = di.nullableFirebaseInstance()?.crashlytics,
        href = initialTeam.href
    )
    override val teamState: StateFlow<TeamStateMachine.State> = teamStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = teamStateMachine.currentState
    )

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)
    }

    @Composable
    override fun render() {
        onRenderApplyCommonScheme(initialTeam.href) {
            TeamDetailsScreen(this)
        }
    }

    override fun back() {
        onBack()
    }

    override fun retryLoading() {
        launchIO {
            teamStateMachine.dispatch(TeamStateMachine.Action.Retry)
        }
    }
}