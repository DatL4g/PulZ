package dev.datlag.pulz.ui.navigation.screen.initial

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.ui.navigation.Component
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource

interface InitialComponent : Component {

    val pagerItems: List<PagerItem>
    val selectedPage: Value<Int>

    @OptIn(ExperimentalDecomposeApi::class)
    val pages: Value<ChildPages<*, Component>>

    fun selectPage(index: Int)

    data class PagerItem(
        val label: Any?,
        val fallbackLabel: StringResource,
        val icon: Any?,
        val fallbackIcon: ImageVector,
        val iconSchemeKey: Any? = null
    ) {
        constructor(label: StringResource, icon: ImageVector) : this(label, label, icon, icon)

        @Composable
        fun labelAsString(): String {
            return if (label is StringResource) {
                stringResource(label)
            } else {
                (label as? CharSequence)?.toString() ?: stringResource(fallbackLabel)
            }
        }
    }
}