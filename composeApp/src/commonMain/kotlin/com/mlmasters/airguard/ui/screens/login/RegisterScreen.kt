package com.mlmasters.airguard.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.ui.theme.Primary
import com.mlmasters.airguard.ui.theme.PrimaryDark
import com.mlmasters.airguard.ui.theme.PrimaryLight
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var selectedVille by remember { mutableStateOf<Int?>(null) }
    var showVillePicker by remember { mutableStateOf(false) }
    var villeSearch by remember { mutableStateOf("") }

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) onRegisterSuccess()
    }

    val selectedVilleName = remember(selectedVille, state.villes) {
        state.villes.find { it.id == selectedVille }?.let { "${it.nom} (${it.regionNom})" }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(PrimaryDark, Primary, PrimaryLight))),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            item {
                Text("AirGuard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                Text("Créer un compte", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                Spacer(Modifier.height(24.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Inscription", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(
                            "Surveillez la qualité de l'air dans votre ville",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                        )
                        Spacer(Modifier.height(16.dp))

                        if (state.error != null) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Text(
                                    text = state.error ?: "",
                                    color = Color(0xFFDC2626),
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(12.dp),
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                        }

                        // Name fields
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = { Text("Prénom") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                            )
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = { Text("Nom") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Adresse email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Mot de passe (6 caractères min.)") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Text(if (showPassword) "🙈" else "👁", fontSize = 18.sp)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(16.dp))

                        // City of residence
                        Text(
                            "📍 Ville de résidence",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                        )
                        Text(
                            "Recevez des alertes AQI pour votre ville",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                        )
                        Spacer(Modifier.height(6.dp))

                        OutlinedButton(
                            onClick = { showVillePicker = !showVillePicker },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                selectedVilleName ?: "Choisir votre ville",
                                fontSize = 13.sp,
                                color = if (selectedVille != null) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        if (showVillePicker && !state.villesLoading) {
                            Spacer(Modifier.height(4.dp))
                            OutlinedTextField(
                                value = villeSearch,
                                onValueChange = { villeSearch = it },
                                placeholder = { Text("Rechercher...", fontSize = 13.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp),
                            ) {
                                val filteredVilles = remember(villeSearch, state.villes) {
                                    val q = villeSearch.lowercase()
                                    if (q.isBlank()) state.villes
                                    else state.villes.filter {
                                        it.nom.lowercase().contains(q) || it.regionNom.lowercase().contains(q)
                                    }
                                }
                                LazyColumn(modifier = Modifier.padding(4.dp)) {
                                    items(filteredVilles, key = { it.id }) { ville ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedVille = ville.id
                                                    showVillePicker = false
                                                    villeSearch = ""
                                                }
                                                .background(
                                                    if (ville.id == selectedVille) MaterialTheme.colorScheme.primaryContainer
                                                    else Color.Transparent
                                                )
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Column {
                                                Text(ville.nom, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                                Text(
                                                    ville.regionNom,
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Button(
                            onClick = {
                                viewModel.register(
                                    email = email,
                                    password = password,
                                    firstName = firstName,
                                    lastName = lastName,
                                    selectedVilles = if (selectedVille != null) listOf(selectedVille!!) else emptyList(),
                                )
                            },
                            enabled = !state.isLoading && email.isNotBlank() && password.length >= 6,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            } else {
                                Text("S'inscrire", fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        TextButton(
                            onClick = onBackToLogin,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Déjà un compte ? Se connecter", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
