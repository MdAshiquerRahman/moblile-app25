package com.example.practice.pages.post

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.practice.R
import com.example.practice.screen.VideoTutorials
import com.example.practice.viewmodel.VideoViewModel


@Composable
fun RecipePostsCard(
    navController: NavController,
    title: String,
    description: String,
    author: String,
    totalLikes: Int,
    totalDislikes: Int,
    videoUrl: String,
    videoId: Int,
    thumbnailUrl: String?,
    isFavorite: Boolean
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .background(color = Color.White)
            .clickable(
                onClick = {
                    // Navigate to the tutorial screen
                    navController.navigate("tutorial/$title/$description/$author/$totalLikes/$totalDislikes/${Uri.encode(videoUrl)}/${Uri.encode(thumbnailUrl)}/$videoId")
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile_Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // title
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 28.sp,
                fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                fontWeight = FontWeight(700),
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFEFE7DC))
        ) {
            // description
            Text(
                text = description,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // author
            Text(
                text = author,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center,
                )
            )
            Row{
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(R.drawable.heart_reatc),
                    contentDescription = null,
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
                Spacer(modifier = Modifier.width(10.dp))

                // total likes
                Text(
                    text = "$totalLikes",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF212121),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "$totalDislikes",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF212121),
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}