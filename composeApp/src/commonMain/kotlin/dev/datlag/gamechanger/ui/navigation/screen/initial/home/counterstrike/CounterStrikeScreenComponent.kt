package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.nextDateWithWeekDay
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.common.onRenderApplyCommonScheme
import dev.datlag.gamechanger.common.onRenderWithScheme
import dev.datlag.gamechanger.game.Game
import dev.datlag.gamechanger.game.SteamLauncher
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
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

    private val backCallback = BackCallback {
        back()
    }

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

    override fun dismissContent() {
        back()
    }

    override fun launch() {
        Game.Steam.CounterStrike.launch()
    }
}