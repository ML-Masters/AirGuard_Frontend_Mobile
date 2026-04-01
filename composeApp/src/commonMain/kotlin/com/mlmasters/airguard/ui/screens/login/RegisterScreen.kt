package com.mlmasters.airguard.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlmasters.airguard.ui.theme.Primary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    onGoogleSignIn: () -> Unit = {},
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
        state.villes.find { it.id == selectedVille }?.nom
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(60.dp))

        // Brand
        Text("AirGuard", color = Primary, fontWeight = FontWeight.Bold, fontSize = 32.sp)
        Text(
            "Créez votre compte",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
        )

        Spacer(Modifier.height(28.dp))

        // Error
        if (state.error != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
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

        // Name row
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
        Spacer(Modifier.height(10.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(10.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe (6 car. min.)") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "\uD83D\uDE48" else "\uD83D\uDC41", fontSize = 18.sp)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(14.dp))

        // City picker
        OutlinedButton(
            onClick = { showVillePicker = !showVillePicker },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        ) {
            Text("📍", fontSize = 16.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                selectedVilleName ?: "Votre ville de résidence",
                color = if (selectedVille != null) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
            )
        }

        if (showVillePicker && !state.villesLoading) {
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = villeSearch,
                onValueChange = { villeSearch = it },
                placeholder = { Text("Rechercher...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().heightIn(max = 180.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            ) {
                val filtered = remember(villeSearch, state.villes) {
                    val q = villeSearch.lowercase()
                    if (q.isBlank()) state.villes
                    else state.villes.filter {
                        it.nom.lowercase().contains(q) || it.regionNom.lowercase().contains(q)
                    }
                }
                LazyColumn(modifier = Modifier.padding(4.dp)) {
                    items(filtered, key = { it.id }) { ville ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedVille = ville.id
                                    showVillePicker = false
                                    villeSearch = ""
                                }
                                .background(
                                    if (ville.id == selectedVille) Primary.copy(alpha = 0.08f)
                                    else Color.Transparent
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                        ) {
                            Text(ville.nom, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text(
                                " · ${ville.regionNom}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Register button
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
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text("S'inscrire", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Divider
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
            Text("  ou  ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE2E8F0))
        }

        Spacer(Modifier.height(16.dp))

        // Google button
        OutlinedButton(
            onClick = { onGoogleSignIn() },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        ) {
            Text("G", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFFDB4437))
            Spacer(Modifier.width(10.dp))
            Text("S'inscrire avec Google", color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Login link
        TextButton(onClick = onBackToLogin) {
            Text("Déjà un compte ? ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            Text("Se connecter", color = Primary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }

        Spacer(Modifier.height(12.dp))

        // Footer
        Text("Hackathon IndabaX Cameroun 2026", color = Color(0xFFBBBBBB), fontSize = 11.sp)
        Text("ML Masters", color = Color(0xFFBBBBBB), fontWeight = FontWeight.Medium, fontSize = 11.sp)

        Spacer(Modifier.height(24.dp))
    }
}
