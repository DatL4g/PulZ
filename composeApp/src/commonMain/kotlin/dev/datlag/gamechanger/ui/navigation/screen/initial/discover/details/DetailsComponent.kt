package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details

import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GameInfoStateMachine
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent : ContentHolderComponent {

    val state: Flow<GameInfoStateMachine.State>
    val game: StateFlow<Game>
    val isCounterStrike: Flow<Boolean>

    fun back()
    fun currentState(): GameInfoStateMachine.State
    fun openCounterStrike()

}