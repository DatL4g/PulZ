package dev.datlag.pulz.ui.navigation.screen.login

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.datlag.pulz.firebase.GoogleUser

@Composable
actual fun GoogleLoginButton(
    onClick: (GoogleUser?) -> Unit,
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) {
}