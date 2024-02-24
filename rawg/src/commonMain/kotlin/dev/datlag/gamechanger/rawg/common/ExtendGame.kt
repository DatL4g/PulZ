package dev.datlag.gamechanger.rawg.common

import dev.datlag.gamechanger.rawg.model.Game

fun Collection<Game.PlatformInfo>.normalizePlatform(): Set<Game.PlatformInfo> {
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

fun Collection<Game.StoreInfo.Store>.normalizeStore(): Set<Game.StoreInfo.Store> {
    val grouped = this.groupBy {
        it.slug
    }

    val mapped = grouped.map { (key, it) ->
        val id = it.firstNotNullOfOrNull { store ->
            if (store.id == -1) {
                null
            } else {
                store.id
            }
        } ?: it.first().id

        val name = it.firstNotNullOfOrNull { store ->
            store.name.ifBlank { null }
        } ?: it.first().name

        val domain = it.firstNotNullOfOrNull { store ->
            store.domain?.ifBlank { null }
        }

        Game.StoreInfo.Store(
            id = id,
            slug = key,
            name = name,
            domain = domain
        )
    }
    return mapped.toSet()
}

fun Collection<Game.StoreInfo>.normalizeStoreInfo(): Set<Game.StoreInfo> {
    val grouped = this.groupBy {
        it.store?.slug
    }.mapNotNull { (key, value) ->
        if (key == null) {
            return@mapNotNull null
        }

        key to value
    }.toMap()

    val mapped = grouped.map { (key, group) ->
        val infoId = group.firstNotNullOfOrNull {
            if (it.id == -1) {
                null
            } else {
                it.id
            }
        } ?: group.first().id
        val infoUrl = group.firstNotNullOfOrNull { it.url?.ifBlank { null } }

        Game.StoreInfo(
            id = infoId,
            url = infoUrl,
            store = group.mapNotNull { it.store }.normalizeStore().firstOrNull { it.slug == key }
        )
    }
    return mapped.toSet()
}
