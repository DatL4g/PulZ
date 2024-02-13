package dev.datlag.gamechanger.game.model.steam

import dev.datlag.gamechanger.model.Celebrity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okio.Path

data class User(
    val id: String,
    val config: Config,
    val avatarPath: Path?
) {

    val name = config.personaName?.ifBlank { null } ?: config.accountName.ifBlank { id }
    val celebrity: Celebrity? = Celebrity.valueOf(id)

    @Serializable
    data class Config(
        @SerialName("AccountName") val accountName: String,
        @SerialName("PersonaName") val personaName: String? = accountName,
        @SerialName("MostRecent") val mostRecent: Int,
        @SerialName("Timestamp") val timestamp: Long
    )

    companion object {
        fun fromMap(map: Map<String, Config>, avatarPathResolver: (String) -> Path?) = map.map { (id, config) ->
            User(id, config, avatarPathResolver(id))
        }
    }
}