package com.mlmasters.airguard.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlmasters.airguard.data.repository.AirGuardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
)

class LoginViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoggedIn = repository.isLoggedIn())
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.loginWithGoogle(idToken)
            _state.value = if (result.isSuccess) {
                _state.value.copy(isLoading = false, isLoggedIn = true)
            } else {
                _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Erreur Google Sign-In"
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.login(email, password)
            _state.value = if (result.isSuccess) {
                _state.value.copy(isLoading = false, isLoggedIn = true)
            } else {
                _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Email ou mot de passe incorrect"
                )
            }
        }
    }
}
