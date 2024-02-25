package dev.datlag.gamechanger.ui.navigation.screen.initial.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
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
    color: Color = rememberSchemeThemeDominantColor(game) ?: Color.Black,
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
                brush = Brush.verticalGradient(
                    0.0f to Color.Transparent,
                    0.1f to animatedColor.copy(alpha = 0.15f),
                    0.3f to animatedColor.copy(alpha = 0.35f),
                    0.5f to animatedColor.copy(alpha = 0.55f),
                    0.7f to animatedColor.copy(alpha = 0.75f),
                    0.9f to animatedColor
                )
            )
        )
        Text(
            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp),
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