package com.example.practice.screen


import android.net.Uri
import android.view.View
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.Log
import com.example.practice.elements.FixedButton
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.PlayerView
import com.example.practice.viewmodel.Recipe
import com.example.practice.viewmodel.RecipeViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RecipeTitle(title: String) {
    var selectedButton by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .background(color = Color(0xFFEFE7DC)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable(
                    onClick = { }
                )
        )
        Spacer(modifier = Modifier.padding(start = screenWidth / 4))
        FixedButton(
            text = title,
            isSelected = true,
            onClick = { selectedButton },
            modifier = Modifier.wrapContentWidth()
        )
    }

}

@OptIn(UnstableApi::class)
@Composable
fun RecipeTutorial(videoUrl: String) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    val viewModel: RecipeViewModel = viewModel()
    val videoUrlState = remember { mutableStateOf<String?>(null) }

    // Fetch video URL from Firestore
    LaunchedEffect(Unit) {
        viewModel.fetchRecipe("Recipe")
    }

    // Prepare ExoPlayer when videoUrl is fetched
    LaunchedEffect(videoUrlState.value) {
        videoUrlState.value?.let { videoUrl ->
            val mediaSource = MediaItem.fromUri(videoUrl)
            exoPlayer.setMediaItem(mediaSource)
            exoPlayer.prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = true

                // Enable fast-forward and rewind buttons
                setShowRewindButton(true)
                setShowFastForwardButton(true)
            }
        },
        modifier = Modifier.fillMaxWidth()
            .height(screenHeight / 3)
    )
}



@Composable
fun TutorialScreen(innerPaddingValues: PaddingValues,recipes: List<Recipe>) {
    Column(
        modifier = Modifier
            .padding(innerPaddingValues)
    ) {
        recipes.forEach { recipe ->
            RecipeTitle(title = recipe.title)
            RecipeTutorial(videoUrl = recipe.videoUrl)
        }
    }

}


//@Preview
//@Composable
//private fun TutorialScreenPreview() {
//    TutorialScreen()
//}