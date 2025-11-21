package com.sailinghawklabs.burgerrestaurant.core.data.domain

import com.sailinghawklabs.burgerrestaurant.core.data.model.Country
import com.sailinghawklabs.burgerrestaurant.core.data.model.toCountryOrNull
import com.sailinghawklabs.burgerrestaurant.core.data.remote.RestCountriesApi
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CountryRepository {
    suspend fun fetchCountries(): Flow<RequestState<List<Country>>>
}

class CountryRepositoryImpl(
    private val restCountriesApi: RestCountriesApi
) : CountryRepository {
    override suspend fun fetchCountries(): Flow<RequestState<List<Country>>> = flow {
        try {
            emit(RequestState.Loading)
            val countries = restCountriesApi.getAllCountries()
                .mapNotNull { it.toCountryOrNull() }
                .distinctBy { it.code }
                .sortedBy { it.name }
            emit(RequestState.Success(countries))
        } catch (e: Exception) {
            emit(
                RequestState.Error(
                    "Cannot access the Country server: ${e.message}"
                )
            )
        }
    }
}