package com.mlmasters.airguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.ui.theme.severiteColor

@Composable
fun SeverityBadge(niveau: String, modifier: Modifier = Modifier) {
    val label = when (niveau) {
        "modere" -> "Modéré"
        "grave" -> "Grave"
        "critique" -> "Critique"
        else -> niveau
    }
    Text(
        text = label.uppercase(),
        color = Color.White,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(severiteColor(niveau))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    )
}
