package com.mlmasters.airguard

import androidx.compose.ui.window.ComposeUIViewController
import com.mlmasters.airguard.data.remote.createDataStore
import com.mlmasters.airguard.di.appModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}

private var koinStarted = false
private fun initKoin() {
    if (koinStarted) return
    startKoin {
        modules(
            appModule,
            module {
                single { createDataStore() }
            },
        )
    }
    koinStarted = true
}
