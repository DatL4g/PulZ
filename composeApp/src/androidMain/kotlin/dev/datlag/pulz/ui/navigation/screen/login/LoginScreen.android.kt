package dev.datlag.pulz.ui.navigation.screen.login

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dev.datlag.pulz.LocalDI
import dev.datlag.pulz.common.googleButtonColors
import dev.datlag.pulz.firebase.GoogleAuthCredentials
import dev.datlag.pulz.firebase.GoogleAuthProvider
import dev.datlag.pulz.firebase.GoogleUser
import dev.datlag.tooling.async.scopeCatching
import dev.datlag.tooling.async.suspendCatching
import io.github.aakira.napier.Napier
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI

@Composable
actual fun GoogleLoginButton(
    onClick: (GoogleUser?) -> Unit,
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) = withDI(LocalDI.current) {
    val context = LocalContext.current
    val credentialManager by rememberInstance<CredentialManager>()
    val authProvider by rememberInstance<GoogleAuthProvider>()
    var login by remember { mutableStateOf(false) }

    LaunchedEffect(login) {
        if (login) {
            val credentialResult = suspendCatching {
                credentialManager.getCredential(
                    context = context,
                    request = getCredentialRequest(authProvider.credentials)
                ).credential
            }

            val user = suspendCatching {
                googleUserFromCredential(
                    credential = credentialResult.getOrNull()!!
                )
            }.getOrNull()

            onClick(user)
        }
    }

    Button(
        modifier = modifier,
        onClick = {
            login = true
        },
        content = content,
        colors = ButtonDefaults.googleButtonColors()
    )
}

private fun googleUserFromCredential(credential: Credential): GoogleUser? {
    return when {
        credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
            scopeCatching {
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)

                GoogleUser(
                    idToken = googleIdToken.idToken,
                    accessToken = null
                )
            }.getOrNull()
        }
        else -> null
    }
}

private fun getCredentialRequest(credentials: GoogleAuthCredentials): GetCredentialRequest {
    return GetCredentialRequest.Builder()
        .addCredentialOption(getGoogleIdOption(credentials.serverId))
        .build()
}

private fun getGoogleIdOption(serverId: String): GetGoogleIdOption {
    return GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(true)
        .setServerClientId(serverId)
        .build()
}