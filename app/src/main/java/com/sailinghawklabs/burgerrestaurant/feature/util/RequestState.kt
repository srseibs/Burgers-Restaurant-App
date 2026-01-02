package com.sailinghawklabs.burgerrestaurant.feature.util

sealed class RequestState<out T> {
    data object Idle : RequestState<Nothing>()
    data object Loading : RequestState<Nothing>()
    data class Success<out T>(val data: T) : RequestState<T>()
    data class Error(val message: String) : RequestState<Nothing>()

    fun isIdle(): Boolean = this is Idle
    fun isLoading(): Boolean = this is Loading
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error

    fun getSuccessData() = (this as Success).data
    fun getSuccessDataOrNull() = (this as? Success)?.data
    fun getErrorMessage() = (this as Error).message
}


