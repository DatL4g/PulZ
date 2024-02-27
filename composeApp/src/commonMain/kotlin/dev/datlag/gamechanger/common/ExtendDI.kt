package dev.datlag.gamechanger.common

import dev.datlag.gamechanger.other.FirebaseFactory
import dev.gitlive.firebase.FirebaseApp
import org.kodein.di.DIAware
import org.kodein.di.instanceOrNull

fun DIAware.nullableFirebaseInstance(): FirebaseApp? {
    val instance by this.instanceOrNull<FirebaseFactory>()
    return when (val state = instance) {
        is FirebaseFactory.Initialized -> state.app
        is FirebaseFactory.Empty -> null
        else -> null
    }
}