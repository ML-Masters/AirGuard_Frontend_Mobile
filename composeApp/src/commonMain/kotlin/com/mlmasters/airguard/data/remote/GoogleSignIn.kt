package com.mlmasters.airguard.data.remote

expect class GoogleSignInHelper {
    suspend fun signIn(): Result<String> // Returns Google ID token
}
