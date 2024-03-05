package dev.datlag.pulz.settings

import kotlinx.coroutines.flow.MutableStateFlow

class DefaultAppSettings : Settings.PlatformAppSettings {
    override val showWelcome: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override suspend fun setWelcomeCompleted(value: Boolean) {
        showWelcome.emit(value)
    }
}