package com.mlmasters.airguard.ui.screens.cities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.data.model.WeekPrediction
import com.mlmasters.airguard.ui.components.*
import com.mlmasters.airguard.ui.components.airQualityInfo
import com.mlmasters.airguard.ui.i18n.S
import com.mlmasters.airguard.ui.theme.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    villeNom: String,
    villeId: Int,
    onBack: () -> Unit,
    viewModel: CityDetailViewModel = koinViewModel(),
) {
    LaunchedEffect(villeNom, villeId) { viewModel.load(villeNom, villeId) }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(villeNom) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingState(modifier = Modifier.padding(padding))
            state.error != null -> ErrorState(state.error ?: "", onRetry = { viewModel.load(villeNom, villeId) }, modifier = Modifier.padding(padding))
            else -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Current AQI
                    val aq = state.latestAqi
                    if (aq != null) {
                        val info = airQualityInfo(aq.categorie)
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = info.color.copy(alpha = 0.08f)),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Icon(info.icon, contentDescription = null, tint = info.color, modifier = Modifier.size(56.dp))
                                Spacer(Modifier.height(8.dp))
                                Text(info.label, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = info.color)
                                Text(info.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                Spacer(Modifier.height(12.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Info, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(8.dp))
                                        Text(info.conseil, fontSize = 13.sp, color = Color(0xFF1E293B))
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Indice : ${aq.indiceAqi} \u00b7 PM2.5 : ${aq.valeurPm25} \u00b5g/m\u00b3",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }

                    // Predictions
                    val pred = state.prediction?.predictions
                    if (pred != null) {
                        Text(S.tomorrowPredictions, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        // Air quality prediction
                        PredictionCard(
                            title = S.airQualityPred,
                            icon = Icons.Default.FavoriteBorder,
                            color = aqiColor(pred.qualiteAir.categorie),
                            items = listOf(
                                S.pollutionForecast to "${pred.qualiteAir.pm25} \u00b5g/m\u00b3",
                                S.estimatedAqi to "${pred.qualiteAir.aqiEstime}",
                                S.category to pred.qualiteAir.categorie,
                            ),
                        )

                        // Heat prediction
                        PredictionCard(
                            title = S.heatHealth,
                            icon = Icons.Default.Warning,
                            color = if (pred.chaleurSante.avertissement == "Danger") AqiMalsain else Primary,
                            items = listOf(
                                S.feelsLike to "${pred.chaleurSante.heatIndex}\u00b0C",
                                S.heatRisk to "${pred.chaleurSante.chaleurExtreme}/10",
                                S.warning to pred.chaleurSante.avertissement,
                            ),
                        )

                        // Flood risk prediction
                        PredictionCard(
                            title = S.naturalRisks,
                            icon = Icons.Default.Info,
                            color = if (pred.risquesNaturels.risqueInondation > 6) AqiMalsain else Primary,
                            items = listOf(
                                S.drought to "${pred.risquesNaturels.stressHydrique}",
                                S.floodRisk to "${pred.risquesNaturels.risqueInondation}/10",
                                S.category to pred.risquesNaturels.categorieInondation,
                            ),
                        )
                    }

                    // Week predictions
                    val weekPred = state.weekPrediction
                    if (weekPred != null && weekPred.jours.isNotEmpty()) {
                        Text(S.weekForecast, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1E293B))
                        Text(weekPred.resume, fontSize = 13.sp, color = Color(0xFF64748B))
                        Spacer(Modifier.height(4.dp))

                        weekPred.jours.forEach { jour ->
                            val jourInfo = airQualityInfo(jour.categorie)
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    // Color bar
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(40.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(jourInfo.color)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(jour.jour, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color(0xFF1E293B))
                                        Text(jour.label, fontSize = 12.sp, color = jourInfo.color, fontWeight = FontWeight.Medium)
                                    }
                                    Text(jour.date, fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PredictionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    items: List<Pair<String, String>>,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF1E293B))
            }
            Spacer(Modifier.height(12.dp))
            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(label, color = Color(0xFF64748B), fontSize = 13.sp)
                    Text(value, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = color)
                }
            }
        }
    }
}
