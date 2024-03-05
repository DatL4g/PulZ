package dev.datlag.pulz.common

import dev.datlag.pulz.firebase.FirebaseFactory
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