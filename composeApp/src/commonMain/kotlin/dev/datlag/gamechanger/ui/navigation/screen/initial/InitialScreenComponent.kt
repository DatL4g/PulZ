package dev.datlag.gamechanger.ui.navigation.screen.initial

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.game.SteamLauncher
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.HomeScreenComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.user.UserScreenComponent
import org.kodein.di.DI

class InitialScreenComponent(
    componentContext: ComponentContext,
    override val di: DI
) : InitialComponent, ComponentContext by componentContext {

    override val pagerItems: List<InitialComponent.PagerItem> = listOf(
        InitialComponent.PagerItem(
            label = SharedRes.strings.app_name,
            icon = Icons.Default.Home
        ),
        run {
            val user = SteamLauncher.loggedInUsers.firstOrNull()

            InitialComponent.PagerItem(
                label = user?.name,
                fallbackLabel = SharedRes.strings.user,
                icon = user?.avatarPath?.toString(),
                fallbackIcon = Icons.Default.AccountCircle,
                iconSchemeKey = user?.id
            )
        }
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
                    View.Home,
                    View.User
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
            is View.User -> UserScreenComponent(
                componentContext = context,
                di = di
            )
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun selectPage(index: Int) {
        pagesNavigation.select(index = index) { new, old ->
            if (new.items[new.selectedIndex] == old.items[old.selectedIndex]) {
                (pages.value.items[pages.value.selectedIndex].instance as? ContentHolderComponent)?.dismissContent()
            }
        }
    }
}