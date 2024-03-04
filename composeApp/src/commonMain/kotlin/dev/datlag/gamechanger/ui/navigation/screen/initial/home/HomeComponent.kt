package dev.datlag.gamechanger.ui.navigation.screen.initial.home

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.Flow

interface HomeComponent : ContentHolderComponent {

    val child: Value<ChildSlot<HomeConfig, Component>>
    val isInstantApp: Boolean

    fun showCounterStrike()
    fun showRocketLeague()
}