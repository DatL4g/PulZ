package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.article

import dev.datlag.pulz.ui.navigation.ContentHolderComponent

interface ArticleComponent : ContentHolderComponent {

    val link: String

    fun back()
    override fun dismissContent() {
        back()
    }
}