package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
import com.sailinghawklabs.burgerrestaurant.feature.nav.Destination
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageProductViewModel(
    private val adminRepository: AdminRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = savedStateHandle.toRoute<Destination.ManageProductScreen>()
    private val productId = args.productId


    private val _state = MutableStateFlow(ManageProductState())
    val state = _state
        .onStart {
            val args = savedStateHandle.toRoute<Destination.ManageProductScreen>()
            _state.update { it.copy(productId = args.productId) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ManageProductState()
        )

    private val _commands = Channel<ManageProductScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()

    private fun uploadProductImageToStorage(imageUri: Uri?) {
        if (imageUri == null) {
            _state.update {
                it.copy(
                    imageUploaderState = RequestState.Error("No image selected. Please choose an image to continue.")
                )
            }
            return
        }

        _state.update { it.copy(imageUploaderState = RequestState.Loading) }

        viewModelScope.launch {
            val updateResult = adminRepository.uploadProductImageToStorage(imageUri)
            updateResult
                .onSuccess { downloadUrl ->
                    _state.update {
                        it.copy(
                            imageUploaderState = RequestState.Success(Unit),
                            productImageUri = downloadUrl
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            imageUploaderState = RequestState.Error(
                                error.message ?: "Unknown error uploading the image"
                            )
                        )
                    }
                }
        }
    }

    private fun deleteProductImageFromStorage(
        onResult: (Boolean, String) -> Unit
    ) {
        val downloadUrl = _state.value.productImageUri
        if (downloadUrl.isBlank()) {
            onResult(false, "No image to delete")
            return
        }

        viewModelScope.launch {
            val updateResult = adminRepository.deleteProductImageFromStorage(downloadUrl)
            updateResult
                .onSuccess {
                    onResult(true, "Image deleted successfully")
                }
                .onFailure { error ->
                    onResult(false, error.message ?: "Unknown error deleting the image")
                }
        }

    }

    fun onEvent(event: ManageProductScreenEvent) {
        when (event) {
            is ManageProductScreenEvent.RequestUploadImage ->
                uploadProductImageToStorage(event.imageUri)

            is ManageProductScreenEvent.DeleteUploadedImage ->
                deleteProductImageFromStorage { success, message -> }
        }

    }
}