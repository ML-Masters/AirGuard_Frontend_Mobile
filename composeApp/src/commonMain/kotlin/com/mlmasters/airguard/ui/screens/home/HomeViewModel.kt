package com.mlmasters.airguard.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlmasters.airguard.data.model.AirQuality
import com.mlmasters.airguard.data.model.Alert
import com.mlmasters.airguard.data.model.Ville
import com.mlmasters.airguard.data.repository.AirGuardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val villes: List<Ville> = emptyList(),
    val airQuality: List<AirQuality> = emptyList(),
    val alerts: List<Alert> = emptyList(),
    val avgAqi: Int = 0,
    val criticalCities: Int = 0,
    val isRefreshing: Boolean = false,
)

class HomeViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadData()
        // Auto-refresh every 30 seconds
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(30_000)
                loadData()
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val villesResult = repository.getVilles()
                val aqResult = repository.getAirQuality(estPrediction = false)
                val alertsResult = repository.getActiveAlerts()

                val villes = villesResult.getOrDefault(emptyList())
                val airQuality = aqResult.getOrDefault(emptyList())
                val alerts = alertsResult.getOrDefault(emptyList())

                // Compute latest AQI per city
                val latestByCity = mutableMapOf<Int, AirQuality>()
                for (aq in airQuality) {
                    val existing = latestByCity[aq.ville]
                    if (existing == null || aq.dateCible > existing.dateCible) {
                        latestByCity[aq.ville] = aq
                    }
                }

                val cityAQIs = latestByCity.values.toList()
                val avgAqi = if (cityAQIs.isNotEmpty()) {
                    cityAQIs.map { it.indiceAqi }.average().toInt()
                } else 0

                val criticalCities = cityAQIs.count {
                    it.categorie in listOf("Malsain", "Tres_malsain", "Dangereux")
                }

                _state.value = HomeState(
                    isLoading = false,
                    villes = villes,
                    airQuality = airQuality,
                    alerts = alerts,
                    avgAqi = avgAqi,
                    criticalCities = criticalCities,
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Erreur de chargement",
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            loadData()
            _state.value = _state.value.copy(isRefreshing = false)
        }
    }
}
