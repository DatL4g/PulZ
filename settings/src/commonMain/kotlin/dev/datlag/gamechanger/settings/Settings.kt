package dev.datlag.gamechanger.settings

import kotlinx.coroutines.flow.Flow

data object Settings {

    interface PlatformAppSettings {
        val showWelcome: Flow<Boolean>

        suspend fun setWelcomeCompleted(value: Boolean)
    }
}