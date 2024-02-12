/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.item

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import java.text.NumberFormat

/**
 * ViewModel to validate and insert items in the Room database.
 */
class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun getEncryptedSharedPrefs(context: Context) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val supplierStr = sharedPreferences.getString("supplier", "")
        val emailStr = sharedPreferences.getString("email", "")
        val phoneStr = sharedPreferences.getString("phone", "")
        val useBool = sharedPreferences.getBoolean("use", true)

        itemUiState = if (useBool) ItemUiState(
            itemDetails = ItemDetails(
                supplier = supplierStr!!,
                email = emailStr!!,
                phone = phoneStr!!,
            )
        ) else ItemUiState()
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && priceValidator(price) && quantityValidator(quantity) &&
                    supplier.isNotBlank() && emailValidator(email) && phoneValidator(phone)
        }
    }

    fun priceValidator(value: String) = Regex("\\d*\\.?\\d+").matches(value)

    fun quantityValidator(value: String) = Regex("\\d+").matches(value)

    fun emailValidator(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun phoneValidator(phone: String) = Regex("\\+?[1-9]\\d{7,14}\$").matches(phone)

    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val supplier: String = "",
    val email: String = "",
    val phone: String = "",
)

/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0,
    supplier = supplier,
    email = email,
    phone = phone
)

fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    supplier = supplier,
    email = email,
    phone = phone
)

fun ItemDetails.toFormatedString(): String {
    return "Item: $name\n" +
            "Quantity in stock: $quantity\n" +
            "Price: ${NumberFormat.getCurrencyInstance().format(price.toDoubleOrNull() ?: 0.0)}\n" +
            "Supplier\n" +
            "Name: $supplier\n" +
            "Email: $email\n" +
            "Phone: $phone\n"
}

fun ItemDetails.hideSupplier(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price,
    quantity = quantity,
    supplier = supplier.replace(Regex("."), "\u2588"),
    email = email.replace(Regex("."), "\u2588"),
    phone = phone.replace(Regex("."), "\u2588")
)
