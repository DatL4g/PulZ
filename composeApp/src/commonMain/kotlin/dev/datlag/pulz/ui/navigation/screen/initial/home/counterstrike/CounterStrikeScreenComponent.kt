package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.pulz.common.nextDateWithWeekDay
import dev.datlag.pulz.common.onRender
import dev.datlag.pulz.common.onRenderApplyCommonScheme
import dev.datlag.pulz.common.onRenderWithScheme
import dev.datlag.pulz.game.Game
import dev.datlag.pulz.game.SteamLauncher
import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.hltv.state.HomeStateMachine
import dev.datlag.pulz.ui.navigation.Component
import dev.datlag.pulz.ui.navigation.ContentHolderComponent
import dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.article.ArticleScreenComponent
import dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team.TeamDetailsScreenComponent
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.decompose.ioScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.datetime.*
import org.kodein.di.DI
import org.kodein.di.instance
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class CounterStrikeScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onBack: () -> Unit
) : CounterStrikeComponent, ComponentContext by componentContext {

    override val canLaunch: Boolean = SteamLauncher.launchSupported
    override val steamGame: Game.Steam = Game.Steam.CounterStrike

    private val hltvHomeStateMachine by di.instance<HomeStateMachine>()
    override val hltvHomeState: Flow<HomeStateMachine.State> = hltvHomeStateMachine.state.flowOn(ioDispatcher())

    override val dropsReset = flow<DateTimePeriod> {
        while (currentCoroutineContext().isActive) {
            emit(calculateDateTimePeriod())
            delay(1.seconds)
        }
    }.flowOn(ioDispatcher()).stateIn(ioScope(), SharingStarted.WhileSubscribed(), calculateDateTimePeriod())

    private fun calculateDateTimePeriod(): DateTimePeriod {
        val instant = Clock.System.now()
        val datetimeInGMT = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val wednesday = datetimeInGMT.nextDateWithWeekDay(DayOfWeek.WEDNESDAY).atTime(
            hour = 2,
            minute = 0,
            second = 0
        ).toInstant(TimeZone.of("GMT+1"))

        return instant.periodUntil(wednesday, TimeZone.currentSystemDefault())
    }

    private val navigation = SlotNavigation<CounterStrikeConfig>()
    override val child: Value<ChildSlot<CounterStrikeConfig, Component>> = childSlot(
        source = navigation,
        serializer = CounterStrikeConfig.serializer()
    ) { config, context ->
        when (config) {
            is CounterStrikeConfig.Article -> ArticleScreenComponent(
                componentContext = context,
                di = di,
                link = config.link,
                onBack = navigation::dismiss
            )
            is CounterStrikeConfig.Details -> {
                when (config) {
                    is CounterStrikeConfig.Details.Team -> TeamDetailsScreenComponent(
                        componentContext = context,
                        di = di,
                        initialTeam = config.initial,
                        onBack = navigation::dismiss
                    )
                }
            }
        }
    }

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)
    }

    @Composable
    override fun render() {
        onRenderApplyCommonScheme(game) {
            CounterStrikeScreen(this)
        }
    }

    override fun back() {
        onBack()
    }

    override fun launch() {
        Game.Steam.CounterStrike.launch()
    }

    override fun dismissContent() {
        (child.value.child?.instance as? ContentHolderComponent)?.dismissContent() ?: navigation.dismiss { active ->
            if (!active) {
                back()
            }
        }
    }

    override fun retryLoadingHLTV() {
        launchIO {
            hltvHomeStateMachine.dispatch(HomeStateMachine.Action.Retry)
        }
    }

    override fun showArticle(link: String) {
        navigation.activate(CounterStrikeConfig.Article(link))
    }

    override fun showTeam(team: Home.Team) {
        navigation.activate(CounterStrikeConfig.Details.Team(team))
    }
}