package com.hybris.tlv.ui.navigation.graph

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hybris.tlv.ui.navigation.Screen
import com.hybris.tlv.ui.screen.StoreFactory
import com.hybris.tlv.ui.screen.credit.CreditScreen

internal fun NavGraphBuilder.creditScreen(storeFactory: StoreFactory) =
    composable<Screen.Credit> {
        CreditScreen(store = viewModel { storeFactory.getCreditStore() })
    }
