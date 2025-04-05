package com.example.practice.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/registration/")
    suspend fun registerUser(@Body request: RegisterRequest): Response<Unit>

    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/user/")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): UserProfile
}
