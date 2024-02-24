package dev.datlag.gamechanger.rawg.common

import dev.datlag.gamechanger.rawg.model.Game

fun Collection<Game.PlatformInfo>.normalize(): Set<Game.PlatformInfo> {
    val grouped = this.groupBy { it.platform?.slug }.mapNotNull { (key, value) ->
        if (key == null) {
            return@mapNotNull null
        }

        value
    }
    val mapped = grouped.map { group ->
        val requirements = group.firstNotNullOfOrNull {
            it.requirements
        }?.let {
            if (it.minimum.isNullOrBlank() || it.recommended.isNullOrBlank()) {
                null
            } else {
                it
            }
        } ?: run {
            val requirementsMin = group.firstNotNullOfOrNull {
                it.requirements?.minimum?.ifBlank { null }
            }
            val requirementsRec = group.firstNotNullOfOrNull {
                it.requirements?.recommended?.ifBlank { null }
            }
            if (requirementsMin == null && requirementsRec == null) {
                null
            } else {
                Game.PlatformInfo.Requirements(
                    minimum = requirementsMin,
                    recommended = requirementsRec
                )
            }
        }
        Game.PlatformInfo(
            platform = group.firstNotNullOfOrNull { it.platform },
            _requirements = requirements
        )
    }
    return mapped.toSet()
}