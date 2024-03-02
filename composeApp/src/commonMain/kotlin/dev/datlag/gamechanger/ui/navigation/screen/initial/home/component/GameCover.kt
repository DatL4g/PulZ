package dev.datlag.gamechanger.ui.navigation.screen.initial.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.game.Game
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun GameCover(
    game: Game,
    fallback: ImageResource,
    onClick: (Game) -> Unit = { }
) {
    ElevatedCard(
        onClick = {
            onClick(game)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        val errorPainter = painterResource(fallback)
        val scope = rememberCoroutineScope()

        val headerUrl = (game as? Game.Steam)?.headerUrl ?: (game as? Game.Multi)?.headerUrl
        val heroUrl = (game as? Game.Steam)?.heroUrl ?: (game as? Game.Multi)?.heroUrl
        val fallbackPainter = if (heroUrl != null) {
            rememberAsyncImagePainter(
                model = heroUrl,
                placeholder = errorPainter,
                error = errorPainter,
                onSuccess = { state ->
                    SchemeTheme.update(
                        key = game,
                        input = state.painter,
                        scope = scope
                    )
                }
            )
        } else {
            errorPainter
        }

        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = headerUrl,
            contentDescription = stringResource(SharedRes.strings.rocket_league),
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
            error = fallbackPainter,
            placeholder = errorPainter,
            onSuccess = { state ->
                SchemeTheme.update(
                    key = game,
                    input = state.painter,
                    scope = scope
                )
            }
        )
    }
}
