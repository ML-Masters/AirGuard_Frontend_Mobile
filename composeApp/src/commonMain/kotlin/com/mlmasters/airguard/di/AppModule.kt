package com.mlmasters.airguard.di

import com.mlmasters.airguard.data.remote.ApiClient
import com.mlmasters.airguard.data.remote.TokenStorage
import com.mlmasters.airguard.data.repository.AirGuardRepository
import com.mlmasters.airguard.ui.screens.login.LoginViewModel
import com.mlmasters.airguard.ui.screens.login.RegisterViewModel
import com.mlmasters.airguard.ui.screens.home.HomeViewModel
import com.mlmasters.airguard.ui.screens.cities.CitiesViewModel
import com.mlmasters.airguard.ui.screens.cities.CityDetailViewModel
import com.mlmasters.airguard.ui.screens.alerts.AlertsViewModel
import com.mlmasters.airguard.ui.screens.chat.ChatViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { TokenStorage(get()) }
    single { ApiClient(get()) }
    single { AirGuardRepository(get(), get()) }

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::CitiesViewModel)
    viewModelOf(::CityDetailViewModel)
    viewModelOf(::AlertsViewModel)
    viewModelOf(::ChatViewModel)
}
