package com.mlmasters.airguard.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenStorage(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
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

    suspend fun clearTokens() {
        dataStore.edit {
            it.remove(ACCESS_TOKEN)
            it.remove(REFRESH_TOKEN)
        }
    }

    suspend fun isLoggedIn(): Boolean = getAccessToken() != null
}
