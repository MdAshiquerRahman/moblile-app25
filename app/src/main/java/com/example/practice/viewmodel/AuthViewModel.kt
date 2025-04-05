package com.example.practice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.practice.api.AuthRetrofitInstance
import com.example.practice.api.LoginRequest
import com.example.practice.api.RegisterRequest
import com.example.practice.api.UserProfile


class AuthViewModel : ViewModel() {
    var token by mutableStateOf<String?>(null)
    var userProfile by mutableStateOf<UserProfile?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    var registrationSuccess by mutableStateOf(false)

    fun login(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = AuthRetrofitInstance.api.login(
                    LoginRequest(username = username, email = email, password = password)
                )
                token = response.key

                val user = AuthRetrofitInstance.api.getUserInfo("Token ${response.key}")
                userProfile = user
                errorMessage = null

            } catch (e: Exception) {
                errorMessage = "Login failed: ${e.localizedMessage}"
            }
        }
    }

    fun register(username: String, email: String, password1: String, password2: String) {
        viewModelScope.launch {
            val request = RegisterRequest(username, email, password1, password2)
            try {
                val response = AuthRetrofitInstance.api.registerUser(request)
                if (response.isSuccessful) {
                    registrationSuccess = true
                } else {
                    errorMessage = response.errorBody()?.string() ?: "Registration failed"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "An error occurred"
            }
        }
    }

}
