import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import dev.datlag.gamechanger.App
import dev.datlag.gamechanger.module.NetworkModule
import dev.datlag.gamechanger.other.ConsentInfo
import dev.datlag.gamechanger.other.LocalConsentInfo
import dev.datlag.gamechanger.other.StateSaver
import org.kodein.di.DI

private val di = DI {
    import(NetworkModule.di)
}

private val consentInfo = ConsentInfo()

fun MainViewController() = ComposeUIViewController {
    StateSaver.sekretLibraryLoaded = true

    CompositionLocalProvider(
        LocalConsentInfo provides consentInfo
    ) {
        App(
            di = di
        ) {

        }
    }
}
