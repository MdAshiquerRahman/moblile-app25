package com.example.practice.screen.auth

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.practice.elements.CustomOutlinedTextField
import com.example.practice.elements.CustomizedPasswordField
import com.example.practice.viewmodel.AuthViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: AuthViewModel,context: Context) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val user = viewModel.profile
    val error = viewModel.errorMessage

    val isLoading = viewModel.isLoading

    val token = viewModel.token

    LaunchedEffect(user) {
        user?.let {
            navController.navigate("myapp") {
                popUpTo("login") { inclusive = true } // Remove login from the back stack
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
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Login", style = MaterialTheme.typography.headlineMedium)

            CustomOutlinedTextField(
                value = username,
                onValueChange = { username = it },
                labelText ="Username"
            )
            CustomOutlinedTextField(
                value = email,
                onValueChange = { email = it },
                labelText ="Email"
            )
            CustomizedPasswordField(
                value = password,
                onValueChange = { password = it },
                labelText = "Password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Calling login function in ViewModel
                viewModel.login(username, email, password)
                viewModel.savePassword(context = context, password)
                Log.e("Pass", password)
            }) {
                Text("Login")
            }
            viewModel.saveUsername(context = context, user?.username ?: "Null")
            viewModel.saveEmail(context = context, user?.email?: "Null")

            Log.e("Picture", user?.profile_picture.toString())


            viewModel.saveToken(context, token ?: "Null")
            // Show error message if any
            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }
        }
    }
}
