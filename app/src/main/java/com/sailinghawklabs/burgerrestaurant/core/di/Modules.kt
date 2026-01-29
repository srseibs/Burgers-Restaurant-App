package com.sailinghawklabs.burgerrestaurant.core.di

import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.core.data.auth.GoogleUiClient
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CountryRepository
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CountryRepositoryImpl
import com.sailinghawklabs.burgerrestaurant.core.data.domain.CustomerRepository
import com.sailinghawklabs.burgerrestaurant.core.data.domain.ProductRepository
import com.sailinghawklabs.burgerrestaurant.core.data.remote.RestCountriesApi
import com.sailinghawklabs.burgerrestaurant.core.data.repoImpl.AdminRepoImpl
import com.sailinghawklabs.burgerrestaurant.core.data.repoImpl.CustomerRepoImpl
import com.sailinghawklabs.burgerrestaurant.core.data.repoImpl.ProductRepoImpl
import com.sailinghawklabs.burgerrestaurant.feature.admin.AdminViewModel
import com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product.ManageProductViewModel
import com.sailinghawklabs.burgerrestaurant.feature.auth.AuthViewModel
import com.sailinghawklabs.burgerrestaurant.feature.home.HomeViewModel
import com.sailinghawklabs.burgerrestaurant.feature.home.productOverview.ProductOverviewViewModel
import com.sailinghawklabs.burgerrestaurant.feature.productdetails.ProductDetailsViewModel
import com.sailinghawklabs.burgerrestaurant.feature.profile.ProfileViewModel
import com.sailinghawklabs.burgerrestaurant.feature.splash.SplashViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val appModule = module {

    single {
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    single {
        OkHttpClient.Builder()
            .connectTimeout(timeout = 30, unit = TimeUnit.SECONDS)
            .readTimeout(timeout = 30, unit = TimeUnit.SECONDS)
            .writeTimeout(timeout = 30, unit = TimeUnit.SECONDS)
            .addInterceptor(interceptor = get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<RestCountriesApi> { get<Retrofit>().create(RestCountriesApi::class.java) }

    single<CustomerRepository> { CustomerRepoImpl() }
    single<CountryRepository> { CountryRepositoryImpl(restCountriesApi = get()) }
    single<AdminRepository> { AdminRepoImpl() }
    single<ProductRepository> { ProductRepoImpl() }


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
        ProfileViewModel(
            customerRepository = get(),
            countryRepository = get()
        )
    }
    viewModel {
        ManageProductViewModel(
            adminRepository = get(),
            savedStateHandle = get()
        )
    }
    viewModel {
        AdminViewModel(adminRepository = get())
    }
    viewModel {
        ProductOverviewViewModel(productRepository = get())
    }
    viewModel {
        ProductDetailsViewModel(
            customerRepository = get(),
            productRepository = get(),
            savedStateHandle = get()
        )
    }

    single {
        GoogleUiClient(
            context = androidContext(),
            auth = get(),
            serverClient = androidContext().getString(R.string.default_web_client_id)
        )
    }
}
