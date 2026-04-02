package com.mlmasters.airguard.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlmasters.airguard.data.model.Ville
import com.mlmasters.airguard.data.repository.AirGuardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val villeNom: String = "",
    val langue: String = "fr",
    val villes: List<Ville> = emptyList(),
    val isLoading: Boolean = true,
)

class ProfileViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val villesResult = repository.getVilles()
            _state.value = _state.value.copy(
                villes = villesResult.getOrDefault(emptyList()),
                isLoading = false,
            )
        }
    }
}
