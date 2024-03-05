package dev.datlag.pulz.ui.navigation.screen.initial.user

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import dev.datlag.pulz.common.nullableFirebaseInstance
import dev.datlag.pulz.common.onRender
import dev.datlag.pulz.other.Platform
import dev.datlag.pulz.ui.navigation.DialogComponent
import dev.datlag.pulz.ui.navigation.screen.initial.user.dialog.library.LibraryDialogComponent
import dev.datlag.pulz.ui.navigation.screen.initial.user.dialog.license.LicenseDialogComponent
import org.kodein.di.DI
import org.kodein.di.instance

class UserScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onLogin: () -> Unit
) : UserComponent, ComponentContext by componentContext {

    private val firebaseApp by lazy {
        di.nullableFirebaseInstance()
    }
    override val isSignedIn: Boolean
        get() = firebaseApp?.isSignedIn ?: false

    private val platform by di.instance<Platform>()
    override val isInstantApp: Boolean = platform.isInstantApp()

    private val dialogNavigation = SlotNavigation<DialogConfig>()
    override val dialog: Value<ChildSlot<DialogConfig, DialogComponent>> = childSlot(
        source = dialogNavigation,
        serializer = DialogConfig.serializer()
    ) { config, context ->
        when (config) {
             is DialogConfig.LibraryDetails -> LibraryDialogComponent(
                 componentContext = context,
                 di = di,
                 name = config.name,
                 description = config.description,
                 website = config.website,
                 version = config.version,
                 openSource = config.openSource,
                 onDismiss = dialogNavigation::dismiss
             )
            is DialogConfig.LicenseDetails -> LicenseDialogComponent(
                componentContext = context,
                di = di,
                name = config.name,
                url = config.url,
                year = config.year,
                description = config.description,
                onDismiss = dialogNavigation::dismiss
            )
        }
    }

    @Composable
    override fun render() {
        onRender {
            UserScreen(this)
        }
    }

    override fun login() {
        onLogin()
    }

    override fun libraryDetails(library: Library) {
        dialogNavigation.activate(DialogConfig.LibraryDetails(
            name = library.name,
            description = library.description?.ifBlank { null }
                ?: library.website?.ifBlank { null }
                ?: library.scm?.url?.ifBlank { null }
                ?: library.name,
            website = library.website?.ifBlank { null } ?: library.scm?.url?.ifBlank { null },
            version = library.artifactVersion?.ifBlank { null },
            openSource = library.openSource
        ))
    }

    override fun licenseDetails(license: License) {
        dialogNavigation.activate(DialogConfig.LicenseDetails(
            name = license.name,
            url = license.url?.ifBlank { null },
            year = license.year?.ifBlank { null },
            description = license.licenseContent?.ifBlank { null } ?: license.name
        ))
    }
}