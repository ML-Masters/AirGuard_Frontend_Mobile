package com.mlmasters.airguard.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.mlmasters.airguard.ui.theme.Primary

@Composable
fun ProfileScreen(onLogout: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(Modifier.height(12.dp))
        Text("Mon profil", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(
            "G\u00e9rez vos informations",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
        )

        Spacer(Modifier.height(24.dp))

        // Settings sections
        ProfileSection(title = "Compte") {
            ProfileItem(
                icon = Icons.Default.Person,
                title = "Informations personnelles",
                subtitle = "Nom, pr\u00e9nom",
                onClick = {},
            )
            ProfileItem(
                icon = Icons.Default.Email,
                title = "Adresse email",
                subtitle = "Modifier votre email",
                onClick = {},
            )
            ProfileItem(
                icon = Icons.Default.LocationOn,
                title = "Ville de r\u00e9sidence",
                subtitle = "Changer votre ville",
                onClick = {},
            )
        }

        Spacer(Modifier.height(16.dp))

        ProfileSection(title = "Pr\u00e9f\u00e9rences") {
            ProfileItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Alertes qualit\u00e9 de l'air",
                onClick = {},
            )
            ProfileItem(
                icon = Icons.Default.Settings,
                title = "Langue",
                subtitle = "Fran\u00e7ais",
                onClick = {},
            )
        }

        Spacer(Modifier.height(16.dp))

        ProfileSection(title = "Autres") {
            ProfileItem(
                icon = Icons.Default.Info,
                title = "\u00c0 propos",
                subtitle = "AirGuard v1.0 -- ML Masters",
                onClick = {},
            )
        }

        Spacer(Modifier.height(24.dp))

        // Logout button
        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Se d\u00e9connecter", fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(22.dp),
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
    }
}
