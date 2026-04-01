package com.mlmasters.airguard.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoogleSignIn: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(PrimaryDark, Primary, PrimaryLight))
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Text("AG", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text("AirGuard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
            Text("Cameroun", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            Spacer(Modifier.height(32.dp))

            // Form
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Connexion", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        "Accédez au tableau de bord",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                    )
                    Spacer(Modifier.height(20.dp))

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
                        label = { Text("Mot de passe") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Text(
                                    if (showPassword) "\uD83D\uDE48" else "\uD83D\uDC41",
                                    fontSize = 18.sp,
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.login(email, password) },
                        enabled = !state.isLoading && email.isNotBlank() && password.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text("Se connecter", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            "  ou  ",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(16.dp))

                    // Google Sign-In button
                    OutlinedButton(
                        onClick = { onGoogleSignIn() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Text("G", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFDB4437))
                        Spacer(Modifier.width(8.dp))
                        Text("Continuer avec Google", color = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Pas de compte ? S'inscrire", fontSize = 13.sp)
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Hackathon IndabaX Cameroon 2026 — ML Masters",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
            }
        }
    }
}
