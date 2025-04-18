package com.example.practice.screen



import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import com.example.practice.elements.FixedButton
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.practice.R
import com.example.practice.elements.EditProfileDialog
import com.example.practice.elements.UserCommentsCard
import com.example.practice.viewmodel.AuthViewModel
import com.example.practice.viewmodel.CommentViewModel
import org.w3c.dom.Comment

@Composable
fun RecipeTitle(navController: NavController,title: String) {
    var selectedButton by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp


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
                    onClick = {
                        navController.popBackStack()
                    }
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
    var isFullScreen by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUrl)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    fun playerViewFactory(ctx: Context): PlayerView {
        return PlayerView(ctx).apply {
            player = exoPlayer
            useController = true
            setControllerShowTimeoutMs(0)
            showController()
            isFocusable = true
            isFocusableInTouchMode = true
            isClickable = true
            setShutterBackgroundColor(android.graphics.Color.BLACK)
            requestFocus()
        }
    }

    // Normal (non-fullscreen) view
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(screenHeight / 3)
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { ctx -> playerViewFactory(ctx) },
            modifier = Modifier
                .matchParentSize()
                .clickable { }
        )

        IconButton(
            onClick = { isFullScreen = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.4f), shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_full_screen),
                contentDescription = "Fullscreen",
                tint = Color.White
            )
        }
    }

    // Fullscreen dialog
    if (isFullScreen) {
        Dialog(onDismissRequest = { isFullScreen = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { ctx -> playerViewFactory(ctx) },
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { }
                )

                IconButton(
                    onClick = { isFullScreen = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.4f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Exit Fullscreen",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


@OptIn(UnstableApi::class)
@Composable
fun CommentSection(recipeId: Int) {


    val viewModel : CommentViewModel = viewModel()
    val isLoading = viewModel.isLoading.observeAsState(false)
    val errorMessage = viewModel.errorMessage.observeAsState(null)

    var selectedButton by remember { mutableStateOf(false) }


    // Fetch videos after login status is confirmed
    LaunchedEffect(Unit) {
        viewModel.fetchComments()
    }
    if (isLoading.value) {
        CircularProgressIndicator()
    } else if (!errorMessage.value.isNullOrEmpty()) {
        Text(text = "Error: ${errorMessage.value}")
    } else {
        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .background(color = Color(0xFFEFE7DC))
            ) {
                FixedButton(
                    text = "Comments",
                    isSelected = true,
                    onClick = {
                        selectedButton
                    },
                    modifier = Modifier.wrapContentWidth()
                )
            }
            val comments = viewModel.commentList.observeAsState(emptyList())

            LazyColumn {
                items(comments.value){ comment ->
                    if (recipeId == comment.video) {
                        UserCommentsCard(
                            userName = comment.user,
                            comment = comment.text,
                            profileUrl = R.drawable.profile.toString()
                        )
                    }
                }
            }
        }
    }


}


@OptIn(UnstableApi::class)
@Composable
fun TutorialScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    recipeTitle: String,
    recipeUrl: String,
    recipeId: Int,
) {
    Log.e("VideoId", recipeId.toString())

//    val viewModel: RecipeViewModel = viewModel()
//    val recipes by viewModel.recipe.observeAsState()
//
//    val videoTitle = recipes?: "Null"
//    val videoUrl = recipes?:"Null"
//    val videoShoots = recipes?: "Null"
//    val comment = Comment(
//        //profileUrl = R.drawable.profile,
//        userName = "User A",
//        comment = "Hi, how are you?"
//    )


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        RecipeTitle(navController,recipeTitle)
        RecipeTutorial(recipeUrl)
//        RecipeShoots(videoShoots.toString())
        CommentSection(recipeId)
    }

}
