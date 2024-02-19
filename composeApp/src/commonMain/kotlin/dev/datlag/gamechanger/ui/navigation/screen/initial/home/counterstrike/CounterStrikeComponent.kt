package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent

interface CounterStrikeComponent : ContentHolderComponent {

    val canLaunch: Boolean

    fun back()

    fun launch()
}