package com.sailinghawklabs.burgerrestaurant.feature.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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

@Composable
fun <T> RequestState<T>.DisplayResult(
    modifier: Modifier = Modifier,
    onIdle: (@Composable () -> Unit)? = null,
    onLoading: (@Composable () -> Unit)? = null,
    onError: (@Composable (String) -> Unit)? = null,
    onSuccess: @Composable (T) -> Unit,
    backgroundColor: Color? = null,
    transitionSpec: ContentTransform? =
        scaleIn(tween(durationMillis = 4))
                + fadeIn(tween(durationMillis = 800))
                togetherWith scaleOut(tween(durationMillis = 400))
                + fadeOut(tween(durationMillis = 800))
) {
    AnimatedContent(
        modifier = modifier
            .background(color = backgroundColor ?: Color.Unspecified),
        targetState = this,
        transitionSpec = {
            transitionSpec ?: (EnterTransition.None togetherWith ExitTransition.None)
        },
        label = "Content animation"
    ) { state ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (state) {
                is RequestState.Idle -> {
                    onIdle?.invoke()
                }

                is RequestState.Loading -> {
                    onLoading?.invoke()
                }

                is RequestState.Error -> {
                    onError?.invoke(state.getErrorMessage())
                }

                is RequestState.Success -> {
                    onSuccess(state.getSuccessData())
                }
            }
        }
    }
}
