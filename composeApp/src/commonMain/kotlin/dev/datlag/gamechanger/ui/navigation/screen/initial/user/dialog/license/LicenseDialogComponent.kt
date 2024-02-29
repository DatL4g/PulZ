package dev.datlag.gamechanger.ui.navigation.screen.initial.user.dialog.license

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.gamechanger.common.onRender
import org.kodein.di.DI

class LicenseDialogComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val name: String,
    override val url: String?,
    override val year: String?,
    override val description: String,
    private val onDismiss: () -> Unit
) : LicenseComponent, ComponentContext by componentContext {

    @Composable
    override fun render() {
        onRender {
            LicenseDialog(this)
        }
    }

    override fun dismiss() {
        onDismiss()
    }
}