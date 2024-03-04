package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.article

import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent

interface ArticleComponent : ContentHolderComponent {

    val link: String

    fun back()
    override fun dismissContent() {
        back()
    }
}