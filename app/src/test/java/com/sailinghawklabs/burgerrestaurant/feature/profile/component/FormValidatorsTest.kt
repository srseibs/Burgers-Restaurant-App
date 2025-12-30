package com.sailinghawklabs.burgerrestaurant.feature.profile.component

import com.sailinghawklabs.burgerrestaurant.core.data.model.PhoneNumber
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FormValidatorsTest {

    @Test
    fun `validateFirstName returns null for valid name`() {
        val result = ProfileFormValodator.validateFirstName("John")
        assertNull(result)
    }

    @Test
    fun `validateFirstName returns error for name that is too short`() {
        val result = ProfileFormValodator.validateFirstName("Jo")
        assertNotNull(result)
        assertEquals("Must be 3 to 50 characters", result)
    }

    @Test
    fun `validateFirstName returns null for empty name`() {
        val result = ProfileFormValodator.validateFirstName("")
        assertNull(result)
    }

    @Test
    fun `validateLastName returns null for valid name`() {
        val result = ProfileFormValodator.validateLastName("Doe")
        assertNull(result)
    }

    @Test
    fun `validateLastName returns error for name that is too short`() {
        val result = ProfileFormValodator.validateLastName("D")
        assertNotNull(result)
    }

    @Test
    fun `validateCity returns null for valid city`() {
        val result = ProfileFormValodator.validateCity("Anytown")
        assertNull(result)
    }

    @Test
    fun `validateCity returns null for null city`() {
        val result = ProfileFormValodator.validateCity(null)
        assertNull(result)
    }

    @Test
    fun `validateCity returns error for city that is too short`() {
        val result = ProfileFormValodator.validateCity("An")
        assertNotNull(result)
    }

    @Test
    fun `validatePostalCode returns null for valid code`() {
        val result = ProfileFormValodator.validatePostalCode(12345)
        assertNull(result)
    }

    @Test
    fun `validatePostalCode returns null for null code`() {
        val result = ProfileFormValodator.validatePostalCode(null)
        assertNull(result)
    }

    @Test
    fun `validatePostalCode returns error for code that is too short`() {
        val result = ProfileFormValodator.validatePostalCode(12)
        assertNotNull(result)
    }

    @Test
    fun `validatePhoneNumber returns null for valid number`() {
        val result = ProfileFormValodator.validatePhoneNumber(PhoneNumber(1, "5551234567"))
        assertNull(result)
    }

    @Test
    fun `validatePhoneNumber returns null for null number`() {
        val result = ProfileFormValodator.validatePhoneNumber(null)
        assertNull(result)
    }

    @Test
    fun `validatePhoneNumber returns error for number that is too short`() {
        val result = ProfileFormValodator.validatePhoneNumber(PhoneNumber(1, "123"))
        assertNotNull(result)
    }
}
