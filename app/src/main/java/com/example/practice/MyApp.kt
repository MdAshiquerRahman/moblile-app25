package com.example.practice

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice.navitem.BottomNavigationItem
import com.example.practice.screen.FavoriteScreen
import com.example.practice.screen.HomeScreen
import com.example.practice.screen.PostScreen
import com.example.practice.screen.ProfileScreen
import com.example.practice.screen.TutorialScreen
import com.example.practice.viewmodel.AuthViewModel
import com.example.practice.viewmodel.VideoViewModel

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(20.dp),
            painter = painterResource(id = R.drawable.drawer),
            contentDescription = "Drawer"
        )
        Text(text = "Explore")
        Text(text = "Followers") // New text item
        Icon(
            modifier = Modifier.padding(end = 10.dp).size(20.dp),
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search",
            tint = Color.Unspecified
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    videoViewModel: VideoViewModel,
    context: Context
) {
    val navItemList = listOf(
        BottomNavigationItem(title = "home", icon = R.drawable.home),
        BottomNavigationItem(title = "favorite", icon = R.drawable.favorite),
        BottomNavigationItem(title = "post", icon = R.drawable.post),
        BottomNavigationItem(title = "profile", icon = R.drawable.profile)
    )
    var selectedIndex by remember { mutableIntStateOf(0) }

    val navController = rememberNavController()



    Scaffold(
        bottomBar = {
            NavigationBar{
                navItemList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            if (selectedIndex != index) { // Prevent redundant navigation
                                selectedIndex = index
                                navController.navigate(item.title) // Navigate using route name
                            }
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(
                                        color = if (selectedIndex == index) Color(0xFFB1CB90)
                                        else Color(0xFFf9B77C), // Unselected color
                                        shape = CircleShape
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        if (selectedIndex != index) { // Ensure navigation triggers only once
                                            selectedIndex = index
                                            navController.navigate(item.title)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = "Icon",
                                    tint = Color.Unspecified,
                                )
                            }
                        },
                        interactionSource = remember { MutableInteractionSource() }, // Disables ripple effect
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent // Removes selection effect
                        )
                    )
                }
            }
        }
    ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = "home", // Set a valid initial route
            ) {
                composable("home") {
                    HomeScreen(
                        navController,
                        videoViewModel,
                        modifier.padding(innerPadding),
                    )
                }
                composable("favorite") {
                    FavoriteScreen(
                        navController,
                        modifier.padding(innerPadding),
                        viewModel,
                        context
                    )
                }
                composable("post") {
                    PostScreen(
                        modifier.padding(innerPadding),
                        navController = navController,
                    )
                }
                composable("tutorial/{title}/{description}/{author}/{totalLikes}/{totalDislikes}/{videoUrl}/{thumbnailUrl}/{videoId}") { backStackEntry ->

                    val title = backStackEntry.arguments?.getString("title")
                    val description = backStackEntry.arguments?.getString("description")
                    val author = backStackEntry.arguments?.getString("author")
                    val totalLikes = backStackEntry.arguments?.getString("totalLikes")?.toInt()
                    val totalDislikes = backStackEntry.arguments?.getString("totalDislikes")?.toInt()
                    val videoUrl = backStackEntry.arguments?.getString("videoUrl")
                    val recipeThumbnail = backStackEntry.arguments?.getString("thumbnailUrl")
                    val videoId = backStackEntry.arguments?.getString("videoId")

                    TutorialScreen(
                        navController,
                        modifier.padding(innerPadding),
                        videoViewModel = videoViewModel,
                        recipeTitle = title ?: "Default Title",
                        recipeDescription = description ?: "Default Description",
                        author = author ?: "Default Author",
                        totalLikes = totalLikes ?: 0,
                        totalDislikes = totalDislikes ?: 0,
                        recipeUrl = videoUrl ?: "Default URL",
                        recipeThumbnail = recipeThumbnail.toString(),
                        recipeId = videoId?.toIntOrNull() ?: 0,
                    )
                }
                composable("profile") {
                    ProfileScreen(modifier.padding(innerPadding),viewModel,context,navController)
                }
            }
    }
}


