package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.credit.CreditScreen
import com.hybris.tlv.ui.screen.credit.CreditStore
import com.hybris.tlv.usecase.UseCases

internal fun NavGraphBuilder.creditScreen(
    useCases: UseCases
) = composable<Screen.Credit> {
    CreditScreen(store = viewModel {
        CreditStore(creditUseCases = useCases.credit)
    })
}
