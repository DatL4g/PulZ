package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.onRenderApplyCommonScheme
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GameInfoStateMachine
import dev.datlag.gamechanger.ui.navigation.DialogComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details.dialog.platform.requirements.PlatformRequirementsDialogComponent
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.decompose.ioScope
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
import org.kodein.di.DI
import org.kodein.di.instance

class DetailsScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val initialGame: Game,
    private val onBack: () -> Unit,
    private val showCounterStrike: () -> Unit,
    private val showRocketLeague: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val rawg by di.instance<RAWG>()
    private val key by di.instance<String>("RAWG_API_KEY")
    private val detailsStateMachine = GameInfoStateMachine(
        rawg = rawg,
        key = key,
        slug = initialGame.slug.ifBlank { initialGame.id.toString() }
    )
    override val state: Flow<GameInfoStateMachine.State> = detailsStateMachine.state.flowOn(ioDispatcher())
    override val game: StateFlow<Game> = state.mapNotNull { it as? GameInfoStateMachine.State.Success }.map {
        it.game.combine(initialGame)
    }.stateIn(ioScope(), SharingStarted.WhileSubscribed(), initialGame)

    override val isCounterStrike: Flow<Boolean> = game.transform {
        if (it.slug.equals("counter-strike-2-2", ignoreCase = true) || it.id == 965470) {
            return@transform emit(true)
        } else if (it.slug.equals("counter-strike-global-offensive", ignoreCase = true) || it.id == 4291) {
            return@transform emit(true)
        } else {
            return@transform emit(false)
        }
    }.flowOn(ioDispatcher())

    override val isRocketLeague: Flow<Boolean> = game.transform {
        if (it.slug.equals("rocket-league", ignoreCase = true) || it.id == 3272) {
            return@transform emit(true)
        } else {
            return@transform emit(false)
        }
    }.flowOn(ioDispatcher())

    private val dialogNavigation = SlotNavigation<DialogConfig>()
    override val dialog: Value<ChildSlot<DialogConfig, DialogComponent>> = childSlot(
        source = dialogNavigation,
        serializer = DialogConfig.serializer()
    ) { config, context ->
        when (config) {
            is DialogConfig.PlatformRequirements -> PlatformRequirementsDialogComponent(
                componentContext = context,
                di = di,
                platformInfo = config.platformInfo,
                onDismiss = dialogNavigation::dismiss
            )
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
        val collectedGame by game.collectAsStateWithLifecycle()

        onRenderApplyCommonScheme(collectedGame.slug) {
            DetailsScreen(collectedGame, this)
        }
    }

    override fun currentState(): GameInfoStateMachine.State {
        return detailsStateMachine.currentState
    }

    override fun back() {
        onBack()
    }

    override fun openCounterStrike() {
        showCounterStrike()
    }

    override fun openRocketLeague() {
        showRocketLeague()
    }

    override fun showPlatformRequirements(platform: Game.PlatformInfo) {
        dialogNavigation.activate(DialogConfig.PlatformRequirements(platform))
    }
}