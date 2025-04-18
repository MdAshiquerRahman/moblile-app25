package com.example.practice.api


data class LoginRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    val key: String
)

data class Token(
    val detail: String
)

class UploadVideos : ArrayList<UploadVideosItem>()

data class UploadVideosItem(
    val description: String,
    val id: Int,
    val title: String,
    val total_dislikes: Int,
    val total_likes: Int,
    val uploaded_at: String,
    val uploaded_by: String,
    val video_file: String,
    val thamnail: String
)


//data class UserProfile(
//    val pk: Int,
//    val username: String,
//    val email: String,
//    val first_name: String,
//    val last_name: String,
//)

data class Password(
    val password: String
)

data class SignUpRequest(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)


data class Profile(
    val username: String,
    val email: String,
    val profile_picture: String,
    val UserId: String
)

data class ProfileResponse(
    val username: String,
    val email: String,
    val profile_picture: String,
    val UserId: String
)


