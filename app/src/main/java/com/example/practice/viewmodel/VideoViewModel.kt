package com.example.practice.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.api.AuthRetrofitInstance
import com.example.practice.api.UploadVideos
import com.example.practice.api.UploadVideosItem
import kotlinx.coroutines.launch
import retrofit2.Response
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



class VideoViewModel (
    private val authViewModel: AuthViewModel
) : ViewModel() {
    // LiveData for storing video list
    private val _videoList = MutableLiveData<List<UploadVideosItem>>()
    val videoList: LiveData<List<UploadVideosItem>> = _videoList

    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Fetch videos if user is logged in
    fun fetchVideos(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Use AuthViewModel to check login status
                authViewModel.checkLoginStatus(context)
                val isLoggedIn = authViewModel.isLoggedIn.value

                if (isLoggedIn) {
                    // Fetch videos from API
                    val response: Response<UploadVideos> = AuthRetrofitInstance.api.getVideos()
                    if (response.isSuccessful) {
                        _videoList.value = response.body() ?: emptyList()
                        _errorMessage.value = null
                    } else {
                        _errorMessage.value = "Error fetching videos: ${response.message()}"
                    }
                } else {
                    _errorMessage.value = "User is not logged in"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

