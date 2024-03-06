package dev.datlag.pulz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.router.stack.*
import dev.datlag.pulz.LocalDI
import dev.datlag.pulz.common.getValueBlocking
import dev.datlag.pulz.common.nullableFirebaseInstance
import dev.datlag.pulz.common.onRender
import dev.datlag.pulz.other.Platform
import dev.datlag.pulz.settings.Settings
import dev.datlag.pulz.ui.navigation.screen.initial.InitialScreenComponent
import dev.datlag.pulz.ui.navigation.screen.initial.home.HomeComponent
import dev.datlag.pulz.ui.navigation.screen.initial.home.HomeScreenComponent
import dev.datlag.pulz.ui.navigation.screen.login.LoginComponent
import dev.datlag.pulz.ui.navigation.screen.login.LoginScreenComponent
import dev.datlag.pulz.ui.navigation.screen.welcome.WelcomeScreenComponent
import dev.datlag.tooling.compose.launchIO
import dev.datlag.tooling.decompose.ioScope
import org.kodein.di.DI
import org.kodein.di.instance

class RootComponent(
    componentContext: ComponentContext,
    override val di: DI
) : Component, ComponentContext by componentContext {

    private val platform by di.instance<Platform>()
    private val appSettings: Settings.PlatformAppSettings by di.instance()

    private val navigation = StackNavigation<ScreenConfig>()
    private val stack = childStack(
        source = navigation,
        serializer = ScreenConfig.serializer(),
        initialConfiguration = run {
            val showWelcome = appSettings.showWelcome.getValueBlocking(false)
            if (showWelcome) {
                ScreenConfig.Welcome
            } else {
                ScreenConfig.Home
            }
        },
        childFactory = ::createScreenComponent
    )

    private fun createScreenComponent(
        screenConfig: ScreenConfig,
        componentContext: ComponentContext
    ) : Component {
        return when (screenConfig) {
            is ScreenConfig.Welcome -> WelcomeScreenComponent(
                componentContext = componentContext,
                di = di,
                onFinish = {
                    goToHome(replace = true) {
                        launchIO {
                            appSettings.setWelcomeCompleted(true)
                        }
                    }
                },
                onLogin = {
                    if (platform.isInstantApp()) {
                        goToHome(replace = true) {
                            launchIO {
                                appSettings.setWelcomeCompleted(true)
                            }
                        }
                    } else {
                        goToLogin (replace = true){
                            launchIO {
                                appSettings.setWelcomeCompleted(true)
                            }
                        }
                    }
                }
            )
            is ScreenConfig.Home -> InitialScreenComponent(
                componentContext = componentContext,
                di = di,
                onLogin = {
                    goToLogin(replace = false)
                }
            )
            is ScreenConfig.Login -> LoginScreenComponent(
                componentContext = componentContext,
                di = di,
                onFinish = {
                    goToHome(replace = true)
                }
            )
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    @Composable
    override fun render() {
        onRender {
            Children(
                stack = stack,
                animation = predictiveBackAnimation(
                    backHandler = this.backHandler,
                    onBack = {
                        navigation.pop()
                    }
                )
            ) {
                it.instance.render()
            }
        }
    }

    private fun goToHome(replace: Boolean, done: () -> Unit = { }) {
        val homeInBackStack = stack.backStack.any {
            it.configuration is ScreenConfig.Home || it.instance is HomeComponent
        }
        if (homeInBackStack) {
            navigation.bringToFront(ScreenConfig.Home, done)
        } else {
            if (replace) {
                navigation.replaceCurrent(ScreenConfig.Home, done)
            } else {
                navigation.push(ScreenConfig.Home, done)
            }
        }
    }

    private fun goToLogin(replace: Boolean, done: () -> Unit = { }) {
        if (di.nullableFirebaseInstance() == null) {
            return goToHome(replace = true, done = done)
        }

        val loginInBackStack = stack.backStack.any {
            it.configuration is ScreenConfig.Login || it.instance is LoginComponent
        }

        if (loginInBackStack) {
            navigation.bringToFront(ScreenConfig.Home, done)
        } else {
            if (replace) {
                navigation.replaceCurrent(ScreenConfig.Login, done)
            } else {
                navigation.push(ScreenConfig.Login, done)
            }
        }
    }
}