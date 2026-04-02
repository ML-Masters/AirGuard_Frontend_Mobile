package com.mlmasters.airguard.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ville(
    val id: Int,
    val nom: String,
    val region: Int,
    @SerialName("region_nom") val regionNom: String = "",
    val latitude: Double,
    val longitude: Double,
)

@Serializable
data class AirQuality(
    val id: Int,
    val ville: Int,
    @SerialName("date_cible") val dateCible: String,
    @SerialName("valeur_pm25") val valeurPm25: Double,
    @SerialName("indice_aqi") val indiceAqi: Int,
    val categorie: String,
    @SerialName("est_prediction") val estPrediction: Boolean,
)

@Serializable
data class NationalKPIs(
    val date: String = "",
    @SerialName("aqi_moyen_national") val aqiMoyenNational: Double = 0.0,
    @SerialName("nombre_villes_critiques") val nombreVillesCritiques: Int = 0,
    @SerialName("total_villes_scannees") val totalVillesScannees: Int = 0,
    val message: String? = null,
)

@Serializable
data class Alert(
    val id: Int,
    val ville: Int,
    @SerialName("ville_nom") val villeNom: String = "",
    @SerialName("ville_region") val villeRegion: String = "",
    @SerialName("date_creation") val dateCreation: String,
    @SerialName("date_publication") val datePublication: String? = null,
    @SerialName("niveau_severite") val niveauSeverite: String,
    val statut: String,
    val source: String = "admin",
    @SerialName("message_fr") val messageFr: String,
    @SerialName("message_en") val messageEn: String = "",
    @SerialName("recommandations_residents_fr") val recommandationsResidentsFr: String = "",
    @SerialName("recommandations_residents_en") val recommandationsResidentsEn: String = "",
    @SerialName("recommandations_visiteurs_fr") val recommandationsVisiteursFr: String = "",
    @SerialName("recommandations_visiteurs_en") val recommandationsVisiteursEn: String = "",
    @SerialName("duree_estimee") val dureeEstimee: String = "",
    @SerialName("est_active") val estActive: Boolean,
)

@Serializable
data class AuthTokens(
    val access: String,
    val refresh: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class RefreshRequest(
    val refresh: String,
)

@Serializable
data class ChatRequest(
    val message: String,
)

@Serializable
data class ChatResponse(
    val response: String,
    val source: String = "",
)

@Serializable
data class PredictRequest(
    @SerialName("ville_nom") val villeNom: String,
    @SerialName("meteo_data") val meteoData: Map<String, Double> = emptyMap(),
)

@Serializable
data class PredictionResult(
    val ville: String,
    val predictions: Predictions,
)

@Serializable
data class Predictions(
    @SerialName("qualite_air") val qualiteAir: QualiteAirPrediction,
    @SerialName("chaleur_sante") val chaleurSante: ChaleurSantePrediction,
    @SerialName("risques_naturels") val risquesNaturels: RisquesNaturelsPrediction,
)

@Serializable
data class QualiteAirPrediction(
    @SerialName("pm25_proxy_ugm3") val pm25: Double,
    @SerialName("aqi_estime") val aqiEstime: Int,
    val categorie: String,
    @SerialName("alerte_couleur") val alerteCouleur: String,
)

@Serializable
data class ChaleurSantePrediction(
    @SerialName("heat_index_ressenti") val heatIndex: Double,
    @SerialName("chaleur_extreme_0_10") val chaleurExtreme: Double,
    val avertissement: String,
)

@Serializable
data class RisquesNaturelsPrediction(
    @SerialName("stress_hydrique_agricole") val stressHydrique: Double,
    @SerialName("risque_inondation_0_10") val risqueInondation: Double,
    @SerialName("categorie_inondation") val categorieInondation: String,
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    @SerialName("first_name") val firstName: String = "",
    @SerialName("last_name") val lastName: String = "",
    @SerialName("langue_preferee") val languePreferee: String = "fr",
    @SerialName("villes_favorites") val villesFavorites: List<Int> = emptyList(),
)

@Serializable
data class RegisterResponse(
    val access: String,
    val refresh: String,
)

@Serializable
data class GoogleAuthRequest(
    @SerialName("id_token") val idToken: String,
)

@Serializable
data class GoogleAuthResponse(
    val access: String,
    val refresh: String,
    val created: Boolean = false,
)

// Paginated response wrapper (DRF pagination)
@Serializable
data class PaginatedResponse<T>(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<T>,
)

@Serializable
data class TomorrowPrediction(
    val ville: String = "",
    val date: String = "",
    val aqi: Int = 0,
    val pm25: Double = 0.0,
    val categorie: String = "",
    val label: String = "",
    val conseil: String = "",
    val chaleur: ChaleurPrediction = ChaleurPrediction(),
    val risques: RisquesPrediction = RisquesPrediction(),
)

@Serializable
data class ChaleurPrediction(
    @SerialName("heat_index") val heatIndex: Double = 0.0,
    val extreme: Double = 0.0,
    val avertissement: String = "Normal",
)

@Serializable
data class RisquesPrediction(
    val inondation: Double = 0.0,
    val secheresse: Double = 0.0,
    @SerialName("categorie_inondation") val categorieInondation: String = "",
)

@Serializable
data class WeekPrediction(
    val ville: String = "",
    val semaine: String = "",
    val resume: String = "",
    val jours: List<DayPrediction> = emptyList(),
)

@Serializable
data class DayPrediction(
    val jour: String = "",
    val date: String = "",
    val aqi: Int = 0,
    val categorie: String = "",
    val label: String = "",
    val conseil: String = "",
)
