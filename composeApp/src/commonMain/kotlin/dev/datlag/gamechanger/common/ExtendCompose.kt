package dev.datlag.gamechanger.common

import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable

fun LazyGridScope.fullRow(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

fun <T> LazyGridScope.fullRowItems(
    list: List<T>,
    content: @Composable LazyGridItemScope.(T) -> Unit
) {
    items(
        items = list,
        span = {
            GridItemSpan(this.maxLineSpan)
        },
        itemContent = content
    )
}

fun <T> LazyGridScope.fullRowItemsIndexed(
    list: List<T>,
    content: @Composable LazyGridItemScope.(Int, T) -> Unit
) {
    itemsIndexed(
        items = list,
        span = { _, _ ->
            GridItemSpan(this.maxLineSpan)
        },
        itemContent = content
    )
}