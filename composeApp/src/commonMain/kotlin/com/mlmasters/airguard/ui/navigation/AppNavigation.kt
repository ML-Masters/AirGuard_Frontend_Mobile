package com.mlmasters.airguard.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mlmasters.airguard.ui.i18n.S
import com.mlmasters.airguard.ui.screens.home.HomeScreen
import com.mlmasters.airguard.ui.screens.cities.CitiesScreen
import com.mlmasters.airguard.ui.screens.cities.CityDetailScreen
import com.mlmasters.airguard.ui.screens.alerts.AlertsScreen
import com.mlmasters.airguard.ui.screens.chat.ChatScreen
import com.mlmasters.airguard.ui.screens.profile.ProfileScreen
import kotlinx.serialization.Serializable

// Routes
@Serializable object HomeRoute
@Serializable object CitiesRoute
@Serializable data class CityDetailRoute(val villeNom: String, val villeId: Int)
@Serializable object AlertsRoute
@Serializable object ChatRoute
@Serializable object ProfileRoute

data class BottomNavItem(
    val label: () -> String,
    val icon: ImageVector,
    val route: Any,
)

val bottomNavItems = listOf(
    BottomNavItem({ S.navHome }, Icons.Default.Home, HomeRoute),
    BottomNavItem({ S.navCities }, Icons.Default.Place, CitiesRoute),
    BottomNavItem({ S.navAlerts }, Icons.Default.Notifications, AlertsRoute),
    BottomNavItem({ S.navChat }, Icons.Default.MailOutline, ChatRoute),
    BottomNavItem({ S.navProfile }, Icons.Default.Person, ProfileRoute),
)

@Composable
fun AppNavigation(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.let { dest ->
        bottomNavItems.any { dest.hasRoute(it.route::class) }
    } ?: true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hasRoute(item.route::class) == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(HomeRoute) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label()) },
                            label = { Text(item.label(), style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            ),
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
        ) {
            composable<HomeRoute> { HomeScreen() }
            composable<CitiesRoute> {
                CitiesScreen(
                    onCityClick = { nom, id ->
                        navController.navigate(CityDetailRoute(nom, id))
                    }
                )
            }
            composable<CityDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<CityDetailRoute>()
                CityDetailScreen(
                    villeNom = route.villeNom,
                    villeId = route.villeId,
                    onBack = { navController.popBackStack() },
                )
            }
            composable<AlertsRoute> { AlertsScreen() }
            composable<ChatRoute> { ChatScreen() }
            composable<ProfileRoute> { ProfileScreen(onLogout = onLogout) }
        }
    }
}
