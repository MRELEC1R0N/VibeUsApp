package com.example.chattingapplication.feature.Login


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chattingapplication.R
import com.example.chattingapplication.feature.ReusableCardLayout
import com.example.chattingapplication.feature.navigation.NavigationRouts as Screen

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: SignInViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()

    var email by remember{
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {

        when (uiState.value) {
            is SignInState.Success -> {

                navController.navigate(Screen.MapScreen.route)
            }

            is SignInState.Error -> {
                Toast.makeText(context, "Sign In failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }



    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        ReusableCardLayout(
            modifier = Modifier.padding(it),
            imageResource = R.drawable.logincouple,
            imageHeightFraction = 0.5f,
            cardHeightFraction = 0.6f
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),

                    )
                Text(
                    text = "Login to your account",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    label = { Text(text = "Email") })
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.size(16.dp))

                if (uiState.value == SignInState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { viewModel.signIn(email, password) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = email.isNotEmpty() && password.isNotEmpty() && (uiState.value == SignInState.Nothing || uiState.value == SignInState.Error)
                    ) {
                        Text(text = "Sign In")
                    }

                    TextButton(onClick = { navController.navigate(Screen.SignupScreen.route) }) {
                        Text(text = "Don't have an account? Sign Up")
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true , showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}