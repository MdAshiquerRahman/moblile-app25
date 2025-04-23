package com.example.practice.screen



import android.content.Context
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
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
import com.example.practice.viewmodel.VideoViewModel
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
        horizontalArrangement = Arrangement.Center
    ) {
//        Icon(
//            imageVector = Icons.Default.ArrowBack,
//            contentDescription = null,
//            modifier = Modifier
//                .clickable(
//                    onClick = {
//                        navController.popBackStack()
//                    }
//                )
//        )
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
fun CommentSection(
    videoViewModel: VideoViewModel,
    title: String,
    description: String,
    video_file: String,
    thamnail: String,
    totalLikes: Int,
    totalDislikes: Int,
    recipeId: Int
) {


    val viewModel : CommentViewModel = viewModel()

    val isLoading = viewModel.isLoading.observeAsState(false)
    val errorMessage = viewModel.errorMessage.observeAsState(null)

    var selectedButton by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val token = viewModel.authViewModel.getToken(context)

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
                    .background(color = Color(0xFFEFE7DC)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FixedButton(
                    text = "Comments",
                    isSelected = true,
                    onClick = {
                        selectedButton
                        showDialog = true
                    },
                    modifier = Modifier.wrapContentWidth()
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(R.drawable.heart_reatc),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            onClick = {

                            }
                        )
                )
                Spacer(modifier = Modifier.width(40.dp))
                LikeDislikeButtons(
                    videoViewModel = videoViewModel,
                    videoId = recipeId,
                    title = title,
                    description = description,
                    video_file = video_file,
                    thamnail = thamnail,
                    initialLikes = totalLikes,
                    initialDislikes = totalDislikes
                )
                if (showDialog) {
                    CommentDialog(
                        onDismissRequest = { showDialog = false },
                        onConfirmRequest = { comment ->
                            showDialog = false
                            // Perform the postComment action here
                            viewModel.postComment(
                                token = token.toString(),
                                videoId = recipeId,
                                text = comment // Use the input from the dialog
                            )
                        }
                    )
                }

            }
            val comments = viewModel.commentList.observeAsState(emptyList())

            LazyColumn {
                items(comments.value){ comment ->
                    if (recipeId == comment.video) {
                        UserCommentsCard(
                            userName = comment.user,
                            comment = comment.text,
                            profileUrl = ""
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LikeDislikeButtons(
    videoViewModel: VideoViewModel,
    title: String,
    description: String,
    video_file: String,
    thamnail: String,
    videoId: Int,
    initialLikes: Int,
    initialDislikes: Int
) {
    // States to track likes, dislikes, and current user selection
    var totalLikes by remember { mutableIntStateOf(initialLikes) }
    var totalDislikes by remember { mutableIntStateOf(initialDislikes) }
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val token = videoViewModel.authViewModel.getToken(context)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(16.dp)
    ) {
        // Like Button
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_like),
                contentDescription = "Like Icon",
                tint = if (isLiked) Color.Blue else Color.Gray, // Dynamic color based on state
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = {
//                            if (!isLiked) {
//                                totalLikes++
//                                if (isDisliked) {
//                                    totalDislikes--
//                                    isDisliked = false
//                                }
//                                isLiked = true
//                            } else {
//                                totalLikes--
//                                isLiked = false
//                            }
                            videoViewModel.updateLike(
                                videoId = videoId,
                                token = token.toString(),
                                onSuccess = {
                                    // Update UI or refresh video list
                                    Toast.makeText(context, "Liked successfully!", Toast.LENGTH_SHORT).show()
                                }
                            )

                        }
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = totalLikes.toString(),
                style = TextStyle(fontSize = 14.sp, color = Color.Black)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Dislike Button
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_dislike),
                contentDescription = "Dislike Icon",
                tint = if (isDisliked) Color.Red else Color.Gray, // Dynamic color based on state
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = {
//                            if (!isDisliked) {
//                                totalDislikes++
//                                if (isLiked) {
//                                    totalLikes--
//                                    isLiked = false
//                                }
//                                isDisliked = true
//                            } else {
//                                totalDislikes--
//                                isDisliked = false
//                            }

                            videoViewModel.updateDislike(
                                videoId = videoId,
                                token = token.toString(),
                                onSuccess = {
                                    // Update UI or refresh video list
                                    Toast.makeText(context, "Disliked successfully!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = totalDislikes.toString(),
                style = TextStyle(fontSize = 14.sp, color = Color.Black)
            )
        }
    }
}






@Composable
fun RecipeDescription(author: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(
                color = Color(0xFFEFD1BF)
            )
    ) {
        DescriptionTextWithDialog(author,description)
    }
}

@Composable
fun DescriptionTextWithDialog(author: String, description: String) {
    var isDialogOpen by remember { mutableStateOf(false) } // State for dialog visibility

    Box(modifier = Modifier.fillMaxWidth()) {
        // Text content
        Text(
            text = " Author: $author \n Description: $description",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis, // Ellipsis for overflow
            modifier = Modifier.clickable { isDialogOpen = true } // Open dialog on click
        )
    }

    // Dialog to show full description
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            title = { Text("Full Description") },
            text = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(text = description) // Show full description in scrollable view
                    }
                }
            },
            confirmButton = {
                Button(onClick = { isDialogOpen = false }) {
                    Text("Close")
                }
            }
        )
    }
}



@Composable
fun CommentDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Add Comment")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text(text = "Comment") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirmRequest(commentText) }) {
                Text(text = "Submit")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        }
    )
}



@OptIn(UnstableApi::class)
@Composable
fun TutorialScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    videoViewModel: VideoViewModel,
    recipeTitle: String,
    recipeDescription: String,
    author: String,
    totalLikes: Int,
    totalDislikes: Int,
    recipeUrl: String,
    recipeThumbnail: String,
    recipeId: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        RecipeTitle(navController,recipeTitle)
        RecipeTutorial(recipeUrl)
        RecipeDescription(author,recipeDescription)
        CommentSection(
            videoViewModel,
            recipeTitle,
            recipeDescription,
            recipeUrl,
            recipeThumbnail,
            totalLikes,
            totalDislikes,
            recipeId
        )
    }

}
