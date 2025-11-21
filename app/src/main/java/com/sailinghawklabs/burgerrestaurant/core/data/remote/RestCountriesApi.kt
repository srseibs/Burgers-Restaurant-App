package com.sailinghawklabs.burgerrestaurant.core.data.remote

import com.sailinghawklabs.burgerrestaurant.core.data.model.RestCountriesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RestCountriesApi {
    @GET("v3.1/all")
    suspend fun getAllCountries(
        @Query(value = "fields") fields: String = "name,idd,flags,cca2",
    ): List<RestCountriesDto>

}