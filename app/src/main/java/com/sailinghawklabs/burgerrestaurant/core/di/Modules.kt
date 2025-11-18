package com.sailinghawklabs.burgerrestaurant.core.di

import com.google.firebase.auth.FirebaseAuth
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.core.data.auth.GoogleUiClient
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import com.sailinghawklabs.burgerrestaurant.core.data.repoImpl.CustomerRepoImpl
import com.sailinghawklabs.burgerrestaurant.feature.auth.AuthViewModel
import com.sailinghawklabs.burgerrestaurant.feature.home.HomeViewModel
import com.sailinghawklabs.burgerrestaurant.feature.profile.ProfileViewModel
import com.sailinghawklabs.burgerrestaurant.feature.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    single<CustomerRepository> { CustomerRepoImpl() }

    viewModel {
        AuthViewModel(customerRepository = get(), googleAuthUiClient = get())
    }

    viewModel {
        SplashViewModel(googleAuthUiClient = get())
    }
    viewModel {
        HomeViewModel(customerRepository = get())
    }

    viewModel {
        ProfileViewModel(get())
    }

    single {
        GoogleUiClient(
            context = androidContext(),
            auth = get(),
            serverClient = androidContext().getString(R.string.default_web_client_id)
        )
    }
}
