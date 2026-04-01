package com.mlmasters.airguard

import androidx.compose.runtime.*
import com.mlmasters.airguard.ui.navigation.AppNavigation
import com.mlmasters.airguard.ui.screens.login.LoginScreen
import com.mlmasters.airguard.ui.screens.login.OnboardingCityScreen
import com.mlmasters.airguard.ui.screens.login.RegisterScreen
import com.mlmasters.airguard.ui.theme.AirGuardTheme
import com.mlmasters.airguard.data.repository.AirGuardRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App(onGoogleSignIn: (onToken: (String) -> Unit) -> Unit = { }) {
    val repository: AirGuardRepository = koinInject()
    var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }
    var showRegister by remember { mutableStateOf(false) }
    var showOnboarding by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoggedIn = repository.isLoggedIn()
    }

    val googleSignInHandler: () -> Unit = {
        onGoogleSignIn { idToken ->
            scope.launch {
                val result = repository.loginWithGoogle(idToken)
                if (result.isSuccess) {
                    showOnboarding = true
                }
            }
        }
    }

    AirGuardTheme {
        when {
            isLoggedIn == null -> {}
            showOnboarding -> OnboardingCityScreen(
                onComplete = {
                    showOnboarding = false
                    isLoggedIn = true
                }
            )
            isLoggedIn == true -> AppNavigation(onLogout = {
                scope.launch {
                    repository.logout()
                    isLoggedIn = false
                }
            })
            showRegister -> RegisterScreen(
                onRegisterSuccess = { isLoggedIn = true },
                onBackToLogin = { showRegister = false },
                onGoogleSignIn = googleSignInHandler,
            )
            else -> LoginScreen(
                onLoginSuccess = { isLoggedIn = true },
                onGoogleSignIn = googleSignInHandler,
                onNavigateToRegister = { showRegister = true },
            )
        }
    }
}
