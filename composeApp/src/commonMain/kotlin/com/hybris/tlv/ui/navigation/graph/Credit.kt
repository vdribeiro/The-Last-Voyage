package com.hybris.tlv.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.navigation.graph
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.creditScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<Screen.Credit, CreditStore>(
    navController = navController,
    store = { CreditStore(creditUseCases = useCases.credit) },
    screen = { CreditScreen(store = it) }
)
