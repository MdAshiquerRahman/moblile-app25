package com.example.practice


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice.pages.ChatPage
import com.example.practice.pages.HomePage
import com.example.practice.pages.PostPage
import com.example.practice.pages.ProfilePage
import com.example.practice.pages.SettingsPage


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileScreen() {
    val navItemList = listOf(
        NavItem(icon = R.drawable.favorite, badgeCount = 0),
        NavItem(icon = R.drawable.home, badgeCount = 1),
        NavItem(icon = R.drawable.post, badgeCount = 2),
        NavItem(icon = R.drawable.chat, badgeCount = 3),
        NavItem(icon = R.drawable.profile, badgeCount = 4),
    )

    var selectedIndex by remember { mutableIntStateOf(4) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, // Space around all items
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.drawer),
                            contentDescription = "Drawer"
                        )
                        Text(text = "Explore")
                        Text(text = "Followers") // New text item
                        Icon(
                            modifier = Modifier.padding(end = 10.dp),
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            tint = Color.Unspecified
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFFf9B77C))
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFF3ECEC))
                    .padding(8.dp), // Add padding around the row
                horizontalArrangement = Arrangement.SpaceAround, // Distribute items evenly
                verticalAlignment = Alignment.CenterVertically,
            ) {
                navItemList.forEachIndexed { index, navItem ->
                    Box(
                        modifier = Modifier
                            .size(52.dp) // Set the size for each navigation item
                            .background(
                                color = if (selectedIndex == index) Color(0xFFB1CB90) // Selected color
                                else Color(0xFFf9B77C), // Unselected color
                                shape = CircleShape
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() }, // Disable ripple effect
                                indication = null // Ensure no ripple is shown
                            ) {
                                selectedIndex = index // Handle item selection
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            painter = painterResource(id = navItem.icon),
                            contentDescription = "Icon",
                            tint = Color.Unspecified
                        )

                    }
                }
            }
        }

    ) {

        ContentScreen(selectedIndex)
    }
}


@Composable
private fun ContentScreen(selectedIndex: Int) {
    when (selectedIndex) {
        0 -> SettingsPage()
        1 -> HomePage()
        2 -> PostPage()
        3 -> ChatPage()
        4 -> ProfilePage()
    }
}

