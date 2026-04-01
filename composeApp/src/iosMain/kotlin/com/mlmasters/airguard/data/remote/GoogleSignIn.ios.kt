package com.mlmasters.airguard.data.remote

actual class GoogleSignInHelper {
    actual suspend fun signIn(): Result<String> {
        return Result.failure(Exception("Google Sign-In non disponible sur iOS pour le moment"))
    }
}
