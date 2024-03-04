package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.component.hltv

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.hltv.model.Home

@Composable
fun NewsCard(news: Home.News, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        onClick = {

        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!news.image.isNullOrBlank()) {
                    AsyncImage(
                        model = news.image,
                        contentDescription = news.title,
                        modifier = Modifier.weight(0.3F),
                        contentScale = ContentScale.FillWidth,
                        alignment = Alignment.Center
                    )
                }
                Text(
                    modifier = Modifier.weight(0.7F),
                    text = news.title,
                    maxLines = 3,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            news.text?.let {
                Text(
                    text = it,
                    maxLines = 2,
                    softWrap = true,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}