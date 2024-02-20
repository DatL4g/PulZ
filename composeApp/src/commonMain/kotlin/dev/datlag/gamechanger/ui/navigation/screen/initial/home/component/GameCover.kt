package dev.datlag.gamechanger.ui.navigation.screen.initial.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import dev.datlag.gamechanger.game.Game
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.gamechanger.ui.theme.rememberSchemeThemeDominantColor
import dev.datlag.tooling.compose.onClick
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun GameCover(
    title: String,
    game: Game?,
    fallback: ImageResource,
    color: Color = rememberSchemeThemeDominantColor(game) ?: MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    contentDescription: String = title,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = modifier.onClick {
            onClick()
        },
        contentAlignment = Alignment.Center
    ) {
        val painter = painterResource(fallback)
        val scope = rememberCoroutineScope()
        val model = if (game is Game.Steam) {
            game.headerUrl
        } else {
            null
        }
        val fallbackPainter = if (game is Game.Steam) {
            rememberAsyncImagePainter(
                model = game.heroUrl,
                placeholder = painter,
                error = painter,
                onSuccess = { state ->
                    SchemeTheme.update(
                        key = game,
                        input = state.painter,
                        scope = scope
                    )
                }
            )
        } else {
            painter
        }
        val animatedColor by animateColorAsState(
            targetValue = color
        )

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model,
            contentDescription = contentDescription,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
            error = fallbackPainter,
            placeholder = painter,
            onSuccess = { state ->
                SchemeTheme.update(
                    key = game,
                    input = state.painter,
                    scope = scope
                )
            }
        )
        Box(
            modifier = Modifier.matchParentSize().background(
                Brush.verticalGradient(
                    colors = listOf(animatedColor.copy(alpha = 0.1F), animatedColor.copy(alpha = 0.75F))
                )
            )
        )
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium.copy(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(3F, 3F),
                    blurRadius = 2F
                )
            )
        )
    }
}