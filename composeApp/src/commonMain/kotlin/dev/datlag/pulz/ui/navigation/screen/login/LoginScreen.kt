package dev.datlag.pulz.ui.navigation.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.datlag.pulz.LocalDarkMode
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.common.autofill
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LoginScreen(component: LoginComponent) {
    val email = remember { mutableStateOf("") }
    val pass = remember { mutableStateOf("") }
    val emailValid = remember(email.value) {
        email.value.matches(component.emailRegex)
    }
    val passValid = remember(pass.value) {
        pass.value.isNotBlank() && pass.value.trim().length >= 8
    }

    when (calculateWindowSizeClass().widthSizeClass) {
        WindowWidthSizeClass.Compact -> CompactScreen(
            email = email,
            emailValid = emailValid,
            pass = pass,
            passValid = passValid,
            component = component
        )
        else -> DefaultScreen(
            email = email,
            emailValid = emailValid,
            pass = pass,
            passValid = passValid,
            component = component
        )
    }
}

@Composable
private fun CompactScreen(
    email: MutableState<String>,
    emailValid: Boolean,
    pass: MutableState<String>,
    passValid: Boolean,
    component: LoginComponent
) {
    LazyColumn(
        modifier = Modifier.safeDrawingPadding().fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            val res = if (LocalDarkMode.current) {
                SharedRes.images.account_dark
            } else {
                SharedRes.images.account_light
            }

            Image(
                modifier = Modifier.fillParentMaxWidth(0.5F).padding(bottom = 16.dp),
                painter = painterResource(res),
                contentDescription = stringResource(SharedRes.strings.login),
                contentScale = ContentScale.FillWidth
            )
        }
        Content(
            email = email,
            emailValid = emailValid,
            pass = pass,
            passValid = passValid,
            component = component
        )
    }
}

@Composable
private fun DefaultScreen(
    email: MutableState<String>,
    emailValid: Boolean,
    pass: MutableState<String>,
    passValid: Boolean,
    component: LoginComponent
) {
    Row(
        modifier = Modifier.safeDrawingPadding().fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val res = if (LocalDarkMode.current) {
                SharedRes.images.account_dark
            } else {
                SharedRes.images.account_light
            }

            Image(
                modifier = Modifier.fillMaxWidth(0.5F),
                painter = painterResource(res),
                contentDescription = stringResource(SharedRes.strings.login),
                contentScale = ContentScale.FillWidth
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Content(
                email = email,
                emailValid = emailValid,
                pass = pass,
                passValid = passValid,
                component = component
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
private fun LazyListScope.Content(
    email: MutableState<String>,
    emailValid: Boolean,
    pass: MutableState<String>,
    passValid: Boolean,
    component: LoginComponent
) {
    item {
        Text(
            text = stringResource(SharedRes.strings.account),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge
        )
    }
    item {
        OutlinedTextField(
            modifier = Modifier.fillParentMaxWidth(0.75F).autofill(
                autofillTypes = listOf(AutofillType.EmailAddress),
                onFill = {
                    email.value = it
                }
            ),
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = {
                Text(text = stringResource(SharedRes.strings.email))
            },
            placeholder = {
                Text(text = stringResource(SharedRes.strings.enter_your_email))
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Email
            )
        )
    }
    item {
        OutlinedTextField(
            modifier = Modifier.fillParentMaxWidth(0.75F).autofill(
                autofillTypes = listOf(AutofillType.Password),
                onFill = {
                    pass.value = it
                }
            ),
            value = pass.value,
            onValueChange = {
                pass.value = it
            },
            label = {
                Text(text = stringResource(SharedRes.strings.password))
            },
            placeholder = {
                Text(text = stringResource(SharedRes.strings.enter_your_password))
            },
            singleLine = true,
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Password
            )
        )
    }
    item {

        val loggingIn by component.loggingIn.collectAsStateWithLifecycle()

        Button(
            modifier = Modifier.fillParentMaxWidth(0.75F),
            onClick = {
                component.login(email.value, pass.value.trim())
            },
            enabled = (emailValid && passValid && !loggingIn) || run {
                Napier.e("Email: $emailValid")
                Napier.e("Pass: $passValid")
                Napier.e("Login: $loggingIn")
                false
            }
        ) {
            Text(
                text = stringResource(SharedRes.strings.login),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    item {
        val passwordReset by component.passwordReset.collectAsStateWithLifecycle()

        AnimatedVisibility(
            visible = emailValid && pass.value.isBlank() && !passwordReset,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TextButton(
                modifier = Modifier.fillParentMaxWidth(0.75F),
                onClick = {
                    component.resetPassword(email.value)
                },
                enabled = emailValid && pass.value.isBlank() && !passwordReset
            ) {
                Text(text = stringResource(SharedRes.strings.reset_password))
            }
        }
    }
    item {
        TextButton(
            modifier = Modifier.padding(vertical = 16.dp),
            onClick = {
                component.skip()
            }
        ) {
            Text(text = stringResource(SharedRes.strings.skip))
        }
    }
}