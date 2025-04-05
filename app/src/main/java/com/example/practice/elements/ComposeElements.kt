package com.example.practice.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R

@Composable
fun UserProfile(boxDynamicHeight: Dp, boxDynamicWidth: Dp) {
    Box(
        modifier = Modifier
            .height(boxDynamicHeight)
            .width(boxDynamicWidth)
            .background(color = Color(0xFFAFC888), shape = CircleShape)
    ) {
        Icon(
            modifier = Modifier
                .height(boxDynamicHeight - 32.dp)
                .width(boxDynamicWidth - 32.dp)
                .align(Alignment.Center),
            painter = painterResource(R.drawable.profile),
            contentDescription = "Profile",
        )
    }
}



@Composable
fun FixedButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            if (isSelected) Color(0xFFAFC988) else Color(0xFFF7B474)
        ),
        enabled = true, // Buttons are always enabled, selection is controlled via state
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.source_code_pro_regular)),
                fontWeight = FontWeight(400),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        )
    }
}