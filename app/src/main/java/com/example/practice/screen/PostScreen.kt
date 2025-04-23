package com.example.practice.screen

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.practice.R
import com.example.practice.viewmodel.VideoViewModel


@Composable
fun PostScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: VideoViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedThumbnailUri by remember { mutableStateOf<Uri?>(null) }


    val context = LocalContext.current
    val token = viewModel.authViewModel.getToken(context)

    // Launcher for selecting video
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedVideoUri = uri
    }

    // Launcher for selecting thumbnail
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedThumbnailUri = uri
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Create a Post",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TitleField(
            context = LocalContext.current,
            description = title,
            onValueChange = { title = it }
        )

        DescriptionField(
            context = LocalContext.current,
            description = description,
            onValueChange = { description = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon for Video Picker
            IconButton(
                onClick = { videoPickerLauncher.launch("video/*") },
                modifier = Modifier.size(64.dp) // Set size for the icon
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_movie), // Use a relevant built-in icon
                    contentDescription = "Select Video",
                    tint = Color.Blue, // Set icon color
                    modifier = Modifier.size(48.dp) // Set size for the icon inside IconButton
                )
            }

            // Icon for Thumbnail Picker
            IconButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_image), // Use a relevant built-in icon
                    contentDescription = "Select Thumbnail",
                    tint = Color.Green, // Set icon color
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        if (selectedVideoUri != null) {
            Text(
                text = "Selected Video: ${selectedVideoUri?.lastPathSegment}",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        if (selectedThumbnailUri != null) {
            Text(
                text = "Selected Thumbnail: ${selectedThumbnailUri?.lastPathSegment}",
                style = TextStyle(fontSize = 14.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isNotEmpty() && description.isNotEmpty() &&
                    selectedVideoUri != null && selectedThumbnailUri != null) {
                    viewModel.postVideos(
                        context = context,
                        token = token.toString(),
                        title = title,
                        description = description,
                        video_file = selectedVideoUri,
                        thamnail = selectedThumbnailUri
                    )
                    navController.navigate("home")
                } else {
                    Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Post", fontSize = 18.sp)
        }
    }
}

@Composable
fun DescriptionField(context: Context, description: String, onValueChange: (String) -> Unit) {
    var isError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = description,
        onValueChange = {
            if (it.length <= 200) {
                onValueChange(it)
                isError = it.lines().size > 3 // Check if the text exceeds 3 lines
            } else {
                Toast.makeText(context, "Description cannot exceed 300 characters", Toast.LENGTH_SHORT).show()
                isError = true
            }
        },
        maxLines = 3,
        label = { Text("Description") },
        modifier = Modifier.fillMaxWidth(),
        isError = isError, // Apply error state
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = if (isError) Color.Red else Color.Blue, // Change underline color
            unfocusedIndicatorColor = if (isError) Color.Red else Color.Gray, // Change underline color
        )
    )
}

@Composable
fun TitleField(context: Context, description: String, onValueChange: (String) -> Unit) {
    var isError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = description,
        onValueChange = {
            if (it.length <= 25) {
                onValueChange(it)
                isError = it.lines().size > 1 // Check if the text exceeds 3 lines
            } else {
                Toast.makeText(context, "Title cannot exceed 25 characters", Toast.LENGTH_SHORT).show()
                isError = true
            }
        },
        maxLines = 3,
        label = { Text("Title") },
        modifier = Modifier.fillMaxWidth(),
        isError = isError, // Apply error state
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = if (isError) Color.Red else Color.Blue, // Change underline color
            unfocusedIndicatorColor = if (isError) Color.Red else Color.Gray, // Change underline color
        )
    )
}


