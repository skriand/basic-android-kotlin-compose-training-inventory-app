package com.example.inventory.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneMode

class InventoryViewModel() : ViewModel() {
    var inventoryUiState by mutableStateOf(InventoryUiState())
        private set

    fun setPaneMode(paneMode: TwoPaneMode) {
        inventoryUiState = InventoryUiState(paneMode)
    }


}

data class InventoryUiState(val paneMode: TwoPaneMode = TwoPaneMode.HorizontalSingle)