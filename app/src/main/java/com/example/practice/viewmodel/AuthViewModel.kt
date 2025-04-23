package com.example.practice.viewmodel


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.practice.api.AuthRetrofitInstance
import androidx.core.content.edit
import com.example.practice.api.dataclass.login.LoginRequest
import com.example.practice.api.dataclass.profile.ProfileResponse
import com.example.practice.api.dataclass.signup.SignUpRequest
import com.example.practice.repository.SharedPreferencesHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class AuthViewModel : ViewModel() {
    // State variables
    var token by mutableStateOf<String?>(null)
//    var userProfile by mutableStateOf<UserProfile?>(null)
    var userPassword by mutableStateOf("pass")
    var errorMessage by mutableStateOf<String?>(null)
    var registrationSuccess by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    var profile by mutableStateOf<ProfileResponse?>(null)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn



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

    fun login(username: String, email: String, password: String) {
        userPassword = password
        viewModelScope.launch {
            isLoading = true
            try {
                val response = AuthRetrofitInstance.api.login(LoginRequest(username = username, email = email, password = password))
                token = response.key
                val user = AuthRetrofitInstance.api.getUserInfo("Token $token")

                profile = user
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Login failed: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    // Function to update user profile (POST method)
    fun updateProfile(
        context: Context,
        username: String,
        email: String,
        profile_picture: Uri?,
        userId: String?
    ) {
        viewModelScope.launch {
            try {
                val token = getToken(context)
                val contentResolver = context.contentResolver

                val usernamePart = username.toRequestBody("text/plain".toMediaType())
                val emailPart = email.toRequestBody("text/plain".toMediaType())
                val userIdPart = userId?.toRequestBody("text/plain".toMediaType())

                val imagePart = profile_picture?.let {
                    val inputStream = contentResolver.openInputStream(it)!!
                    val fileBytes = inputStream.readBytes()
                    val requestBody = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("profile_picture", "profile.jpg", requestBody)
                }

                val response = AuthRetrofitInstance.api.updateUserInfo(
                    token = "Token $token",
                    username = usernamePart,
                    email = emailPart,
                    profile_picture = imagePart,
                    userId = userIdPart
                )

                profile = response
                Log.d("UPDATE_PROFILE", "Success: $response")

            } catch (e: Exception) {
                Log.e("UPDATE_PROFILE", "Error: ${e.localizedMessage}")
            }
        }
    }



    fun logout(context: Context) {
        viewModelScope.launch {
            isLoading = true
            try {
                // Retrieve the token
                val savedToken = token
                Log.d("TOKEN", "Token: $savedToken") // Debugging log

                if (!savedToken.isNullOrEmpty()) {
                    // Make the API call to logout
                    val response = AuthRetrofitInstance.api.logout("Token $savedToken")
                    if (response.isSuccessful) {
                        // Clear the token from SharedPreferences
                        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit { clear() }

                        // Update login state
                        _isLoggedIn.value = false
                        token = null
                        profile = null

                        Log.d("LOGOUT", "Successfully logged out")
                    } else {
                        Log.e("LOGOUT", "Logout failed: ${response.code()} - ${response.message()}")
                        errorMessage = response.errorBody()?.string() ?: "Logout failed"
                    }
                } else {
                    Log.e("LOGOUT", "No token found, cannot log out")
                    errorMessage = "No token found"
                }
            } catch (e: Exception) {
                Log.e("LOGOUT", "Error during logout: ${e.localizedMessage}")
                errorMessage = "An error occurred: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
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

    fun saveEmail(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        sharedPreferences.edit { putString("email", email) }
    }

    fun getEmail(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", null)
    }

    fun savePassword(context: Context, password: String) {
        val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        sharedPreferences.edit { putString("password", password) }
    }

    fun getPassword(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPreferences.getString("password", null)
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
