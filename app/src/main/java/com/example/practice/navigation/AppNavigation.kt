package com.example.practice.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice.MyApp
import com.example.practice.screen.auth.AuthScreen
import com.example.practice.screen.auth.LoginScreen
import com.example.practice.screen.auth.SignUpScreen
import com.example.practice.viewmodel.AuthViewModel
import com.example.practice.viewmodel.VideoViewModel
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    ) {
    val viewModel: AuthViewModel = viewModel()
    val videoViewModel: VideoViewModel = viewModel()
    val navController = rememberNavController()
    val context = LocalContext.current

    val isLoggedIn = remember { mutableStateOf(false) }
    val isReady = remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        delay(1000)
        isReady.value = true
    }

    // Call checkLoginStatus to update the login state reactively
    LaunchedEffect(context) {
        viewModel.checkLoginStatus(context)
        isLoggedIn.value = viewModel.isLoggedIn(context) // Reflect the updated state
    }

    val startDestination = if (isLoggedIn.value) "myapp" else "auth"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("auth") {
            AuthScreen(
                modifier = modifier,
                navController = navController,
                isReadyToRender = isReady.value
            )
        }
        composable("signup") {
            SignUpScreen(modifier,navController,viewModel)
        }
        composable("login") {
            LoginScreen(modifier,navController,viewModel,context)
        }
        composable("myapp") {
            MyApp(
                modifier,
                viewModel,
                videoViewModel = videoViewModel,
                context = context
            )
        }
    }

}