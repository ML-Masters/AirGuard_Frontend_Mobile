package com.mlmasters.airguard.ui.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlmasters.airguard.data.model.Alert
import com.mlmasters.airguard.data.repository.AirGuardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AlertsState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val alerts: List<Alert> = emptyList(),
)

class AlertsViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(AlertsState())
    val state = _state.asStateFlow()

    init {
        loadAlerts()
        // Silent auto-refresh every 2 minutes
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(120_000)
                val result = repository.getActiveAlerts()
                val alerts = result.getOrDefault(_state.value.alerts)
                _state.value = _state.value.copy(alerts = alerts)
            }
        }
    }

    fun loadAlerts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val result = repository.getActiveAlerts()
            _state.value = AlertsState(
                isLoading = false,
                alerts = result.getOrDefault(emptyList()),
                error = if (result.isFailure) result.exceptionOrNull()?.message else null,
            )
        }
    }
}
