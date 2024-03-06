package dev.datlag.pulz.firebase

import dev.datlag.tooling.async.suspendCatching
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.auth.auth

open class CommonFirebase(private val app: FirebaseApp) : FirebaseFactory {

    override val auth: Auth = Auth(app)
    override val crashlytics: Crashlytics = Crashlytics(app)

    data class Auth(private val app: FirebaseApp) : FirebaseFactory.Auth {

        override val isSignedIn: Boolean
            get() = Firebase.auth(app).currentUser != null

        override suspend fun loginOrCreateEmail(email: String, password: String, done: suspend (Boolean) -> Unit) {
            val auth = Firebase.auth(app)

            val loginResult = suspendCatching {
                auth.signInWithEmailAndPassword(email, password)
            }.getOrNull()

            if (loginResult?.user == null) {
                val createResult = suspendCatching {
                    auth.createUserWithEmailAndPassword(email, password)
                }.getOrNull()

                done(createResult?.user != null)
            } else {
                done(true)
            }
        }

        override suspend fun signOut() {
            Firebase.auth(app).signOut()
        }

        override suspend fun resetPassword(email: String) {
            Firebase.auth(app).sendPasswordResetEmail(email)
        }
    }

    data class Crashlytics(private val app: FirebaseApp) : FirebaseFactory.Crashlytics {
        override fun customKey(key: String, value: String) {
            crashlyticsCustomKey(app, key, value)
        }
        override fun customKey(key: String, value: Boolean) {
            crashlyticsCustomKey(app, key, value)
        }
        override fun customKey(key: String, value: Int) {
            crashlyticsCustomKey(app, key, value)
        }
        override fun customKey(key: String, value: Long) {
            crashlyticsCustomKey(app, key, value)
        }
        override fun customKey(key: String, value: Float) {
            crashlyticsCustomKey(app, key, value)
        }
        override fun customKey(key: String, value: Double) {
            crashlyticsCustomKey(app, key, value)
        }
        override fun log(throwable: Throwable?) {
            crashlyticsLog(app, throwable)
        }
        override fun log(message: String?) {
            crashlyticsLog(app, message)
        }
    }
}