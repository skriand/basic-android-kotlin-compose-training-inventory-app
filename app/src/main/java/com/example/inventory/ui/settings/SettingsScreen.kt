package com.example.inventory.ui.settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.navigation.NavigationDestination

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.settings
}

/**
 * Entry route for Settings screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    //val settingsUiState by viewModel.settingsUiState.collectAsState()
    val context = LocalContext.current
    viewModel.getEncryptedSharedPrefs(context)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val string = stringResource(id = R.string.supplier_default_values_saved)

    BackHandler(enabled = true, onBack = { navigateBack() })
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            InventoryTopAppBar(
                title = stringResource(SettingsDestination.titleRes),
                canNavigateBack = true,
                navigateBack = navigateBack,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        SettingsBody(
            settingsUiState = viewModel.settingsUiState,
            onSettingsValueChange = viewModel::updateUiState,
            onSettingValueChange = {
                viewModel.updateSettingsUiState(it)
                viewModel.setEncryptedSharedPref(context)
            },
            onSaveClick = {
                viewModel.setEncryptedSharedPrefs(context)
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
            },
            viewModel = viewModel,
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        )
    }
}

@Composable
private fun SettingsBody(
    settingsUiState: SettingsUiState,
    onSettingsValueChange: (SupplierDetails) -> Unit,
    onSettingValueChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        DefaultSupplierInputForm(
            SupplierDetails = settingsUiState.supplierDetails,
            onValueChange = onSettingsValueChange,
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = onSaveClick,
            enabled = settingsUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.save_action))
        }
        SettingSwitchItem(
            checked = viewModel.useDefaultValues,
            onCheckedChange = { onSettingValueChange("use") },
            title = R.string.use_default_values,
            description = R.string.use_default_values_description,
        )
        SettingSwitchItem(
            checked = viewModel.hideSensitiveData,
            onCheckedChange = { onSettingValueChange("hide") },
            title = R.string.hide_sensitive_data,
            description = R.string.hide_sensitive_data_description,
        )
        SettingSwitchItem(
            checked = viewModel.prohibitSendingData,
            onCheckedChange = { onSettingValueChange("prohibit") },
            title = R.string.prohibit_sending_data,
            description = R.string.prohibit_sending_data_description,
        )
    }
}

@Composable
fun DefaultSupplierInputForm(
    SupplierDetails: SupplierDetails,
    modifier: Modifier = Modifier,
    onValueChange: (SupplierDetails) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(R.string.supplier_default_values),
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = SupplierDetails.supplier,
            onValueChange = { onValueChange(SupplierDetails.copy(supplier = it)) },
            label = { Text(stringResource(R.string.item_supplier)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = SupplierDetails.email,
            onValueChange = { onValueChange(SupplierDetails.copy(email = it)) },
            label = { Text(stringResource(R.string.email)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = SupplierDetails.phone,
            onValueChange = { onValueChange(SupplierDetails.copy(phone = it)) },
            label = { Text(stringResource(R.string.phone)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@Composable
private fun SettingSwitchItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    @StringRes title: Int,
    @StringRes description: Int,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.padding_small))
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val contentAlpha = if (enabled) ContentAlpha.high else ContentAlpha.disabled

            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                modifier = Modifier.alpha(contentAlpha)
            )
            Text(
                text = stringResource(id = description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(contentAlpha)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled
        )
    }
}