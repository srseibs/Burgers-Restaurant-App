package com.sailinghawklabs.burgerrestaurant.feature.profile.component

import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber

/**
 * A toolbox of reusable, stateless validation functions.
 * This is decoupled from any specific UI state.
 */
object DataValidator {

    /**
     * Validates a required string field. An empty string is considered valid.
     * @return An error message or null if validation passes.
     */
    fun validateRequiredLength(value: String, min: Int, max: Int): String? {
        if (value.isNotEmpty() && value.length !in min..max) {
            return "Must be $min to $max characters"
        }
        return null
    }

    /**
     * Validates an optional string field. It only runs validation if the field is not null or blank.
     * @return An error message or null if validation passes.
     */
    fun validateOptionalLength(value: String?, min: Int, max: Int): String? {
        if (!value.isNullOrBlank() && value.length !in min..max) {
            return "Must be $min to $max characters"
        }
        return null
    }

    /**
     * Validates an optional postal code.
     * @return An error message or null if validation passes.
     */
    fun validatePostalCode(value: Int?): String? {
        if (value == null) return null // Assume null is valid
        val stringCode = value.toString()
        if (stringCode.length !in 3..10)
            return "Must be 3 to 10 characters"
        return null
    }

    /**
     * Validates an optional phone number.
     * @return An error message or null if validation passes.
     */
    fun validatePhoneNumber(value: PhoneNumber?): String? {
        if (value != null) {
            val phoneString = value.dialCode.toString() + value.number
            if (phoneString.length !in 8..15)
                return "Must be 8 to 15 characters"
        }
        return null
    }
}
