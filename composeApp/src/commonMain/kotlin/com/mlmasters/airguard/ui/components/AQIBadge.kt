package com.mlmasters.airguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.ui.i18n.S
import com.mlmasters.airguard.ui.theme.aqiColor

data class AirQualityInfo(
    val icon: ImageVector,
    val label: String,
    val description: String,
    val conseil: String,
    val color: Color,
)

fun airQualityInfo(categorie: String): AirQualityInfo {
    val icon = when (categorie) {
        "Bon" -> Icons.Default.CheckCircle
        "Modere", "Modéré" -> Icons.Default.Info
        "Sensible" -> Icons.Default.Warning
        "Malsain" -> Icons.Default.Error
        "Tres_malsain", "Très malsain" -> Icons.Default.Warning
        "Dangereux" -> Icons.Default.Warning
        else -> Icons.Default.Info
    }
    return AirQualityInfo(
        icon = icon,
        label = S.aqiLabel(categorie),
        description = S.aqiDescription(categorie),
        conseil = S.aqiAdvice(categorie),
        color = aqiColor(categorie),
    )
}

@Composable
fun AQIBadge(categorie: String, indiceAqi: Int, modifier: Modifier = Modifier) {
    val info = airQualityInfo(categorie)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(info.color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(info.icon, contentDescription = null, tint = info.color, modifier = Modifier.size(14.dp))
        Text(
            text = info.label,
            color = info.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun AQIBadgeSmall(categorie: String, indiceAqi: Int, modifier: Modifier = Modifier) {
    val info = airQualityInfo(categorie)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Icon(info.icon, contentDescription = null, tint = info.color, modifier = Modifier.size(12.dp))
        Text(
            text = info.label,
            color = info.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
