package com.example.practice

import androidx.compose.ui.graphics.Color

data class NavItem(
    val icon: Int,
    val badgeCount: Int? = null,
)

data class TopBarItem(
    val drawer : Int,
    val search : Int
)
