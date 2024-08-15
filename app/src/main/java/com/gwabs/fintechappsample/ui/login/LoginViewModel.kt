package com.gwabs.fintechappsample.ui.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.gwabs.fintechappsample.data.model.LoginState
import com.gwabs.fintechappsample.util.isValidEmail
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var loginState = mutableStateOf<LoginState>(LoginState.Idle)

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun login() {
        // Set state to Loading
        loginState.value = LoginState.Loading

        // Perform login with Firebase
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login success
                            loginState.value = LoginState.Success("Login successful")
                        } else {
                            // Login failure
                            loginState.value = LoginState.Error("Authentication failed: ${task.exception?.message}")
                        }
                    }
            } catch (e: Exception) {
                loginState.value = LoginState.Error("An error occurred: ${e.message}")
                Log.i("TAG",e.message.toString())
            }
        }
    }
}
