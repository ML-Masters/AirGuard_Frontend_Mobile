package com.mlmasters.airguard.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF0F766E)
val PrimaryLight = Color(0xFF14B8A6)
val PrimaryDark = Color(0xFF134E4A)
val PrimaryBg = Color(0xFFF0FDFA)
val Surface = Color(0xFFFFFFFF)
val Border = Color(0xFFE2E8F0)
val TextPrimary = Color(0xFF1E293B)
val TextSecondary = Color(0xFF64748B)

val AqiBon = Color(0xFF22C55E)
val AqiModere = Color(0xFFEAB308)
val AqiSensible = Color(0xFFF97316)
val AqiMalsain = Color(0xFFEF4444)
val AqiTresMalsain = Color(0xFF7C3AED)
val AqiDangereux = Color(0xFF991B1B)

val SeveriteModere = Color(0xFFEAB308)
val SeveriteGrave = Color(0xFFF97316)
val SeveriteCritique = Color(0xFFEF4444)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryBg,
    onPrimaryContainer = PrimaryDark,
    secondary = PrimaryLight,
    onSecondary = Color.White,
    surface = Surface,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    background = Color(0xFFF8FAFC),
    onBackground = TextPrimary,
    outline = Border,
    error = Color(0xFFEF4444),
    onError = Color.White,
)

fun aqiColor(categorie: String): Color = when (categorie) {
    "Bon" -> AqiBon
    "Modere", "Modéré" -> AqiModere
    "Sensible" -> AqiSensible
    "Malsain" -> AqiMalsain
    "Tres_malsain", "Très malsain" -> AqiTresMalsain
    "Dangereux" -> AqiDangereux
    else -> TextSecondary
}

fun severiteColor(niveau: String): Color = when (niveau) {
    "modere" -> SeveriteModere
    "grave" -> SeveriteGrave
    "critique" -> SeveriteCritique
    else -> TextSecondary
}

@Composable
fun AirGuardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content,
    )
}
