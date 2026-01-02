package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.sailinghawklabs.burgerrestaurant.core.data.domain.AdminRepository
import com.sailinghawklabs.burgerrestaurant.core.data.model.Product
import com.sailinghawklabs.burgerrestaurant.core.data.model.ProductCategory
import com.sailinghawklabs.burgerrestaurant.feature.nav.Destination
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

    private val _state = MutableStateFlow(ManageProductState())
    val state = _state
        .onStart {
            val args = savedStateHandle.toRoute<Destination.ManageProductScreen>()
            val passedProductId = args.productId
            loadStateData(passedProductId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ManageProductState()
        )

    private val _commands = Channel<ManageProductScreenCommand>()
    val commandsForScreen = _commands.receiveAsFlow()


    private fun loadStateData(passedProductId: String?) {
        _state.update { it.copy(allCategories = ProductCategory.entries) }

        if (!passedProductId.isNullOrEmpty()) {

            viewModelScope.launch {
                _state.update { it.copy(productDownloadState = RequestState.Loading) }

                when (val result = adminRepository.readProductById(passedProductId)) {
                    is RequestState.Error -> {
                        _state.update {
                            it.copy(
                                productDownloadState =
                                    RequestState.Error(message = "Error loading product: ${result.message}")
                            )
                        }

                    }

                    RequestState.Idle -> {}
                    RequestState.Loading -> {}

                    is RequestState.Success -> {
                        val downloadedProductData = result.data
                        _state.update {
                            it.copy(
                                title = downloadedProductData.title,
                                description = downloadedProductData.description,
                                selectedCategory = lookupCategory(downloadedProductData.category),
                                calories = downloadedProductData.calories,
                                allergyAdvice = downloadedProductData.allergyAdvice,
                                ingredients = downloadedProductData.ingredients,
                                price = downloadedProductData.price,
                                productImageUri = downloadedProductData.productImage,
                                productDownloadState = RequestState.Success(Unit),
                                imageUploaderState = RequestState.Success(Unit),
                                productId = downloadedProductData.id,
                            )
                        }
                    }
                }
            }
        }
    }

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
        return ProductCategory.entries.find {
            it.title.equals(
                categoryTitle,
                ignoreCase = true
            )
        }
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

            is ManageProductScreenEvent.CreateNewProduct -> {
                createNewProduct()
            }

            is ManageProductScreenEvent.UpdateExistingProduct -> {
                updateProductDetails()
            }

            ManageProductScreenEvent.DeleteExistingProduct -> {
                deleteProduct(_state.value.productId)
            }
        }
    }

    private fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _state.update { it.copy(deleteProductState = RequestState.Loading) }
            val result = adminRepository.deleteProduct(productId)
            result
                .onSuccess {
                    _state.update { it.copy(deleteProductState = RequestState.Success(Unit)) }
                    _commands.send(ManageProductScreenCommand.ShowMessage("Product deleted successfully"))
                    delay(1000)
                    _commands.send(ManageProductScreenCommand.NavigateBack)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            deleteProductState = RequestState.Error(
                                message = "Error while deleting product: ${error.message ?: "Unknown"}"
                            )
                        )
                    }
                }
        }
    }

    private fun updateProductDetails() {
        viewModelScope.launch {
            _state.update { it.copy(updateProductState = RequestState.Loading) }

            val isDataComplete = checkIsFormValid()
            if (isDataComplete.isError()) {
                _state.update {
                    it.copy(
                        updateProductState = RequestState.Error(
                            message = "Product Form is not complete: ${isDataComplete.getErrorMessage()}"
                        )
                    )
                }
                return@launch
            }

            val currentState = _state.value
            val updatedProduct = Product(
                id = currentState.productId,
                title = currentState.title,
                description = currentState.description,
                category = currentState.selectedCategory!!.title,
                allergyAdvice = currentState.allergyAdvice,
                ingredients = currentState.ingredients,
                price = currentState.price,
                calories = currentState.calories,
                productImage = currentState.productImageUri
            )

            val result = adminRepository.updateProduct(updatedProduct)

            result.onSuccess {
                _state.update { it.copy(updateProductState = RequestState.Success(Unit)) }
                _commands.send(ManageProductScreenCommand.ShowMessage("Product updated successfully"))
            }.onFailure { error ->

                _state.update {
                    it.copy(
                        updateProductState = RequestState.Error(
                            message = "Error while updating product: ${error.message ?: "Unknown"}"
                        )
                    )
                }
                _commands.send(
                    ManageProductScreenCommand.ShowMessage(
                        "Error while updating product: ${error.message ?: "Unknown"}"
                    )
                )
            }
        }
    }


    private fun createNewProduct() {

        viewModelScope.launch {
            val formValidation = checkIsFormValid()

            if (formValidation.isError()) {
                _state.update {
                    it.copy(
                        createProductState = RequestState.Error(formValidation.getErrorMessage())
                    )
                }
                _commands.send(
                    ManageProductScreenCommand.ShowMessage(
                        "Data entry error: ${formValidation.getErrorMessage()}"
                    )
                )
                return@launch
            }

            try {
                _state.update { it.copy(createProductState = RequestState.Loading) }

                val productState = _state.value
                val productToCreate = Product(
                    id = productState.productId,
                    title = productState.title,
                    description = productState.description,
                    category = productState.selectedCategory!!.title,
                    allergyAdvice = productState.allergyAdvice,
                    ingredients = productState.ingredients,
                    price = productState.price,
                    calories = productState.calories,
                    productImage = productState.productImageUri
                )

                adminRepository.createNewProduct(productToCreate)
                _state.update { it.copy(createProductState = RequestState.Success(Unit)) }
                _commands.send(ManageProductScreenCommand.ShowMessage("Product created successfully"))
                delay(1000)
                _commands.send(ManageProductScreenCommand.NavigateBack)


            } catch (e: Exception) {
                val message = e.message ?: "Unknown error while creating a new product"
                _state.update { it.copy(createProductState = RequestState.Error(message)) }
            }
        }

    }

    fun checkIsFormValid(): RequestState<String> {
        if (_state.value.title.isEmpty()) {
            return RequestState.Error("Title cannot be blank")
        }
        if (_state.value.description.isEmpty()) {
            return RequestState.Error("Description cannot be blank")
        }
        if (_state.value.productImageUri.isEmpty()) {
            return RequestState.Error("Product image cannot be blank")
        }
        if (_state.value.selectedCategory == null) {
            return RequestState.Error("Category cannot be empty")
        }
        if (_state.value.price <= 0.0) {
            return RequestState.Error("Price must be greater than 0")
        }

        return RequestState.Success("")
    }
}