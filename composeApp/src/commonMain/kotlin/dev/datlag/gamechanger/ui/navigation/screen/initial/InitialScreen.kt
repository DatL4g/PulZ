package dev.datlag.gamechanger.ui.navigation.screen.initial

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.pages.Pages
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.ui.custom.ExpandedPages
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.tooling.compose.EndCornerShape
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun InitialScreen(component: InitialComponent) {
    val haze = remember { HazeState() }

    CompositionLocalProvider(
        LocalHaze provides haze
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (calculateWindowSizeClass().widthSizeClass) {
                WindowWidthSizeClass.Compact -> CompactScreen(component)
                WindowWidthSizeClass.Medium -> MediumScreen(component)
                WindowWidthSizeClass.Expanded -> ExpandedScreen(component)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalDecomposeApi::class, ExperimentalHazeMaterialsApi::class)
@Composable
private fun CompactScreen(component: InitialComponent) {
    val selectedPage by component.selectedPage.subscribeAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.hazeChild(
                    state = LocalHaze.current,
                    style = HazeMaterials.regular(NavigationBarDefaults.containerColor)
                ).fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.contentColorFor(NavigationBarDefaults.containerColor)
            ) {
                component.pagerItems.forEachIndexed { index, pagerItem ->
                    NavigationBarItem(
                        selected = selectedPage == index,
                        icon = {
                            NavIcon(pagerItem)
                        },
                        onClick = {
                            component.selectPage(index)
                        },
                        label = {
                            Text(text = pagerItem.labelAsString())
                        },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) {
        CompositionLocalProvider(
            LocalPaddingValues provides it
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Pages(
                    pages = component.pages,
                    onPageSelected = { index ->
                        if (selectedPage != index) {
                            component.selectPage(index)
                        }
                    },
                    pager = { modifier, state, key, pageContent ->
                        HorizontalPager(
                            modifier = modifier,
                            state = state,
                            key = key,
                            pageContent = pageContent,
                            userScrollEnabled = false
                        )
                    }
                ) { _, page ->
                    page.render()
                }
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun MediumScreen(component: InitialComponent) {
    val selectedPage by component.selectedPage.subscribeAsState()

    Scaffold {
        Row(
            modifier = Modifier.padding(it)
        ) {
            NavigationRail {
                Spacer(modifier = Modifier.weight(1F))
                component.pagerItems.forEachIndexed { index, pagerItem ->
                    NavigationRailItem(
                        selected = selectedPage == index,
                        icon = {
                            NavIcon(pagerItem)
                        },
                        onClick = {
                            component.selectPage(index)
                        },
                        label = {
                            Text(text = pagerItem.labelAsString())
                        },
                        alwaysShowLabel = true
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
            }

            ExpandedPages(
                pages = component.pages
            ) { _, page ->
                page.render()
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun ExpandedScreen(component: InitialComponent) {
    val selectedPage by component.selectedPage.subscribeAsState()

    Scaffold {
        PermanentNavigationDrawer(
            modifier = Modifier.padding(it),
            drawerContent = {
                PermanentDrawerSheet(
                    drawerShape = EndCornerShape(otherCorner = 0.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1F))
                    component.pagerItems.forEachIndexed { index, pagerItem ->
                        NavigationDrawerItem(
                            selected = selectedPage == index,
                            icon = {
                                NavIcon(pagerItem)
                            },
                            onClick = {
                                component.selectPage(index)
                            },
                            label = {
                                Text(text = pagerItem.labelAsString())
                            }
                        )
                    }
                    Spacer(modifier = Modifier.weight(1F))
                }
            }
        ) {
            ExpandedPages(
                pages = component.pages
            ) { _, page ->
                page.render()
            }
        }
    }
}

@Composable
private fun NavIcon(item: InitialComponent.PagerItem) {
    if (item.icon is ImageVector) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.labelAsString(),
            modifier = Modifier.size(24.dp),
            tint = LocalContentColor.current
        )
    } else {
        val scope = rememberCoroutineScope()
        var applyColorFilter by remember { mutableStateOf(false) }
        val colorFilter = if (applyColorFilter) {
            ColorFilter.tint(color = LocalContentColor.current)
        } else {
            null
        }

        AsyncImage(
            model = item.icon,
            contentDescription = item.labelAsString(),
            error = rememberVectorPainter(item.fallbackIcon),
            placeholder = rememberVectorPainter(item.fallbackIcon),
            modifier = Modifier.size(24.dp).clip(CircleShape),
            clipToBounds = true,
            onSuccess = { state ->
                applyColorFilter = false
                SchemeTheme.update(key = item.iconSchemeKey, state.painter, scope)
            },
            colorFilter = colorFilter,
            onError = {
                applyColorFilter = true
            },
            onLoading = {
                applyColorFilter = true
            }
        )
    }
}