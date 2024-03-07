package dev.datlag.pulz.firebase

interface FirebaseFactory {

    val auth: Auth
    val crashlytics: Crashlytics

    data object Empty : FirebaseFactory, Auth, Crashlytics {
        override val auth: Auth = this
        override val crashlytics: Crashlytics = this
    }

    interface Auth {

        val isSignedIn: Boolean
            get() = false

        val googleAuthSupported: Boolean
            get() = false

        suspend fun loginOrCreateEmail(email: String, password: String, done: suspend (Boolean) -> Unit) {
            done(false)
        }
        suspend fun signOut() { }
        suspend fun resetPassword(email: String) { }

        suspend fun googleSignIn(googleUser: GoogleUser?, done: suspend (Boolean) -> Unit) {
            done(false)
        }
    }

    interface Crashlytics {
        fun customKey(key: String, value: String) { }
        fun customKey(key: String, value: Boolean) { }
        fun customKey(key: String, value: Int) { }
        fun customKey(key: String, value: Long) { }
        fun customKey(key: String, value: Float) { }
        fun customKey(key: String, value: Double) { }
        fun log(throwable: Throwable?) { }
        fun log(message: String?) { }
    }

    companion object
}