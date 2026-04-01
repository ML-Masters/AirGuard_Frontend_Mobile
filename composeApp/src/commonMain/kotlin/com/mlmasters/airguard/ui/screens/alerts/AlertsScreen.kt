package com.mlmasters.airguard.ui.screens.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.ui.components.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AlertsScreen(viewModel: AlertsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> LoadingState()
        state.error != null -> ErrorState(state.error ?: "", onRetry = { viewModel.loadAlerts() })
        state.alerts.isEmpty() -> EmptyState("Aucune alerte active")
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text("Alertes actives", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(
                                "${state.alerts.size} alerte${if (state.alerts.size > 1) "s" else ""}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                            )
                        }
                        TextButton(onClick = { viewModel.loadAlerts() }) {
                            Text("↻", fontSize = 16.sp)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }

                items(state.alerts, key = { it.id }) { alert ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(alert.villeNom, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                SeverityBadge(alert.niveauSeverite)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                alert.villeRegion,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(alert.messageFr, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)

                            if (alert.recommandationsResidentsFr.isNotBlank()) {
                                Spacer(Modifier.height(10.dp))
                                Text("Recommandations", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                                Text(
                                    alert.recommandationsResidentsFr,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }

                            if (alert.dureeEstimee.isNotBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Durée estimée : ${alert.dureeEstimee}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
