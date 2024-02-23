package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.onRenderApplyCommonScheme
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GameInfoStateMachine
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
    private val showCounterStrike: () -> Unit
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

    override fun dismissContent() {
        back()
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
}