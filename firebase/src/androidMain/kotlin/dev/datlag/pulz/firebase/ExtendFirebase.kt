package dev.datlag.pulz.firebase

import android.content.Context
import androidx.credentials.CredentialManager
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

fun FirebaseFactory.Companion.initialize(
    context: Context,
    projectId: String?,
    applicationId: String,
    apiKey: String,
    googleAuthProvider: GoogleAuthProvider?
): FirebaseFactory {
    return CommonFirebase(
        Firebase.initialize(
            context = context,
            options = FirebaseOptions(
                projectId = projectId,
                applicationId = applicationId,
                apiKey = apiKey
            )
        ),
        googleAuthProvider
    )
}

fun GoogleAuthProvider.Companion.create(
    credentialManager: CredentialManager,
    credentials: GoogleAuthCredentials
): GoogleAuthProvider {
    return GoogleAuth(
        credentialManager = credentialManager,
        credentials = credentials
    )
}