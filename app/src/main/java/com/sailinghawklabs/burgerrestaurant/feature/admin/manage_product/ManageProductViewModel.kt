package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory
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
            _state.update {
                it.copy(
                    productId = args.productId,
                    allCategories = ProductCategory.entries
                )
            }
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
                    _commands.send(ManageProductScreenCommand.ShowMessage("Image uploaded successfully"))
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

    private fun deleteProductImageFromStorage() {
        val downloadUrl = _state.value.productImageUri
        if (downloadUrl.isBlank()) {
            viewModelScope.launch {
                _commands.send(
                    ManageProductScreenCommand.ShowMessage("No image to delete")
                )
            }
            return
        }

        viewModelScope.launch {
            val updateResult = adminRepository.deleteProductImageFromStorage(downloadUrl)
            updateResult
                .onSuccess {
                    _state.update { it.copy(productImageUri = "") }
                    _commands.send(ManageProductScreenCommand.ShowMessage("Image deleted successfully"))
                }
                .onFailure { error ->
                    _commands.send(
                        ManageProductScreenCommand.ShowMessage(
                            message = "Error deleting the image: ${error.message ?: "Unknown"}"
                        )
                    )
                }
        }
    }

    private fun lookupCategory(categoryTitle: String): ProductCategory? {
        return ProductCategory.entries.find { it.title.equals(categoryTitle, ignoreCase = true) }
    }

    fun onEvent(event: ManageProductScreenEvent) {
        when (event) {
            is ManageProductScreenEvent.RequestUploadImage -> {
                uploadProductImageToStorage(event.imageUri)
            }

            is ManageProductScreenEvent.DeleteUploadedImage -> {
                deleteProductImageFromStorage()
                _state.update {
                    it.copy(
                        imageUploaderState = RequestState.Idle
                    )
                }
            }

            ManageProductScreenEvent.RetryImageAccess -> {
                _state.update {
                    it.copy(
                        imageUploaderState = RequestState.Idle
                    )
                }
            }

            ManageProductScreenEvent.CategoryFieldClicked -> {
                _state.update {
                    it.copy(
                        isCategoryDialogOpen = true
                    )
                }
            }

            ManageProductScreenEvent.CategoryDialogDismissed -> {
                _state.update {
                    it.copy(
                        isCategoryDialogOpen = false
                    )
                }
            }

            is ManageProductScreenEvent.CategorySelected -> {
                _state.update {
                    it.copy(
                        selectedCategory = event.category,
                        isCategoryDialogOpen = false
                    )
                }
            }

            is ManageProductScreenEvent.UpdateTitle -> {
                _state.update { it.copy(title = event.newValue) }
            }

            is ManageProductScreenEvent.UpdateDescription -> {
                _state.update { it.copy(description = event.newValue) }
            }

            is ManageProductScreenEvent.UpdateCalories -> {
                _state.update {
                    it.copy(calories = event.newValue)
                }
            }

            is ManageProductScreenEvent.UpdateAllergyAdvice -> {
                _state.update {
                    it.copy(allergyAdvice = event.newValue)
                }
            }

            is ManageProductScreenEvent.UpdateIngredients -> {
                _state.update {
                    it.copy(ingredients = event.newValue)
                }
            }

            is ManageProductScreenEvent.UpdatePrice -> {
                _state.update {
                    it.copy(price = event.newValue)
                }
            }
        }
    }
}