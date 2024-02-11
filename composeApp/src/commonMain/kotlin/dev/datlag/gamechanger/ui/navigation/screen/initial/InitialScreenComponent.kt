package dev.datlag.gamechanger.ui.navigation.screen.initial

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import dev.datlag.gamechanger.LocalDI
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.screen.initial.counterstrike.CounterStrikeScreenComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.HomeScreenComponent
import org.kodein.di.DI

class InitialScreenComponent(
    componentContext: ComponentContext,
    override val di: DI
) : InitialComponent, ComponentContext by componentContext {

    override val pagerItems: List<InitialComponent.PagerItem> = listOf(
        InitialComponent.PagerItem(
            SharedRes.strings.app_name,
            Icons.Default.Home
        )
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private val pagesNavigation = PagesNavigation<View>()

    @OptIn(ExperimentalDecomposeApi::class)
    override val pages: Value<ChildPages<View, Component>> = childPages(
        source = pagesNavigation,
        serializer = View.serializer(),
        initialPages = {
            Pages(
                items = listOf(
                    View.Home
                ),
                selectedIndex = 0
            )
        },
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    override val selectedPage: Value<Int> = pages.map { it.selectedIndex }

    @Composable
    override fun render() {
        onRender {
            InitialScreen(this)
        }
    }

    private fun createChild(
        view: View,
        context: ComponentContext
    ) : Component {
        return when (view) {
            is View.Home -> HomeScreenComponent(
                componentContext = context,
                di = di
            )
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun selectPage(index: Int) {
        pagesNavigation.select(index = index)
    }
}