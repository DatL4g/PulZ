package dev.datlag.gamechanger.other

import dev.gitlive.firebase.FirebaseApp

sealed interface FirebaseFactory {
    data class Initialized(val app: FirebaseApp) : FirebaseFactory
    data object Empty : FirebaseFactory
}