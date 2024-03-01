package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GameInfoStateMachine
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.DialogComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent : ContentHolderComponent {

    val state: Flow<GameInfoStateMachine.State>
    val game: StateFlow<Game>
    val isCounterStrike: Flow<Boolean>
    val isRocketLeague: Flow<Boolean>

    val dialog: Value<ChildSlot<DialogConfig, DialogComponent>>

    fun back()
    fun currentState(): GameInfoStateMachine.State
    fun openCounterStrike()
    fun openRocketLeague()
    fun showPlatformRequirements(platform: Game.PlatformInfo)

    override fun dismissContent() {
        back()
    }

}