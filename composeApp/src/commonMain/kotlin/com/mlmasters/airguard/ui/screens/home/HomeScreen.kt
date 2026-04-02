package com.mlmasters.airguard.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.data.model.AirQuality
import com.mlmasters.airguard.data.model.Ville
import com.mlmasters.airguard.ui.components.*
import com.mlmasters.airguard.ui.i18n.S
import com.mlmasters.airguard.ui.theme.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> LoadingState()
        state.error != null -> ErrorState(state.error ?: "", onRetry = { viewModel.loadData() })
        else -> HomeContent(state, viewModel)
    }
}

@Composable
private fun HomeContent(state: HomeState, viewModel: HomeViewModel) {
    val villeMap = remember(state.villes) { state.villes.associateBy { it.id } }
    val latestByCity = remember(state.airQuality) {
        val map = mutableMapOf<Int, AirQuality>()
        for (aq in state.airQuality) {
            val existing = map[aq.ville]
            if (existing == null || aq.dateCible > existing.dateCible) {
                map[aq.ville] = aq
            }
        }
        map
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        S.greeting(state.userName),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                    Text(
                        S.airToday,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                    )
                }
                IconButton(onClick = { viewModel.loadData() }) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
        }

        // National summary card
        item {
            val avgInfo = airQualityInfo(
                when {
                    state.avgAqi <= 50 -> "Bon"
                    state.avgAqi <= 100 -> "Modere"
                    state.avgAqi <= 150 -> "Sensible"
                    state.avgAqi <= 200 -> "Malsain"
                    state.avgAqi <= 300 -> "Tres_malsain"
                    else -> "Dangereux"
                }
            )
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = avgInfo.color.copy(alpha = 0.08f)),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(avgInfo.icon, contentDescription = null, tint = avgInfo.color, modifier = Modifier.size(56.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        avgInfo.label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = avgInfo.color,
                    )
                    Text(
                        avgInfo.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                    ) {
                        Text(
                            avgInfo.conseil,
                            fontSize = 13.sp,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        S.nationalAvg(state.avgAqi, state.villes.size),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // Quick stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                KPICard(
                    title = S.dangerousAir,
                    value = "${state.criticalCities}",
                    accentColor = AqiMalsain,
                    icon = { Icon(Icons.Default.Warning, null, tint = AqiMalsain, modifier = Modifier.size(20.dp)) },
                    subtitle = S.citiesAtRisk,
                    modifier = Modifier.weight(1f),
                )
                KPICard(
                    title = S.alerts,
                    value = "${state.alerts.size}",
                    accentColor = Color(0xFFF59E0B),
                    icon = { Icon(Icons.Default.Notifications, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(20.dp)) },
                    subtitle = S.ongoing,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Tomorrow prediction for top city
        item {
            val topCity = latestByCity.entries.maxByOrNull { it.value.indiceAqi }
            val topVille = topCity?.let { villeMap[it.key] }
            if (topVille != null && topCity != null) {
                val info = airQualityInfo(topCity.value.categorie)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = info.color.copy(alpha = 0.08f)),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = info.color, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(S.tomorrowForecast, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1E293B))
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "${topVille.nom} : ${info.label}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = info.color,
                        )
                        Text(info.conseil, fontSize = 13.sp, color = Color(0xFF64748B))
                    }
                }
            }
        }

        // Active alerts
        if (state.alerts.isNotEmpty()) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = AqiMalsain, modifier = Modifier.size(18.dp))
                    Text(
                        S.activeAlerts,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                }
            }
            items(state.alerts.take(3)) { alert ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(alert.villeNom, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color(0xFF1E293B))
                                SeverityBadge(alert.niveauSeverite)
                            }
                            Text(
                                alert.messageFr.take(120),
                                color = Color(0xFF64748B),
                                fontSize = 12.sp,
                                maxLines = 2,
                            )
                        }
                    }
                }
            }
        }

    }
}
