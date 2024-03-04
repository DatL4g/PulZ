package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.article

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.onRender
import org.kodein.di.DI

class ArticleScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val link: String,
    private val onBack: () -> Unit
) : ArticleComponent, ComponentContext by componentContext {

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)
    }

    @Composable
    override fun render() {
        onRender {
            ArticleScreen(this)
        }
    }

    override fun back() {
        onBack()
    }
}