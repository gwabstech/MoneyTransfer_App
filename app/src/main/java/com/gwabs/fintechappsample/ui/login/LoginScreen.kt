package com.gwabs.fintechappsample.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.gwabs.fintechappsample.data.model.LoginState
import com.gwabs.fintechappsample.ui.ErrorMessage
import com.gwabs.fintechappsample.ui.GradientButton
import com.gwabs.fintechappsample.ui.LoadingIndicator
import com.gwabs.fintechappsample.ui.StandardTextField
import com.gwabs.fintechappsample.ui.theme.FintechAppSampleTheme
import com.gwabs.fintechappsample.util.isValidEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onLogin: () -> Unit
) {
    val email by loginViewModel.email
    val password by loginViewModel.password
    var errorMessage by remember { mutableStateOf("") }
    var showErrorMessage by remember { mutableStateOf(false) }
    val loginState by loginViewModel.loginState
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(confirmValueChange = { false })
    var showLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(30.dp),
            text = "Sign in",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge
        )

        StandardTextField(
            value = email,
            onValueChange = {
                showErrorMessage = false
                loginViewModel.onEmailChange(it)
            },
            label = "Email",
            keyboardType = KeyboardType.Email,
            modifier = Modifier.padding(16.dp)
        )

        StandardTextField(
            value = password,
            onValueChange = {
                showErrorMessage = false
                loginViewModel.onPasswordChange(it)
            },
            label = "Password",
            isPassword = true,
            modifier = Modifier.padding(16.dp)
        )

        GradientButton(
            modifier = Modifier.padding(16.dp),
            title = "Login",
            isEnable = true,
            onClick = {
                when {
                    email.isEmpty() -> {
                        showErrorMessage = true
                        errorMessage = "Email is required"
                    }
                    !isValidEmail(email) -> {
                        showErrorMessage = true
                        errorMessage = "Invalid Email"
                    }
                    password.isEmpty() -> {
                        showErrorMessage = true
                        errorMessage = "Password is required"
                    }
                    password.length < 6 -> {
                        showErrorMessage = true
                        errorMessage = "Invalid Password"
                    }
                    else -> {
                        loginViewModel.login()
                    }
                }
            }
        )

        when (loginState) {
            is LoginState.Loading -> {
                showLoading = true
            }

            is LoginState.Success -> {
                showLoading = false
                Toast.makeText(
                    context,
                    (loginState as LoginState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                onLogin()
            }

            is LoginState.Error -> {
                showLoading = false
                ErrorMessage(message = (loginState as LoginState.Error).error)
            }

            LoginState.Idle -> {
                // Do nothing
            }
        }

        if (showErrorMessage) {
            ErrorMessage(message = errorMessage)
        }
        if (showLoading) {

            ModalBottomSheet(
                sheetState = sheetState,
                modifier = Modifier.padding(bottom = 20.dp),
                containerColor = MaterialTheme.colorScheme.background,
                dragHandle = { BottomSheetDefaults.DragHandle() },
                onDismissRequest = { showLoading = false }
            ) {
              LoadingIndicator()
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ComponentsPreview() {
    val navController = rememberNavController()
    FintechAppSampleTheme(darkTheme = true) {
        LoginScreen(onLogin = {})
    }
}
