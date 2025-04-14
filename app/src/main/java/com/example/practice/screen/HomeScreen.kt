package com.example.practice.screen


import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.practice.api.allRecipeData
import com.example.practice.pages.post.RecipePostsCard
import com.example.practice.viewmodel.VideoViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val videoViewModel: VideoViewModel = viewModel()
    val isLoading = videoViewModel.isLoading.observeAsState(false)
    val errorMessage = videoViewModel.errorMessage.observeAsState(null)

    // Fetch videos after login status is confirmed
    LaunchedEffect(Unit) {
        videoViewModel.fetchVideos()
    }
    if (isLoading.value) {
        CircularProgressIndicator()
    } else if (!errorMessage.value.isNullOrEmpty()) {
        Text(text = "Error: ${errorMessage.value}")
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 16.dp),
        ) {
            VideoTutorials(navController,modifier,videoViewModel)
        }
    }
}



@Composable
fun VideoTutorials(navController: NavController,modifier: Modifier = Modifier, videoViewModel: VideoViewModel) {
    val videoList = videoViewModel.videoList.observeAsState(emptyList())
    val gridCells = GridCells.Fixed(2) // Define grid column count

    LazyVerticalGrid(
        columns = gridCells,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videoList.value) { video ->
            Box(
                modifier = Modifier
                    .height(250.dp) // Set a fixed height for grid items
                    .fillMaxWidth()
            ) {
                RecipePostsCard(
                    navController = navController,
                    title = video.title,
                    description = video.description,
                    author = video.uploaded_by.toString(),
                    totalLikes = video.total_likes,
                    videoUrl = video.video_file,
                )
            }
        }
    }
}