package com.mlmasters.airguard.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlmasters.airguard.data.model.Ville
import com.mlmasters.airguard.data.repository.AirGuardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false,
    val villes: List<Ville> = emptyList(),
    val villesLoading: Boolean = true,
)

class RegisterViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val result = repository.getVilles()
            _state.value = _state.value.copy(
                villes = result.getOrDefault(emptyList()),
                villesLoading = false,
            )
        }
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        selectedVilles: List<Int>,
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.register(email, password, firstName, lastName, "fr", selectedVilles)
            _state.value = if (result.isSuccess) {
                _state.value.copy(isLoading = false, isRegistered = true)
            } else {
                _state.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Erreur lors de l'inscription"
                )
            }
        }
    }
}
