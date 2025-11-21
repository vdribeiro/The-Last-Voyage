package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.creditScreen(
    navController: NavHostController,
    useCases: UseCases
) = composable<Screen.Credit, CreditStore>(
    navController = navController,
    store = { CreditStore(creditUseCases = useCases.credit) },
    CreditScreen(store = viewModel { }
)
