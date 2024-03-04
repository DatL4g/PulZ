package dev.datlag.gamechanger.firebase

import dev.datlag.tooling.async.suspendCatching
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth

sealed interface FirebaseFactory {
    data class Initialized(private val app: FirebaseApp) : FirebaseFactory {
        suspend fun loginOrCreateEmail(email: String, password: String, done: suspend (FirebaseUser?) -> Unit) {
            val auth = Firebase.auth(app)

            val loginResult = suspendCatching {
                auth.signInWithEmailAndPassword(email, password)
            }.getOrNull()

            if (loginResult?.user == null) {
                val createResult = suspendCatching {
                    auth.createUserWithEmailAndPassword(email, password)
                }.getOrNull()

                done(createResult?.user)
            } else {
                done(loginResult.user)
            }
        }

        suspend fun signOut() {
            Firebase.auth(app).signOut()
        }

        suspend fun resetPassword(email: String) {
            Firebase.auth(app).sendPasswordResetEmail(email)
        }

        val isSignedIn: Boolean
            get() = Firebase.auth(app).currentUser != null

        fun crashlyticsCustomKey(key: String, value: String) {
            crashlyticsCustomKey(app, key, value)
        }
        fun crashlyticsCustomKey(key: String, value: Boolean) {
            crashlyticsCustomKey(app, key, value)
        }
        fun crashlyticsCustomKey(key: String, value: Int) {
            crashlyticsCustomKey(app, key, value)
        }
        fun crashlyticsCustomKey(key: String, value: Long) {
            crashlyticsCustomKey(app, key, value)
        }
        fun crashlyticsCustomKey(key: String, value: Float) {
            crashlyticsCustomKey(app, key, value)
        }
        fun crashlyticsCustomKey(key: String, value: Double) {
            crashlyticsCustomKey(app, key, value)
        }
    }
    data object Empty : FirebaseFactory

    companion object
}