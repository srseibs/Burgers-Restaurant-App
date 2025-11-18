package com.sailinghawklabs.burgerrestaurant.feature.profile.component

import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber

/**
 * A central place for all validation constants to ensure consistency.
 */
object ValidationConstants {
    const val NAME_MIN_LENGTH = 3
    const val NAME_MAX_LENGTH = 50
    const val CITY_MIN_LENGTH = 3
    const val CITY_MAX_LENGTH = 50
    const val POSTAL_CODE_MIN_LENGTH = 3
    const val POSTAL_CODE_MAX_LENGTH = 10
    const val ADDRESS_MIN_LENGTH = 3
    const val ADDRESS_MAX_LENGTH = 50
    const val PHONE_MIN_LENGTH = 8
    const val PHONE_MAX_LENGTH = 15
}

/**
 * A generic interface for a single validation rule.
 * @return An error message String, or null if validation passes.
 */
fun interface Validator {
    fun validate(): String?
}

/**
 * A convenient facade providing simple, readable validation functions
 * for use in ViewModels and UI Composables.
 */
object FormValidators {
    fun validateFirstName(value: String): String? = RequiredLengthValidator(
        value = value,
        min = ValidationConstants.NAME_MIN_LENGTH,
        max = ValidationConstants.NAME_MAX_LENGTH
    ).validate()

    fun validateLastName(value: String): String? = RequiredLengthValidator(
        value = value,
        min = ValidationConstants.NAME_MIN_LENGTH,
        max = ValidationConstants.NAME_MAX_LENGTH
    ).validate()

    fun validateCity(value: String?): String? = OptionalLengthValidator(
        value = value,
        min = ValidationConstants.CITY_MIN_LENGTH,
        max = ValidationConstants.CITY_MAX_LENGTH
    ).validate()

    fun validateAddress(value: String?): String? = OptionalLengthValidator(
        value = value,
        min = ValidationConstants.ADDRESS_MIN_LENGTH,
        max = ValidationConstants.ADDRESS_MAX_LENGTH
    ).validate()

    fun validatePostalCode(value: Int?): String? = PostalCodeValidator(value).validate()

    fun validatePhoneNumber(value: PhoneNumber?): String? = PhoneNumberValidator(value).validate()
}

// --- Low-Level Validator Implementations ---

class RequiredLengthValidator(
    private val value: String,
    private val min: Int,
    private val max: Int
) : Validator {
    override fun validate(): String? {
        if (value.isNotEmpty() && value.length !in min..max) {
            return "Must be $min to $max characters"
        }
        return null
    }
}

class OptionalLengthValidator(
    private val value: String?,
    private val min: Int,
    private val max: Int
) : Validator {
    override fun validate(): String? {
        if (!value.isNullOrBlank() && value.length !in min..max) {
            return "Must be $min to $max characters"
        }
        return null
    }
}

class PostalCodeValidator(private val value: Int?) : Validator {
    override fun validate(): String? {
        if (value == null) return null // Assume null is valid
        val stringCode = value.toString()
        if (stringCode.length !in ValidationConstants.POSTAL_CODE_MIN_LENGTH..ValidationConstants.POSTAL_CODE_MAX_LENGTH) {
            return "Must be ${ValidationConstants.POSTAL_CODE_MIN_LENGTH} to ${ValidationConstants.POSTAL_CODE_MAX_LENGTH} characters"
        }
        return null
    }
}

class PhoneNumberValidator(private val value: PhoneNumber?) : Validator {
    override fun validate(): String? {
        if (value != null) {
            val phoneString = value.dialCode.toString() + value.number
            if (phoneString.length !in ValidationConstants.PHONE_MIN_LENGTH..ValidationConstants.PHONE_MAX_LENGTH) {
                return "Must be ${ValidationConstants.PHONE_MIN_LENGTH} to ${ValidationConstants.PHONE_MAX_LENGTH} characters"
            }
        }
        return null
    }
}
