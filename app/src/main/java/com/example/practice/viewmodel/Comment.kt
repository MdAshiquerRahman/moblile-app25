package com.example.practice.viewmodel

class Comments : ArrayList<CommentItem>()

data class CommentItem(
    val id: Int,
    val video: Int,
    val user: String,
    val text: String,
    val created_at: String,
)

data class CommentPostRequest(
    val video: Int,
    val text: String
)