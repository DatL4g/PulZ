package dev.datlag.gamechanger.ui.navigation.screen.initial.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.tooling.compose.launchIO
import dev.datlag.tooling.compose.onClick
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun GameCover(
    title: String,
    image: ImageResource,
    color: Color,
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
        val painter = painterResource(image)

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = contentDescription,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth
        )
        Box(
            modifier = Modifier.matchParentSize().background(
                Brush.verticalGradient(
                    colors = listOf(color.copy(alpha = 0.05F), color)
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

        SchemeTheme.update(key = title, input = painter)
    }
}