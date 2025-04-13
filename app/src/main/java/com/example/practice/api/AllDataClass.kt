package com.example.practice.api


data class LoginRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    val key: String
)

class UploadVideos : ArrayList<UploadVideosItem>()

data class UploadVideosItem(
    val description: String,
    val id: Int,
    val title: String,
    val total_dislikes: Int,
    val total_likes: Int,
    val uploaded_at: String,
    val uploaded_by: Int,
    val video_file: String
)


data class UserProfile(
    val username: String,
    val email: String,
    val profilePicture: String
)

data class SignUpRequest(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)
