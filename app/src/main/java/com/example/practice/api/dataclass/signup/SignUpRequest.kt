package com.example.practice.api.dataclass.signup

data class SignUpRequest(
    val username: String,
    val email: String,
    val password1: String,
    val password2: String
)
