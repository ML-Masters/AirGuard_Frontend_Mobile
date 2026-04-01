package com.mlmasters.airguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mlmasters.airguard.data.remote.GoogleSignInHelper
import com.mlmasters.airguard.data.remote.createDataStore
import com.mlmasters.airguard.di.appModule
import kotlinx.coroutines.MainScope
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
                onGoogleSignIn = { onToken ->
                    MainScope().launch {
                        googleSignIn.signIn()
                            .onSuccess { idToken -> onToken(idToken) }
                    }
                }
            )
        }
    }
}
