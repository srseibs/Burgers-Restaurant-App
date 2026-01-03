package com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sailinghawklabs.burgerrestaurant.R
import com.sailinghawklabs.burgerrestaurant.feature.admin.manage_product.component.CategoryDialog
import com.sailinghawklabs.burgerrestaurant.feature.component.BurgerTextField
import com.sailinghawklabs.burgerrestaurant.feature.component.CurrencyVisualTransformation
import com.sailinghawklabs.burgerrestaurant.feature.component.ErrorCard
import com.sailinghawklabs.burgerrestaurant.feature.component.LoadingCard
import com.sailinghawklabs.burgerrestaurant.feature.component.ObserveAsCommand
import com.sailinghawklabs.burgerrestaurant.feature.component.PrimaryButton
import com.sailinghawklabs.burgerrestaurant.feature.util.DisplayResult
import com.sailinghawklabs.burgerrestaurant.feature.util.MessageUtils
import com.sailinghawklabs.burgerrestaurant.feature.util.RequestState
import com.sailinghawklabs.burgerrestaurant.ui.theme.AppFontSize
import com.sailinghawklabs.burgerrestaurant.ui.theme.BorderIdle
import com.sailinghawklabs.burgerrestaurant.ui.theme.ButtonPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.IconPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.Resources
import com.sailinghawklabs.burgerrestaurant.ui.theme.Surface
import com.sailinghawklabs.burgerrestaurant.ui.theme.SurfaceLight
import com.sailinghawklabs.burgerrestaurant.ui.theme.TextPrimary
import com.sailinghawklabs.burgerrestaurant.ui.theme.oswaldVariableFont
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToLong

@Composable
fun ManageProductScreen(
    viewModel: ManageProductViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    productId: String?
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ManageProductScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        commands = viewModel.commandsForScreen,
        productId = productId,
        onNavigateBack = { onNavigateBack() },
        checkIsFormValid = viewModel::checkIsFormValid
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreenContent(
    state: ManageProductState,
    productId: String?,
    onEvent: (ManageProductScreenEvent) -> Unit,
    commands: Flow<ManageProductScreenCommand>,
    onNavigateBack: () -> Unit,
    checkIsFormValid: () -> RequestState<String>
) {
    val selectedProductCategory = state.selectedCategory
    val showCategoryDialog = state.isCategoryDialogOpen
    val allCategories = state.allCategories
    val context = LocalContext.current
    var dropDownMenuOpen by rememberSaveable { mutableStateOf(false) }

    ObserveAsCommand(flow = commands) { command ->
        when (command) {
            is ManageProductScreenCommand.NavigateBack -> onNavigateBack()
            is ManageProductScreenCommand.ShowMessage -> {
                MessageUtils.showToast(context, command.message)
            }
        }
    }

    val imagPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onEvent(ManageProductScreenEvent.RequestUploadImage(uri))
        }
    )


    AnimatedVisibility(
        visible = showCategoryDialog
    ) {
        CategoryDialog(
            categories = allCategories,
            onDismiss = { onEvent(ManageProductScreenEvent.CategoryDialogDismissed) },
            onSelectedCategory = { onEvent(ManageProductScreenEvent.CategorySelected(it)) },
            selectedCategory = selectedProductCategory
        )
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (productId.isNullOrBlank()) "New Product" else "Edit Product",
                        fontFamily = oswaldVariableFont,
                        fontSize = AppFontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateBack() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Back Arrow",
                        )
                    }
                },
                actions = {
                    productId.takeIf { it != null }?.let {
                        Box {
                            IconButton(onClick = { dropDownMenuOpen = true }) {
                                Icon(
                                    painter = painterResource(Resources.Icon.VerticalMenu),
                                    contentDescription = "Vertical Menu Icon",
                                    tint = IconPrimary
                                )
                            }
                            DropdownMenu(
                                expanded = dropDownMenuOpen,
                                onDismissRequest = { dropDownMenuOpen = false }
                            ) {
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = painterResource(Resources.Icon.Delete),
                                            contentDescription = "Delete",
                                            tint = IconPrimary
                                        )
                                    },
                                    text = { Text("Delete Product", color = TextPrimary) },
                                    onClick = {
                                        dropDownMenuOpen = false
                                        onEvent(ManageProductScreenEvent.DeleteExistingProduct)
                                    }
                                )
                            }
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }

    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(horizontal = 24.dp)
                    .imePadding() // is this needed in a scaffold?
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = BorderIdle,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable(
                                enabled = state.imageUploaderState.isIdle(),
                                onClick = { imagPickerLauncher.launch("image/*") }
                            )
                            .background(SurfaceLight),
                        contentAlignment = Alignment.Center
                    ) {
                        state.imageUploaderState.DisplayResult(
                            onIdle = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(Resources.Icon.Plus),
                                    contentDescription = "Add Photo",
                                    tint = IconPrimary
                                )
                            },
                            onLoading = {
                                LoadingCard(modifier = Modifier.fillMaxSize())
                            },
                            onSuccess = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(state.productImageUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Product Image",
                                        modifier = Modifier
                                            .matchParentSize(),
                                        contentScale = ContentScale.Fit
                                    )

                                    Box(
                                        modifier = Modifier
                                            .padding(top = 12.dp, end = 12.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(color = ButtonPrimary)
                                            .clickable(
                                                onClick = {
                                                    onEvent(
                                                        ManageProductScreenEvent.DeleteUploadedImage
                                                    )
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(Resources.Icon.Delete),
                                            contentDescription = "Delete",
                                            tint = IconPrimary
                                        )
                                    }
                                }
                            },
                            onError = { message ->
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ErrorCard(message = message)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    TextButton(
                                        onClick = {
                                            onEvent(
                                                ManageProductScreenEvent.RetryImageAccess
                                            )
                                        }
                                    ) {
                                        Text("Try again")
                                    }
                                }

                            }

                        )
                    }
                    BurgerTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.title,
                        onValueChange = { onEvent(ManageProductScreenEvent.UpdateTitle(it)) },
                        label = "Title",
                    )
                    BurgerTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        value = state.description,
                        onValueChange = { onEvent(ManageProductScreenEvent.UpdateDescription(it)) },
                        label = "Description",
                        expanded = true
                    )

                    BurgerTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(ManageProductScreenEvent.CategoryFieldClicked)
                            },
                        enabled = false,
                        value = state.selectedCategory?.title ?: "",
                        onValueChange = { },
                        showError = false,
                        label = "Category"

                    )
                    BurgerTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.calories?.toString() ?: "",
                        onValueChange = {
                            onEvent(
                                ManageProductScreenEvent.UpdateCalories(
                                    it.toIntOrNull() ?: 0
                                )
                            )
                        },
                        label = "Calories",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    BurgerTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        value = state.allergyAdvice,
                        expanded = true,
                        onValueChange = { onEvent(ManageProductScreenEvent.UpdateAllergyAdvice(it)) },
                        label = "Allergy Advice"
                    )
                    BurgerTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        value = state.ingredients,
                        expanded = true,
                        onValueChange = { onEvent(ManageProductScreenEvent.UpdateIngredients(it)) },
                        label = "Ingredients"
                    )
                    BurgerTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = if (state.price == 0.0) "" else (state.price * 100).roundToLong()
                            .toString(),
                        onValueChange = { newText ->
                            if (newText.length < 9 && newText.all { it.isDigit() }) {
                                onEvent(
                                    ManageProductScreenEvent.UpdatePrice(
                                        (newText.toLongOrNull() ?: 0L) / 100.0
                                    )
                                )
                            }
                        },
                        label = "Price",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = CurrencyVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                PrimaryButton(
                    onClick = {
                        if (productId.isNullOrEmpty()) {
                            onEvent(ManageProductScreenEvent.CreateNewProduct)
                        } else {
                            onEvent(ManageProductScreenEvent.UpdateExistingProduct)
                        }
                    },
                    text = if (productId.isNullOrBlank()) "Add New Product" else "Update Product",
                    icon = painterResource(
                        if (productId.isNullOrBlank())
                            Resources.Icon.Plus
                        else
                            Resources.Icon.Checkmark
                    ),
                    enabled = checkIsFormValid().isSuccess()
                )
            }
        }
    }
}
// https://youtu.be/urlYyyZH6Eo?si=O7vFV2kKwX9AL5ZY&t=11808

@Preview(showBackground = true, showSystemUi = false, backgroundColor = 0xFF721E1E)
@Composable
private fun ManageProductScreenPrev() {
    ManageProductScreenContent(
        onNavigateBack = {},
        productId = null,
        state = ManageProductState(),
        onEvent = {},
        commands = flowOf(),
        checkIsFormValid = { RequestState.Success("") }
    )
}
