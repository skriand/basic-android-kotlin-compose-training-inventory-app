package com.example.inventory.ui.settings

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SettingsViewModel : ViewModel() {
    /**
     * Ui State for SettingsScreen
     */

    var settingsUiState by mutableStateOf(SettingsUiState())
        private set
    var useDefaultValues by mutableStateOf(true)
    var hideSensitiveData by mutableStateOf(false)
    var prohibitSendingData by mutableStateOf(false)

    fun getEncryptedSharedPrefs(context: Context) {
        // creating a master key for encryption of shared preferences.
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        // Initialize/open an instance of EncryptedSharedPreferences on below line.
        val sharedPreferences = EncryptedSharedPreferences.create(
            // passing a file name to share a preferences
            "preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        // on below line creating a variable
        // to get the data from shared prefs.
        val supplierStr = sharedPreferences.getString("supplier", "")
        val emailStr = sharedPreferences.getString("email", "")
        val phoneStr = sharedPreferences.getString("phone", "")
        val useBool = sharedPreferences.getBoolean("use", true)
        val hideBool = sharedPreferences.getBoolean("hide", false)
        val prohibitBool = sharedPreferences.getBoolean("prohibit", false)

        settingsUiState = SettingsUiState(
            supplierDetails = SupplierDetails(
                supplierStr!!,
                emailStr!!,
                phoneStr!!,
            )
        )
        useDefaultValues = useBool
        hideSensitiveData = hideBool
        prohibitSendingData = prohibitBool
    }

    fun setEncryptedSharedPref(context: Context) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences = EncryptedSharedPreferences.create(
            "preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit()
            .putBoolean("use", useDefaultValues)
            .putBoolean("hide", hideSensitiveData)
            .putBoolean("prohibit", prohibitSendingData)
            .apply()
    }

    fun setEncryptedSharedPrefs(context: Context) {
        if (validateInput(settingsUiState.supplierDetails)) {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val sharedPreferences = EncryptedSharedPreferences.create(
                "preferences",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            // on below line we are storing data in shared preferences file.
            sharedPreferences.edit()
                .putString("supplier", settingsUiState.supplierDetails.supplier)
                .putString("email", settingsUiState.supplierDetails.email)
                .putString("phone", settingsUiState.supplierDetails.phone)
                .apply()

            settingsUiState =
                SettingsUiState(
                    supplierDetails = settingsUiState.supplierDetails,
                    isEntryValid = false
                )
        }
    }

    private fun validateInput(uiState: SupplierDetails = settingsUiState.supplierDetails): Boolean {
        return with(uiState) {
            (emailValidator(email) || email.isBlank()) && (phoneValidator(phone) || phone.isBlank())
        }
    }

    private fun emailValidator(email: String) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun phoneValidator(phone: String) = Regex("\\+?[1-9]\\d{7,14}\$").matches(phone)

    fun updateUiState(supplierDetails: SupplierDetails) {
        settingsUiState =
            SettingsUiState(
                supplierDetails = supplierDetails,
                isEntryValid = validateInput(supplierDetails),
            )
    }

    fun updateSettingsUiState(key: String) {
        when (key) {
            "use" -> useDefaultValues = !useDefaultValues
            "hide" -> hideSensitiveData = !hideSensitiveData
            "prohibit" -> prohibitSendingData = !prohibitSendingData
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class SettingsUiState(
    val supplierDetails: SupplierDetails = SupplierDetails(),
    val isEntryValid: Boolean = false,
)

data class SupplierDetails(
    val supplier: String = "",
    val email: String = "",
    val phone: String = "",
)