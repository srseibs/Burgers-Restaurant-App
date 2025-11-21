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
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    single {
        OkHttpClient.Builder()
            .connectTimeout(timeout = 30, unit = TimeUnit.SECONDS)
            .readTimeout(timeout = 30, unit = TimeUnit.SECONDS)
            .writeTimeout(timeout = 30, unit = TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<CustomerRepository> { CustomerRepoImpl() }

    viewModel { AuthViewModel(customerRepository = get(), googleAuthUiClient = get()) }
    viewModel { SplashViewModel(googleAuthUiClient = get()) }
    viewModel { HomeViewModel(customerRepository = get()) }
    viewModel { ProfileViewModel(customerRepository = get()) }

    single {
        GoogleUiClient(
            context = androidContext(),
            auth = get(),
            serverClient = androidContext().getString(R.string.default_web_client_id)
        )
    }
}
