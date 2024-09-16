package com.example.chattingapplication.feature.Singup

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
import com.example.chattingapplication.feature.navigation.NavigationRouts

@Composable
fun SignupScreen(navController: NavController) {

    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirm by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {

        when (uiState.value) {
            is SignUpState.Success -> {
                navController.navigate(NavigationRouts.MapScreen.route)
            }

            is SignUpState.Error -> {
                Toast.makeText(context, "Sign In failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
    Scaffold {
        ReusableCardLayout(
            modifier = Modifier.padding(it),
            imageResource = R.drawable.singupcouple,
            imageHeightFraction = 0.5f,
            cardHeightFraction = 0.7f
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "Welcome to Vibe Us",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),

                    )
                Text(
                    text = "Join us today",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.size(16.dp))

                OutlinedTextField(value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Full Name") })

                OutlinedTextField(value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Email") })
                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = confirm, onValueChange = { confirm = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = password.isNotEmpty() && confirm.isNotEmpty() && password != confirm
                )
                Spacer(modifier = Modifier.size(16.dp))
                if (uiState.value == SignUpState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            viewModel.signUp(name, email, password)
                        }, modifier = Modifier.fillMaxWidth(),
                        enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && password == confirm
                    ) {
                        Text(text = "Sign Up")
                    }
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(text = "Already have an account? Sign In")
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun SignupScreenPreview(){
    SignupScreen(rememberNavController())
}