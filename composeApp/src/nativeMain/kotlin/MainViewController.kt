import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import dev.datlag.pulz.App
import dev.datlag.pulz.module.NetworkModule
import dev.datlag.pulz.other.ConsentInfo
import dev.datlag.pulz.other.LocalConsentInfo
import dev.datlag.pulz.other.StateSaver
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
