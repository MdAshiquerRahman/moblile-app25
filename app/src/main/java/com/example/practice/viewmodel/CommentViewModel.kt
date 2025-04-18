package com.example.practice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.api.AuthRetrofitInstance
import kotlinx.coroutines.launch



class CommentViewModel : ViewModel() {

    private val _commentList = MutableLiveData<List<CommentItem>>()
    val commentList: LiveData<List<CommentItem>> = _commentList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    val authViewModel: AuthViewModel = AuthViewModel()

    // Post comment function
    fun postComment(token: String, videoId: Int, text: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = CommentPostRequest(video = videoId, text = text)
                val response = AuthRetrofitInstance.api.postComment(
                    token = "Token $token",
                    comment = request
                )
                if (response.isSuccessful) {
                    fetchComments() // Optionally refresh comments after posting
                } else {
                    _errorMessage.value = "Error posting comment: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Exception: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fetch comments
    fun fetchComments() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = AuthRetrofitInstance.api.getComments()
                if (response.isSuccessful) {
                    _commentList.value = response.body() ?: emptyList()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error fetching comments: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
