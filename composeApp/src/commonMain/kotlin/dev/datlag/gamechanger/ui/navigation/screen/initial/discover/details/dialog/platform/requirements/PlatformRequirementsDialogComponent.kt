package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details.dialog.platform.requirements

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.rawg.model.Game
import org.kodein.di.DI

class PlatformRequirementsDialogComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val platformInfo: Game.PlatformInfo,
    private val onDismiss: () -> Unit
) : PlatformRequirementsComponent, ComponentContext by componentContext {

    @Composable
    override fun render() {
        onRender {
            PlatformRequirementsDialog(this)
        }
    }

    override fun dismiss() {
        onDismiss()
    }
}