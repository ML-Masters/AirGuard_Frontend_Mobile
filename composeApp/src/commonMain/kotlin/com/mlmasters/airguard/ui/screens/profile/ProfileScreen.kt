package com.mlmasters.airguard.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Edit
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
import com.mlmasters.airguard.ui.i18n.S
import com.mlmasters.airguard.ui.theme.Primary
import org.koin.compose.viewmodel.koinViewModel

private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var showEditName by remember { mutableStateOf(false) }
    var showEditCity by remember { mutableStateOf(false) }
    var showEditLangue by remember { mutableStateOf(false) }
    var editFirstName by remember { mutableStateOf("") }
    var editLastName by remember { mutableStateOf("") }
    var citySearch by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(16.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Primary, modifier = Modifier.size(40.dp))
        }

        Spacer(Modifier.height(12.dp))
        if (state.firstName.isNotEmpty() || state.lastName.isNotEmpty()) {
            Text(
                "${state.firstName} ${state.lastName}".trim(),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFF1E293B),
            )
            Text(state.email, fontSize = 14.sp, color = Color(0xFF64748B))
        } else {
            Text(S.myProfile, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF1E293B))
            Text(S.manageInfo, fontSize = 14.sp, color = Color(0xFF64748B))
        }

        Spacer(Modifier.height(24.dp))

        // Compte section
        ProfileSection(title = S.account) {
            ProfileItem(
                icon = Icons.Default.Person,
                title = S.personalInfo,
                subtitle = if (state.firstName.isNotEmpty()) "${state.firstName} ${state.lastName}" else S.notSpecified,
                onClick = {
                    editFirstName = state.firstName
                    editLastName = state.lastName
                    showEditName = true
                },
            )
            HorizontalDivider(color = Color(0xFFF1F5F9))
            ProfileItem(
                icon = Icons.Default.Email,
                title = S.emailAddress,
                subtitle = state.email.ifEmpty { S.notSpecified },
                onClick = {},
            )
            HorizontalDivider(color = Color(0xFFF1F5F9))
            ProfileItem(
                icon = Icons.Default.LocationOn,
                title = S.cityOfResidence,
                subtitle = state.villeNom.ifEmpty { S.notSpecified },
                onClick = {
                    citySearch = ""
                    showEditCity = true
                },
            )
        }

        Spacer(Modifier.height(16.dp))

        // Preferences section
        ProfileSection(title = S.preferences) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Primary, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(S.notifications, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                    Text(S.airAlerts, fontSize = 12.sp, color = Color(0xFF64748B))
                }
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = Primary),
                )
            }
            HorizontalDivider(color = Color(0xFFF1F5F9))
            ProfileItem(
                icon = Icons.Default.Settings,
                title = S.language,
                subtitle = if (state.langue == "fr") S.french else S.english,
                onClick = { showEditLangue = true },
            )
        }

        Spacer(Modifier.height(16.dp))

        // About section
        ProfileSection(title = S.other) {
            ProfileItem(
                icon = Icons.Default.Info,
                title = S.about,
                subtitle = "AirGuard v1.0 -- ML Masters",
                onClick = {},
            )
        }

        Spacer(Modifier.height(24.dp))

        // Logout
        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(S.logout, fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(24.dp))
    }

    // Edit Name Dialog
    if (showEditName) {
        AlertDialog(
            onDismissRequest = { showEditName = false },
            title = { Text(S.editName, color = TextDark) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editFirstName,
                        onValueChange = { editFirstName = it },
                        label = { Text(S.firstName) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = editLastName,
                        onValueChange = { editLastName = it },
                        label = { Text(S.lastName) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateName(editFirstName, editLastName)
                        showEditName = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                ) {
                    Text(S.save)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditName = false }) {
                    Text(S.cancel, color = TextMuted)
                }
            },
        )
    }

    // Edit City Dialog
    if (showEditCity) {
        AlertDialog(
            onDismissRequest = { showEditCity = false },
            title = { Text(S.changeCity, color = TextDark) },
            text = {
                Column(modifier = Modifier.heightIn(max = 350.dp)) {
                    OutlinedTextField(
                        value = citySearch,
                        onValueChange = { citySearch = it },
                        placeholder = { Text(S.search) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    val filtered = state.villes.filter {
                        val q = citySearch.lowercase()
                        q.isBlank() || it.nom.lowercase().contains(q) || it.regionNom.lowercase().contains(q)
                    }
                    LazyColumn {
                        items(filtered, key = { it.id }) { ville ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.updateCity(ville.id, ville.nom)
                                        showEditCity = false
                                    }
                                    .padding(horizontal = 4.dp, vertical = 10.dp),
                            ) {
                                Column {
                                    Text(ville.nom, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextDark)
                                    Text(ville.regionNom, fontSize = 12.sp, color = TextMuted)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showEditCity = false }) {
                    Text(S.close, color = TextMuted)
                }
            },
        )
    }

    // Language Dialog
    if (showEditLangue) {
        AlertDialog(
            onDismissRequest = { showEditLangue = false },
            title = { Text(S.changeLanguage, color = TextDark) },
            text = {
                Column {
                    listOf("fr" to S.french, "en" to S.english).forEach { (code, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateLangue(code)
                                    showEditLangue = false
                                }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = state.langue == code,
                                onClick = {
                                    viewModel.updateLangue(code)
                                    showEditLangue = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Primary),
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(label, fontSize = 15.sp, color = Color(0xFF1E293B))
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showEditLangue = false }) {
                    Text(S.close, color = TextMuted)
                }
            },
        )
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column { content() }
        }
    }
}

@Composable
private fun ProfileItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextDark)
            Text(subtitle, fontSize = 12.sp, color = TextMuted)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(20.dp))
    }
}
