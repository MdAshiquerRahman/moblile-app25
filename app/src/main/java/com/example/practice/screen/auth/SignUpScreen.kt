package com.example.practice.screen.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.practice.elements.CustomOutlinedTextField
import com.example.practice.elements.CustomizedPasswordField
import com.example.practice.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    // Observe registration success
    val registrationSuccess = viewModel.registrationSuccess

    LaunchedEffect(registrationSuccess) {
        if (registrationSuccess) {
            Toast.makeText(context, "Registration successful please login", Toast.LENGTH_LONG).show()
            delay(2000L)
            // Navigate to AuthScreen upon successful registration
            navController.navigate("auth") {
                popUpTo("signUpScreen") { inclusive = true } // Remove SignUpScreen from the back stack
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registration", style = MaterialTheme.typography.headlineMedium)

        CustomOutlinedTextField(
            value = username,
            onValueChange = { username = it },
            labelText = "Username"
        )
        CustomOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            labelText = "Email"
        )
        CustomizedPasswordField(
            value = password,
            onValueChange = { password = it },
            labelText = "Password"
        )
        CustomizedPasswordField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            labelText = "Confirm Password"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Trigger sign-up function in the ViewModel
            viewModel.signUp(username, email, password, confirmPassword)
        }) {
            Text("Sign Up")
        }
    }
}
