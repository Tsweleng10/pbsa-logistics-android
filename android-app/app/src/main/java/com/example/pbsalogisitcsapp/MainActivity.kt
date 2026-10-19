package com.example.pbsalogisticsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pbsalogisticsapp.ui.theme.PbsaLogisticsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PbsaLogisticsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // We create the ViewModel here so we can share it
                    val loginViewModel: LoginViewModel = viewModel()

                    // If loginSuccess is true, show Dashboard. Else, show Login.
                    // In MainActivity.kt
                    if (loginViewModel.loginSuccess) {
                        DashboardScreen(
                            viewModel = loginViewModel, // Pass it here
                            onLogout = {
                                loginViewModel.loginSuccess = false
                                loginViewModel.email = ""
                                loginViewModel.password = ""
                                loginViewModel.authToken = "" // Clear token on logout
                            }
                        )
                    } else {
                        LoginScreen(viewModel = loginViewModel)
                    }
                        )
                    } else {
                        LoginScreen(viewModel = loginViewModel)
                    }
                }
            }
        }
    }

    private fun viewModel(): LoginViewModel {
        val todo = TODO("Not yet implemented")
    }
}