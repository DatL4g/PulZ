package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.rawg.model.Game

@Composable
fun Rating(game: Game, color: Color = LocalContentColor.current) {
    if (game.rating > 0.0F) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = color
            )
            Text(
                text = game.rating.toString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = color
            )
        }
    }
}