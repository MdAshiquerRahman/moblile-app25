package com.example.practice.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.practice.api.AuthRetrofitInstance
import com.example.practice.api.LoginRequest
import com.example.practice.api.SignUpRequest
import com.example.practice.api.UserProfile
import androidx.core.content.edit
import com.example.practice.repository.SharedPreferencesHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AuthViewModel : ViewModel() {
    // State variables
    var token by mutableStateOf<String?>(null)
    var userProfile by mutableStateOf<UserProfile?>(null)
    var errorMessage by mutableStateOf<String?>(null)
    var registrationSuccess by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun login(username: String, email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
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
            } finally {
                isLoading = false
            }
        }
    }



    fun signUp(username: String, email: String, password1: String, password2: String) {
        viewModelScope.launch {
            val request = SignUpRequest(username, email, password1, password2)
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

    fun logout(context: Context) {
        viewModelScope.launch {
            // Clear the saved token from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit { remove("auth_token") }

            // Update the login state
            _isLoggedIn.value = false
            token = null
            userProfile = null
        }
    }


    fun saveUsername(context: Context, username: String) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit { putString("username", username) }
    }

    fun getUsername(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    fun saveToken(context: Context, token: String) {
        SharedPreferencesHelper.save(context, "auth_token", token)
    }

    fun getToken(context: Context): String? {
        return SharedPreferencesHelper.get(context, "auth_token")
    }

    // Check if the user is logged in
    fun isLoggedIn(context: Context): Boolean {
        return !getToken(context).isNullOrEmpty()
    }

    // Check login status by retrieving token from SharedPreferences
    fun checkLoginStatus(context: Context) {
        viewModelScope.launch {
            // Use the isLoggedIn function to simplify the logic
            _isLoggedIn.value = isLoggedIn(context)
        }
    }



}
