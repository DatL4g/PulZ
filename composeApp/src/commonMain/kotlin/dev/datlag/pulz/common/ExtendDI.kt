package dev.datlag.pulz.common

import dev.datlag.pulz.firebase.FirebaseFactory
import dev.datlag.pulz.firebase.GoogleAuthCredentials
import dev.datlag.pulz.firebase.GoogleAuthProvider
import org.kodein.di.DIAware
import org.kodein.di.DirectDI
import org.kodein.di.instanceOrNull

fun DIAware.nullableFirebaseInstance(): FirebaseFactory? {
    val instance by this.instanceOrNull<FirebaseFactory>()
    return when (val state = instance) {
        is FirebaseFactory.Empty -> null
        else -> state
    }
}

fun DirectDI.nullableFirebaseInstance(): FirebaseFactory? {
    return when (val instance = instanceOrNull<FirebaseFactory>()) {
        is FirebaseFactory.Empty -> null
        else -> instance
    }
}

fun DirectDI.nullableGoogleAuthProvider(): GoogleAuthProvider? {
    return when (val instance = instanceOrNull<GoogleAuthProvider>()) {
        is GoogleAuthProvider.Empty -> null
        else -> instance
    }
}