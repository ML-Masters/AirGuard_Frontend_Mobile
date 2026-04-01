package com.mlmasters.airguard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.mlmasters.airguard.data.remote.GoogleSignInHelper
import com.mlmasters.airguard.data.remote.createDataStore
import com.mlmasters.airguard.di.appModule
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(applicationContext)
            modules(
                appModule,
                module {
                    single { createDataStore(applicationContext) }
                },
            )
        }

        val googleSignIn = GoogleSignInHelper(this)

        setContent {
            App(
                onGoogleSignIn = { onToken, onError ->
                    lifecycleScope.launch {
                        Log.d("AirGuard", "Lancement Google Sign-In...")
                        val result = googleSignIn.signIn()
                        result.onSuccess { idToken ->
                            Log.d("AirGuard", "Google token OK: ${idToken.take(20)}...")
                            onToken(idToken)
                        }.onFailure { error ->
                            Log.e("AirGuard", "Google Sign-In échoué: ${error.message}", error)
                            onError(error.message ?: "Erreur Google Sign-In")
                        }
                    }
                }
            )
        }
    }
}
