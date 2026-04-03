package com.mlmasters.airguard.ui.screens.cities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.ui.components.*
import com.mlmasters.airguard.ui.components.airQualityInfo
import com.mlmasters.airguard.ui.i18n.S
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CitiesScreen(
    onCityClick: (String, Int) -> Unit,
    viewModel: CitiesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> LoadingState()
        state.error != null -> ErrorState(state.error ?: "", onRetry = { viewModel.loadData() })
        else -> {
            val filtered = remember(state.villes, state.searchQuery, state.filterCategorie) { viewModel.filteredVilles() }
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(S.cities, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(
                                "${state.villes.size} ${S.monitoredCities}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp,
                            )
                        }
                        IconButton(onClick = { viewModel.loadData() }) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.updateSearch(it) },
                        placeholder = { Text(S.searchCity) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))

                    // AQI filter chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        val filters = listOf(
                            "all" to S.all,
                            "Bon" to S.good,
                            "Modere" to S.acceptable,
                            "Sensible" to S.degraded,
                            "Malsain" to S.unhealthy,
                        )
                        filters.forEach { (code, label) ->
                            FilterChip(
                                selected = state.filterCategorie == code,
                                onClick = { viewModel.updateFilter(code) },
                                label = { Text(label, fontSize = 11.sp) },
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }

                items(filtered, key = { it.id }) { ville ->
                    val aq = state.latestAqi[ville.id]
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth().clickable {
                            onCityClick(ville.nom, ville.id)
                        },
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (aq != null) {
                                val info = airQualityInfo(aq.categorie)
                                Icon(info.icon, contentDescription = null, tint = info.color, modifier = Modifier.size(28.dp))
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(ville.nom, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color(0xFF1E293B))
                                    Text(
                                        info.label,
                                        color = info.color,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Text(ville.regionNom, color = Color(0xFF64748B), fontSize = 11.sp)
                                }
                                Text(
                                    "${aq.indiceAqi}",
                                    color = Color(0xFF64748B),
                                    fontSize = 11.sp,
                                )
                            } else {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(ville.nom, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color(0xFF1E293B))
                                    Text(ville.regionNom, color = Color(0xFF64748B), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
