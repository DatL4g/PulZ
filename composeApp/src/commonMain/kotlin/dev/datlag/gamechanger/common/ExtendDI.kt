package dev.datlag.gamechanger.common

import dev.datlag.gamechanger.firebase.FirebaseFactory
import org.kodein.di.DIAware
import org.kodein.di.instanceOrNull

fun DIAware.nullableFirebaseInstance(): FirebaseFactory.Initialized? {
    val instance by this.instanceOrNull<FirebaseFactory>()
    return when (val state = instance) {
        is FirebaseFactory.Initialized -> state
        is FirebaseFactory.Empty -> null
        else -> null
    }
}