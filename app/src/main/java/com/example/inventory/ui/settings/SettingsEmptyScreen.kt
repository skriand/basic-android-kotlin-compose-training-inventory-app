package com.example.inventory.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.inventory.R
import com.example.inventory.ui.navigation.NavigationDestination

object SettingsEmptyDestination : NavigationDestination {
    override val route = "settings_empty"
    override val titleRes = R.string.app_name
}

@Composable
fun SettingsEmptyScreen(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "",
                modifier = Modifier
                    .height(130.dp)
                    .width(100.dp)
                    .padding(12.dp)
            )
            Text(
                text = stringResource(id = R.string.settings_empty),
            )
        }
    }
}