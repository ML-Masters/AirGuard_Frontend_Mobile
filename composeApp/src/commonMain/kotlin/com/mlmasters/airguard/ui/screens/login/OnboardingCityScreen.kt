package com.mlmasters.airguard.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.data.model.Ville
import com.mlmasters.airguard.data.repository.AirGuardRepository
import com.mlmasters.airguard.ui.components.LoadingState
import com.mlmasters.airguard.ui.theme.Primary
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun OnboardingCityScreen(onComplete: () -> Unit) {
    val repository: AirGuardRepository = koinInject()
    var villes by remember { mutableStateOf<List<Ville>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var selectedVille by remember { mutableStateOf<Int?>(null) }
    var search by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val result = repository.getVilles()
        villes = result.getOrDefault(emptyList())
        loading = false
    }

    val filtered = remember(search, villes) {
        val q = search.lowercase()
        if (q.isBlank()) villes
        else villes.filter { it.nom.lowercase().contains(q) || it.regionNom.lowercase().contains(q) }
    }

    val selectedVilleName = remember(selectedVille, villes) {
        villes.find { it.id == selectedVille }?.let { "${it.nom} (${it.regionNom})" }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
    ) {
        Spacer(Modifier.height(48.dp))

        Icon(
            Icons.Default.LocationOn,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Où habitez-vous ?",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Text(
            "Choisissez votre ville pour recevoir les alertes et suivre la qualité de l'air chez vous.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 20.dp),
        )

        if (loading) {
            LoadingState()
        } else {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Rechercher une ville...") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(filtered, key = { it.id }) { ville ->
                    val isSelected = ville.id == selectedVille
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Primary.copy(alpha = 0.1f) else Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedVille = ville.id },
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedVille = ville.id },
                                colors = RadioButtonDefaults.colors(selectedColor = Primary),
                            )
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(ville.nom, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                                Text(
                                    ville.regionNom,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    saving = true
                    scope.launch {
                        // TODO: save city to user profile via API when endpoint is ready
                        saving = false
                        onComplete()
                    }
                },
                enabled = selectedVille != null && !saving,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                if (saving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Continuer", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }

            TextButton(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Passer cette étape", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
