package com.example.practice.api.dataclass.video

data class FavoriteVideosItem(
    var id: Int,
    val title: String,
    val description: String,
    val video_file: String,
    val uploaded_by: String,
    val uploaded_at: String,
    val total_dislikes: Int,
    val total_likes: Int,
    val thamnail: String,
    val is_favorited: Boolean
)
