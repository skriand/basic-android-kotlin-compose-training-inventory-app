package com.example.inventory.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel() : ViewModel() {
    //val settingsUiState =
}

/**
 * Ui State for SettingsScreen
 */
data class SettingsUiState(val settingsList: List<Item> = listOf())