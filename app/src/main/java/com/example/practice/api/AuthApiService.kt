package com.example.practice.api


import com.example.practice.api.dataclass.comment.CommentRequest
import com.example.practice.api.dataclass.comment.CommentResponse
import com.example.practice.api.dataclass.comment.Comments
import com.example.practice.api.dataclass.likedislike.LikeDislikeRequest
import com.example.practice.api.dataclass.login.LoginRequest
import com.example.practice.api.dataclass.login.LoginResponse
import com.example.practice.api.dataclass.profile.ProfileResponse
import com.example.practice.api.dataclass.signup.SignUpRequest
import com.example.practice.api.dataclass.video.FavoriteVideos
import com.example.practice.api.dataclass.video.UploadVideos
import com.example.practice.api.dataclass.video.UploadVideosItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApiService {

    @POST("auth/registration/")
    suspend fun registerUser(@Body request: SignUpRequest): Response<Unit>

    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse



    @GET("api/videos/upload-videos/")
    suspend fun getVideos(): Response<UploadVideos>

    @Multipart
    @POST("api/videos/upload-videos/")
    suspend fun uploadVideo(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part video: MultipartBody.Part?,
        @Part thumbnail: MultipartBody.Part?
    ): Response<UploadVideosItem>

    // Make as favorite videos
    @POST("api/videos/toggle-favorite/{video-id}/")
    suspend fun makeFavorite(
        @Header("Authorization") token: String,  // Fix the variable name type
        @Path("video-id") videoId: Int  // Add Path annotation
    ): Response<Unit>

    // Fetch favorite videos
    @GET("api/videos/upload-videos/favorite-videos/")
    suspend fun getFavorite(
        @Header("Authorization") token: String
    ): Response<FavoriteVideos>

    @POST("api/videos/{video_id}/like/")
    suspend fun likeVideo(
        @Header("Authorization") token: String,
        @Path("video_id") videoId: Int
    ): Response<Unit>

    @POST("api/videos/{video_id}/dislike/")
    suspend fun dislikeVideo(
        @Header("Authorization") token: String,
        @Path("video_id") videoId: Int
    ): Response<Unit>



    // Fetch Comments
    @GET("api/videos/video-comments/")
    suspend fun getComments(): Response<Comments>

    @POST("api/videos/video-comments/")
    suspend fun postComment(
        @Header("Authorization") token: String,
        @Body comment: CommentRequest
    ): Response<CommentResponse>



    @GET("api/profile/update/")
    suspend fun getUserInfo(@Header("Authorization") token: String): ProfileResponse

    // POST request to update user information
    @Multipart
    @PUT("api/profile/update/")
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part profile_picture: MultipartBody.Part? = null, // No part name here
        @Part("UserId") userId: RequestBody? = null
    ): ProfileResponse


    @POST("auth/logout/")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>


}
