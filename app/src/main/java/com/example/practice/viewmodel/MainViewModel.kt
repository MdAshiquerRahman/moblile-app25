package com.example.practice.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.*
import com.example.practice.api.*
import com.example.practice.repository.SharedPreferencesHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class MainViewModel : ViewModel() {

    // AUTH STATE
    var token by mutableStateOf<String?>(null)
    var userPassword by mutableStateOf("pass")
    var errorMessage by mutableStateOf<String?>(null)
    var registrationSuccess by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var profile by mutableStateOf<ProfileResponse?>(null)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // VIDEO STATE
    private val _videoList = MutableLiveData<List<UploadVideosItem>>()
    val videoList: LiveData<List<UploadVideosItem>> = _videoList

    private val _videoError = MutableLiveData<String?>()
    val videoError: LiveData<String?> = _videoError

    // COMMENT STATE
    private val _commentList = MutableLiveData<List<CommentItem>>()
    val commentList: LiveData<List<CommentItem>> = _commentList

    private val _commentError = MutableLiveData<String?>()
    val commentError: LiveData<String?> = _commentError

    // --- AUTH FUNCTIONS ---

    fun signUp(username: String, email: String, password1: String, password2: String) {
        viewModelScope.launch {
            try {
                val request = SignUpRequest(username, email, password1, password2)
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
                val response = AuthRetrofitInstance.api.login(LoginRequest(username, email, password))
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

    fun logout(context: Context) {
        viewModelScope.launch {
            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit {
                remove("auth_token")
            }
            _isLoggedIn.value = false
            token = null
            profile = null
        }
    }

    fun checkLoginStatus(context: Context) {
        viewModelScope.launch {
            _isLoggedIn.value = isLoggedIn(context)
        }
    }

    fun isLoggedIn(context: Context): Boolean {
        return !getToken(context).isNullOrEmpty()
    }

    fun saveToken(context: Context, token: String) {
        SharedPreferencesHelper.save(context, "auth_token", token)
    }

    fun getToken(context: Context): String? {
        return SharedPreferencesHelper.get(context, "auth_token")
    }

    // --- PROFILE ---

    fun updateProfile(
        context: Context,
        username: String,
        email: String,
        profile_picture: Uri?,
        userId: String?
    ) {
        viewModelScope.launch {
            try {
                val token = getToken(context) ?: return@launch
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

    // --- VIDEO ---

    fun fetchVideos() {
        viewModelScope.launch {
            _videoError.value = null
            try {
                val response = AuthRetrofitInstance.api.getVideos()
                if (response.isSuccessful) {
                    _videoList.value = response.body() ?: emptyList()
                } else {
                    _videoError.value = "Error fetching videos: ${response.message()}"
                }
            } catch (e: Exception) {
                _videoError.value = "Error: ${e.localizedMessage}"
            }
        }
    }

    // --- COMMENTS ---

    fun fetchComments() {
        viewModelScope.launch {
            _commentError.value = null
            try {
                val response = AuthRetrofitInstance.api.getComments()
                if (response.isSuccessful) {
                    _commentList.value = response.body() ?: emptyList()
                } else {
                    _commentError.value = "Error fetching comments: ${response.message()}"
                }
            } catch (e: Exception) {
                _commentError.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    fun postComment(context: Context, videoId: Int, text: String) {
        viewModelScope.launch {
            _commentError.value = null
            try {
                val token = getToken(context) ?: return@launch
                val request = CommentPostRequest(video = videoId, text = text)
                val response = AuthRetrofitInstance.api.postComment("Token $token", request)
                if (response.isSuccessful) {
                    fetchComments()
                } else {
                    _commentError.value = "Error posting comment: ${response.message()}"
                }
            } catch (e: Exception) {
                _commentError.value = "Exception: ${e.localizedMessage}"
            }
        }
    }

    // --- Optional: Email/Password SharedPrefs Helper ---

    fun saveUsername(context: Context, username: String) {
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit {
            putString("username", username)
        }
    }

    fun getUsername(context: Context): String? {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            .getString("username", null)
    }

    fun saveEmail(context: Context, email: String) {
        context.getSharedPreferences("user_pref", Context.MODE_PRIVATE).edit {
            putString("email", email)
        }
    }

    fun getEmail(context: Context): String? {
        return context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)
            .getString("email", null)
    }

    fun savePassword(context: Context, password: String) {
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit {
            putString("password", password)
        }
    }

    fun getPassword(context: Context): String? {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE)
            .getString("password", null)
    }
}
