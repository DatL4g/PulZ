package dev.datlag.gamechanger.ui.navigation.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.LocalDarkMode
import dev.datlag.gamechanger.SharedRes
import dev.datlag.tooling.compose.StartCornerShape
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun WelcomeScreen(component: WelcomeComponent) {
    Box(
        modifier = Modifier.safeDrawingPadding().fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                val res = if (LocalDarkMode.current) {
                    SharedRes.images.fans_dark
                } else {
                    SharedRes.images.fans_light
                }

                Image(
                    modifier = Modifier.fillParentMaxWidth(0.5F),
                    painter = painterResource(res),
                    contentDescription = stringResource(SharedRes.strings.welcome),
                    contentScale = ContentScale.FillWidth
                )
            }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(0.85F),
                    text = buildAnnotatedString {
                        append(stringResource(SharedRes.strings.welcome_title))
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append(stringResource(SharedRes.strings.app_name))
                        }
                    },
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(SharedRes.strings.welcome_text_part1))
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append(stringResource(SharedRes.strings.gaming))
                        }
                        append(" ")
                        append(stringResource(SharedRes.strings.welcome_text_part2))
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append(stringResource(SharedRes.strings.esport))
                        }
                        append(" ")
                        append(stringResource(SharedRes.strings.welcome_text_part3))
                    },
                    modifier = Modifier.fillMaxWidth(0.75F),
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            modifier = Modifier.padding(bottom = 32.dp).align(Alignment.BottomEnd),
            onClick = {
                component.login()
            },
            shape = StartCornerShape(otherCorner = 0.dp)
        ) {
            Text(stringResource(SharedRes.strings.start))
        }
    }
}