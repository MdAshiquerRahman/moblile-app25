package com.example.practice.screen



import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.practice.pages.post.RecipePostsCard
import com.example.practice.viewmodel.VideoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    videoViewModel: VideoViewModel,
    modifier: Modifier = Modifier
) {
    val isLoading = videoViewModel.isLoading.observeAsState(false)
    val errorMessage = videoViewModel.errorMessage.observeAsState(null)

    // Fetch videos after login status is confirmed
    LaunchedEffect(Unit) {
        videoViewModel.fetchVideos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Explore") },
                actions = {
                    IconButton(onClick = { /* Handle search action */ }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) { // Ensure content avoids overlap with AppBar
            when {
                isLoading.value -> {
                    // Center the CircularProgressIndicator
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                !errorMessage.value.isNullOrEmpty() -> {
                    Text(
                        text = "Error: ${errorMessage.value}",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                    ) {
                        VideoTutorials(navController, modifier, videoViewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun VideoTutorials(
    navController: NavController,
    modifier: Modifier = Modifier,
    videoViewModel: VideoViewModel,
) {
    val videoList = videoViewModel.videoList.observeAsState(emptyList())
    val isLoading = videoViewModel.isLoading.observeAsState(false) // Observing loading state
    val showDialog = remember { mutableStateOf(false) }

    val favoriteVideoList = videoViewModel.favoriteVideoList.observeAsState(emptyList())

    // Show dialog only when loading is complete and the video list is empty
    LaunchedEffect(isLoading.value, videoList.value) {
        if (!isLoading.value && videoList.value.isEmpty()) {
            showDialog.value = true
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(text = "Database Empty")
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "No videos found. Post a video to get started!")
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning Icon",
                        modifier = modifier.size(50.dp) // Adjust size as needed
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    navController.navigate("post") // Navigate to the Post Screen
                }) {
                    Text(text = "Go to Post Screen")
                }
            }
        )
    }



    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videoList.value) { video ->
            val isFavorite = favoriteVideoList.value.any{ it.id == video.id } == true
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
                    totalDislikes = video.total_dislikes,
                    videoUrl = video.video_file,
                    videoId = video.id,
                    thumbnailUrl = video.thamnail,
                    isFavorite = isFavorite
                )
            }
        }
    }
}
