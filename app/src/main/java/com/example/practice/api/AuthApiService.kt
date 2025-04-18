package com.example.practice.api

import com.example.practice.viewmodel.CommentItem
import com.example.practice.viewmodel.CommentPostRequest
import com.example.practice.viewmodel.Comments
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface AuthApiService {

//    @GET("api/profile/update/")
//    suspend fun getProfile(
//        @Header("Authorization") token: String
//    ): Response<ProfileResponse>


    @POST("auth/registration/")
    suspend fun registerUser(@Body request: SignUpRequest): Response<Unit>

    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/videos/upload_videos/")
    suspend fun getVideos(): Response<UploadVideos>

    @GET("api/videos/video-comments/")
    suspend fun getComments(): Response<Comments>

    @POST("api/videos/video-comments/")
    suspend fun postComment(
        @Header("Authorization") token: String,
        @Body comment: CommentPostRequest
    ): Response<CommentItem>


    @GET("api/profile/update/")
    suspend fun getUserInfo(@Header("Authorization") token: String): ProfileResponse

    // POST request to update user information
    @Multipart
    @PUT("api/profile/update/")
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part profile_picture: MultipartBody.Part? = null,
        @Part("UserId") userId: RequestBody? = null
    ): ProfileResponse


}
