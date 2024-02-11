import androidx.compose.ui.window.ComposeUIViewController
import dev.datlag.gamechanger.App
import dev.datlag.gamechanger.module.NetworkModule
import org.kodein.di.DI

private val di = DI {
    import(NetworkModule.di)
}

fun MainViewController() = ComposeUIViewController {
    App(
        di = di
    ) {

    }
}
