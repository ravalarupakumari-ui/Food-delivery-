package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CraveonNonVegRed
import com.example.ui.theme.CraveonVegGreen

@Composable
fun VegBadge(
    isVeg: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp
) {
    val borderColor = if (isVeg) CraveonVegGreen else CraveonNonVegRed
    val dotColor = if (isVeg) CraveonVegGreen else CraveonNonVegRed

    Box(
        modifier = modifier
            .size(size)
            .border(width = 1.5.dp, color = borderColor, shape = RoundedCornerShape(3.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * 0.5f)
                .clip(CircleShape)
                .background(dotColor)
        )
    }
}
