package dev.datlag.pulz.firebase

import androidx.credentials.*

class GoogleAuth(
    override val credentials: GoogleAuthCredentials,
    private val credentialManager: CredentialManager
) : GoogleAuthProvider {
    override suspend fun signOut() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }
}