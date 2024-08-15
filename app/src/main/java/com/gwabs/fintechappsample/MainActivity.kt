package com.gwabs.fintechappsample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.gwabs.fintechappsample.ui.login.LoginScreen
import com.gwabs.fintechappsample.ui.login.LoginViewModel
import com.gwabs.fintechappsample.ui.theme.FintechAppSampleTheme

class MainActivity : ComponentActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = LoginViewModel()
        setContent {
            FintechAppSampleTheme {
                   LoginScreen(viewModel, onLogin = {
                       val intent = Intent(this,HomeActivity::class.java)
                       startActivity(intent)
                       finish()
                   })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

