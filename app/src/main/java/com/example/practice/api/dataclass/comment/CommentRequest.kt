package com.example.practice.api.dataclass.comment

// Request Body
data class CommentRequest(
    val video: Int,    // Video ID
    val text: String   // Comment text
)
