package dev.datlag.gamechanger.ui.navigation.screen.initial.home

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.ui.navigation.Component
import kotlinx.coroutines.flow.Flow

interface HomeComponent : Component {

    val showWelcome: Flow<Boolean>
    val child: Value<ChildSlot<HomeConfig, Component>>

    fun setWelcome(value: Boolean)

    fun showCounterStrike()

}