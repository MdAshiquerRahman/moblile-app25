package com.example.practice.screen


import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.practice.viewmodel.AuthViewModel
import com.example.practice.viewmodel.VideoViewModel


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
//    val videoViewModel: VideoViewModel = viewModel()
//    val videoList = videoViewModel.videoList.observeAsState(emptyList())
//    val isLoading = videoViewModel.isLoading.observeAsState(false)
//    val errorMessage = videoViewModel.errorMessage.observeAsState(null)
//    val context = LocalContext.current
//
//    // Fetch videos after login status is confirmed
//    LaunchedEffect(Unit) {
//        videoViewModel.fetchVideos(context)
//    }
//    if (isLoading.value) {
//        CircularProgressIndicator()
//    } else if (!errorMessage.value.isNullOrEmpty()) {
//        Text(text = "Error: ${errorMessage.value}")
//    } else {
//        LazyColumn {
//            items(videoList.value) { video ->
//                Text(text = "Title: ${video.title}")
//                Text(text = "Description: ${video.description}")
//            }
//        }
//    }

    Text(text = "Hello from HomeScreen")


}