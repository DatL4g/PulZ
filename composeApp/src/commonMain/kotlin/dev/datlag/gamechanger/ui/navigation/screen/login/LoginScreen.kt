package dev.datlag.gamechanger.ui.navigation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
import dev.datlag.gamechanger.LocalDarkMode
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.autofill
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(component: LoginComponent) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

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
                        email = it
                    }
                ),
                value = email,
                onValueChange = {
                    email = it
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
                        pass = it
                    }
                ),
                value = pass,
                onValueChange = {
                    pass = it
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
            val emailValid = remember(email) {
                email.matches(component.emailRegex)
            }
            val passValid = remember(pass) {
                pass.isNotBlank() && pass.trim().length >= 8
            }

            Button(
                modifier = Modifier.fillParentMaxWidth(0.75F),
                onClick = {
                    component.login(email, pass)
                },
                enabled = emailValid && passValid
            ) {
                Text(
                    text = stringResource(SharedRes.strings.login),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        item {
            TextButton(
                modifier = Modifier.fillParentMaxWidth(0.75F).padding(vertical = 16.dp),
                onClick = {
                    component.skip()
                }
            ) {
                Text(text = stringResource(SharedRes.strings.skip))
            }
        }
    }
}