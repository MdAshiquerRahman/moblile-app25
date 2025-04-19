package com.example.practice.api.dataclass.comment

// Response Body
data class CommentResponse(
    val id: Int,              // Comment ID
    val video: Int,           // Video ID
    val user: String,         // Username
    val text: String,         // Comment text
    val created_at: String    // Creation timestamp
)
