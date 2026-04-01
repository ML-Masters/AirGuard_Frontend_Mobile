package com.mlmasters.airguard.data.remote

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

actual class GoogleSignInHelper(private val context: Context) {
    companion object {
        // Web client ID from Firebase Console -> Authentication -> Sign-in method -> Google
        const val WEB_CLIENT_ID = "81645766104-9tp9o0q30t2s948jv89kpitdc0r1jobs.apps.googleusercontent.com"
    }

    actual suspend fun signIn(): Result<String> = runCatching {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = result.credential
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        googleIdTokenCredential.idToken
    }
}
