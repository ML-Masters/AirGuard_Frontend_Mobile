package com.mlmasters.airguard.data.remote

import com.mlmasters.airguard.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient(private val tokenStorage: TokenStorage) {
    companion object {
        const val BASE_URL = "https://api.airguard-cm.duckdns.org/api/v1"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.NONE
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    private suspend fun HttpRequestBuilder.withAuth() {
        val token = tokenStorage.getAccessToken()
        if (token != null) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    // --- Auth ---

    suspend fun login(email: String, password: String): Result<AuthTokens> = runCatching {
        val response = client.post("$BASE_URL/login/") {
            setBody(LoginRequest(email, password))
        }
        val tokens: AuthTokens = response.body()
        tokenStorage.saveTokens(tokens.access, tokens.refresh)
        tokens
    }

    suspend fun register(request: RegisterRequest): Result<AuthTokens> = runCatching {
        val response = client.post("$BASE_URL/register/") {
            setBody(request)
        }
        val body: RegisterResponse = response.body()
        tokenStorage.saveTokens(body.access, body.refresh)
        AuthTokens(body.access, body.refresh)
    }

    suspend fun loginWithGoogle(idToken: String): Result<GoogleAuthResponse> = runCatching {
        val response = client.post("$BASE_URL/auth/google/") {
            setBody(GoogleAuthRequest(idToken))
        }
        val body: GoogleAuthResponse = response.body()
        tokenStorage.saveTokens(body.access, body.refresh)
        body
    }

    suspend fun refreshToken(): Result<String> = runCatching {
        val refresh = tokenStorage.getRefreshToken() ?: throw Exception("No refresh token")
        val response = client.post("$BASE_URL/token/refresh/") {
            setBody(RefreshRequest(refresh))
        }
        val tokens: AuthTokens = response.body()
        tokenStorage.saveTokens(tokens.access, tokens.refresh)
        tokens.access
    }

    suspend fun logout() {
        tokenStorage.clearTokens()
    }

    // --- Villes ---

    suspend fun getVilles(): Result<List<Ville>> = authRequest {
        val response: PaginatedResponse<Ville> = client.get("$BASE_URL/villes/") { withAuth() }.body()
        response.results
    }

    // --- Air Quality ---

    suspend fun getAirQuality(params: String = ""): Result<List<AirQuality>> = authRequest {
        val url = if (params.isNotEmpty()) "$BASE_URL/air-quality/?$params" else "$BASE_URL/air-quality/"
        val response: PaginatedResponse<AirQuality> = client.get(url) { withAuth() }.body()
        response.results
    }

    suspend fun getNationalKPIs(): Result<NationalKPIs> = authRequest {
        client.get("$BASE_URL/air-quality/national_kpis/") { withAuth() }.body()
    }

    // --- Alerts ---

    suspend fun getActiveAlerts(): Result<List<Alert>> = authRequest {
        client.get("$BASE_URL/alerts/active/") { withAuth() }.body()
    }

    // --- Predictions ---

    suspend fun predict(villeNom: String): Result<PredictionResult> = authRequest {
        client.post("$BASE_URL/air-quality/predict/") {
            withAuth()
            setBody(PredictRequest(villeNom))
        }.body()
    }

    suspend fun getPredictionTomorrow(villeNom: String): Result<TomorrowPrediction> = authRequest {
        client.get("$BASE_URL/predictions/tomorrow/?ville_nom=$villeNom") { withAuth() }.body()
    }

    suspend fun getPredictionWeek(villeNom: String): Result<WeekPrediction> = authRequest {
        client.get("$BASE_URL/predictions/week/?ville_nom=$villeNom") { withAuth() }.body()
    }

    // --- Chat ---

    suspend fun chat(message: String): Result<ChatResponse> = authRequest {
        client.post("$BASE_URL/air-quality/chat/") {
            withAuth()
            setBody(ChatRequest(message))
        }.body()
    }

    // --- User Profile ---

    suspend fun getUserProfile(): Result<UserProfile> = authRequest {
        client.get("$BASE_URL/users/me/") { withAuth() }.body()
    }

    suspend fun updateUserProfile(firstName: String, lastName: String, villesFavorites: List<Int>? = null): Result<UserProfile> = authRequest {
        val body = mutableMapOf<String, Any>(
            "first_name" to firstName,
            "last_name" to lastName,
        )
        if (villesFavorites != null) {
            body["villes_favorites"] = villesFavorites
        }
        client.patch("$BASE_URL/users/me/") {
            withAuth()
            setBody(body)
        }.body()
    }

    // --- Helper: auto-retry on 401 ---

    private suspend fun <T> authRequest(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                // Try refreshing token
                val refreshResult = refreshToken()
                if (refreshResult.isSuccess) {
                    try {
                        Result.success(block())
                    } catch (e2: Exception) {
                        Result.failure(e2)
                    }
                } else {
                    tokenStorage.clearTokens()
                    tokenStorage.authExpired.tryEmit(Unit)
                    Result.failure(Exception("Session expirée"))
                }
            } else {
                Result.failure(e)
            }
        } catch (e: io.ktor.client.plugins.HttpRequestTimeoutException) {
            Result.failure(Exception("Délai de connexion dépassé"))
        } catch (e: Exception) {
            val message = when {
                e.message?.contains("Unable to resolve host") == true -> "Pas de connexion internet"
                e.message?.contains("timeout") == true -> "Délai de connexion dépassé"
                else -> e.message ?: "Erreur inconnue"
            }
            Result.failure(Exception(message))
        }
    }
}
