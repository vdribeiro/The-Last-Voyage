package com.hybris.tlv.ui.navigation

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.creditScreen(
    navController: NavHostController,
    useCases: UseCases
) = graph<CreditScreen, CreditStore>(
    navController = navController,
    store = { CreditStore(creditUseCases = useCases.credit) },
    screen = { CreditScreen(store = it) }
)

@Serializable
internal data object CreditScreen: Screen
