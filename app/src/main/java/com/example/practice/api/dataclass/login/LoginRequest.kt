package com.example.practice.api.dataclass.login

data class LoginRequest(
    val username: String,
    val email: String,
    val password: String
)