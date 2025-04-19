package com.example.practice


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.practice.navigation.AppNavigation
import com.example.practice.screen.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplash by remember { mutableStateOf(true) }

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                if (showSplash) {
                    SplashScreen(
                        onSplashComplete = { showSplash = false } // Transition to AuthScreen
                    )
                } else {
                    AppNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }
}
