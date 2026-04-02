package com.mlmasters.airguard.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenStorage(private val dataStore: DataStore<Preferences>) {
    val authExpired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
        private val USER_LAST_NAME = stringPreferencesKey("user_last_name")
        private val USER_VILLE = stringPreferencesKey("user_ville")
    }

    suspend fun getAccessToken(): String? =
        dataStore.data.map { it[ACCESS_TOKEN] }.first()

    suspend fun getRefreshToken(): String? =
        dataStore.data.map { it[REFRESH_TOKEN] }.first()

    suspend fun saveTokens(access: String, refresh: String) {
        dataStore.edit {
            it[ACCESS_TOKEN] = access
            it[REFRESH_TOKEN] = refresh
        }
    }

    suspend fun saveUserInfo(email: String, firstName: String, lastName: String, ville: String = "") {
        dataStore.edit {
            it[USER_EMAIL] = email
            it[USER_FIRST_NAME] = firstName
            it[USER_LAST_NAME] = lastName
            it[USER_VILLE] = ville
        }
    }

    suspend fun getUserEmail(): String = dataStore.data.map { it[USER_EMAIL] ?: "" }.first()
    suspend fun getUserFirstName(): String = dataStore.data.map { it[USER_FIRST_NAME] ?: "" }.first()
    suspend fun getUserLastName(): String = dataStore.data.map { it[USER_LAST_NAME] ?: "" }.first()
    suspend fun getUserVille(): String = dataStore.data.map { it[USER_VILLE] ?: "" }.first()

    suspend fun clearTokens() {
        dataStore.edit { it.clear() }
    }

    suspend fun isLoggedIn(): Boolean = getAccessToken() != null
}
