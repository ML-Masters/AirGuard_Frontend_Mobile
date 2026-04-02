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
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null,
)

class ProfileViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val villesResult = repository.getVilles()
            val profileResult = repository.getUserProfile()

            val profile = profileResult.getOrNull()

            _state.value = ProfileState(
                firstName = profile?.firstName ?: "",
                lastName = profile?.lastName ?: "",
                email = profile?.email ?: "",
                villeNom = profile?.villesFavorites?.firstOrNull() ?: "",
                langue = profile?.languePreferee ?: "fr",
                villes = villesResult.getOrDefault(emptyList()),
                isLoading = false,
            )
        }
    }

    fun updateName(firstName: String, lastName: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, saveSuccess = false)
            val result = repository.updateUserProfile(firstName, lastName)
            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    firstName = firstName,
                    lastName = lastName,
                    isSaving = false,
                    saveSuccess = true,
                )
            } else {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = result.exceptionOrNull()?.message,
                )
            }
        }
    }

    fun updateCity(villeId: Int, villeNom: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true, saveSuccess = false)
            val result = repository.updateUserProfile(
                _state.value.firstName,
                _state.value.lastName,
                listOf(villeId),
            )
            if (result.isSuccess) {
                _state.value = _state.value.copy(
                    villeNom = villeNom,
                    isSaving = false,
                    saveSuccess = true,
                )
            } else {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = result.exceptionOrNull()?.message,
                )
            }
        }
    }
}
