package com.mlmasters.airguard.ui.screens.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlmasters.airguard.data.model.AirQuality
import com.mlmasters.airguard.data.model.PredictionResult
import com.mlmasters.airguard.data.model.Ville
import com.mlmasters.airguard.data.model.WeekPrediction
import com.mlmasters.airguard.data.repository.AirGuardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CitiesState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val villes: List<Ville> = emptyList(),
    val latestAqi: Map<Int, AirQuality> = emptyMap(),
    val searchQuery: String = "",
)

class CitiesViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(CitiesState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val villesResult = repository.getVilles()
            val aqResult = repository.getAirQuality(estPrediction = false)

            val villes = villesResult.getOrDefault(emptyList())
            val airQuality = aqResult.getOrDefault(emptyList())

            val latestByCity = mutableMapOf<Int, AirQuality>()
            for (aq in airQuality) {
                val existing = latestByCity[aq.ville]
                if (existing == null || aq.dateCible > existing.dateCible) {
                    latestByCity[aq.ville] = aq
                }
            }

            _state.value = CitiesState(
                isLoading = false,
                villes = villes,
                latestAqi = latestByCity,
                error = if (villesResult.isFailure) villesResult.exceptionOrNull()?.message else null,
            )
        }
    }

    fun updateSearch(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun filteredVilles(): List<Ville> {
        val q = _state.value.searchQuery.lowercase()
        return if (q.isBlank()) _state.value.villes
        else _state.value.villes.filter {
            it.nom.lowercase().contains(q) || it.regionNom.lowercase().contains(q)
        }
    }
}

// City detail
data class CityDetailState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val prediction: PredictionResult? = null,
    val latestAqi: AirQuality? = null,
    val weekPrediction: WeekPrediction? = null,
)

class CityDetailViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(CityDetailState())
    val state = _state.asStateFlow()

    fun load(villeNom: String, villeId: Int) {
        viewModelScope.launch {
            _state.value = CityDetailState(isLoading = true)

            val predResult = repository.predict(villeNom)
            val aqResult = repository.getAirQuality(estPrediction = false)
            val weekResult = repository.getPredictionWeek(villeNom)

            val latestAqi = aqResult.getOrDefault(emptyList())
                .filter { it.ville == villeId }
                .maxByOrNull { it.dateCible }

            _state.value = CityDetailState(
                isLoading = false,
                prediction = predResult.getOrNull(),
                latestAqi = latestAqi,
                weekPrediction = weekResult.getOrNull(),
                error = if (predResult.isFailure) predResult.exceptionOrNull()?.message else null,
            )
        }
    }
}
