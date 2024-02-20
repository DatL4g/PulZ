package dev.datlag.gamechanger.ui.custom

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Tab
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.datlag.tooling.compose.ifTrue

@Composable
fun RoundTab(
    selected: Boolean,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderColor by animateFloatAsState(
        targetValue = if (selected) {
            1F
        } else {
            0F
        }
    )

    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier.clip(CircleShape).ifTrue(selected) {
            border(width = 2.dp, color = LocalContentColor.current.copy(alpha = borderColor), shape = CircleShape)
        },
        content = content
    )
}