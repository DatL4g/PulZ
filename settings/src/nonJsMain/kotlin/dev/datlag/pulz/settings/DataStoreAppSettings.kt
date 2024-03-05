package dev.datlag.pulz.settings

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class DataStoreAppSettings(
    private val dataStore: DataStore<ApplicationSettings>
) : Settings.PlatformAppSettings {
    override val showWelcome: Flow<Boolean> = dataStore.data.map { !it.welcomeCompleted }

    override suspend fun setWelcomeCompleted(value: Boolean) {
        dataStore.updateData {
            it.copy(welcomeCompleted = value)
        }
    }
}