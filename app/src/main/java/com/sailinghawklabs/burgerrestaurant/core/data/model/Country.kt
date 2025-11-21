package com.sailinghawklabs.burgerrestaurant.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val code: String,
    val name: String,
    val dialCode: Int,
    val flagUrl: String?
)

@Serializable
data class RestCountriesDto(
    val name: Name? = null,
    val idd: Idd? = null,
    val flags: Flags? = null,
    val cca2: String? = null
)

@Serializable
data class Name(
    val common: String? = null
)

@Serializable
data class Idd(
    val root: String? = null,
    val suffixes: List<String>? = null
)

@Serializable
data class Flags(
    val png: String? = null,
    val svg: String? = null,
    @SerialName("alt") val alt: String? = null
)

@Serializable
data class CountryDto(
    val name: Name? = null,
    val alt: String? = null
)

fun RestCountriesDto.toCountryOrNull(): Country? {
    val name = name?.common?.takeIf { it.isNotBlank() } ?: return null
    val code = cca2?.takeIf { it.isNotBlank() } ?: return null
    val idd = idd ?: return null
    val root = idd.root?.takeIf { it.startsWith("+") } ?: return null

    val suffix = idd.suffixes?.singleOrNull()?.takeIf { it.isNotBlank() }
    val dialText = if (suffix != null) root + suffix else root

    val dialCode = dialText.filter { it.isDigit() }.toIntOrNull() ?: return null

    return Country(
        code = code,
        name = name,
        dialCode = dialCode,
        flagUrl = flags?.png ?: flags?.svg
    )
}
