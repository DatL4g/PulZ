package dev.datlag.gamechanger.ui.navigation.screen.welcome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.ui.navigation.screen.welcome.component.FirstPage
import dev.datlag.tooling.compose.EndCornerShape
import dev.datlag.tooling.compose.StartCornerShape
import dev.datlag.tooling.compose.launchMain

// ToDo("maybe only one small welcome screen")
// ToDo("no paging")
// ToDo("too much explanation is distracting and confusing")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(component: WelcomeComponent) {
    val state = rememberPagerState { 3 }

    Box(Modifier.safeDrawingPadding()) {
        HorizontalPager(
            state = state,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            Box(modifier = Modifier.fillMaxSize()) {
                val contentModifier = Modifier.align(Alignment.Center).padding(32.dp)

                when (index) {
                    0 -> FirstPage(contentModifier)
                    else -> FirstPage(contentModifier)
                }
            }
        }

        var buttonHeight by remember { mutableIntStateOf(0) }

        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(bottom = 32.dp).onSizeChanged {
                buttonHeight = it.height
            },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val hasPrevious = state.currentPage > 0
            val lastPage = state.currentPage == state.pageCount - 1
            val scope = rememberCoroutineScope()

            if (hasPrevious) {
                FilledTonalButton(
                    onClick = {
                        scope.launchMain {
                            state.animateScrollToPage(
                                page = state.currentPage - 1
                            )
                        }
                    },
                    shape = EndCornerShape(otherCorner = 0.dp)
                ) {
                    Text(text = "Back")
                }
            }
            Spacer(modifier = Modifier.weight(1F))
            Button(
                onClick = {
                    if (lastPage) {
                        component.finish()
                    } else {
                        scope.launchMain {
                            state.animateScrollToPage(
                                page = state.currentPage + 1
                            )
                        }
                    }
                },
                shape = StartCornerShape(otherCorner = 0.dp)
            ) {
                if (lastPage) {
                    Text(text = "Start")
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Icon(
                        imageVector = Icons.Default.DoubleArrow,
                        contentDescription = "Start"
                    )
                } else {
                    Text(text = "Next")
                }
            }
        }
    }
}