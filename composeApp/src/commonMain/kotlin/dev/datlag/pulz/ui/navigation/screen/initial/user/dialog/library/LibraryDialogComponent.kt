package dev.datlag.pulz.ui.navigation.screen.initial.user.dialog.library

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.pulz.common.onRender
import org.kodein.di.DI

class LibraryDialogComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val name: String,
    override val description: String,
    override val website: String?,
    override val version: String?,
    override val openSource: Boolean,
    private val onDismiss: () -> Unit
) : LibraryComponent, ComponentContext by componentContext {

    @Composable
    override fun render() {
        onRender {
            LibraryDialog(this)
        }
    }

    override fun dismiss() {
        onDismiss()
    }
}