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

package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeEmptyDestination
import com.example.inventory.ui.home.HomeEmptyScreen
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.item.ItemDetailsDestination
import com.example.inventory.ui.item.ItemDetailsScreen
import com.example.inventory.ui.item.ItemEditDestination
import com.example.inventory.ui.item.ItemEditScreen
import com.example.inventory.ui.item.ItemEntryDestination
import com.example.inventory.ui.item.ItemEntryScreen
import com.example.inventory.ui.settings.SettingsDestination
import com.example.inventory.ui.settings.SettingsEmptyDestination
import com.example.inventory.ui.settings.SettingsEmptyScreen
import com.example.inventory.ui.settings.SettingsScreen
import com.microsoft.device.dualscreen.twopanelayout.Screen
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneLayoutNav
import com.microsoft.device.dualscreen.twopanelayout.TwoPaneMode
import com.microsoft.device.dualscreen.twopanelayout.twopanelayoutnav.composable
import com.microsoft.device.dualscreen.windowstate.WindowState

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun InventoryNavHost(
    windowState: WindowState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    TwoPaneLayoutNav(
        navController = navController,
        paneMode = viewModel.inventoryUiState.paneMode,
        singlePaneStartDestination = HomeDestination.route,
        pane1StartDestination = HomeDestination.route,
        pane2StartDestination = HomeEmptyDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = {
                    navController.navigateTo(
                        ItemEntryDestination.route,
                        Screen.Pane2
                    )
                },
                navigateToItemUpdate = {
                    navController.navigateTo("${ItemDetailsDestination.route}/${it}", Screen.Pane2)
                },
                navigateToSettings = {
                    if (windowState.foldIsSeparating)
                        navController.navigateTo(SettingsEmptyDestination.route, Screen.Pane2)
                    else
                        viewModel.setPaneMode(TwoPaneMode.SinglePane)
                    navController.navigateTo(SettingsDestination.route, Screen.Pane1)
                },
                modifier = Modifier.weight(.45f)
            )
        }
        composable(route = HomeEmptyDestination.route) {
            if (isSinglePane)
                navController.navigateTo(HomeDestination.route, Screen.Pane1)
            else
                HomeEmptyScreen(modifier = Modifier.weight(.55f))
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.navigateBack() },
                modifier = Modifier.weight(.55f)
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
        ) { twoPaneBackStack ->
            // Extracting the argument
            val itemId =
                twoPaneBackStack.arguments?.getString(ItemEditDestination.itemIdArg).toString()

            ItemDetailsScreen(
                navigateToEditItem = {
                    navController.navigateTo(
                        "${ItemEditDestination.route}/$itemId",
                        Screen.Pane2
                    )
                },
                navigateBack = {
                    navController.navigateTo(
                        HomeEmptyDestination.route,
                        Screen.Pane2
                    )
                },
                modifier = Modifier.weight(.55f),
                itemId = itemId
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
        ) { twoPaneBackStack ->
            // Extracting the argument
            val itemId =
                twoPaneBackStack.arguments?.getString(ItemEditDestination.itemIdArg).toString()

            ItemEditScreen(
                navigateBack = { navController.navigateBack() },
                modifier = Modifier.weight(.55f),
                itemId = itemId
            )
        }
        composable(route = SettingsDestination.route) {
            if (windowState.foldIsSeparating) {
                viewModel.setPaneMode(TwoPaneMode.HorizontalSingle)
                navController.navigateTo(SettingsEmptyDestination.route, Screen.Pane2)
            }
            SettingsScreen(
                navigateBack = {
                    viewModel.setPaneMode(TwoPaneMode.HorizontalSingle)
                    navController.navigateTo(HomeDestination.route, Screen.Pane1)
                    if (windowState.foldIsSeparating)
                        navController.navigateTo(HomeEmptyDestination.route, Screen.Pane2)
                },
            )
        }
        composable(route = SettingsEmptyDestination.route) {
            if (isSinglePane)
                navController.navigateTo(SettingsDestination.route, Screen.Pane1)
            else
                SettingsEmptyScreen()
        }
    }
}
