package com.mlmasters.airguard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.ui.theme.aqiColor

data class AirQualityInfo(
    val emoji: String,
    val label: String,
    val description: String,
    val conseil: String,
    val color: Color,
)

fun airQualityInfo(categorie: String): AirQualityInfo = when (categorie) {
    "Bon" -> AirQualityInfo(
        emoji = "\uD83D\uDE0A",
        label = "Air pur",
        description = "La qualit\u00e9 de l'air est excellente",
        conseil = "Profitez de vos activit\u00e9s en plein air !",
        color = aqiColor(categorie),
    )
    "Modere", "Mod\u00e9r\u00e9" -> AirQualityInfo(
        emoji = "\uD83D\uDE42",
        label = "Air acceptable",
        description = "La qualit\u00e9 de l'air est correcte",
        conseil = "Les personnes sensibles doivent rester vigilantes.",
        color = aqiColor(categorie),
    )
    "Sensible" -> AirQualityInfo(
        emoji = "\uD83D\uDE10",
        label = "Air d\u00e9grad\u00e9",
        description = "Peut affecter les personnes sensibles",
        conseil = "Limitez les efforts physiques prolong\u00e9s en ext\u00e9rieur. Enfants et personnes \u00e2g\u00e9es : restez prudents.",
        color = aqiColor(categorie),
    )
    "Malsain" -> AirQualityInfo(
        emoji = "\uD83D\uDE37",
        label = "Air malsain",
        description = "Risque pour la sant\u00e9 de tous",
        conseil = "\u00c9vitez les activit\u00e9s en ext\u00e9rieur. Fermez les fen\u00eatres. Portez un masque si vous sortez.",
        color = aqiColor(categorie),
    )
    "Tres_malsain", "Tr\u00e8s malsain" -> AirQualityInfo(
        emoji = "\uD83E\uDD22",
        label = "Air tr\u00e8s malsain",
        description = "Danger pour la sant\u00e9",
        conseil = "Restez \u00e0 l'int\u00e9rieur. Ne faites aucun effort physique dehors. Prot\u00e9gez les enfants et personnes \u00e2g\u00e9es.",
        color = aqiColor(categorie),
    )
    "Dangereux" -> AirQualityInfo(
        emoji = "\uD83D\uDEA8",
        label = "Air dangereux",
        description = "Urgence sanitaire",
        conseil = "NE SORTEZ PAS. Fermez portes et fen\u00eatres. Appelez le 119 en cas de difficult\u00e9 respiratoire.",
        color = aqiColor(categorie),
    )
    else -> AirQualityInfo(
        emoji = "\u2753",
        label = categorie,
        description = "",
        conseil = "",
        color = Color.Gray,
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
        Text(info.emoji, fontSize = 14.sp)
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
    Text(
        text = "${info.emoji} ${info.label}",
        color = info.color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier,
    )
}
