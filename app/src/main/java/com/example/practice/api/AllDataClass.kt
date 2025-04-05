package com.example.practice.api


data class LoginRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    val key: String
)


data class UserProfile(
    val username: String,
    val email: String,
    val profilePicture: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)
