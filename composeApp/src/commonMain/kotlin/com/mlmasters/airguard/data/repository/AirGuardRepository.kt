package com.mlmasters.airguard.data.repository

import com.mlmasters.airguard.data.model.*
import com.mlmasters.airguard.data.remote.ApiClient
import com.mlmasters.airguard.data.remote.TokenStorage

class AirGuardRepository(
    private val api: ApiClient,
    private val tokenStorage: TokenStorage,
) {
    suspend fun isLoggedIn(): Boolean = tokenStorage.isLoggedIn()

    suspend fun login(email: String, password: String): Result<AuthTokens> =
        api.login(email, password)

    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        langue: String,
        villesFavorites: List<Int>,
    ): Result<AuthTokens> = api.register(
        RegisterRequest(email, password, firstName, lastName, langue, villesFavorites)
    )

    suspend fun loginWithGoogle(idToken: String): Result<GoogleAuthResponse> =
        api.loginWithGoogle(idToken)

    suspend fun logout() = api.logout()

    suspend fun getVilles(): Result<List<Ville>> = api.getVilles()

    suspend fun getAirQuality(estPrediction: Boolean = false): Result<List<AirQuality>> =
        api.getAirQuality("est_prediction=$estPrediction")

    suspend fun getNationalKPIs(): Result<NationalKPIs> = api.getNationalKPIs()

    suspend fun getActiveAlerts(): Result<List<Alert>> = api.getActiveAlerts()

    suspend fun predict(villeNom: String): Result<PredictionResult> = api.predict(villeNom)

    suspend fun getPredictionTomorrow(villeNom: String): Result<TomorrowPrediction> =
        api.getPredictionTomorrow(villeNom)

    suspend fun getPredictionWeek(villeNom: String): Result<WeekPrediction> =
        api.getPredictionWeek(villeNom)

    suspend fun chat(message: String, lang: String = "fr"): Result<ChatResponse> = api.chat(message, lang)

    suspend fun getUserProfile(): Result<UserProfile> = api.getUserProfile()

    suspend fun updateUserProfile(firstName: String, lastName: String, villesFavorites: List<Int>? = null, languePreferee: String? = null): Result<UserProfile> =
        api.updateUserProfile(firstName, lastName, villesFavorites, languePreferee)

    suspend fun getStoredUserInfo(): Triple<String, String, String> {
        return Triple(tokenStorage.getUserEmail(), tokenStorage.getUserFirstName(), tokenStorage.getUserLastName())
    }
}
